<!--suppress VueUnrecognizedSlot -->
<script setup lang="ts">
  import {type Ref, ref, toRaw} from 'vue';
  import {type TableItem, useModal} from "bootstrap-vue-next";
  import AddUserModal from "@/components/userManagement/AddUserModal.vue";
  import ConfirmRemovalModal from "@/components/userManagement/ConfirmRemovalModal.vue";
  import {Roles, User} from "@/main";
  import EditUserModal from "@/components/userManagement/EditUserModal.vue";
  import {
    getAllPilots,
    addPilot,
    removePilot,
    editPilot,
    getAllBookings, removeBooking
  } from "@/components/userManagement/UsermanagementRestCalls";

  let selectedIds: Ref<String[]> = ref([])
  let editedUserId: Ref<string> = ref('')
  let editedUser: Ref<User | null> = ref(null)
  let editedUserName: string = ''

  const roleConversion: any = {
    'admin'         : Roles.ADMIN,
    'security'      : Roles.SECURITY,
    'user'          : Roles.USER,
    'travel_office' : Roles.TRAVEL_OFFICE,
    'none'          : Roles.NONE
  }

  function showModal(id: string, userId: string | null = null): void {
    if (userId) {
      editedUserId.value = userId
      editedUser.value = getUserById(userId)
      editedUserName = editedUser.value ? editedUser.value?.name : ''
    }

    const {show} = useModal(id)
    show()
  }

  let users: Ref<User[]> = ref([])

  // Load data from database
  function loadAllPilots(): void {
    users.value = []
    getAllPilots().then(res => {
          res.forEach(pilot => {
            loadPilot(pilot)
          })
          usersCoverted.value = convertUserData()
        }
    )
  }

  loadAllPilots()

  function loadPilot(p: any): void {
    let role = roleConversion[p.role]
    let user: User = new User(p.id, p.name, '', role, p.is_driver)

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

  usersCoverted.value = convertUserData()

  function addUser(user: User): void {
    const roleConverted = Object.keys(roleConversion).find(key => roleConversion[key] == user.role)

    addPilot(user, roleConverted).then(() => {
      loadAllPilots()
    })
  }

  function removeUser(): void {
    // Remove user from database
    selectedIds.value.forEach(id => {
      removeSingleUser(Number(id))
    })

    // Update UI
    usersCoverted.value = []
    users.value = users.value.filter(user => !selectedIds.value.includes(user.getUiId()))
    usersCoverted.value = convertUserData()

    selectedIds.value = []

    changeAll(true)
  }

  function removeSingleUser(id: number): void{
    // Remove bookings from the user
    getAllBookings().then(res => {
      let bookingsDelete = res.filter(r => r.driver_id == Number(id))

      bookingsDelete.forEach(booking => {
        removeBooking(booking.id)
      })
      removePilot(Number(id))
    })
  }

  function selectRow(index: number, forceDeselect: boolean = false): void {
    let id: String = users.value[index].getUiId()
    let idIndex: number = selectedIds.value.indexOf(id)
    let addHighlight: boolean = (idIndex == -1) && (!forceDeselect)

    if (addHighlight) {
      selectedIds.value.push(id)
    } else {
      selectedIds.value.splice(idIndex, 1)
    }
    highlightRow(index, addHighlight)
  }

  function changeAll(forceDeselect: boolean = false): void {
    let deselect: boolean = (selectedIds.value.length == users.value.length) || forceDeselect
    selectedIds.value = []

    for (let i = 0; i < users.value.length; i++) {
      selectRow(i, deselect)
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

  // !TODO! Warning if name already exists
  function editUser(user: User): void {
    const roleConverted = Object.keys(roleConversion).find(key => roleConversion[key] == user.role)

    editPilot(user, user.name == editedUserName, roleConverted)

    usersCoverted.value = convertUserData()
  }

  function getUserById(id: string): any {
    return users.value.find(user => user.getUiId() == id)
  }
</script>

<template>
  <div class="pl-3">
    <b-button variant="primary" size="md" @click="showModal('creation-dialog')" class="mt-4 mb-3">Benutzer hinzufügen
    </b-button>
    <b-button variant="primary" size="md" @click="showModal('confirmation-dialog')" :disabled="selectedIds.length == 0"
              class="ml-3 mt-4 mb-3">Benutzer entfernen
    </b-button>

    <b-table id="userTable" :fields="fields" :items="usersCoverted">
      <template #head(cb)="">
        <b-form-checkbox @change="changeAll()" :checked="(selectedIds.length == users.length) && (users.length != 0)"
                         id="selectAllCheckbox"></b-form-checkbox>
      </template>
      <template #head(editRow)=""></template>
      <template #cell(cb)="data:any">
        <b-form-checkbox :id="`rowCheckbox-${data.index}`" :checked="selectedIds.includes(data.item['uiId'])"
                         @change="selectRow(data.index)"></b-form-checkbox>
      </template>
      <template #cell(role)="data: any">
        {{ selectRoles[data.item.role] }} <!-- !FIXME! -->
      </template>
      <template #cell(isDriver)="data: any">
        {{ data.item['isDriver'] ? 'Ja' : 'Nein' }}
      </template>
      <template #cell(editRow)="data: any">
        <i @click="showModal('edit-dialog', data.item['uiId'])" class="bi bi-pencil mr-5"></i>
      </template>
    </b-table>

    <AddUserModal @createUser="addUser"/>
    <EditUserModal @updateUser="editUser" :user="editedUser"/>
    <ConfirmRemovalModal @removeUser="removeUser"/>
  </div>
</template>

<style scoped>
  select {
    width: fit-content;
  }

  .bi-pencil:hover {
    cursor: pointer;
    opacity: 0.6;
  }
</style>

<script lang="ts">
  export default {
    data() {
      return {
        selectRoles: {
          [Roles.ADMIN]         : 'Admin',
          [Roles.TRAVEL_OFFICE] : 'Travel Office',
          [Roles.SECURITY]      : 'Security',
          [Roles.USER]          : 'Benutzer',
          [Roles.NONE]          : '-'
        },
        fields: [
          {key: 'cb',       thStyle: {width: '25px'}},
          {key: 'name',     label: 'Name'},
          {key: 'role',     label: 'Rolle'},
          {key: 'isDriver', label: 'Darf fahren'},
          {key: 'editRow',  thStyle: {width: '25px'}}
        ]
      }
    }
  }
</script>