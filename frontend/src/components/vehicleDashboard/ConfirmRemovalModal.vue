<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";

  const {hide} = useModal('confirmation-dialog')

  defineEmits<{
    (event: "removeVehicle"): void
  }>()
</script>

<template>
  <BModal v-if="true" id="confirmation-dialog" @on-cancel="hide()" size="sm" hide-header hide-footer>
    <b-row>
      <b-col>
        <p class="m-0 text-center">Möchten Sie die Auswahl löschen?</p>
      </b-col>
    </b-row>
    <b-row>
      <b-col>
        <b-button class="mt-3" size="sm" variant="danger" block @click="$emit.call(null, 'removeVehicle'); hide()">Ja
        </b-button>
      </b-col>
      <b-col>
        <b-button class="mt-3" size="sm" block @click="hide()">Nein</b-button>
      </b-col>
    </b-row>
  </BModal>
</template>