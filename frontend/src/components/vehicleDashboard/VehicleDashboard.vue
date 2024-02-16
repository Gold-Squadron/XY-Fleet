<!--suppress VueUnrecognizedSlot -->
<script setup lang="ts">
  import {type Ref, ref, toRaw} from 'vue';
  import {type TableItem, useModal} from "bootstrap-vue-next";
  import { Insurance, Pricing, Vehicle} from "@/main";
  import AddVehicleModal from "@/components/vehicleDashboard/AddVehicleModal.vue";
  import ConfirmRemovalModal from "@/components/vehicleDashboard/ConfirmRemovalModal.vue";
  import EditVehicleModal from "@/components/vehicleDashboard/EditVehicleModal.vue";
  import VehicleDetailsModal from "@/components/vehicleDashboard/VehicleDetailsModal.vue";
  import {
    getAllXYWings,
    getInsurance,
    getPricing,
    addWing,
    editWing,
    editInsurance,
    editPricing,
    getAllInsurances,
    getAllPricings,
    removeInsurance,
    removeWing,
    removePricing, addPricing, addInsurance
  } from "@/components/vehicleDashboard/VehicleDashboardRestCalls";
  import {getAllBookings, removeBooking} from "@/components/userManagement/UsermanagementRestCalls";

  let editeVehicleId: Ref<string> = ref('')
  let editedVehicle: Ref<Vehicle | null> = ref(null)

  function showModal(id: string, vehicleId: string | null = null): void {
    if (vehicleId) {
      editeVehicleId.value = vehicleId
      editedVehicle.value = getVehicleById(vehicleId)
    }

    const {show} = useModal(id)
    show()
  }

  let vehicles: Ref<Vehicle[]> = ref([])
  let selectedIds: Ref<String[]> = ref([])

  // Load data from database
  function loadAllWings(): void {
    vehicles.value = []
    getAllXYWings().then(res => {
          res.forEach(wing => {
            loadWing(wing)
          })
          vehiclesConverted.value = convertVehicleData()
        }
    )
  }

  function loadWing(w: any): void {
    let wing = new Vehicle(w.id, w.license_plate, w.brand, w.model, w.chassis_number, w.mileage, w.annual_performance, w.type)

    // Load rest of data
    getInsurance(w.insurance_id).then(i => {
      // !TODO! Change from number to date
      wing.insurance = new Insurance(i.insurance_number, i.registration_date, i.insurance_number_expiration)
    })
    getPricing(w.pricing_id).then(p => {
      wing.pricing = new Pricing(p.purchase_date, p.list_price_gross, p.leasing_installment_net)
    })

    vehicles.value.push(wing)
  }

  loadAllWings()


  // Convert the raw data into the rendering format
  let vehiclesConverted: Ref<TableItem[]> = ref([])

  function convertVehicleData(): TableItem[] {
    let dataConverted: TableItem[] = []
    let dataRaw = vehicles.value

    dataRaw.forEach((data) => {
      let dataObj: { [key: string]: string | boolean | number } = {}
      let values: any = toRaw(data)

      Object.keys(data).forEach(param => {
        dataObj[param] = values[param]
      })

      dataConverted.push(dataObj)
    })

    return dataConverted
  }

  vehiclesConverted.value = convertVehicleData()

  function addVehicle(vehicle: Vehicle): void {
    // Add vehicle do database
    addPricing(vehicle.pricing).then(p => {
      addInsurance(vehicle.insurance).then(i => {
        addWing(vehicle, p.id, i.id).then(() => {
          loadAllWings()
        })
    })
      })

    // Update UI
    vehiclesConverted.value = []
    vehicles.value.push(vehicle)
    vehiclesConverted.value = convertVehicleData()
  }

  function removeVehicle(): void {
    // Remove vehicle from database
    selectedIds.value.forEach(id => {
      let wing = getVehicleById("" + id)
      removeSingleWing(Number(id), wing.insurance.id, wing.pricing.id)
    })

    // Update UI
    vehiclesConverted.value = []
    vehicles.value = vehicles.value.filter(vehicle => !selectedIds.value.includes(vehicle.getUiId()))
    vehiclesConverted.value = convertVehicleData()

    selectedIds.value = []

    changeAll(true)
  }

  function removeSingleWing(wingId: number, insuranceId: number, pricingId: number): void{
    // !TODO! Yes, this is currently a mess. But it works (for now). Will be fixed after the presentation

    // Remove insurance, pricing and bookings from the vehicle
    getAllBookings().then(res => {
      let bookingsDelete = res.filter(r => r.vehicle_id == Number(wingId))

      bookingsDelete.forEach(booking => {
        removeBooking(booking.id)
      })

      getAllInsurances().then(res => {
        let insurancesDelete = res.filter(r => r.id == Number(insuranceId))

        insurancesDelete.forEach(insurance => {
          removeInsurance(insurance.id)
        })
        getAllPricings().then(res => {
          let pricingsDelete = res.filter(r2 => r2.id == Number(pricingId))

          pricingsDelete.forEach(pricing => {
            removePricing(pricing.id)
          })

          removeWing(Number(wingId)).then(res => {
            removeInsurance(res.insurance_id)
            removePricing(res.pricing_id)
          })
        })
      })
    })
  }

  function selectRow(index: number, forceDeselect: boolean = false): void {
    let id: String = vehicles.value[index].getUiId()
    let idIndex: number = selectedIds.value.indexOf(id)
    let addHighlight: boolean = (idIndex == -1) && (!forceDeselect)

    if (addHighlight) {
      selectedIds.value.push(id)
    } else {
      selectedIds.value.splice(idIndex, 1)
    }
    highlightRow(index, addHighlight)
  }

  function changeAll(forceDeselect: boolean = false): void {
    let deselect: boolean = (selectedIds.value.length == vehicles.value.length) || forceDeselect
    selectedIds.value = []

    for (let i = 0; i < vehicles.value.length; i++) {
      selectRow(i, deselect)
    }
  }

  function highlightRow(index: number, mark: boolean = true): void {
    const SELECTION_COLOR: string = '#9a9a9a45'
    const table: HTMLElement | null = document.getElementById('vehicleTable')

    if (!table) {
      return
    }

    let row: any = table.children[1].children[index]

    row.style.backgroundColor = mark ? SELECTION_COLOR : ''
  }

  function editVehicle(data: Vehicle): void {
    editWing(data).then(res => {
      const pricingId = res.pricing_id
      const insuranceId = res.insurance_id

      editPricing(data, pricingId)
      editInsurance(data, insuranceId)
    })

    vehiclesConverted.value = convertVehicleData()
  }

  function getVehicleById(id: string): any {
    return vehicles.value.find(vehicle => vehicle.getUiId() == id)
  }
