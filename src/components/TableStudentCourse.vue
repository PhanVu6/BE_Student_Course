<template>
  <div>
    <nav>
      <section>
        <h2>Search</h2>
        <input @input="getAllStudent" type="search" placeholder="Search" v-model="paramNameForStudent">
        <button @click="getAllStudent">Search</button>
      </section>
    </nav>

    <section>
      <table id="table-mApi">
        <thead>
        <tr>
          <th>Id</th>
          <th>Name</th>
          <th>Email</th>
          <th>Course</th>
          <th>Register The Course</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in students" :key="student.id">
          <td>{{ student.id }}</td>
          <td>{{ student.name }}</td>
          <td>{{ student.email }}</td>
          <td>
            <ul>
              <li v-for="course in student.courseDtos" :key="course.id">
                {{ course.title }}
              </li>
            </ul>
          </td>

          <td>
            <label> Choose course </label>
            <select id="table-select-course">
              <option v-for="course in courses" :key="course.id">{{ course.title }}</option>
            </select>
          </td>
        </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<script setup>
import {onMounted, ref} from 'vue';
import axios from 'axios';

const URL_STUDENT = "http://localhost:8080/student";
const URL_COURSE = "http://localhost:8080/course";

const paramNameForStudent = ref('');
const students = ref([]);
const courses = ref([]);

const getAllStudent = async () => {
  try {
    const response = await axios.get(`${URL_STUDENT}/search`, {
      params: {name: paramNameForStudent.value}
    });

    if (response.data && response.data.content) {
      students.value = response.data.content;
    } else {
      console.log("No students found or response structure is incorrect.");
    }
  } catch (error) {
    console.error("Error getting all students:", error.message);
  }
};

const getAllCourse = async () => {
  try {
    const response = await axios.get(URL_COURSE);

    if (response.data && response.data.result) {
      courses.value = response.data.result;
    } else {
      console.log("No courses found or response structure is incorrect.");
    }
  } catch (error) {
    console.error("Error getting all courses:", error.message);
  }
};

onMounted(() => {
  getAllStudent();
  getAllCourse();
});
</script>

<style scoped>
table {
  width: 80vw;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
}

th {
  background-color: #f2f2f2;
  text-align: left;
}
</style>
