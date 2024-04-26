<script setup lang="ts">

</script>

<template>
  <nav class="navbar navbar-expand-sm navbar-light bg-light shadow-custom">
    <a class="navbar-brand text-white p-0">
      <img src="/src/assets/logo.png" alt="XY-Fleet" style="height: 7vh"> <!-- don't ask me why-->
    </a>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
      <ul class="navbar-nav mr-auto"></ul>


      <i class="bi bi-gear text-white action-element"/>
      <BAvatar badge class="action-element badge-red"
                src="https://placekitten.com/300/300">
      </BAvatar>
    </div>
  </nav>

</template>

<style scoped>
nav{
  z-index: 5;
}

.shadow-custom{
  box-shadow: 0 0 15px 2px black;
}

.badge-red + span {
  content: '🔗';
  height: 100vh;
  text-color: red;
}

.badge-red span span {
  color: #fff;
  background-color: #dc3545;
}

.navbar-light{
  background-color: #407fb7 !important;

  font-size: 2.35rem;
}

.action-element {
  transition: color 0.1s;
  margin: 0 5px;
  padding: 0 0px;
  cursor: pointer;
}

.trans-down {
  translate: 0px 2px;
}

.action-element:hover{
  color: #e0e0e0 !important;
}

a:hover {
  background-color: #407fb7 !important;
}
</style>