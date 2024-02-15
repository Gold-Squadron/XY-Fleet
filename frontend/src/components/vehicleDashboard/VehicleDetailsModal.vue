<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {ref, watch} from "vue";
  import {Vehicle} from "@/main";

  const {hide} = useModal('details-dialog')

  const props = defineProps({
    vehicle: null
  })

  watch(() => props.vehicle, () => {
    vehicle.value = props.vehicle
  })

  const hidingPin: string = '*******'
  let vehicle = ref(new Vehicle())

  // only temporarily
  let canSeePin: boolean = true
</script>

<template>
  <BModal v-if="true" id="details-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Fahrzeugdetails</h3>
    </div>
    <br>
    <b-row>
      <b-col>
        <h6>Fahrzeuginformationen</h6>
        <b-row>
          <b-col class="col-6">
            <p>Kennzeichen</p>
            <p>Modell</p>
            <p>IdNr.</p>
            <p>Leistung (km)</p>
            <p>Erwartete Leistung (km)</p>
          </b-col>
          <b-col>
            <p>{{ vehicle.licensePlate }}</p>
            <p>{{ vehicle.brand + ' ' + vehicle.model }}</p>
            <p>{{ vehicle.chassisNumber }}</p>
            <p>{{ vehicle.mileage }}</p>
            <p>{{ vehicle.annualPerformance }}</p>
          </b-col>
        </b-row>
      </b-col>
      <b-col>
        <h6>Registrierung</h6>
        <b-row>
          <b-col class="col-6">
            <p>Versicherungsnummer</p>
            <p>Registrierungsdatum</p>
            <p>Ablaufdatum</p>
          </b-col>
          <b-col>
            <p>{{ vehicle.insurance.number }}</p>
            <p>{{ vehicle.insurance.registrationDate }}</p>
            <p>{{ vehicle.insurance.expiration }}</p>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
    <b-row class="mt-3">
      <b-col>
        <h6>Tankkarteninformation</h6>
        <b-row>
          <b-col class="col-5">
            <p>Nummer (Aral)</p>
            <p>Nummer (Shell)</p>
            <p>Halter</p>
            <p>Pin</p>
          </b-col>
          <b-col>
            <p>{{ vehicle.gasCard.numberAral }}</p>
            <p>{{ vehicle.gasCard.numberShell }}</p>
            <p>{{ vehicle.gasCard.holder }}</p>
            <p>{{ canSeePin ? vehicle.gasCard.pin : hidingPin }}</p>
          </b-col>
        </b-row>
      </b-col>
      <b-col>
        <h6>Kosten</h6>
        <b-row>
          <b-col class="col-6">
            <p>Kaufdatum</p>
            <p>Listenpreis</p>
            <p>Mietkosten</p>
          </b-col>
          <b-col>
            <p>{{ vehicle.prcing.purchaseDate }}</p>
            <p>{{ vehicle.prcing.listPriceGross }}</p>
            <p>{{ vehicle.prcing.leasingCostNet }}</p>
          </b-col>
        </b-row>
      </b-col>
    </b-row>
    <b-row class="mt-4 text-right">
      <b-col>
        <b-button type="button" @click="hide()" variant="secondary">Schließen</b-button>
      </b-col>
    </b-row>
  </BModal>
</template>

<style scoped>
  p {
    margin-bottom: 5px;
    font-size: 92.5%;
    opacity: 0.8;
    min-height: 22px;
  }

  h6 {
    width: 90%;
    padding-bottom: 5px;
    font-size: 110%;
    border-bottom: 1px solid rgb(170, 170, 170);
  }
</style>