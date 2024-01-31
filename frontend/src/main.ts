import './assets/main.css'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

import { createApp } from 'vue'
import App from './App.vue'

export enum Views {
    NONE,
    CALENDAR,
    ROADMAP,
    VEHICLE_DASHBOARD,
    USER_MANAGEMENT
}

export enum Roles{
    ADMIN,
    SECRUITY,
    TRAVEL_OFFICE
}

export class User {
    constructor(public name: String = "", public email: String = "" ,public password: String = "1234", public role: Roles = Roles.ADMIN, public isDriver: boolean = false) {
    }
}

const app = createApp(App);
//token that will be used for REST requests
//could be refactored to a better way to store session tokens
app.config.globalProperties.$accessToken = "";
app.mount('#app')