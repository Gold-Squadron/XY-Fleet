<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {User} from "@/main";
  import {ref, watch} from "vue";

  const {hide} = useModal('edit-dialog')

  const emit = defineEmits<{
    (event: 'updateUser', data: any) : void
  }>()

  const props = defineProps({
    user: null
  })

  watch(() => props.user, () => {
    res.value = props.user
  })

  let res = ref(new User())
</script>

<template>
  <BModal v-if="true" id="edit-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Benutzer bearbeiten</h3>
    </div>
    <br>
    <BForm @submit="emit('updateUser', res); hide()">
      <BFormRow>
        <BCol>
          <b-form-floating-label label-for="name">Name</b-form-floating-label>
          <BFormInput id="name" v-model="res.name" placeholder="mmustermann" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="role">Rolle</b-form-floating-label>
          <b-form-select id="role" v-model="res.role" :options="selectRoles" class="w-100 form-control"></b-form-select>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol class="ml-1">
          <b-form-checkbox v-model="res.isDriver">Diese Person darf Fahrzeuge der Flotte fahren</b-form-checkbox>
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
  import {Roles} from "@/main";
  export default {
    data() {
      return {
        selectRoles: [
          {value: Roles.ADMIN, text: 'Admin'},
          {value: Roles.SECURITY, text: 'Security'},
          {value: Roles.TRAVEL_OFFICE, text: 'Travel Office'}
        ]
      }
    }
  }
</script>