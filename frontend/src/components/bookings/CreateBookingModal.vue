<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {computed, type Ref, ref} from "vue";
  import {Booking} from "@/main";

  defineProps<{
    cars?: string[]
  }>()

  defineEmits<{
    (event: "createBooking", booking : Booking) : void
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
            res.value.start?.setDate(new Date(newValue).getDate())
          }
        }),
        endDate: computed({
          get() : string {
            return res.value.end.toString();
          },
          // setter
          set(newValue : string) {
            res.value.end?.setDate(new Date(newValue).getDate())
          }
        })
      }

  const {show, hide, modal} = useModal('creation-dialog')

  let drivers = ref(["nsimon", "lhelbig", "laußem"])
</script>

<template>
  <BModal v-if="true" id="creation-dialog" @on-cancel="hide()" @ok="$emit.call(null, 'createBooking', res)" size="lg" hide-header>
    <div class="modal-header">
      <h3>Neue Fahrt eintragen</h3>
    </div>
    <br>
    <BForm>
      <BFormRow>
        <BCol>
          <b-input-group prepend="@">
            <BFormInput id="driver" list="input-list" placeholder="Fahrer" v-model="res.driver"></BFormInput>
            <b-tooltip target="driver" triggers="hover">
              Use their normal <b>black</b> name, e.g. mmustermann!
            </b-tooltip>
            <datalist id="input-list">
              <option v-for="el in drivers" :value="el"/>
            </datalist>
          </b-input-group>
        </BCol>
        <BCol>
          <BFormInput id="input-with-list" list="car-datalist" placeholder="Fahrzeug" v-model="res.car"> </BFormInput>
          <datalist id="car-datalist">
            <option v-for="el in cars" :value="el"/>
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
    </BForm>
  </BModal>
</template>

<style scoped>

</style>