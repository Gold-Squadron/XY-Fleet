<!--suppress VueUnrecognizedSlot -->
<script setup lang="ts">
  import { type Ref, ref, toRaw } from 'vue';
  import AddUserModal from "@/components/userManagement/AddUserModal.vue";
  import { type TableItem, useModal } from "bootstrap-vue-next";
  import { Roles, User } from "@/main";

  const { show } = useModal('creation-dialog')
  const SELECTION_COLOR: string = '#9a9a9a45'
  let selectedRows: number[] = []

  // Demodata
  let users = ref([new User('Luca Außem', 'luca-aussem@t-online.de', '1234', Roles.ADMIN, true), new User('Noah Simon', 'snoah@gmail.com', '4321')]);

  // Convert the raw data into the rendering format
  let usersCoverted: Ref<TableItem[]> = ref([])
  function convertUserData() : TableItem[]{
    let dataConverted: TableItem[] = []
    let dataRaw = users.value

    dataRaw.forEach((data) => {
      let dataObj: {[key: string]: string|boolean|number} = {}
      let values: any = toRaw(data)

      Object.keys(data).forEach(param => {
        dataObj[param] = values[param]
      })

      dataConverted.push(dataObj)
    })

    return dataConverted
  }
  usersCoverted.value = convertUserData()

  function addUser(user: User) : void{
    console.log(user)
    usersCoverted.value = []
    users.value.push(user)
    usersCoverted.value = convertUserData()
  }

  function selectRow(index: number) : void{
    let rowDom: HTMLElement = document.getElementsByTagName('tr')[index + 1]

    if(rowDom.style.backgroundColor == ''){
      rowDom.style.backgroundColor = SELECTION_COLOR
      selectedRows.push(index)
    }else {
      let arrIndex: number = selectedRows.indexOf(index) - 1
      rowDom.style.backgroundColor = ''

      if(arrIndex >= 0){
        selectedRows.splice(arrIndex, 1)
      }else{
        selectedRows.shift()
      }
    }

    // Check if "select all" checkbox needs to be (un-)checked
    let checkbox: HTMLInputElement = <HTMLInputElement> document.getElementById('selectAllCheckbox')
    checkbox.checked = document.getElementsByTagName('tr').length - 1 == selectedRows.length
  }

  function changeAll() : void{
    let rowDoms = document.getElementsByTagName('tr')
    let selectionNum: number = selectedRows.length
    let select: boolean = (rowDoms.length - 1) != selectionNum
    selectedRows = []

    for(let i=1; i<rowDoms.length; i++){
      let rowDom: HTMLElement = rowDoms[i]
      let checkbox: HTMLInputElement = <HTMLInputElement> document.getElementById(`rowCheckbox-${i - 1}`)

      if(select){
        selectedRows.push(i - 1)
      }

      rowDom.style.backgroundColor = select ? SELECTION_COLOR : ''
      checkbox.checked = select
    }
  }
</script>

<template>
  <div class="pl-3">
    <b-button variant="primary" size="md" @click="show" class="mt-4 mb-3">Add User</b-button>
    <b-table :fields="fields" :items="usersCoverted">
      <template #head(cb)="">
        <b-form-checkbox @change="changeAll()" id="selectAllCheckbox"></b-form-checkbox>
      </template>
      <template #cell(cb)="data">
        <b-form-checkbox :id="`rowCheckbox-${data.index}`" @change="selectRow(data.index)"></b-form-checkbox>
      </template>
      <template #cell(role)="data: any">
        <b-form-select v-model="data.item.role" :options="selectRoles"></b-form-select>
      </template>
      <template #cell(isDriver)="data: any">
        <b-form-select v-model="data.item.isDriver" :options="selectDriver"></b-form-select>
      </template>
    </b-table>

    <AddUserModal @createUser="addUser"></AddUserModal>
  </div>
</template>

<style scoped>
  select{
    width: fit-content;
  }
</style>

<script lang="ts">
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
          {value: Roles.SECRUITY, text: 'Secruity'},
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