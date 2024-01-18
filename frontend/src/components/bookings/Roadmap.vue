<script setup lang="ts">
import {onMounted, ref} from 'vue';

const dayWidth = 1.5 * parseFloat(getComputedStyle(document.documentElement).fontSize);
let vehicles = ref(["Blue Van", "Red Van", "Green Smart", "Phillip's broken e-scooter"]);

class Booking {
  constructor(public car: String, public start: number, public end : number) {}
  isWithin(day : number) : Boolean {
    return this.start <= day && this.end >= day;
  }
}

let bookings = ref([new Booking("Blue Van", 3, 7)])

function isWithin(vehicle : string, day : number) : Boolean {
  return bookings.value.every((car) => car.car == vehicle && car.isWithin(day));
}

function showBookingDialog(carIndex : number,startDay: number, endDay: number) {
  if(startDay > endDay) [startDay, endDay] = [endDay, startDay]

  bookings.value.push(new Booking(vehicles.value[carIndex], startDay, endDay));

}

function afterLoad() {
  for (let index = 0; index < vehicles.value.length; index++) {
    console.log(`init ${index}`);
    const container : HTMLElement  = document.querySelector('#el' + index)!;
    //if(container == null) return;
    const button : HTMLElement  = document.querySelector('#btn' + index)!;
    //if(ball == null) return;
    const startPos : number = button.getBoundingClientRect().x;
    container.onmousemove = function (event : MouseEvent) {
      let x = event.clientX;
      x = x - x % dayWidth;
      if(x < startPos || isWithin(vehicles.value[index], x)) return;
      button.style.position = "absolute";
      button.style.left = `${x}px`;
    }
    container.onmouseout = function (event : MouseEvent) {
      console.log("out")
      //const relatedTargetAsHTML : HTMLElement = event.relatedTarget;
      if(event.relatedTarget && event.relatedTarget.id == button.id) {
        return;
      }
      button.style.position = "initial";
    }

    let startDay : number = 0;
    button.onmousedown = function (event : MouseEvent) {
      startDay = Math.round((event.x - startPos) / dayWidth);
    }
    button.onmouseup = function (event : MouseEvent) {
      showBookingDialog(index, startDay, Math.round((event.x - startPos) / dayWidth));
    }
  }
}

onMounted(() => afterLoad());
</script>

<template class="" >
  <BTableSimple  class="roadmap-main-comp table-bordered " responsive>
    <BThead class="table-dark">
      <BTr>
        <BTh>Cars</BTh>
        <BTh v-for='m in ["Jan"]'>
            <div style="">
              <span>{{m}}</span>
              <br>
              <span v-for="i in 31" :style="'text-align: center; display: inline-block; width: ' + dayWidth + 'px;'">
                {{i}}
              </span>
            </div>
          </BTh>
      </BTr>
    </BThead>
    <BTbody class="table-dark">
      <BTr v-for="(str, index) in vehicles" v-bind:id="'el' + index">
        <BTh >{{str}}</BTh>
        <BTd style="width: 85vw" colspan="2">
          <div v-if="true" style="height: 30px">
<!--            <BBadge bg-variant="info">+</BBadge>-->
            <div class="h-100 position-absolute">
              <span class="badge badge-warning position-relative" v-for="b in bookings.filter((el) => el.car === str)"
                    :style="`top: 0px;width: ${dayWidth * (b.end - b.start + 1)}px; left: ${dayWidth * (b.start - 0.5)}px`" >
                {{ b.start + ((b.start != b.end) ? (" - " + b.end) : "")}}
              </span>
            </div>
            <BButton class="small-rm-btn" variant="outline-primary" v-bind:id="'btn' + index">+</BButton>
          </div>
        </BTd>
      </BTr>
    </BTbody>
  </BTableSimple >
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