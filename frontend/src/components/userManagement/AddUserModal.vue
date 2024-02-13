<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {User} from "@/main";

  const {hide} = useModal('creation-dialog')

  defineEmits<{
    (event: "createUser", newUser: User): void
  }>()

  let res = new User()
  function createUser(): User {

    // This ID is used for reference in the table (not in the id)
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
    return res
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
          <BFormInput v-model="res.name" id="name" placeholder="Name" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="email">E-Mail Adresse</b-form-floating-label>
          <BFormInput v-model="res.email" type="email" id="email" placeholder="E-Mail Address" required></BFormInput>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol>
          <b-form-floating-label label-for="password">Password</b-form-floating-label>
          <BFormInput v-model="res.password" type="password" id="password" placeholder="Password" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="role">Rolle</b-form-floating-label>
          <b-form-select v-model="res.role" id="role" :options="selectRoles" class="w-100 form-control"></b-form-select>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol class="ml-1">
          <b-form-checkbox v-model="res.isDriver">Diese Person darf Fahrzeuge der Flotte fahren</b-form-checkbox>
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
  import {Roles} from "@/main";
  export default {
    data() {
      return {
        selectRoles: [
          {value: Roles.ADMIN, text: 'Admin'},
          {value: Roles.SECURITY, text: 'Security'},
          {value: Roles.TRAVEL_OFFICE, text: 'Travel Office'}
        ],
        selectDriver: [
          {value: true, text: 'Yes'},
          {value: false, text: 'No'}
        ]
      }
    }
  }
</script>