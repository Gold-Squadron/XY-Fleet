import './assets/main.css'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css'

import ganttastic from '@infectoone/vue-ganttastic'

import {computed, createApp, ref} from 'vue'
import {type Ref} from 'vue'
import App from './App.vue'

//region Types
export enum Views {
    NONE,
    CALENDAR,
    ROADMAP,
    VEHICLE_DASHBOARD,
    USER_MANAGEMENT
}

function generateAvatarBasedOnInitials(driver: string) {
    return `<span data-v-da537051="" class="b-avatar bg-secondary rounded-circle" style="width: 1.6rem; height: 1.6rem">
            <span class="b-avatar-text">
                ${driver.substring(0, 2)}
            </span>
          </span>
`;
}

export enum Roles{
    ADMIN,
    SECURITY,
    TRAVEL_OFFICE
}

export class User {
    constructor(private id: String = "", public name: String = "", public email: String = "" ,public password: String = "1234", public role: Roles = Roles.ADMIN, public isDriver: boolean = false) {
    }

    public getId(): String{
        return this.id
    }
}

export class Booking {

    private refStart : Ref<Date> | null = null;
    private refEnd : Ref<Date> | null = null;

    html : string = "";

    status: string = "";
    constructor(public car: string = "", public start: Date = new Date(), public end : Date = new Date(), public reason: string = "", public driver: string = "nsimon") {
        this.refStart = null;
        this.refEnd = null;
        this.start.setHours(1)
        this.end.setHours(23)
        if(this.end.getTime() - this.start.getTime() < 1000 * 60 * 60 * 47) {
            this.html = generateAvatarBasedOnInitials(driver)
        }
    }

    //make your own function supporting, ref-less (aka. back to *null*) deep copy
    clone() : Booking {
        return new Booking(this.car, this.start, this.end, this.reason, this.driver)
    }

    isWithin(day : number) : Boolean {
        return true; //his.start <= day && this.end >= day;
    }
    getStartDateAsReference() : Ref<Date> {
        if(this.refStart == null) {
            this.refStart = ref(this.start);
            console.log("initialising refStart for " + this.car + " - " + this.driver)
        }
        return this.refStart;
    }

    getEndDateAsReference() : Ref<Date> {
        if(this.refEnd == null) {
            this.refEnd = ref(this.end);
            console.log("initialising refEnd for " + this.car + " - " + this.driver)
        }
        return this.refEnd;
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