<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {Roles, User} from "@/main";

  const {hide} = useModal('creation-dialog')
  const string_false = 'false'

  defineEmits<{
    (event: "createUser", newUser: User): void
  }>()

  // !TODO! There is surely a better way to do this which is not as stupid as this
  function createUser(): User {
    let name: HTMLInputElement = <HTMLInputElement>document.getElementById('name')
    let email: HTMLInputElement = <HTMLInputElement>document.getElementById('email')
    let password: HTMLInputElement = <HTMLInputElement>document.getElementById('password')
    let role: HTMLInputElement = <HTMLInputElement>document.getElementById('role')
    let isDriver: HTMLInputElement = <HTMLInputElement>document.getElementById('isDriver')

    let inputRole: Roles = Roles.ADMIN
    if (role.value == '1') {
      inputRole = Roles.SECURITY
    } else if (role.value == '2') {
      inputRole = Roles.TRAVEL_OFFICE
    }

    // Generate id
    let id: String = ''

    while (true) {
      id = Math.random().toString(16)
      id = id.substring(id.length - 9)

      if (checkId(id)) {
        break
      }
    }

    return new User(id, name.value, email.value, password.value, inputRole, isDriver.value == 'true')
  }

  function checkId(id: String): boolean {
    // !Todo! Check if id is in use
    return true
  }
</script>

<template>
  <BModal v-if="true" id="creation-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Benutzer hinzufügen</h3>
    </div>
    <br>
    <BForm @submit="$emit.call(null, 'createUser', createUser()); hide()">
      <BFormRow>
        <BCol>
          <b-form-floating-label label-for="name">Name</b-form-floating-label>
          <BFormInput id="name" placeholder="Name" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="email">E-Mail Adresse</b-form-floating-label>
          <BFormInput type="email" id="email" placeholder="E-Mail Address" required></BFormInput>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol class="col-5">
          <b-form-floating-label label-for="password">Password</b-form-floating-label>
          <BFormInput type="password" id="password" placeholder="Password" required></BFormInput>
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
          <b-button type="submit" variant="primary" class="mr-2">Benutzer Hinzufügen</b-button>
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
        ],
      }
    }
  }
</script>