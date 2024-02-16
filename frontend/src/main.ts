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


export enum Roles{
    ADMIN,
    SECURITY,
    TRAVEL_OFFICE,
    USER,
    NONE
}

export class User {
    constructor(private uiId: String = "", public name: string = "", public email: string = "" ,public password: string = "", public role: Roles = Roles.ADMIN, public isDriver: any = false) {
    constructor(private uiId: String = "", public name: string = "", public password: string = "", public role: Roles = Roles.ADMIN, public isDriver: boolean = false) {
    }

    public getUiId(): String{
        return this.uiId
    }
    public setUiId(id: string): void {
        this.uiId = id
    }
}

export class Vehicle {
    public gasCard: GasCard = new GasCard()
    public insurance: Insurance = new Insurance()
    public prcing: Pricing = new Pricing()
    constructor(private uiId: String = "", public licensePlate: string = "", public brand: string = "", public model: string = "", public chassisNumber: string = "", public mileage: number = 0, public annualPerformance: number = 0, public type: string = "") {
    }

    public getUiId(): String{
        return this.uiId
    }
    public setUiId(id: String) : void {
        this.uiId = id
    }
}

export class GasCard {
    constructor(public holder: string = "", public numberAral: any = null, public numberShell: any = null, public pin: any = null) {
    }
}

export class Insurance {
    constructor(public number: any = null, public registrationDate: any = null, public expiration: any = null) {
    }

    public getUiId(): String{
        return this.uiId
    }
    public setUiId(id: String) : void {
        this.uiId = id
}
}
export class Pricing {
            constructor(public purchaseDate: any = null, public listPriceGross: any = null, public leasingCostNet : any = null) {
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