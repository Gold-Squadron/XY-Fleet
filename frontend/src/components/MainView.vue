<script setup lang="ts">
import Header from "@/components/Header.vue";
import SideBar from "@/components/SideBar.vue";
import VehicleDashboard from "@/components/VehicleDashboard.vue";
import { ref } from "vue"
import { Views } from "@/main"
import UserManagement from "@/components/UserManagement.vue";

const currentView = ref(Views.NONE)

function setCurrentView(to : Views) : void {
  currentView.value = to;
}
</script>

<template>
  <Header/>
  <div class="d-flex h-100 flex-grow-1 overflow-hidden">
    <SideBar @changeTab="setCurrentView"/>

    <!-- Container for the different tabs -->
    <div class="w-100 overflow-y-scroll">
      <VehicleDashboard v-if="currentView == Views.VEHICLE_DASHBOARD"/>
      <UserManagement v-else></UserManagement>
    </div>
  </div>
</template>

<style scoped>
  .overflow-y-scroll{
    overflow-y: scroll;
  }
</style>