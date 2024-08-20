<script setup>
import TableStudent from '@/components/TableStudent.vue'
import {ref} from "vue";

const statusViewStudent = ref(false);
const count = ref(0);
const name = ref("Hồng");
const viewStudent = () => {
  statusViewStudent.value = !statusViewStudent.value;
};

const increaseCount = () => {
  count.value++
  console.log("label" + count.value)
};

const handleChange = (newName) => {
  name.value = newName;
}
</script>

<template>
  <section>
    <button @click="viewStudent">Table Student</button>
    <div v-if="statusViewStudent">
      <TableStudent :count="count" :name="name" @some-event="increaseCount" @customChange="handleChange"/>
    </div>

    <button @click="increaseCount">Increase count</button>
  </section>
</template>

<style scoped>
/* Your styles here */
</style>