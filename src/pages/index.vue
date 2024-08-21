<script setup>
import {ref} from "vue";
import {Menu as IconMenu} from '@element-plus/icons-vue'
import TableStudent from '@/components/TableStudent.vue'
import TableCourse from '@/components/TableCourse.vue'

const statusViewStudent = ref(false);
const statusViewCourse = ref(false);
const count = ref(0);
const name = ref("Hồng");
const viewStudent = () => {
  statusViewCourse.value = false;
  statusViewStudent.value = true;
};
const viewCourse = () => {
  statusViewCourse.value = true;
  statusViewStudent.value = false;
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
  <el-container class="layout-container-demo">
    <el-aside width="200px">
      <el-scrollbar>
        <el-menu :default-openeds="['1']">
          <el-sub-menu index="1">
            <template #title>
              <el-icon>
                <icon-menu/>
              </el-icon>
              Menu
            </template>
            <el-menu-item-group>
              <template #title>Table</template>
              <el-menu-item index="1-1" @click="viewStudent">Table Student</el-menu-item>
              <el-menu-item index="1-2" @click="viewCourse">Table Course</el-menu-item>
            </el-menu-item-group>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container>
      <el-header style="text-align: center; font-size: 40px;">
        <div class="toolbar">
          <span>Manager Student</span>
        </div>
      </el-header>

      <el-main>
        <div style="overflow: auto; height: 80vh" v-if="statusViewStudent">
          <TableStudent :count="count" :name="name" @some-event="increaseCount" @customChange="handleChange"/>
          <button @click="increaseCount">Increase count</button>
        </div>
        <div style="overflow: auto; height: 80vh" v-if="statusViewCourse">
          <TableCourse/>
        </div>
      </el-main>
    </el-container>


  </el-container>
</template>

<style scoped>
.layout-container-demo .el-header {
  position: relative;
  background-color: var(--el-color-primary-light-7);
  color: var(--el-text-color-primary);
}

.toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}
</style>