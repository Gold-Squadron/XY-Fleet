<!--suppress VueUnrecognizedSlot -->
<script setup lang="ts">

  const SELECTION_COLOR: string = '#9a9a9a45'
  let selectedRows: number[] = []
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
    <b-table :fields="fields" :items="items">
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
  </div>
</template>

<style scoped>
  select{
    width: fit-content;
  }
</style>

<script lang="ts">
  let user1 = {'name':'Luca Außem','email':'l123@t.de','role':'admin','isDriver': true}
  let user2 = {'name':'Luca Außem2','email': 'l123@gmail.de','role': 'office','isDriver': false}
  let user = [user1, user2]
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
        items: user,
        selectRoles: [
          {value: 'admin', text: 'Admin'},
          {value: 'secruity', text: 'Secruity'},
          {value: 'office', text: 'Travel Office'}
        ],
        selectDriver: [
          {value: 'true', text: 'Yes'},
          {value: 'false', text: 'No'}
        ]
      }
    }
  }

</script>