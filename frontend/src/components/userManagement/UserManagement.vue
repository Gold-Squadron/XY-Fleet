<!--suppress VueUnrecognizedSlot -->
<script setup lang="ts">
  import { type Ref, ref, toRaw } from 'vue';
  import AddUserModal from "@/components/userManagement/AddUserModal.vue";
  import { type TableItem, useModal } from "bootstrap-vue-next";
  import { Roles, User } from "@/main";

  const { show } = useModal('creation-dialog')
  const SELECTION_COLOR: string = '#9a9a9a45'

  // Demodata
  let users = ref([new User('22323fcvd', 'Luca Außem', 'luca-aussem@t-online.de', '1234', Roles.ADMIN, true), new User('c84nfakhf', 'Noah Simon', 'snoah@gmail.com', '4321')]);

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
    // !TODO! Add user to database
    usersCoverted.value = []
    users.value.push(user)
    usersCoverted.value = convertUserData()
  }

  function removeUser() : void{
    // !TODO! Remove user from database
    // !TODO! Add confirmation modal

    if(selectedIds.value.length == 0){
      return
    }

    usersCoverted.value = []
    users.value = users.value.filter(user => !selectedIds.value.includes(user.getId()))
    usersCoverted.value = convertUserData()

    selectedIds.value = []
  }

  //!TODO! Add correct type
  let selectedIds: Ref<any> = ref([])

  function selectRow(index: number) : void{
    let id = users.value[index].getId()
    let idIndex = selectedIds.value.indexOf(id)

    if(idIndex == -1){
      selectedIds.value.push(id)
    }else{
      selectedIds.value.splice(idIndex, 1)
    }
  }
  function changeAll(){
    let numberOfSelections: number = selectedIds.value.length
    selectedIds.value = []

    // Select all
    if(numberOfSelections != users.value.length){
      users.value.forEach(user => {
        selectedIds.value.push(user.getId())
      })
    }
  }
</script>

<template>
  <div class="pl-3">
    <b-button variant="primary" size="md" @click="show" class="mt-4 mb-3">Add User</b-button>
    <b-button variant="primary" size="md" @click="removeUser()" :disabled="selectedIds.length == 0" class="ml-3 mt-4 mb-3">Remove User</b-button>

    <b-table :fields="fields" :items="usersCoverted" :tbody-tr-class="rowClass">
      <template #head(cb)="">
        <b-form-checkbox @change="changeAll()" :checked="(selectedIds.length == users.length) && (users.length != 0)" id="selectAllCheckbox"></b-form-checkbox>
      </template>
      <template #cell(cb)="data">
        <b-form-checkbox :id="`rowCheckbox-${data.index}`" :checked="selectedIds.includes(data.item.id)" @change="selectRow(data.index)"></b-form-checkbox>
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

  .test{
    opacity: 0.5;
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
    },
    methods:{
      //!TODO! Change background color when selected
      rowClass(item:any, type:any){
        return null
      }
    }
  }

</script>