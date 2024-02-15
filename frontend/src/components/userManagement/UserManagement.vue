<!--suppress VueUnrecognizedSlot -->
<script setup lang="ts">
import {type Ref, ref, toRaw} from 'vue';
import {type TableItem, useModal} from "bootstrap-vue-next";
import AddUserModal from "@/components/userManagement/AddUserModal.vue";
import ConfirmRemovalModal from "@/components/userManagement/ConfirmRemovalModal.vue";
import {Roles, User} from "@/main";
import {getAllPilots, addPilot, removePilot} from "@/components/userManagement/UsermanagementRestCalls";

function showModal(id: string): void {
    const {show} = useModal(id)
    show()
  }

  let users: Ref<User[]> = ref([])

  // Load data from database
  getAllPilots().then(res => {
    res.forEach(pilot => { loadPilot(pilot) })
    usersCoverted.value = convertUserData()
  })

  function loadPilot(p: any) : void{
    let role: Roles = Roles.TRAVEL_OFFICE

    if(p.role == 'security'){
      role = Roles.SECURITY
    }else if(p.role == 'admin'){
      role = Roles.ADMIN
    }

    let user: User  = new User(p.id, p.name, 'new-mail', '', role, !!p.is_driver)

    users.value.push(user)
  }

  // Convert the raw data into the rendering format
  let usersCoverted: Ref<TableItem[]> = ref([])

  function convertUserData(): TableItem[] {
    let dataConverted: TableItem[] = []
    let dataRaw = users.value

    dataRaw.forEach((data) => {
      let dataObj: { [key: string]: string | boolean | number } = {}
      let values: any = toRaw(data)

      Object.keys(data).forEach(param => {
        dataObj[param] = values[param]
      })

      dataConverted.push(dataObj)
    })

    return dataConverted
  }

  function addUser(user: User): void {
    // !TODO! Add user to database

    // Update ui
    usersCoverted.value = []
    users.value.push(user)
    usersCoverted.value = convertUserData()
  }

  function removeUser(): void {
    // Remove user from database
    selectedIds.value.forEach(id => {
      removePilot(Number(id))
    })

    // Update ui
    usersCoverted.value = []
    users.value = users.value.filter(user => !selectedIds.value.includes(user.getId()))
    usersCoverted.value = convertUserData()

    selectedIds.value = []

    changeAll(true)
  }

  let selectedIds: Ref<String[]> = ref([])

  function selectRow(index: number): void {
    let id = users.value[index].getId()
    let idIndex = selectedIds.value.indexOf(id)

    if (idIndex == -1) {
      selectedIds.value.push(id)
      highlightRow(index)
    } else {
      selectedIds.value.splice(idIndex, 1)
      highlightRow(index, false)
    }

  }

  function changeAll(forceDeselect: boolean = false): void {
    let selectAll: boolean = (selectedIds.value.length != users.value.length) && !forceDeselect
    selectedIds.value = []

    //!TODO!
    if (selectAll) {
      for (let i = 0; i < users.value.length; i++) {
        selectRow(i)
      }
    } else {
      for (let i = 0; i < users.value.length; i++) {
        selectRow(i)
        selectRow(i)
      }
    }
  }

  function highlightRow(index: number, mark: boolean = true): void {
    const SELECTION_COLOR: string = '#9a9a9a45'
    const table: HTMLElement | null = document.getElementById('userTable')

    if (!table) {
      return
    }

    let row: any = table.children[1].children[index]

    row.style.backgroundColor = mark ? SELECTION_COLOR : ''
  }
</script>

<template>
  <div class="pl-3">
    <b-button variant="primary" size="md" @click="showModal('creation-dialog')" class="mt-4 mb-3">Add User</b-button>
    <b-button variant="primary" size="md" @click="showModal('confirmation-dialog')" :disabled="selectedIds.length == 0"
              class="ml-3 mt-4 mb-3">Remove User
    </b-button>

    <b-table id="userTable" :fields="fields" :items="usersCoverted">
      <template #head(cb)="">
        <b-form-checkbox @change="changeAll()" :checked="(selectedIds.length == users.length) && (users.length != 0)"
                         id="selectAllCheckbox"></b-form-checkbox>
      </template>
      <template #cell(cb)="data:any">
        <b-form-checkbox :id="`rowCheckbox-${data.index}`" :checked="selectedIds.includes(data.item.id)"
                         @change="selectRow(data.index)"></b-form-checkbox>
      </template>
      <template #cell(role)="data: any">
        <b-form-select v-model="data.item.role" :options="selectRoles"></b-form-select>
      </template>
      <template #cell(isDriver)="data: any">
        <BFormSelect v-model="data.item.isDriver" :options="selectDriver"></BFormSelect>
      </template>
    </b-table>

    <AddUserModal @createUser="addUser"></AddUserModal>
    <ConfirmRemovalModal @removeUser="removeUser"></ConfirmRemovalModal>
  </div>
</template>

<style scoped>
  select {
    width: fit-content;
  }

  *{
    color: white;
  }
  select{
    color:black;
  }
</style>

<script lang="ts">

import {reactive} from "vue";

const form = reactive({
  email: '',
  name: '',
  food: null,
  checked: [],
})
  export default {
    data() {
      return {
        fields: [
          {key: 'cb', thStyle: {width: '25px'}},
          {key: 'name', label: 'Name'},
          {key: 'email', label: 'E-Mail'},
          {key: 'role', label: 'Role'},
          {key: 'isDriver', label: 'Is Driver'}
        ],
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