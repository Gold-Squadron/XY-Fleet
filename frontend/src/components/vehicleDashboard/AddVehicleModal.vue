<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {Vehicle} from "@/main";

  const {hide} = useModal('creation-dialog')

  defineEmits<{
    (event: "createVehicle", newVehicle: Vehicle): void
  }>()

  let res = new Vehicle()
  function createVehicle(): Vehicle {

    // This ID is used for reference in the table (not in the db)
    // Generate id
    let id: String = ''

    while (true) {
      id = Math.random().toString(16)
      id = id.substring(id.length - 9)

      if (checkId(id)) {
        break
      }
    }

    res.setUiId(id)
    return res
  }

  function checkId(id: String): boolean {
    // !Todo! Check if id is in use
    return true
  }
</script>

<template>
  <BModal v-if="true" id="creation-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Fahrzeug hinzufügen</h3>
    </div>
    <br>
    <BForm @submit="$emit.call(null, 'createVehicle', createVehicle()); hide()">
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
          <b-button type="submit" variant="primary" class="mr-2">Fahrzeug hinzufügen</b-button>
          <b-button type="button" @click="hide()" variant="secondary">Abbrechen</b-button>
        </b-col>
      </b-row>
    </BForm>
  </BModal>
</template>