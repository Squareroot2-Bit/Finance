import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/theme-chalk/el-message.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import axios from 'axios'

axios.defaults.baseURL = 'http://localhost:3000'
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
app.use(router)
app.use(ElementPlus, {
  locale: zhCn
})
app.mount('#app')
