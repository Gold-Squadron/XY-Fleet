import './assets/main.css'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

import ganttastic from '@infectoone/vue-ganttastic'

import {computed, createApp} from 'vue'
import App from './App.vue'

//region Types
export enum Views {
    NONE,
    CALENDAR,
    ROADMAP,
    VEHICLE_DASHBOARD,
    USER_MANAGEMENT
}

export class Booking {
    status: string = "";
    constructor(public car: string = "", public start: Date = new Date(), public end : Date = new Date(), public reason: string = "", public driver: string = "nsimon") {}
    isWithin(day : number) : Boolean {
        return true; //his.start <= day && this.end >= day;
    }
}
//endregion Types

//region Date Helper Method
declare global {
    interface Date {
        translateDays(days : number): Date;
    }
}

Date.prototype.translateDays = function (days : number): Date {
    return new Date(this.getTime() + days * 24 * 60 * 60 * 1000);
};
//endregion Date Helper Method

const app = createApp(App);

app.use(ganttastic)

//token that will be used for REST requests
//could be refactored to a better way to store session tokens
app.config.globalProperties.$accessToken = "";
app.mount('#app')