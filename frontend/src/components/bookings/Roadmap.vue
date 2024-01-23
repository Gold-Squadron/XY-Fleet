<script setup lang="ts">
import {computed, onMounted, ref} from 'vue';
import {type GanttBarObject, GGanttRow} from "@infectoone/vue-ganttastic";
import CreateBookingDialog from "@/components/bookings/CreateBookingDialog.vue";
import CreateBookingModal from "@/components/bookings/CreateBookingModal.vue";
import {useModal} from "bootstrap-vue-next";

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

class Booking {
  constructor(public car: string, public start: Date, public end : Date) {}
  isWithin(day : number) : Boolean {
    return true; //his.start <= day && this.end >= day;
  }
}

let bookings = ref([new Booking("Blue Van", new Date(Date.now()), new Date("2024-02-2 19:00"))])
bookings.value.push(new Booking("Green Smart", new Date("2024-02-2 19:00"), new Date("2024-02-2 24:00")));

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
          id: booking.car + booking.start,//booking.car + booking.start, // ... and a unique "id" property
          label: "Lorem ipsum dolor",
          hasHandles: false,
          class: "bar-normal"
      }
    }
    console.log("Alu")
    console.log(x)
    map.get(booking.car)?.push(x);
  })
  return map;
})


const onContextmenuBar = (bar: GanttBarObject, e: MouseEvent, datetime?: string) => {
  console.log("contextmenu-bar", bar, e, datetime)
}

onMounted(() => afterLoad());

const {show, hide, modal} = useModal('creation-dialog')
</script>

<template class="" >
  <p style="display: none" v-for="vehicle in vehicles">{{xx.get(vehicle)}}</p>
  <div style="">
    <g-gantt-chart
        :chart-start="new Date().translateDays(-4)"
        chart-end="2024-02-14 12:00"
        precision="day"
        :grid="true"
        bar-start="myBeginDate"
        bar-end="myEndDate">
      <div v-for="vehicle in vehicles">
        <g-gantt-row  :label="vehicle" :bars="xx.get(vehicle)"/>
      </div>
    </g-gantt-chart>
    <div class="float-right m-5">
      <b-button variant="primary" size="lg" @click="show"> Neue Fahrt eintragen </b-button>
    </div>
    <CreateBookingModal/>
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