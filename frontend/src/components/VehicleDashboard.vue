<script setup lang="ts">
  let user = {
    'canEdit' : true,
    'isDriver' : false
  }
</script>

<template>
  <div class="pl-3">
    <b-table sticky-header :items="items" :fields="fields">
      <template #cell(actions)="data">
        <div @click="console.log('show popover')" class="d-flex justify-content-center align-items-center circle-effect">
          <div></div>
          <i class="bi bi-three-dots table-entry-settings" :id="`vehicle-entry-${data.index}`"></i>
          <b-popover :target="`vehicle-entry-${data.index}`" :placement="'left'">
            <p class="m-0 px-2 popover-select" v-bind:class="{ 'disabled' : !user.canEdit }">Edit</p>
            <p class="m-0 px-2 popover-select" v-bind:class="{ 'disabled' : !user.canEdit }">Remove</p>
            <p class="m-0 px-2 popover-select" v-bind:class="{ 'disabled' : !user.isDriver }">Show PIN</p>
          </b-popover>
        </div>
      </template>
    </b-table>
  </div>
</template>

<style scoped>
  /* Table styling */
  .table-entry-settings{
    cursor: pointer;
  }
  .circle-effect{
    position: relative;
  }
  .circle-effect>i, .circle-effect > div{
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, 0);
  }
  .circle-effect > i{
    transition: font-size 0.1s;
  }
  .circle-effect > div{
    transition: opacity 0.1s;
    background-color: #d2d2d2;
    opacity: 0;
    border-radius: 50%;
    width: 25px;
    height: 25px;
  }

  .circle-effect:hover > div{
    opacity: 0.5;
  }
  .circle-effect:hover > i{
    transform: translate(-50%, 0) scale(1.1);
  }

  .circle-effect > div{
    cursor: pointer;
  }

  /* Popover styling */
  /* !TODO! Hightlight -> full width of popover */
  .popover-select{
    cursor: pointer;
    user-select: none;
  }
  .popover-select:hover{
    background-color: rgba(0, 0, 0, 0.05);
    border-radius: 10%;
  }

  .disabled{
    opacity: 0.5;
    pointer-events: none;
  }
</style>

<script lang="ts">
export default {
  data(){
    return{
      fields:[
        { key: 'model'   },
        { key: 'brand'   },
        { key: 'license' },
        { key: 'mileage' },
        { key: 'annual'  },
        { key: 'chassis' },
        { key: 'actions', thClass: 'invisible' }
      ],
      items:[
        { model: 'Corsa E', brand: 'Opel', license: 'DN-LA186', mileage: 34000, annual: 30000, chassis: '4206942069' },
        { model: 'Astra', brand: 'Opel', license: 'DN-W4767', mileage: 20000, annual: 25000, chassis: '123456789' }
      ],
      show: false
    }
  }
}
</script>