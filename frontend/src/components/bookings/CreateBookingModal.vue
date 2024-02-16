<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {computed, type ComputedRef, type Ref, ref, type WritableComputedRef} from "vue";
  import {addFlight, Booking, getPilots, getVehicles, type RPilot, type RXYWing} from "./RoadmapRestCalls";

  defineProps<{
    cars?: RXYWing[],
    type: string
  }>()

  let pilots: RPilot[] = await getPilots(); //no time to debug props
  let vehicles: RXYWing[] = await getVehicles(); //no time to debug props



  const emit = defineEmits<{
    createVirtualBooking: [booking : Booking]
    refresh: []
  }>()

  function getUserById(driverId: number) : string {
    if (isNaN(driverId)) return "If you see this, you f up"
    return pilots.find(pilot => pilot.id == driverId)?.name as string;
  }

  function getUserByName(driverName: string) : number {
    let pilot = pilots.find(pilot => pilot.name == driverName)?.id;
    return pilot ? pilot : -1;
  }

  function getCarByLicensePlate(plate: string) : number {
    let car = vehicles.find(c => c.license_plate == plate)?.id;
    return car ? car : -1;
  }

  function getCarById(id: number) : string {
    let car = vehicles.find(c => c.id == id)?.license_plate;
    return car ? car : "";
  }
  
  let res = ref(new Booking())
  let comp =
      {
        startDate: computed({
          get() : string {
            return res.value.start.toString();
          },
          // setter
          set(newValue : string) {
            res.value.start?.setTime(new Date(newValue).getTime())
          }
        }),
        endDate: computed({
          get() : string {
            return res.value.end.toString();
          },
          // setter
          set(newValue : string) {
            res.value.end?.setTime(new Date(newValue).getTime())
          }
        })
      }

  let driverName : WritableComputedRef<any> = computed({
    get() : string {
      if(res.value.driverId === -1) return "";
      return getUserById(res.value.driverId)
    },
    set(newValue : string) {
      res.value.driverId = getUserByName(newValue);
    }
  })

  let carPlate : WritableComputedRef<any> = computed({
    get() : string {
      if(res.value.carId === -1) return "";
      return getCarById(res.value.carId);
    },
    set(newValue : string) {
      res.value.carId = getCarByLicensePlate(newValue);
    }
  })

  const {show, hide, modal} = useModal('creation-dialog')

  function reset() {
    res.value = new Booking();
  }
  
  function preview() {
    let virtual : Booking = res.value.clone();
    virtual.status = "preview";
    emit('createVirtualBooking', virtual)
    hide(); // minimize the dialog temporarily
  }

  function addBooking() {
    //emit('createVirtualBooking', res.value.clone()) // DEBUG
    // [REST-Call] FINAL
    addFlight(res.value.asFlight()).then(x => {
      console.table(x)
      if(x.code && x.code != 200) {
        alert(x)
      } else {
        emit('refresh') //FINAL
        reset()
        hide()
      }
    })

  }
</script>

<template>
  <!-- TODO validation and proper autocomplete -->
  <BModal v-if="true" id="creation-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Neue Fahrt eintragen</h3>
    </div>
    <br>
    <BForm>
      <BFormRow>
        <BCol>
          <b-input-group v-if="type === 'booking'" prepend="@">
            <BFormInput id="driver" list="input-list" placeholder="Fahrer" v-model="driverName"></BFormInput>
            <b-tooltip target="driver" triggers="hover">
              Use their normal <b>black</b> name, e.g. mmustermann!
            </b-tooltip>
            <datalist id="input-list">
              <option v-for="el in pilots" :value="el.name">
                {{el.name}}
              </option>
            </datalist>
          </b-input-group>
          <b-input-group v-else>
            <BFormInput id="driver" list="input-list" placeholder="Typ" v-model="type"></BFormInput>
            <b-tooltip target="driver" triggers="hover">
              Code monkey needs coffee
            </b-tooltip>
            <datalist id="input-list">
              <option v-for="el in pilots" :value="el.name">
                {{el.name}}
              </option>
            </datalist>
          </b-input-group>
        </BCol>
        <BCol>
          <BFormInput id="input-with-list" list="car-datalist" placeholder="Fahrzeug" v-model="carPlate"> </BFormInput>
          <datalist id="car-datalist">
            <option v-for="el in cars" :value="el.license_plate" :label="el.license_plate"/>
          </datalist>
        </BCol>
      </BFormRow>
      <BFormRow>
        <BCol>
          <BFormGroup label="Start Date" for="start-date">
              <b-form-input id="start-date" type="date" v-model="comp.startDate.value"/>
          </BFormGroup>
        </BCol>
        <BCol>
          <BFormGroup label="End Date" for="start-date">
            <b-form-input id="end-date" type="date" v-model="comp.endDate.value"/>
          </BFormGroup>
        </BCol>
      </BFormRow>
      <BFormRow>
        <BCol cols="8">
          <BFormGroup label="Reason (optional):">
            <BFormInput id="reason" placeholder="I really like driving" v-model="res.reason"></BFormInput>
          </BFormGroup>
        </BCol>
        <BCol cols="4">
          <BFormGroup label="Geschätzte Strecke (in km):">
            <BFormInput id="mileage" placeholder="12" v-model="res.expected_travel_distance"></BFormInput>
          </BFormGroup>
        </BCol>
      </BFormRow>
      <hr/>
      <BButton @click="reset()" variant="outline-warning" class="float-left">Reset</BButton>
      <div class="d-flex justify-content-end">
        <BButton @click="hide()">Cancel</BButton>
        <BButton @click="preview()" variant="success">Preview</BButton>
        <BButton @click="addBooking()" variant="outline-primary">Hinzufügen</BButton>
      </div>
    </BForm>
  </BModal>
</template>

<style scoped>
button {
  margin: 1.5%;
}

</style>