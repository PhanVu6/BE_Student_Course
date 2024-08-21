<template>
  <div>
    <nav>
      <section>
        <div class="flex gap-4 mb-4 items-center">
          <el-input @input="getAllCourse()" v-model="paramNameForCourse" style="width: 340px" placeholder="Search"
                    :suffix-icon="Search"
          />
        </div>

      </section>
    </nav>
    <section>
      <el-table :data="courses" stripe style="width: 100%">
        <el-table-column prop="title" label="Titiles" width="180">
          <template #default="scope">
            <el-input v-model="scope.row.title" v-if="editingRows[scope.row.id]"/>
            <span v-else>{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="Description" width="180">
          <template #default="scope">
            <el-input v-model="scope.row.description" v-if="editingRows[scope.row.id]"/>
            <span v-else>{{ scope.row.description }}</span>
          </template>
        </el-table-column>

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

    <!--Create-->
    <section id="student-input">
      <h1>Create Student</h1>
      <el-form ref="form" :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item prop="title" label="Username"
                      :rules="[
          {
            required: true,
            message: 'Please input title',
            trigger: 'blur',
          }
            ]"
        >
          <el-input v-model="formInline.title" placeholder="Username" @change="validateName" clearable/>
        </el-form-item>
        <el-form-item prop="description" label="Description"
                      :rules="[
        {
          required: true,
          message: 'Please input description',
          trigger: 'blur',
        }
      ]"
        >
          <el-input v-model="formInline.description" @change="validateEmail" clearable/>
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

    </section>

  </div>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive, ref} from 'vue';
import axios from 'axios';
import {CircleCloseFilled, Search} from '@element-plus/icons-vue';
import {ComponentSize} from "element-plus";


const URL_COURSE = "http://localhost:8080/course";

const students = ref([]);
const loadingCreate = ref(false);
const isFormEmail = ref(false);
const isFormName = ref(false);
const paramNameForCourse = ref('');
const size = ref<ComponentSize>('default');
const background = ref(true);
const disabledPage = ref(false);
const courses = ref([]);

const formInline = reactive({
  title: '',
  description: null,
})

const page = reactive({
  currentPage: 1,
  pageSize: 10,
  totalElement: 0,
})
const form = ref(null);
const editingRows = ref({});

const getAllCourse = async () => {
  try {
    page.currentPage--;

    const params = new URLSearchParams({
      title: paramNameForCourse.value,
      number: page.currentPage,
      size: page.pageSize,
    });

    const {data} = await axios.get(`${URL_COURSE}`, {params});

    courses.value = data?.result?.content ?? [];
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

const createStudent = async () => {
  loadingCreate.value = true;

  try {
    const body = {
      title: formInline.title,
      description: formInline.description,
      status: 1,
    };

    const {data} = await axios.post(`${URL_COURSE}`, body);

    if (data) {
      await getAllCourse();
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

const Update = async (course) => {
  try {
    if (editingRows.value[course.id]) {
      const body = {
        title: course.title,
        description: course.description,
        status: 1,
      };


      const {data} = await axios.put(`${URL_COURSE}/${course.id}`, body);

      if (data) {
        await getAllCourse();
        editingRows.value[course.id] = false;
        return data;
      } else {
        console.warn("Error updating student or response structure is incorrect.");
      }
    } else {
      editingRows.value[course.id] = true;
    }
  } catch (error) {
    console.error("Error updating student:", error.message);
  }
};


const Delete = async (courseId) => {
  try {
    const {data} = await axios.delete(`${URL_COURSE}/${courseId}`)

    if (data) {
      await getAllCourse();
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
  getAllCourse();
  console.log(`${val} items per page`)
}
const handleCurrentChange = (val: number) => {
  page.currentPage = val;
  getAllCourse();
  console.log(`current page: ${val}`)
}

const closeFunction = (id) => {
  editingRows.value[id] = false;
  getAllCourse();
}

function validateEmail() {
  form.value.validateField('description', (valid) => {
    isFormEmail.value = valid;
  });
}

function validateName() {
  form.value.validateField('title', (valid) => {
    isFormName.value = valid;
  });
}


onMounted(() => {
  getAllCourse();
});

const buttonStyle = computed(() => {
  return {backgroundColor: props.count % 2 != 0 ? 'red' : 'yellow'}
});


// watch(
//     () => formInline.email,
//     () => {
//       validateEmail();
//     }
// );
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


<!--<template>-->
<!--  <el-table :data="tableData" style="width: 100%">-->
<!--    <el-table-column fixed prop="date" label="Date" width="150"/>-->
<!--    <el-table-column prop="name" label="Name" width="120"/>-->
<!--    <el-table-column prop="state" label="State" width="120"/>-->
<!--    <el-table-column prop="city" label="City" width="120"/>-->
<!--    <el-table-column prop="address" label="Address" width="600"/>-->
<!--    <el-table-column prop="zip" label="Zip" width="120"/>-->
<!--    <el-table-column fixed="right" label="Operations" min-width="120">-->
<!--      <template #default>-->
<!--        <el-button link type="primary" size="small" @click="handleClick">-->
<!--          Detail-->
<!--        </el-button>-->
<!--        <el-button link type="primary" size="small">Edit</el-button>-->
<!--      </template>-->
<!--    </el-table-column>-->
<!--  </el-table>-->
<!--</template>-->

<!--<script lang="ts" setup>-->
<!--const handleClick = () => {-->
<!--  console.log('click')-->
<!--}-->

<!--const tableData = [-->
<!--  {-->
<!--    date: '2016-05-03',-->
<!--    name: 'Tom',-->
<!--    state: 'California',-->
<!--    city: 'Los Angeles',-->
<!--    address: 'No. 189, Grove St, Los Angeles',-->
<!--    zip: 'CA 90036',-->
<!--    tag: 'Home',-->
<!--  },-->
<!--  {-->
<!--    date: '2016-05-02',-->
<!--    name: 'Tom',-->
<!--    state: 'California',-->
<!--    city: 'Los Angeles',-->
<!--    address: 'No. 189, Grove St, Los Angeles',-->
<!--    zip: 'CA 90036',-->
<!--    tag: 'Office',-->
<!--  },-->
<!--  {-->
<!--    date: '2016-05-04',-->
<!--    name: 'Tom',-->
<!--    state: 'California',-->
<!--    city: 'Los Angeles',-->
<!--    address: 'No. 189, Grove St, Los Angeles',-->
<!--    zip: 'CA 90036',-->
<!--    tag: 'Home',-->
<!--  },-->
<!--  {-->
<!--    date: '2016-05-01',-->
<!--    name: 'Tom',-->
<!--    state: 'California',-->
<!--    city: 'Los Angeles',-->
<!--    address: 'No. 189, Grove St, Los Angeles',-->
<!--    zip: 'CA 90036',-->
<!--    tag: 'Office',-->
<!--  },-->
<!--]-->
<!--</script>-->