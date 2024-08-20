<template>
  <div>
    <nav>
      <section>
        <div class="flex gap-4 mb-4 items-center">
          <el-input
              @input="getAllStudent()"
              v-model="paramNameForStudent"
              style="width: 340px"
              placeholder="Search"
              :suffix-icon="Search"
          />
        </div>

      </section>
    </nav>
    <section>
      <table id="table-mApi">
        <thead>
        <tr>
          <th>Id</th>
          <th>Name</th>
          <th>Email</th>
          <th>Courses</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="student in students" :key="student.id">
          <td>{{ student?.id }}</td>
          <td>{{ student?.name }}</td>
          <td>{{ student?.email }}</td>
          <td>{{ student?.courseTitles }}</td>
        </tr>
        </tbody>
      </table>
    </section>

    <section>
      <button @click="$emit('someEvent')" :style="buttonStyle">Increase count at subclass</button>
      <h1>Count: {{ count }}</h1>
    </section>

    <!--Create-->
    <section id="student-input">
      <el-form
          ref="form"
          :inline="true"
          :model="formInline"
          class="demo-form-inline">
        <el-form-item
            prop="name"
            label="Username"
            :rules="[
          {
            required: true,
            message: 'Please input name',
            trigger: 'blur',
          }
            ]"
        >
          <el-input
              v-model="formInline.name"
              placeholder="Username"
              @change="validateName"
              clearable/>
        </el-form-item>
        <el-form-item label="Select Courses">
          <el-select
              v-model="formInline.course"
              placeholder="Select Courses"
              clearable
              @change="pushCourse"
          >
            <el-option v-for="course in courses" :key="course.id"
                       :label="course?.title" :value="course"/>
          </el-select>
        </el-form-item>
        <el-form-item
            prop="email"
            label="Email"
            :rules="[
        {
          required: true,
          message: 'Please input email address',
          trigger: 'blur',
        },
        {
          type: 'email',
          message: 'Please input correct email address',
          trigger: ['blur', 'change'],
        },
      ]"
        >
          <el-input v-model="formInline.email" @change="validateEmail" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     :disabled="!isFormEmail || loadingCreate || !isFormName"
                     @click="createStudent">Create
          </el-button>
        </el-form-item>
      </el-form>

      <div v-show="loadingCreate">
        <h1 class="create-success">Successful</h1>
      </div>

      <div>
        <h1>Sign In Courses</h1>
        <div class="courses-signup-box">
          <label v-for="course in listCourse">{{ course.title }} <br></label>
        </div>

      </div>
    </section>

  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive, ref, watch} from 'vue';
import axios from 'axios';
import {Search} from '@element-plus/icons-vue'

const props = defineProps(
    {
      count: {
        type: Number,
        default: 0,
      },
      name: {
        type: String
      }
    }
)

// Emit
const emit = defineEmits(['someEvent', 'customChange'])
const handleChange = (value) => {
  emit('customChange', value)
}

const URL_STUDENT = "http://localhost:8080/student";
const URL_COURSE = "http://localhost:8080/course";

const students = ref([]);
const listCourse = ref([]);
const loadingCreate = ref(false);
const isFormEmail = ref(false);
const isFormName = ref(false);
const paramNameForStudent = ref('');
const currentPage = ref(0)
const pageSize = ref(10)
const courses = ref([])
const formInline = reactive({
  name: '',
  course: null,
  email: '',
})
const form = ref(null);

const getAllStudent = async () => {
  try {
    const params = new URLSearchParams({
      name: paramNameForStudent.value,
      page: currentPage.value,
      size: pageSize.value
    });

    const {data} = await axios.get(`${URL_STUDENT}/search`, {params});

    students.value = data?.result?.content ?? [];

    if (students.value.length === 0) {
      console.warn("No students found or response structure is incorrect.");
    }
  } catch (error) {
    console.error("Failed to fetch students:", error);
  }
};

const getAllCourse = async () => {
  try {
    const params = new URLSearchParams({
      name: paramNameForStudent.value,
      page: currentPage.value,
      size: pageSize.value
    });

    const {data} = await axios.get(`${URL_COURSE}`, {params});

    courses.value = data?.result?.content ?? [];

    if (students.value.length === 0) {
      console.warn("No students found or response structure is incorrect.");
    }
  } catch (error) {
    console.error("Failed to fetch students:", error);
  }
};

const pushCourse = (selectCourse) => {
  if (selectCourse && !listCourse.value.includes(selectCourse)) {
    listCourse.value.push(selectCourse);
  }
  console.log('Successful push course');
}

function validateEmail() {
  form.value.validateField('email', (valid) => {
    isFormEmail.value = valid;
  });
}

function validateName() {
  form.value.validateField('name', (valid) => {
    isFormName.value = valid;
  });
}

const createStudent = async () => {
  loadingCreate.value = true;

  try {
    const body = {
      name: formInline.name,
      email: formInline.email,
      status: 1,
      courseDtos: listCourse.value
    };

    const {data} = await axios.post(`${URL_STUDENT}/save-many-course`, body);

    if (data) {
      await getAllStudent();
      listCourse.value = null;
      return data;
    } else {
      console.warn("Error creating student or response structure is incorrect.");
    }
  } catch (error) {
    console.error("Error creating student:", error.message);
  } finally {
    setTimeout(
        () => loadingCreate.value = false, 5000
    )
  }
};


onMounted(() => {
  getAllStudent();
  getAllCourse();
});

const buttonStyle = computed(() => {
  return {backgroundColor: props.count % 2 != 0 ? 'red' : 'yellow'}
});

watch(pageSize,
    () => {
      getAllStudent();
    });

watch(
    () => formInline.email,
    () => {
      validateEmail();
    }
);
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

#student-input {
//display: inline-grid;
}

#student-input > input {
//margin-top: 3px; //margin-bottom: 3px;
}

.courses-signup-box {
  width: 30%;
  height: 50px;
  overflow: auto;
  align-content: center;
  border: 3px solid antiquewhite;
  padding: 4px;
  background: white;
}

.create-success {
  color: lightgreen;
}
</style>
