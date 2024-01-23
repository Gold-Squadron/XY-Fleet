<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";

  const {show, hide, modal} = useModal('creation-dialog')
</script>

<template>
  <BModal v-if="true" v-model="programmaticModal" id="creation-dialog">
    <BButton @click="hide">Hide me</BButton>
  </BModal>
</template>

<style scoped>

</style>