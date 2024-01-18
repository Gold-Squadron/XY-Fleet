import './assets/main.css'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

import ganttastic from '@infectoone/vue-ganttastic'

import { createApp } from 'vue'
import App from './App.vue'

const app = createApp(App);

app.use(ganttastic)

//token that will be used for REST requests
//could be refactored to a better way to store session tokens
app.config.globalProperties.$accessToken = "";
app.mount('#app')
