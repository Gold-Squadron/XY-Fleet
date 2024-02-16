<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {User} from "@/main";

  const {hide} = useModal('creation-dialog')

  defineEmits<{
    (event: "createUser", newUser: User): void
  }>()

  let res = new User()
</script>

<template>
  <BModal v-if="true" id="creation-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Benutzer hinzufügen</h3>
    </div>
    <br>
    <BForm @submit="$emit.call(null, 'createUser', res); hide()">
      <BFormRow>
        <BCol>
          <b-form-floating-label label-for="name">Name</b-form-floating-label>
          <BFormInput v-model="res.name" id="name" placeholder="mmustermann" required></BFormInput>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol>
          <b-form-floating-label label-for="password">Password</b-form-floating-label>
          <BFormInput v-model="res.password" type="password" id="password" required></BFormInput>
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
          <b-button type="submit" variant="primary" class="mr-2">Benutzer hinzufügen</b-button>
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
        selectRoles: {
          [Roles.ADMIN]         : 'Admin',
          [Roles.TRAVEL_OFFICE] : 'Travel Office',
          [Roles.SECURITY]      : 'Security',
          [Roles.USER]          : 'Benutzer',
          [Roles.NONE]          : '-'
        }
      }
    }
  }
</script>