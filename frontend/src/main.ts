import './assets/main.css'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

import ganttastic from '@infectoone/vue-ganttastic'

import {computed, createApp} from 'vue'
import App from './App.vue'

export enum Views {
    NONE,
    CALENDAR,
    ROADMAP,
    VEHICLE_DASHBOARD,
    USER_MANAGEMENT
}

export class Booking {
    constructor(public car: string = "", public start: Date = new Date(), public end : Date = new Date(), public reason: string = "", public driver: string = "nsimon") {}
    isWithin(day : number) : Boolean {
        return true; //his.start <= day && this.end >= day;
    }
}

const app = createApp(App);

app.use(ganttastic)

//token that will be used for REST requests
//could be refactored to a better way to store session tokens
app.config.globalProperties.$accessToken = "";
app.mount('#app')