</script>

<template>
  <div class="pl-3">
    <b-button variant="primary" size="md" @click="showModal('creation-dialog')" class="mt-4 mb-3">Fahrzeug hinzufügen
    </b-button>
    <b-button variant="primary" size="md" @click="showModal('confirmation-dialog')" :disabled="selectedIds.length == 0"
              class="ml-3 mt-4 mb-3">Fahrzeug entfernen
    </b-button>

    <b-table id="vehicleTable" :fields="fields" :items="vehiclesConverted">
      <template #head(cb)="">
        <b-form-checkbox @change="changeAll()"
                         :checked="(selectedIds.length == vehicles.length) && (vehicles.length != 0)"
                         id="selectAllCheckbox"></b-form-checkbox>
      </template>
      <template #head(editRow)=""></template>
      <template #head(showDetails)=""></template>
      <template #cell(cb)="data:any">
        <b-form-checkbox :id="`rowCheckbox-${data.index}`" :checked="selectedIds.includes(data.item['uiId'])"
                         @change="selectRow(data.index)"></b-form-checkbox>
      </template>
      <template #cell(editRow)="data: any">
        <i @click="showModal('edit-dialog', data.item['uiId'])" class="bi bi-pencil mr-2"></i>
      </template>
      <template #cell(showDetails)="data: any">
        <i @click="showModal('details-dialog', data.item['uiId'])" class="bi bi-info-circle mr-5"></i>
      </template>
    </b-table>
  </div>

  <AddVehicleModal @createVehicle="addVehicle"/>
  <ConfirmRemovalModal @removeVehicle="removeVehicle"/>
  <EditVehicleModal @updateVehicle="editVehicle" :vehicle="editedVehicle"/>
  <VehicleDetailsModal :vehicle="editedVehicle"/>

</template>

<style scoped>
  .bi-pencil:hover, .bi-info-circle:hover {
    cursor: pointer;
    opacity: 0.6;
  }

  *{
    color: white
  }
</style>

<script lang="ts">
  export default {
    data() {
      return {
        fields: [
          {key: 'cb',                thStyle: {width: '25px'}},
          {key: 'model',             label: 'Modell'},
          {key: 'brand',             label: 'Marke'},
          {key: 'licensePlate',      label: 'Kennzeichen'},
          {key: 'mileage',           label: 'Momentane Leitstung (km)'},
          {key: 'annualPerformance', label: 'Jährliche Leistung (km)'},
          {key: 'chassisNumber',     label: 'Identifikationsnummer'},
          {key: 'editRow',           thStyle: {width: '25px'}},
          {key: 'showDetails',       thStyle: {width: '25px'}}
        ]
      }
    }
  }
</script>