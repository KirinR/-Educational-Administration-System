import axios from 'axios'
import { createApp } from 'vue'
import App from './App.vue'
axios.defaults.withCredentials=true
createApp(App).mount('#app')