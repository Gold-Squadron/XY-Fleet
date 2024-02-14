<script setup lang="ts">
import {computed, type CSSProperties, onMounted, type Ref, ref} from 'vue';
import {type GanttBarObject, GGanttRow} from "@infectoone/vue-ganttastic";
import {type UnwrapRefSimple} from "@vue/reactivity"
import CreateBookingModal from "@/components/bookings/CreateBookingModal.vue";
import {useModal} from "bootstrap-vue-next";
import {Booking, type RFlight, getFlights, getVehicles, type RXYWing, type RPilot, getPilots} from "./RoadmapRestCalls";

const dayWidth = 1.5 * parseFloat(getComputedStyle(document.documentElement).fontSize);

//Demodata
let xywings = ref<RXYWing[]>([]);
let pilots = ref<RPilot[]>([]);
let bookings : Ref<Booking[]> = ref([])
let additionalEvents = ["TÜV Termin", "Reparaturen" , "Bereitschaft", "Auto nicht fahrbereit"]

let chartStart : Ref<Date> = ref(new Date().translateDays(25));
let chartEnd = computed(() => {return chartStart.value.translateDays(31)})

let previewMode = false;

/// shifts the time/day on the component when the mouse scrolls
/// any due to damn event browser support
function mouseWheelHandler(inp : any) : boolean {
  const e = window.event || inp; // old IE support
  const delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));

  chartStart.value = chartStart.value.translateDays(delta * 1/4)

  return false;
}

function getUserById(driverId: number) : string {
  return pilots.value.find(pilot => pilot.id == driverId)?.name as string;
}

//transforms the raw data into the rendering format
const generatedBars = computed( () => {
  let map = new Map<number, GanttBarObject[]>();
  xywings.value.forEach(obj => map.set(obj.id, []))
  bookings.value.forEach( (booking) => {
    let stylingContent : CSSProperties = {
      borderRadius: "5px",
      background: "linear-gradient(117deg, rgba(97,217,3,1) 0%, rgba(21,124,0,1) 100%)",
      color: "white"
    };
    if(booking.status == "broken") stylingContent.background =  "linear-gradient(117deg, rgba(217,3,3,1) 0%, rgba(124,29,0,1) 100%)";
    let label : string = booking.reason != 'none' ? booking.reason : getUserById(booking.driverId);
    if(booking.hasHtml()) label = "";
    let x : GanttBarObject = {
      myBeginDate: booking.getStartDateAsReference(),
      myEndDate: booking.getEndDateAsReference(),
      ganttBarConfig: {
          id: "" + booking.id, // ... and a unique "id" property
          label: label,
          hasHandles: booking.status === 'preview',
          immobile: booking.status !== 'preview',
          style: stylingContent,
          class: `bar-${booking.status}`,
          html: booking.hasHtml() ? booking.generateAvatarBasedOnInitials("Dieter") : ""
      }
    }
    map.get(booking.carId)?.push(x);
  })
  return map;
})

//this is where we query the server for new information... IF WE HAD ANY
//TODO remove the parameter once the REST Interface works
function createVirtualBooking(booking : Booking) : void {
  if(booking.status === "preview") previewMode = true;
  bookings.value.push(booking);
}

async function refresh() {
  getVehicles().then()
  let flights : RFlight[] = await getFlights();
  xywings.value = await getVehicles();
  bookings.value = flights.map(rFlight => Booking.from(rFlight));
  pilots.value = await getPilots();
}

const {show, hide, modal} = useModal('creation-dialog')

function finishPreview(save : boolean) {
  previewMode = false;
  if(!save) {
    const index : number = bookings.value.findIndex((x) => x.status == 'preview');
    bookings.value.splice(index, 1);
    return;
  }
  let first : UnwrapRefSimple<Booking> | undefined = bookings.value.find((x) => x.status == 'preview');
  if(first != undefined) {
    first.status = 'booking'
  }
}

function afterLoad() {
  let scrollable = document.getElementById("roadmap");
  if (typeof scrollable != null) { // For different browsers - I regret using HTML already
    scrollable?.addEventListener("mousewheel", mouseWheelHandler, false);
    scrollable?.addEventListener("DOMMouseScroll", mouseWheelHandler, false);
  }


  refresh();
}

onMounted(() => afterLoad());
</script>

<template>
  <p class="d-none" v-for="vehicle in xywings">{{ generatedBars.get(vehicle.id) }}</p> <!-- Debug -->
  <div style="">
    <g-gantt-chart id="roadmap"
        :chart-start="chartStart"
        :chart-end="chartEnd"
        precision="day"
        :grid="true"
        bar-start="myBeginDate"
        bar-end="myEndDate"
        :highlight-sundays="true"
        color-scheme="default"> <!-- https://github.com/zunnzunn/vue-ganttastic/blob/master/docs/GGanttChart.md#color-schemes -->
      <div v-for="vehicle in xywings"> <!-- create a row for each vehicle -->
        <g-gantt-row  :label="vehicle.license_plate" :bars="generatedBars.get(vehicle.id)"/>
      </div>
    </g-gantt-chart>
    <div v-if="previewMode" class="float-right m-5">
      <b-button variant="secondary" size="lg" @click="finishPreview(false)"> Zurück </b-button>
      <b-button variant="primary" size="lg" @click="finishPreview(true)"> Speichern </b-button>
    </div>
    <div v-else class="float-right m-5 d-flex justify-content-end">
      <div class="btn-group">
        <b-button variant="warning" size="lg">Defekt eintragen</b-button>
        <b-button variant="warning" class="dropdown-toggle dropdown-toggle-split" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" size="lg">
          <span class="sr-only">Toggle Dropdown for more event options</span>
        </b-button>
        <div class="dropdown-menu">
          <a class="dropdown-item" href="#" v-for="ev in additionalEvents">{{ev}}</a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="#">Add a new type</a>
        </div>
      </div>
      <b-button variant="primary" size="lg" @click="show"> Neue Fahrt eintragen </b-button>
    </div>
    <CreateBookingModal @refresh="refresh" @createVirtualBooking="createVirtualBooking" :cars="vehicles"/>
  </div>

  <div>
    <button type="button" class="btn btn-primary" id="liveToastBtn">Show live toast</button>

    <div class="position-fixed bottom-0 right-0 p-3" style="z-index: 5; right: 0; bottom: 0;">
      <div id="liveToast" class="toast hide" role="alert" aria-live="assertive" aria-atomic="true" data-delay="2000">
        <div class="toast-header">
          <img src="..." class="rounded mr-2" alt="...">
          <strong class="mr-auto">Bootstrap</strong>
          <small>11 mins ago</small>
          <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="toast-body">
          Hello, world! This is a toast message.
        </div>
      </div>
    </div>

  </div>
</template>

<style scoped>

  .roadmap-main-comp {
    margin: 10px;
  }

  .small-rm-btn {
    padding: 0.1rem 0.3rem;
  }

  .time-header {
    display: flex; flex-direction: row; margin: 0
  }
  .time-header p {
    margin-bottom: 0;
    margin-right: 0.5rem;
  }

  /* TODO: figure out why this doesn't work automatically */
  .text-bg-secondary {
    background: #407fb7 ;
  }

  button {
    margin-left: 20px;
  }
</style>