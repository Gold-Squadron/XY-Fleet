<script setup lang="ts">
import {computed, onMounted, type Ref, ref} from 'vue';
import {type GanttBarObject, GGanttRow} from "@infectoone/vue-ganttastic";
import CreateBookingModal from "@/components/bookings/CreateBookingModal.vue";
import {useModal} from "bootstrap-vue-next";
import {Booking} from "@/main";

const dayWidth = 1.5 * parseFloat(getComputedStyle(document.documentElement).fontSize);
let vehicles = ref(["Blue Van", "Red Van", "Green Smart", "Phillip's broken e-scooter", "No Smart", "Clown Car"]);

declare global {
  interface Date {
    translateDays(days : number): Date;
  }
}

Date.prototype.translateDays = function (days : number): Date {
  return new Date(this.getTime() + days * 24 * 60 * 60 * 1000);
};



let bookings = ref([new Booking("Blue Van", new Date(Date.now()), new Date("2024-02-2 19:00"), "Amokfahrt")])
bookings.value.push(new Booking("Green Smart", new Date("2024-02-2 10:00"), new Date("2024-02-3 24:00")));

function isWithin(vehicle : string, day : number) : Boolean {
  return bookings.value.every((car) => car.car == vehicle && car.isWithin(day));
}

function showBookingDialog(carIndex : number, startDay: Date, endDay: Date) {
  if(startDay > endDay) [startDay, endDay] = [endDay, startDay]

  bookings.value.push(new Booking(vehicles.value[carIndex], startDay, endDay));

}

let chartStart : Ref<Date> = ref(new Date().translateDays(-4));
let chartEnd = computed(() => {return chartStart.value.translateDays(31)})


// any due to damn event browser support
function MouseWheelHandler(inp : any) : boolean {
  const e = window.event || inp; // old IE support
  const delta = Math.max(-1, Math.min(1, (e.wheelDelta || -e.detail)));

  console.log(delta)

  chartStart.value = chartStart.value.translateDays(delta * 1/4)

  return false;
}

function afterLoad() {
  let scrollable = document.getElementById("roadmap");
  if (typeof scrollable != null) {
    scrollable?.addEventListener("mousewheel", MouseWheelHandler, false);
    scrollable?.addEventListener("DOMMouseScroll", MouseWheelHandler, false);
  }

}

const generatedBars = computed( () => {
  let map = new Map<string, GanttBarObject[]>();
  vehicles.value.forEach(name => map.set(name, []))
  bookings.value.forEach( (booking) => {
    let x : GanttBarObject = {
      myBeginDate: booking.start,
      myEndDate: booking.end,
      ganttBarConfig: {
          id: booking.car + booking.start,//booking.car + booking.start, // ... and a unique "id" property
          label: booking.reason ? booking.reason : booking.driver,
          hasHandles: false,
          class: "bar-normal"
      }
    }
    map.get(booking.car)?.push(x);
  })
  return map;
})

//TODO remove the parameter once the REST Interface works
function refresh(booking : Booking) : void {
  //alert(JSON.stringify(booking))
  bookings.value.push(booking);
}

function test(a : any) {
  console.log(a)
}

const onContextmenuBar = (bar: GanttBarObject, e: MouseEvent, datetime?: string) => {
  console.log("contextmenu-bar", bar, e, datetime)
}

onMounted(() => afterLoad());

const {show, hide, modal} = useModal('creation-dialog')
</script>

<template class="" >
  <p style="display: none" v-for="vehicle in vehicles">{{ generatedBars.get(vehicle) }}</p>
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
      <div v-for="vehicle in vehicles">
        <g-gantt-row  :label="vehicle" :bars="generatedBars.get(vehicle)"/>
      </div>
    </g-gantt-chart>
    <div class="float-right m-5">
      <b-button variant="primary" size="lg" @click="show"> Neue Fahrt eintragen </b-button>
    </div>
    <CreateBookingModal @createBooking="refresh" :cars="vehicles"/>
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

  /*TODO: figure out why this doesn't work automatically*/
  .text-bg-secondary {
    background: #407fb7 ;
  }
</style>