<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {GasCard, Insurance, Pricing, Vehicle} from "@/main";
  import {type Ref, ref} from "vue";

  const {hide} = useModal('creation-dialog')

  defineEmits<{
    (event: "createVehicle", newVehicle: Vehicle): void
  }>()

  let res: Vehicle = new Vehicle()
  let resGasCard: GasCard = new GasCard()
  let resInsurance: Insurance = new Insurance()
  let resPricing: Pricing = new Pricing()
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
    res.gasCard = resGasCard
    res.insurance = resInsurance
    res.prcing = resPricing

    return res
  }

  function checkId(id: String): boolean {
    // !Todo! Check if id is in use
    return true
  }

  let page: Ref<number> = ref(0)
</script>

<template>
  <!-- !TODO! Only allow numbers in certain input fields -->
  <BModal v-if="true" id="creation-dialog" @on-cancel="hide(); page = 0" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Fahrzeug hinzufügen</h3>
    </div>
    <br>
    <BForm @submit="$emit.call(null, 'createVehicle', createVehicle()); hide(); page = 0">
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
            <BFormInput v-model="res.mileage" id="mileage" required></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="annualPerformance">Erwartete Jahresleitstung (in km)*</b-form-floating-label>
            <BFormInput v-model="res.annualPerformance" id="annualPerformance" required></BFormInput>
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
        <b-row class="mt-4 text-right">
          <b-col>
            <b-button type="button" @click="page++" variant="primary" class="mr-2">Weiter</b-button>
            <b-button type="button" @click="hide(); page = 0" variant="secondary">Abbrechen</b-button>
          </b-col>
        </b-row>
      </div>
      <div :class="page == 1 ? 'd-block' : 'd-none'">
        <BFormRow>
          <BCol class="col-6">
            <b-form-floating-label label-for="insuranceNumber">Versicherungsnummer</b-form-floating-label>
            <BFormInput v-model="resInsurance.number" id="insuranceNumber" placeholder="123456789"></BFormInput>
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol>
            <b-form-floating-label label-for="registrationDate">Registrierungsdatum</b-form-floating-label>
            <BFormInput v-model="resInsurance.registrationDate" type="date" id="registrationDate" placeholder=""></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="expirationDate">Ablaufdatum (Registrierung)</b-form-floating-label>
            <BFormInput v-model="resInsurance.expiration" type="date" id="expirationDate"></BFormInput>
          </BCol>
        </BFormRow>
        <b-row class="mt-4 text-right">
          <b-col>
            <b-button type="button" @click="page++" variant="primary" class="mr-2">Weiter</b-button>
            <b-button type="button" @click="hide(); page = 0" variant="secondary">Abbrechen</b-button>
          </b-col>
        </b-row>
      </div>
      <div :class="page == 2 ? 'd-block' : 'd-none'">
        <BFormRow>
          <BCol>
            <b-form-floating-label label-for="numberAral">Tankkartennummer (Aral)</b-form-floating-label>
            <BFormInput v-model="resGasCard.numberAral" id="numberAral" placeholder="123456789"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="numberShell">Tankkartennummer (Shell)</b-form-floating-label>
            <BFormInput v-model="resGasCard.numberShell" id="numberShell" placeholder="123456789"></BFormInput>
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol>
            <b-form-floating-label label-for="holder">Halter</b-form-floating-label>
            <BFormInput v-model="resGasCard.holder" id="holder" placeholder="mmustermann"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="pin">Pin</b-form-floating-label>
            <BFormInput v-model="resGasCard.pin" type="password" id="pin"></BFormInput>
          </BCol>
        </BFormRow>
        <b-row class="mt-4 text-right">
          <b-col>
            <b-button type="button" @click="page++" variant="primary" class="mr-2">Weiter</b-button>
            <b-button type="button" @click="hide(); page = 0" variant="secondary">Abbrechen</b-button>
          </b-col>
        </b-row>
      </div>
      <div :class="page == 3 ? 'd-block' : 'd-none'">
        <BFormRow>
          <BCol>
            <b-form-floating-label label-for="purchaseDate">Kaufdatum</b-form-floating-label>
            <BFormInput v-model="resPricing.purchaseDate" type="date" id="purchaseDate"></BFormInput>
          </BCol>
          <BCol>
            <b-form-floating-label label-for="listPriceGross">Listenpreis (Brutto)</b-form-floating-label>
            <BFormInput v-model="resPricing.listPriceGross" id="listPriceGross" placeholder="30000"></BFormInput>
          </BCol>
        </BFormRow>
        <BFormRow class="mt-3">
          <BCol class="col-6">
            <b-form-floating-label label-for="leasingCostNet">Mietkosten (Netto)</b-form-floating-label>
            <BFormInput v-model="resPricing.leasingCostNet" id="leasingCostNet" placeholder="150"></BFormInput>
          </BCol>
        </BFormRow>
        <b-row class="mt-4 text-right">
          <b-col>
            <b-button type="submit" variant="primary" class="mr-2">Fahrzeug hinzufügen</b-button>
            <b-button type="button" @click="hide(); page = 0" variant="secondary">Abbrechen</b-button>
          </b-col>
        </b-row>
      </div>
    </BForm>
  </BModal>
</template>