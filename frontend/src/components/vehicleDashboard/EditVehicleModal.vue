<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {Vehicle} from "@/main";
  import {ref, watch} from "vue";

  const {hide} = useModal('edit-dialog')

  const emit = defineEmits<{
    (event: 'updateVehicle', data: any) : void
  }>()

  const props = defineProps({
    vehicle: null
  })

  watch(() => props.vehicle, () => {
    res.value = props.vehicle
  })

  let res = ref(new Vehicle())
</script>

<template>
  <BModal v-if="true" id="edit-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Fahrzeug bearbeiten</h3>
    </div>
    <br>
    <BForm @submit="emit('updateVehicle', res); hide()">
      <BFormRow>
        <BCol class="col-3">
          <b-form-floating-label label-for="licensePlate">Kennzeichen</b-form-floating-label>
          <BFormInput v-model="res.licensePlate" id="licensePlate" placeholder="DE-12345" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="brand">Marke</b-form-floating-label>
          <BFormInput v-model="res.brand" id="brand" placeholder="Opel" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="model">Modell</b-form-floating-label>
          <BFormInput v-model="res.model" id="model" placeholder="Corsa E" required></BFormInput>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol>
          <b-form-floating-label label-for="password">Momentane Jahresleistung (in km)</b-form-floating-label>
          <BFormInput v-model="res.mileage" id="mileage" required></BFormInput> <!-- !TODO! Only allow numbers -->
        </BCol>
        <BCol>
          <b-form-floating-label label-for="annualPerformance">Erwartete Jahresleitstung (in km)</b-form-floating-label>
          <BFormInput v-model="res.annualPerformance" id="annualPerformance" required></BFormInput> <!-- !TODO! Only allow numbers -->
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol>
          <b-form-floating-label label-for="type">Typ</b-form-floating-label>
          <BFormInput v-model="res.type" id="type" placeholder="Kleinwagen" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="chassisNumber">Identifikationsnummer</b-form-floating-label>
          <BFormInput v-model="res.chassisNumber" id="chassisNumber" placeholder="WOL000036" required></BFormInput>
        </BCol>
      </BFormRow>
      <b-row class="mt-4 text-right">
        <b-col>
          <b-button type="submit" variant="primary" class="mr-2">Änderungen speichern</b-button>
          <b-button type="button" @click="hide()" variant="secondary">Abbrechen</b-button>
        </b-col>
      </b-row>
    </BForm>
  </BModal>
</template>

<script lang="ts">
</script>