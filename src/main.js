// import './assets/main.css'
//
// import {createApp} from 'vue'
// import App from './App.vue'
//
// createApp(App).mount('#app')


// main.js
import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'

const app = createApp(App)

app.use(ElementPlus)
app.mount('#app')