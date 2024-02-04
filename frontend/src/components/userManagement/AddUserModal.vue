<script setup lang="ts">
  import { useModal } from "bootstrap-vue-next";
  import { Roles, User } from "@/main";

  const { hide } = useModal('creation-dialog')

  const string_false = 'false'

  defineEmits<{
    (event: "createUser", newUser : User) : void
  }>()

  // !TODO!
  // There is surely a better way to do this whis is not as stupid as this
  // Make name and email mandatory
  function createUser(): User{
    let name: HTMLInputElement = <HTMLInputElement> document.getElementById('name')
    let email: HTMLInputElement = <HTMLInputElement> document.getElementById('email')
    let password: HTMLInputElement = <HTMLInputElement> document.getElementById('password')
    let role: HTMLInputElement = <HTMLInputElement> document.getElementById('role')
    let isDriver: HTMLInputElement = <HTMLInputElement> document.getElementById('isDriver')

    let inputRole:Roles = Roles.ADMIN
    if(role.value == '1'){
      inputRole = Roles.SECRUITY
    }else if(role.value == '2'){
      inputRole = Roles.TRAVEL_OFFICE
    }

    // Generate id
    let id: String = ''

    while (true){
      id = Math.random().toString(16)
      id = id.substring(id.length - 9)

      if(checkId(id)){
        break
      }
    }

    return new User(id ,name.value, email.value, password.value, inputRole, isDriver.value == 'true')
  }

  function checkId(id: String) : boolean{
    // !Todo! Check if id is in use
    return true
  }
</script>

<template>
  <BModal v-if="true" id="creation-dialog" @on-cancel="hide()" @ok="$emit.call(null, 'createUser', createUser())" size="lg" hide-header>
    <div class="modal-header">
      <h3>Benutzer hinzufügen</h3>
    </div>
    <br>
    <BForm>
      <BFormRow>
        <BCol>
            <BFormInput id="name" placeholder="Name"></BFormInput>
            <b-tooltip target="driver" triggers="hover">
              Use their normal <b>black</b> name, e.g. mmustermann!
            </b-tooltip>
        </BCol>
        <BCol>
          <BFormInput type="email" id="email" placeholder="E-Mail Adress"></BFormInput>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol>
          <BFormInput id="password" placeholder="Password"></BFormInput>
        </BCol>
        <BCol>
          <b-form-select id="role" v-model="Roles.TRAVEL_OFFICE" :options="selectRoles" class="w-100"></b-form-select>
        </BCol>
        <BCol>
          <b-form-select id="isDriver" v-model="string_false" :options="selectDriver" class="w-100"></b-form-select>
        </BCol>
      </BFormRow>
    </BForm>
  </BModal>
</template>

<script lang="ts">
  export default {
    data() {
      return {
        selectRoles: [
          {value: Roles.ADMIN, text: 'Admin'},
          {value: Roles.SECRUITY, text: 'Secruity'},
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