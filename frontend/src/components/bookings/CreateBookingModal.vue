<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {computed, type Ref, ref} from "vue";
  import {Booking, getPilots, type RXYWing} from "./RoadmapRestCalls";

  defineProps<{
    cars?: RXYWing[]
  }>()



  const emit = defineEmits<{
    createVirtualBooking: [booking : Booking]
    refresh: []
  }>()
  
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

  const {show, hide, modal} = useModal('creation-dialog')

  let drivers = await getPilots();

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
    emit('createVirtualBooking', res.value.clone()) // DEBUG
    // [REST-Call] FINAL
    //emit('refresh') FINAL
    reset()
    hide()
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
          <b-input-group prepend="@">
            <BFormInput id="driver" list="input-list" placeholder="Fahrer" v-model="res.driverId"></BFormInput>
            <b-tooltip target="driver" triggers="hover">
              Use their normal <b>black</b> name, e.g. mmustermann!
            </b-tooltip>
            <datalist id="input-list">
              <option v-for="el in drivers" :value="el.id">
                {{el.name}}
              </option>
            </datalist>
          </b-input-group>
        </BCol>
        <BCol>
          <BFormInput id="input-with-list" list="car-datalist" placeholder="Fahrzeug" v-model="res.carId"> </BFormInput>
          <datalist id="car-datalist">
            <option v-for="el in cars" :value="el.id" :label="el.license_plate"/>
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
      <BFormGroup label="Reason (optional):">
        <BFormInput id="reason" placeholder="I really like driving" v-model="res.reason"></BFormInput>
      </BFormGroup>
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