<script setup lang="ts">
  import {BButton, BModal} from "bootstrap-vue-next";
  import {ref} from "vue";
  import type {Ref} from "vue";
  import LoginWindow from "@/components/LoginWindow.vue";

  const show : Ref<boolean> = ref(false)
</script>

<template>
  <div class="center-content">
    <LoginWindow/>
  </div>

  <div v-if="false">
    <BButton variant="primary" @click="show = !show">Click me</BButton>
    <BModal v-model="show">Test</BModal>
  </div>
</template>

<style scoped>
  .center-content {
    display: grid;

    width: 100vw;
    height: 100vh;

    justify-content: center;
    align-content: center;
  }
</style>
