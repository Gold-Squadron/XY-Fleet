<script setup lang="ts">
import {computed, onMounted, type Ref, ref} from 'vue';
import {type GanttBarObject, GGanttRow} from "@infectoone/vue-ganttastic";
import {type UnwrapRefSimple} from "@vue/reactivity"
import CreateBookingModal from "@/components/bookings/CreateBookingModal.vue";
import {useModal} from "bootstrap-vue-next";
import {Booking} from "@/main";

const dayWidth = 1.5 * parseFloat(getComputedStyle(document.documentElement).fontSize);

//Demodata
let vehicles = ref(["Blue Van", "Red Van", "Green Smart", "Phillip's broken e-scooter", "No Smart", "Clown Car"]);
let bookings = ref([new Booking("Blue Van", new Date(Date.now()), new Date("2024-02-2 19:00"), "Amokfahrt")])
let additionalEvents = ["TÜV Termin", "Reparaturen" , "Bereitschaft", "Auto nicht fahrbereit"]

bookings.value.push(new Booking("Green Smart", new Date("2024-02-2 10:00"), new Date("2024-02-3 24:00")));

let chartStart : Ref<Date> = ref(new Date().translateDays(-4));
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

//transforms the raw data into the rendering format
const generatedBars = computed( () => {
  let map = new Map<string, GanttBarObject[]>();
  vehicles.value.forEach(name => map.set(name, []))
  bookings.value.forEach( (booking) => {
    let x : GanttBarObject = {
      myBeginDate: booking.getStartDateAsReference(),
      myEndDate: booking.getEndDateAsReference(),
      ganttBarConfig: {
          id: booking.car + booking.start, // ... and a unique "id" property
          label: booking.reason ? booking.reason : booking.driver,
          hasHandles: booking.status === 'preview',
          immobile: booking.status !== 'preview',
          class: `bar-${booking.status}`
      }
    }
    map.get(booking.car)?.push(x);
  })
  return map;
})

//this is where we query the server for new information... IF WE HAD ANY
//TODO remove the parameter once the REST Interface works
function createVirtualBooking(booking : Booking) : void {
  console.log(booking)
  if(booking.status === "preview") previewMode = true;
  bookings.value.push(booking);
}

function refresh() {
  //I would put my REST-Call here... IF I HAD ANY
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
}

onMounted(() => afterLoad());
</script>

<template>
  <p class="d-none" v-for="vehicle in vehicles">{{ generatedBars.get(vehicle) }}</p> <!-- Debug -->
  <div style="">
    <g-gantt-chart id="roadmap"
        :chart-start="chartStart"
        :chart-end="chartEnd"
        precision="day"
        :grid="true"
        bar-start="myBeginDate"
        bar-end="myEndDate"
        :highlighted-units=[27,28,3,4,10,11]
        color-scheme="default"> <!-- https://github.com/zunnzunn/vue-ganttastic/blob/master/docs/GGanttChart.md#color-schemes -->
      <div v-for="vehicle in vehicles"> <!-- create a row for each vehicle -->
        <g-gantt-row  :label="vehicle" :bars="generatedBars.get(vehicle)"/>
      </div>
    </g-gantt-chart>
    <div v-if="previewMode" class="float-right m-5">
      <b-button variant="secondary" size="lg" @click="finishPreview(false)"> Zurück </b-button>
      <b-button variant="primary" size="lg" @click="finishPreview(true)"> Speichern </b-button>
    </div>
    <div class="float-right m-5 d-flex justify-content-end">
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