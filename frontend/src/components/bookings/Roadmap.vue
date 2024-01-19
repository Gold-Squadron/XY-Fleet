<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import type {GanttBarObject} from "@infectoone/vue-ganttastic";

const dayWidth = 1.5 * parseFloat(getComputedStyle(document.documentElement).fontSize);
let vehicles = ref(["Blue Van", "Red Van", "Green Smart", "Phillip's broken e-scooter"]);

class Booking {
  constructor(public car: string, public start: Date, public end : Date) {}
  isWithin(day : number) : Boolean {
    return true; //his.start <= day && this.end >= day;
  }
}

let bookings = ref([new Booking("Blue Van", new Date(), new Date("2021-07-13 19:00"))])

function isWithin(vehicle : string, day : number) : Boolean {
  return bookings.value.every((car) => car.car == vehicle && car.isWithin(day));
}

function showBookingDialog(carIndex : number, startDay: Date, endDay: Date) {
  if(startDay > endDay) [startDay, endDay] = [endDay, startDay]

  bookings.value.push(new Booking(vehicles.value[carIndex], startDay, endDay));

}

function afterLoad() {
}
const xx = computed( () => {
  let map = new Map<string, GanttBarObject[]>();
  vehicles.value.forEach(name => map.set(name, []))
  bookings.value.forEach( (booking) => {
    let x : GanttBarObject = {
      myBeginDate: booking.start,
      myEndDate: booking.end,
      ganttBarConfig: {
          id: booking.car + booking.start, // ... and a unique "id" property
          label: "Lorem ipsum dolor",
          hasHandles: false,
          class: "bar-normal"
      }
    }
    map.get(booking.car)?.push(x);
  })
  return map;
})


const onContextmenuBar = (bar: GanttBarObject, e: MouseEvent, datetime?: string) => {
  console.log("contextmenu-bar", bar, e, datetime)
}

onMounted(() => afterLoad());
</script>

<template class="" >
  <div style="">
    <g-gantt-chart
        chart-start="2021-07-12 12:00"
        chart-end="2021-08-14 12:00"
        precision="day"
        bar-start="myBeginDate"
        bar-end="myEndDate"
        @contextmenu-bar="onContextmenuBar($event.bar, $event.e, $event.datetime)">
      <g-gantt-row v-for="vehicle in vehicles" :label="vehicle" :bars="xx.get(vehicle)" />
    </g-gantt-chart>
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