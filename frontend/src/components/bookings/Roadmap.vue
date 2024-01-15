<script setup lang="ts">
import { onMounted } from 'vue';

let vehicles = ["Blue Van", "Red Van", "Green Smart", "Phillip's broken e-scooter"];
const dayWidth = 12;

function afterLoad() {
  for (let index = 0; index < vehicles.length; index++) {
    console.log(`init ${index}`);
    const container = document.querySelector('#el' + index);
    const ball = document.querySelector('#btn' + index);
    ball.startPos = ball.getBoundingClientRect().x;
    container.addEventListener('mousemove', function (event) {
      let x = event.clientX;
      x = x - x % dayWidth;
      if(x < ball.startPos) return;
      ball.style.position = "absolute";
      ball.style.left = `${x}px`;
    }, false)
    container.onmouseout = function (event) {
      console.log("out")
      if(event.relatedTarget.id == ball.id) {
        return;
      }
      ball.style.position = "initial";
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
        <BTh>
          <div class="time-header">
            <div>
              <span>Jan</span>
              <br>
              <p> 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31</p>
            </div>
            <div>
              <span>Dec</span>
              <br>
              <p> 1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31</p>
            </div>
          </div>
        </BTh>
      </BTr>
    </BThead>
    <BTbody class="table-dark">
      <BTr v-for="(str, index) in vehicles" v-bind:id="'el' + index">
        <BTh >{{str}}</BTh>
        <BTd style="width: 85vw">
          <div v-if="true" style="height: 30px">
<!--            <BBadge bg-variant="info">+</BBadge>-->
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
    padding: 0.1rem 0.15rem;
  }

  .time-header {
    display: flex; flex-direction: row; margin: 0
  }
  .time-header p {
    margin-bottom: 0rem;
    margin-right: 0.5rem;
  }

  /*TODO: figure out why this doesn't work automatically*/
  .text-bg-secondary {
    background: #407fb7 ;
  }
</style>