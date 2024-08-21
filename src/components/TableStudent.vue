<template>
  <div>
    <nav>
      <section>
        <div class="flex gap-4 mb-4 items-center">
          <el-input @input="getAllStudent()" v-model="paramNameForStudent" style="width: 340px" placeholder="Search"
                    :suffix-icon="Search"
          />
        </div>

      </section>
    </nav>
    <section>
      <el-table :data="students" stripe style="width: 100%">
        <el-table-column prop="name" label="Name" width="180">
          <template #default="scope">
            <el-input v-model="scope.row.name" v-if="editingRows[scope.row.id]"/>
            <span v-else>{{ scope.row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="Email" width="180">
          <template #default="scope">
            <el-input v-model="scope.row.email" v-if="editingRows[scope.row.id]"/>
            <span v-else>{{ scope.row.email }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="courseTitles" label="Titles" width="180"/>

        <el-table-column fixed="right" label="Operations" min-width="120">
          <template #default="scope">
            <el-button link :type="editingRows[scope.row.id] ? 'warning' :'primary'" size="small"
                       @click="Update(scope.row)">
              {{ editingRows[scope.row.id] ? 'Save' : 'Edit' }}
            </el-button>
            <el-button v-if="editingRows[scope.row.id]" link type="primary" size="small"
                       @click="closeFunction(scope.row.id)">
              <el-icon>
                <CircleCloseFilled/>
              </el-icon>
            </el-button>
            <el-button link type="danger" size="small" @click="Delete(scope.row.id)">
              Delete
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="demo-pagination-block" style="margin-top: 20px">
        <div class="demonstration">Change page size</div>
        <el-pagination
            v-model:current-page="page.currentPage"
            v-model:page-size="page.pageSize"
            :page-sizes="[5,10]"
            :size="size"
            :disabled="disabledPage"
            :background="background"
            layout="sizes, prev, pager, next"
            :total="page.totalElement"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        />
      </div>
    </section>

    <!--    <section>-->
    <!--      <button @click="$emit('someEvent')" :style="buttonStyle">Increase count at subclass</button>-->
    <!--      <p>Count: {{ count }}</p>-->
    <!--    </section>-->

    <!--Create-->
    <section id="student-input">
      <h1>Create Student</h1>
      <el-form ref="form" :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item prop="name" label="Username"
                      :rules="[
          {
            required: true,
            message: 'Please input name',
            trigger: 'blur',
          }
            ]"
        >
          <el-input v-model="formInline.name" placeholder="Username" @change="validateName" clearable/>
        </el-form-item>
        <el-form-item label="Select Courses">
          <el-select v-model="formInline.course" placeholder="Select Courses" clearable @change="pushCourse"
          >
            <el-option v-for="course in courses" :key="course.id"
                       :label="course?.title" :value="course"/>
          </el-select>
        </el-form-item>
        <el-form-item prop="email" label="Email"
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
          <el-button type="primary" :disabled="!isFormEmail || loadingCreate || !isFormName" @click="createStudent">
            Create
          </el-button>
        </el-form-item>
      </el-form>

      <div v-show="loadingCreate">
        <h1 class="create-success">Successful</h1>
      </div>

      <div>
        <h1>List Course Sign-In</h1>
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
import {CircleCloseFilled, Search} from '@element-plus/icons-vue';
import {ComponentSize} from "element-plus";

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
const emit = defineEmits(['someEvent'])

const URL_STUDENT = "http://localhost:8080/student";
const URL_COURSE = "http://localhost:8080/course";

const students = ref([]);
const listCourse = ref([]);
const loadingCreate = ref(false);
const isFormEmail = ref(false);
const isFormName = ref(false);
const paramNameForStudent = ref('');
const size = ref<ComponentSize>('default');
const background = ref(true);
const disabledPage = ref(false);
const courses = ref([]);

const formInline = reactive({
  name: '',
  course: null,
  email: '',
})

const page = reactive({
  currentPage: 1,
  pageSize: 10,
  totalElement: 0,
})
const form = ref(null);
const editingRows = ref({});

const getAllStudent = async () => {

  page.currentPage--;

  try {
    const params = new URLSearchParams({
      name: paramNameForStudent.value,
      number: page.currentPage,
      size: page.pageSize,
    });

    const {data} = await axios.get(`${URL_STUDENT}/search`, {params});

    students.value = data?.result?.content ?? [];
    page.totalElement = data?.result?.totalElements;

    const pageable = data?.result?.pageable;
    page.currentPage = pageable.pageNumber + 1;
    page.pageSize = pageable.pageSize;

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
      page: page.currentPage,
      size: page.pageSize,
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

const Update = async (student) => {
  try {
    if (editingRows.value[student.id]) {
      const body = {
        id: student.id,
        name: student.name,
        email: student.email,
        status: 1,
      };

      const {data} = await axios.put(`${URL_STUDENT}`, body);

      if (data) {
        await getAllStudent();
        editingRows.value[student.id] = false;
        return data;
      } else {
        console.warn("Error updating student or response structure is incorrect.");
      }
    } else {
      editingRows.value[student.id] = true;
    }
  } catch (error) {
    console.error("Error updating student:", error.message);
  }
};


const Delete = async (studentId) => {
  try {
    const {data} = await axios.delete(`${URL_STUDENT}/${studentId}`)

    if (data) {
      await getAllStudent();
      return data;
    } else {
      console.warn("Error deleting student or response structure is incorrect.");
    }
  } catch (error) {
    console.error("Error delete student:", error.message);
  }

}

const handleSizeChange = (val: number) => {
  page.pageSize = val;
  page.currentPage = 1;
  getAllStudent();
  console.log(`${val} items per page`)
}
const handleCurrentChange = (val: number) => {
  page.currentPage = val;
  getAllStudent();
  console.log(`current page: ${val}`)
}

const closeFunction = (id) => {
  editingRows.value[id] = false;
  getAllStudent();
}

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


onMounted(() => {
  getAllStudent();
  getAllCourse();
});

const buttonStyle = computed(() => {
  return {backgroundColor: props.count % 2 != 0 ? 'red' : 'yellow'}
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
