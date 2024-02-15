<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {Vehicle} from "@/main";
  import {type Ref, ref, watch} from "vue";

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
  let page: Ref<number> = ref(0)
</script>

<template>
  <BModal v-if="true" id="edit-dialog" @on-cancel="hide(); page = 0" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Fahrzeug bearbeiten</h3>
    </div>
    <br>
    <BForm @submit="emit('updateVehicle', res); hide()">
      <div :class="page == 0 ? 'd-block' : 'd-none'">
        <BFormRow>
          <BCol class="col-3">
            <b-form-floating-label label-for="licensePlate">Kennzeichen*</b-form-floating-label>
            <BFormInput v-model="res.licensePlate" id="licensePlate" placeholder="DE-12345" required></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="brand">Marke</b-form-floating-label>
            <BFormInput v-model="res.brand" id="brand" placeholder="Opel"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="model">Modell</b-form-floating-label>
            <BFormInput v-model="res.model" id="model" placeholder="Corsa E"></BFormInput>
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol>
            <b-form-floating-label label-for="password">Momentane Jahresleistung (in km)*</b-form-floating-label>
            <BFormInput v-model="res.mileage" id="mileage" required></BFormInput> <!-- !TODO! Only allow numbers -->
          </BCol>
          <BCol>
            <b-form-floating-label label-for="annualPerformance">Erwartete Jahresleitstung (in km)*</b-form-floating-label>
            <BFormInput v-model="res.annualPerformance" id="annualPerformance" required></BFormInput> <!-- !TODO! Only allow numbers -->
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol>
            <b-form-floating-label label-for="type">Typ</b-form-floating-label>
            <BFormInput v-model="res.type" id="type" placeholder="Kleinwagen"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="chassisNumber">Identifikationsnummer</b-form-floating-label>
            <BFormInput v-model="res.chassisNumber" id="chassisNumber" placeholder="WOL000036"></BFormInput>
          </BCol>
        </BFormRow>
      </div>
      <div :class="page == 1 ? 'd-block' : 'd-none'">
        <BFormRow>
          <BCol class="col-6">
            <b-form-floating-label label-for="insuranceNumber">Versicherungsnummer</b-form-floating-label>
            <BFormInput v-model="res.insurance.number" id="insuranceNumber" placeholder="123456789"></BFormInput>
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol>
            <b-form-floating-label label-for="registrationDate">Registrierungsdatum</b-form-floating-label>
            <BFormInput v-model="res.insurance.registrationDate" type="date" id="registrationDate" placeholder=""></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="expirationDate">Ablaufdatum (Registrierung)</b-form-floating-label>
            <BFormInput v-model="res.insurance.expiration" type="date" id="expirationDate"></BFormInput>
          </BCol>
        </BFormRow>
      </div>
      <div :class="page == 2 ? 'd-block' : 'd-none'">
        <BFormRow>
          <BCol>
            <b-form-floating-label label-for="numberAral">Tankkartennummer (Aral)</b-form-floating-label>
            <BFormInput v-model="res.gasCard.numberAral" id="numberAral" placeholder="123456789"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="numberShell">Tankkartennummer (Shell)</b-form-floating-label>
            <BFormInput v-model="res.gasCard.numberShell" id="numberShell" placeholder="123456789"></BFormInput>
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol>
            <b-form-floating-label label-for="holder">Halter</b-form-floating-label>
            <BFormInput v-model="res.gasCard.holder" id="holder" placeholder="mmustermann"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="pin">Pin</b-form-floating-label>
            <BFormInput v-model="res.gasCard.pin" type="password" id="pin"></BFormInput>
          </BCol>
        </BFormRow>
      </div>
      <div :class="page == 3 ? 'd-block' : 'd-none'">
        <BFormRow>
          <BCol>
            <b-form-floating-label label-for="purchaseDate">Kaufdatum</b-form-floating-label>
            <BFormInput v-model="res.prcing.purchaseDate" type="date" id="purchaseDate"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="listPriceGross">Listenpreis (Brutto)</b-form-floating-label>
            <BFormInput v-model="res.prcing.listPriceGross" id="listPriceGross" placeholder="30000"></BFormInput>
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol class="col-6">
            <b-form-floating-label label-for="leasingCostNet">Mietkosten (Netto)</b-form-floating-label>
            <BFormInput v-model="res.prcing.leasingCostNet" id="leasingCostNet" placeholder="150"></BFormInput>
          </BCol>
        </BFormRow>
      </div>
      <b-row class="mt-4">
        <b-col>
          <b-button type="button" @click="page--" variant="primary" class="mr-2" :disabled="page == 0">Zurück</b-button>
          <b-button type="button" @click="page++" variant="primary" class="mr-2" :disabled="page == 3">Weiter</b-button>
        </b-col>
        <b-col class="text-right">
          <b-button type="submit" variant="primary" class="mr-2">Änderungen speichern</b-button>
          <b-button type="button" @click="hide(); page = 0" variant="secondary">Abbrechen</b-button>
        </b-col>
      </b-row>
    </BForm>
  </BModal>
</template>