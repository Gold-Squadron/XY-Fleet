<script setup lang="ts">
  import {useModal} from "bootstrap-vue-next";
  import {Roles} from "@/main";
  import {type Ref, ref, watch} from "vue";

  const {hide} = useModal('edit-dialog')

  const emit = defineEmits<{
    (event: 'updateUser', data: any) : void
  }>()

  const props = defineProps({
    user: null
  })

  watch(() => props.user, () => {
    insertUserData()
  })

  let inputName: Ref<string> = ref('')
  let inputEmail: Ref<string> = ref('')
  let inputPassword: Ref<string> = ref('')
  let inputRole: Ref<Roles> = ref(Roles.TRAVEL_OFFICE)
  let inputIsDriver: Ref<any> = ref(true)

  function updateUser() : void {
    let data = {
      name: inputName.value,
      email: inputEmail.value,
      password: inputPassword.value,
      role: inputRole.value,
      isDriver: inputIsDriver.value
    }

    emit('updateUser', data)
  }

  function insertUserData() : void {
    inputName.value = props.user.name
    inputEmail.value = props.user.email
    inputPassword.value = props.user.password
    inputRole.value = props.user.role
    inputIsDriver.value = props.user.isDriver
  }
</script>

<template>
  <BModal v-if="true" id="edit-dialog" @on-cancel="hide()" size="lg" hide-header hide-footer>
    <div class="modal-header">
      <h3>Benutzer bearbeiten</h3>
    </div>
    <br>
    <BForm @submit="updateUser(); hide()">
      <BFormRow>
        <BCol>
          <b-form-floating-label label-for="name">Name</b-form-floating-label>
          <BFormInput id="name" v-model="inputName" placeholder="mmustermann" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="email">E-Mail Adresse</b-form-floating-label>
          <BFormInput type="email" id="email" v-model="inputEmail" placeholder="example@domain.com" required></BFormInput>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol>
          <b-form-floating-label label-for="password">Password</b-form-floating-label>
          <BFormInput id="password" v-model="inputPassword" type="password" required></BFormInput>
        </BCol>
        <BCol>
          <b-form-floating-label label-for="role">Rolle</b-form-floating-label>
          <b-form-select id="role" v-model="inputRole" :options="selectRoles" class="w-100 form-control"></b-form-select>
        </BCol>
      </BFormRow>
      <BFormRow class="mt-3">
        <BCol class="ml-1">
          <b-form-checkbox v-model="inputIsDriver">Diese Person darf Fahrzeuge der Flotte fahren</b-form-checkbox>
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