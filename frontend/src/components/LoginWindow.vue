<script setup lang="ts">
  import {reactive} from "vue";


  let form = reactive({
    username: "",
    password: ""
  })

  const onSubmit = (event : any) => {
    event.preventDefault()
    alert(JSON.stringify(form))
	  //make REST call, set token and show normal view
  }

</script>

<template>
  <BCard class="card-dimensions shadow">
    <b-card-title>Login</b-card-title>
    <b-card-subtitle>Please use your black credentials.</b-card-subtitle>
    <BCardBody>
      <BForm @submit="onSubmit">
        <BFormGroup id="input-group-2" label="Username:" label-for="usernameField">
          <BFormInput id="usernameField" v-model="form.username" placeholder="mmustermann" required />
        </BFormGroup>
        <BFormGroup id="input-group-2" label="Password:"  label-for="passwordField">
          <BFormInput
              type="password"
              id="passwordField"
              v-model="form.password"
          />
        </BFormGroup>
        <BFormGroup>
          <BButton type="submit" size="lg" block="true" variant="primary" class="mt-3">Login</BButton>
        </BFormGroup>
      </BForm>
    </BCardBody>
  </BCard>
</template>

<style scoped>
  .card-dimensions {
    width: clamp(30vw, 300px, 80vw);
  } 
</style>