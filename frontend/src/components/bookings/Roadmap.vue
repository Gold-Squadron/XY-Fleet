<script setup lang="ts">
  import {computed, type CSSProperties, onMounted, type Ref, ref} from 'vue';
  import {type GanttBarObject, GGanttRow} from "@infectoone/vue-ganttastic";
  import {RefSymbol, type UnwrapRefSimple} from "@vue/reactivity"
  import CreateBookingModal from "@/components/bookings/CreateBookingModal.vue";
  import {useModal} from "bootstrap-vue-next";
  import {Booking} from "@/main";

  //Demodata
  let vehicles = ref(["STO-XY-01", "STO-XY-02", "STO-XY-03", "STO-XY-04", "STO-XY-31", "STO-XY-41", "STO-XY-59", "STO-XY-26",]);
  let bookings: Ref<Booking[]> = ref([])
  let additionalEvents = ["TÜV Termin", "Reparaturen", "Bereitschaft", "Auto nicht fahrbereit"]


bookings.value.push(new Booking("Green Smart", new Date("2024-02-2 10:00"), new Date("2024-02-3 24:00")));

  function pushAndGenerate(number: number, number2: number, s: string, s2: string, none: string, number3: number, status: string = "") {
    let val = new Booking(vehicles.value[number - 101], new Date(s), new Date(s2), none, ["lhelbig", "jwilleke", "nsimon", "laußem"][number2 - 100]);
    val.status = status;
    console.log(val)
    bookings.value.push(val)
  }

  pushAndGenerate(101, 100, '2024-03-01', '2024-03-07', 'Betriebsausflug', 1000);
  pushAndGenerate(102, 100, '2024-03-14', '2024-03-15', 'none', 200);
  pushAndGenerate(101, 100, '2024-03-20', '2024-03-30', 'Betriebsreise', 2800);
  pushAndGenerate(103, 101, '2024-03-05', '2024-03-05', 'none', 100);
  pushAndGenerate(103, 101, '2024-03-12', '2024-03-12', 'none', 100);
  pushAndGenerate(103, 101, '2024-03-19', '2024-03-19', 'none', 100);
  pushAndGenerate(103, 101, '2024-03-26', '2024-03-26', 'none', 100);
  pushAndGenerate(102, 101, '2024-03-20', '2024-03-24', 'Betriebsausflug', 600);
  pushAndGenerate(102, 102, '2024-03-10', '2024-03-18', 'Deutschlandtour', 1000);
  pushAndGenerate(101, 103, '2024-03-11', '2024-03-13', 'none', 200);
  pushAndGenerate(102, 103, '2024-03-20', '2024-03-25', 'none', 300);
  pushAndGenerate(105, 104, '2024-03-11', '2024-03-13', 'none', 200);
  pushAndGenerate(107, 100, '2024-03-20', '2024-03-25', 'none', 300);
  pushAndGenerate(107, 100, '2024-03-2', '2024-03-2', 'none', 300);
  pushAndGenerate(107, 100, '2024-03-9', '2024-03-9', 'none', 300);
  pushAndGenerate(106, 103, '2024-03-5', '2024-04-5', 'Nicht Betriebsfähig', 300, "broken");
  pushAndGenerate(108, 103, '2024-03-5', '2024-03-8', 'Ausflug', 300,);

  let chartStart: Ref<Date> = ref(new Date().translateDays(25));
  let chartEnd = computed(() => {
    return chartStart.value.translateDays(31)
  })

  let previewMode = false;
  let previewElement: GanttBarObject | undefined = undefined;

  /// shifts the time/day on the component when the mouse scrolls
  /// any due to damn event browser support
  function mouseWheelHandler(inp: any): boolean {
    const e = window.event || inp; // old IE support
    const delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));

    chartStart.value = chartStart.value.translateDays(delta * 1 / 4)

    return false;
  }

  //transforms the raw data into the rendering format
  const generatedBars = computed(() => {
    let map = new Map<string, GanttBarObject[]>();
    vehicles.value.forEach(name => map.set(name, []))
    bookings.value.forEach((booking) => {
      let stylingContent: CSSProperties = {
        borderRadius: "5px",
        background: "linear-gradient(117deg, rgba(97,217,3,1) 0%, rgba(21,124,0,1) 100%)",
        color: "white"
      };
      if (booking.status == "broken") stylingContent.background = "linear-gradient(117deg, rgba(217,3,3,1) 0%, rgba(124,29,0,1) 100%)";
      let label = booking.reason != 'none' ? booking.reason : booking.driver;
      if (booking.html) label = "";
      let x: GanttBarObject = {
        myBeginDate: booking.getStartDateAsReference(),
        myEndDate: booking.getEndDateAsReference(),
        ganttBarConfig: {
          id: booking.car + booking.start.getTime(), // ... and a unique "id" property
          label: label,
          hasHandles: booking.status === 'preview',
          immobile: booking.status !== 'preview',
          style: stylingContent,
          class: `bar-${booking.status}`,
          html: booking.html
        }
      }
      if (booking.status === 'preview') previewElement = x;
      map.get(booking.car)?.push(x);
    })
    return map;
  })

  //this is where we query the server for new information... IF WE HAD ANY
  //TODO remove the parameter once the REST Interface works
  function createVirtualBooking(booking: Booking): void {
    if (booking.status === "preview") previewMode = true;
    bookings.value.push(booking);
  }

  function refresh() {
    //I would put my REST-Call here... IF I HAD ANY
  }

  const {show, hide, modal} = useModal('creation-dialog')

  function createBookingRestCall(first: UnwrapRefSimple<Booking>) {
    //TODO Rest that call man
  }

  function finishPreview(save: boolean) {
    previewMode = false;

    let first: UnwrapRefSimple<Booking> | undefined = bookings.value.find((x) => x.status === 'preview');
    if (first == undefined || previewElement == undefined) {
      return;
    }
    const previewedStartDate = previewElement.myBeginDate;
    const previewedEndDate = previewElement.myEndDate;
    first.start = previewedStartDate;
    first.end = previewedEndDate;

    if (!save) {
      const index: number = bookings.value.findIndex((x) => x.status === 'preview');
      bookings.value.splice(index, 1);
      show();
      return;
    }

    first.status = 'booking'

    createBookingRestCall(first)
    previewElement = undefined;

  }

  function afterLoad() {
    let scrollable = document.getElementById("roadmap");
    if (typeof scrollable != null) { // For different browsers - I regret using HTML already
      scrollable?.addEventListener("mousewheel", mouseWheelHandler, false);
      scrollable?.addEventListener("DOMMouseScroll", mouseWheelHandler, false);
    }

  // $('#liveToast').toast('show')

  for (let booking in bookings.value) {

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
                 :highlight-sundays="true"
                 color-scheme="default"
                 @drag-bar="() => {
          chartStart = chartStart.translateDays(0);
          //I need a better way to achieve this, but 🤷 - if you remove this, the view isn't updated when you drag bars around.
        }">
    <div v-for="vehicle in vehicles"> <!-- create a row for each vehicle -->
      <g-gantt-row :label="vehicle" :bars="generatedBars.get(vehicle)" highlight-on-hover/>
    </div>
  </g-gantt-chart>

  <div v-if="previewMode" class="float-right m-5">
    <b-button variant="secondary" size="lg" @click="finishPreview(false)"> Zurück</b-button>
    <b-button variant="primary" size="lg" @click="finishPreview(true)"> Speichern</b-button>
  </div>
  <div v-else class="float-right m-5 d-flex justify-content-end">
    <div class="btn-group">
      <b-button variant="warning" size="lg">Defekt eintragen</b-button>
      <b-button variant="warning" class="dropdown-toggle dropdown-toggle-split" data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="false" size="lg">
        <span class="sr-only">Toggle Dropdown for more event options</span>
      </b-button>
      <div class="dropdown-menu">
        <a class="dropdown-item" href="#" v-for="ev in additionalEvents">{{ ev }}</a>
        <div class="dropdown-divider"></div>
        <a class="dropdown-item" href="#">Add a new type</a>

      </div>
      <b-button variant="primary" size="lg" @click="show" class="add-spacing rounded"> Neue Fahrt eintragen</b-button>
    </div>
    <CreateBookingModal @refresh="refresh" @createVirtualBooking="createVirtualBooking" :cars="vehicles"/>
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
  display: flex;
  flex-direction: row;
  margin: 0
}

.time-header p {
  margin-bottom: 0;
  margin-right: 0.5rem;
}

/* TODO: figure out why this doesn't work automatically */
.text-bg-secondary {
  background: #407fb7;
}

.add-spacing {
  margin-left: 20px !important;
}
</style>