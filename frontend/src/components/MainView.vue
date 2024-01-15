<script setup lang="ts">
import Header from "@/components/Header.vue";
import SideBar from "@/components/SideBar.vue";
import Roadmap from "@/components/bookings/Roadmap.vue";
</script>

<template>
  <Header/>
  <div class="d-flex h-100">
    <SideBar/>
    <!-- Container for the different tabs -->
    <div class="flex-grow-1">
      <Roadmap/>
    </div>
  </div>
</template>

<style scoped>

</style>