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
          label: "Lorem ipsum dolor"
      }
    }
    map.get(booking.car)?.push(x);
  })
  return map;
})
const row1BarList = ref([
  {
    myBeginDate: "2021-07-13 13:00",
    myEndDate: "2021-07-13 19:00",
    ganttBarConfig: {
      // each bar must have a nested ganttBarConfig object ...
      id: "unique-id-1", // ... and a unique "id" property
      label: "Lorem ipsum dolor"
    }
  }
])
const row2BarList = ref([
  {
    myBeginDate: "2021-07-13 00:00",
    myEndDate: "2021-07-14 02:00",
    ganttBarConfig: {
      id: "another-unique-id-2",
      hasHandles: true,
      label: "Hey, look at me",
      style: {
        // arbitrary CSS styling for your bar
        background: "#e09b69",
        borderRadius: "20px",
        color: "black"
      },
      class: "foo" // you can also add CSS classes to your bars!
    }
  }
])

onMounted(() => afterLoad());
</script>

<template class="" >
  <div class="align-self-auto">
    <g-gantt-chart
        chart-start="2021-07-12 12:00"
        chart-end="2021-08-14 12:00"
        precision="day"
        bar-start="myBeginDate"
        bar-end="myEndDate"
        width="95%">
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