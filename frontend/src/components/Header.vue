<script setup lang="ts">

</script>

<template>
  <nav class="navbar navbar-expand-sm navbar-light bg-light shadow-custom">
    <a class="navbar-brand text-white">XY-Fleet</a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto"></ul>


      <i class="bi bi-gear text-white"/>
      <i class="bi bi-person-circle text-white"/>
      </div>
  </nav>

</template>

<style scoped>
  .shadow-custom{
    box-shadow: 0 0 15px 2px black;
  }
  .navbar-light{
    background-color: #407fb7 !important;

    font-size: 25px;
  }

  i {
    transition: color 0.1s;
    margin: 0 2px;
    padding: 0 10px;
    cursor: pointer;
  }

  i:hover{
    color: #e0e0e0 !important;
  }
</style>