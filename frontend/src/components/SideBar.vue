<script setup lang="ts">
  import {Views} from "@/main"

  const emit = defineEmits<{
    (event: 'changeTab', to: Views): void
  }>()

  function changeTab(view: Views): void{
    emit('changeTab', view)
  }
</script>

<template>
  <div class="sidebar-container text-center shadow-custom pt-3 d-flex flex-column">
    <i @click="changeTab(Views.CALENDAR)" class="bi bi-calendar-event"></i>
    <i @click="changeTab(Views.ROADMAP)" class="bi bi-table"></i>
    <i @click="changeTab(Views.VEHICLE_DASHBOARD)" class="bi bi-car-front-fill"></i>
    <i @click="changeTab(Views.USER_MANAGEMENT)" class="bi bi-person-rolodex"></i>
  </div>
</template>


<style scoped>
   .sidebar-container{
     width: 53px;
     max-width: 53px;
     flex-grow: 1;
     background-color: #d2d2d2;
     z-index: 1;
   }
   i{
     transition: background-color 0.1s;
     cursor: pointer;
     font-size: 25px;
     padding: 3px;
     margin: 4px 0;
   }
   .sidebar-container > i:hover{
     background-color: #949494;
   }
   .shadow-custom{
     box-shadow: 0 0 20px 0 black;
   }
</style>