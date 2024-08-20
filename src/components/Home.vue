<script setup>
import {createApp} from 'vue'

const app = createApp({
  data() {
    return {
      count: 0
    }
  }
})
app.mount("#app")
</script>

<template>
  <div id="app">
    <button @click="count++">
      Count is: {{ count }}
    </button>
  </div>
</template>

<style scoped>

</style>