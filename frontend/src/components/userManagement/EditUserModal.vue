<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {Roles} from "@/main";

  const {hide} = useModal('edit-dialog')
  const string_false = 'false'

  const emit = defineEmits<{
    (event: 'updateUser', data: any) : void
  }>()

  let inputName: string = ''
  let inputEmail: string = ''
  let inputPassword: string = ''

  // !TODO! Load original data

  function updateUser() : void {
    // !TODO! Get data from selects
    let data = {
      name: inputName,
      email: inputEmail,
      password: inputPassword
    }

    emit('updateUser', data)
  }
</script>

<template>
  <BModal v-if="true" id="edit-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Benutzer bearbeiten {{inputName}}</h3>
    </div>
    <br>
    <BForm @submit="updateUser(); hide()">
      <BFormRow>
        <BCol>
          <b-form-floating-label label-for="name">Name</b-form-floating-label>
          <BFormInput id="name" v-model="inputName" placeholder="Name" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="email">E-Mail Adresse</b-form-floating-label>
          <BFormInput type="email" id="email" v-model="inputEmail" placeholder="E-Mail Address" required></BFormInput>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol class="col-5">
          <b-form-floating-label label-for="password">Password</b-form-floating-label>
          <BFormInput id="password" v-model="inputPassword" type="password" placeholder="Password" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="role">Rolle</b-form-floating-label>
          <b-form-select id="role" v-model="Roles.TRAVEL_OFFICE" :options="selectRoles" class="w-100 form-control"></b-form-select>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="isDriver">Fahrer</b-form-floating-label>
          <b-form-select id="isDriver" v-model="string_false" :options="selectDriver" class="w-100 form-control"></b-form-select>
        </BCol>
      </BFormRow>
      <b-row class="mt-4 text-right">
        <b-col>
          <b-button type="submit" variant="primary" class="mr-2">Änderungen Speichern</b-button>
          <b-button type="button" @click="hide()" variant="secondary">Abbrechen</b-button>
        </b-col>
      </b-row>
    </BForm>
  </BModal>
</template>

<script lang="ts">
  export default {
    data() {
      return {
        selectRoles: [
          {value: Roles.ADMIN, text: 'Admin'},
          {value: Roles.SECURITY, text: 'Security'},
          {value: Roles.TRAVEL_OFFICE, text: 'Travel Office'}
        ],
        selectDriver: [
          {value: 'true', text: 'Yes'},
          {value: 'false', text: 'No'}
        ]
      }
    }
  }
</script>