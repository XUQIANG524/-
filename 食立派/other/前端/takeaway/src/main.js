import './assets/main.css'
import axios from 'axios';
import {
  createApp
} from 'vue'
import App from './App.vue'
import router from './router'
import * as echarts from 'echarts';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 引入 Element Plus
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css'; // 引入样式

// 导入 vue-toastification 和样式
import Toast, {
  useToast
} from 'vue-toastification';
import 'vue-toastification/dist/index.css';

// 配置全局 Axios 默认值
axios.defaults.baseURL = 'http://localhost:8088'; // 后端地址
axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem('token') || ''}`; // 全局设置 Token

// 创建 Vue 实例
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(router)
app.use(ElementPlus); // 注册 Element Plus
app.mount('#app')

// 使用 vue-toastification 插件
app.use(Toast);
