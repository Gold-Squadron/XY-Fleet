import type {Ref} from "vue";
import {ref} from "vue";
import type {User} from "@/main";

const headers: Headers = new Headers()



//region Types
export class Booking {

    private refStart: Ref<Date> | null = null;
    private refEnd: Ref<Date> | null = null;

    html: string = "";
    status: string = "";

    constructor(public carId: number = -1, public start: Date = new Date(), public end: Date = new Date(), public reason: string = "", public driverId: number = -1, public id = -1) {
        this.refStart = null;
        this.refEnd = null;
        this.start.setHours(1)
        this.end.setHours(23)
    }

    public hasHtml() : boolean {
        return this.end.getTime() - this.start.getTime() < 1000 * 60 * 60 * 47;
    }

    public generateAvatarBasedOnInitials(driver: string): string {
        return `<span data-v-da537051="" class="b-avatar bg-secondary rounded-circle" style="width: 1.6rem; height: 1.6rem">
                    <span class="b-avatar-text">
                        ${driver.substring(0, 2)}
                    </span>
                </span>`;
    }

    //make your own function supporting, ref-less (aka. back to *null*) deep copy
    clone(): Booking {
        return new Booking(this.carId, this.start, this.end, this.reason, this.driverId)
    }

    isWithin(day: number): Boolean {
        return true; //his.start <= day && this.end >= day;
    }


    getStartDateAsReference(): Ref<Date> {
        if (this.refStart == null) {
            this.refStart = ref(this.start);
            console.log("initialising refStart for " + this.carId + " - " + this.driverId)
        }
        return this.refStart;
    }

    getEndDateAsReference(): Ref<Date> {
        if (this.refEnd == null) {
            this.refEnd = ref(this.end);
            console.log("initialising refEnd for " + this.carId + " - " + this.driverId)
        }
        return this.refEnd;
    }

    static from(rFlight: RFlight): Booking {
        return new Booking(rFlight.vehicle_id, new Date(rFlight.leasing_start), new Date(rFlight.leasing_end),
            rFlight.reasoning, rFlight.driver_id, rFlight.id);
    }
}

//endregion Types

export interface RFlight {
    id: number,
    driver_id: number,
    vehicle_id: number,
    leasing_start: string,
    leasing_end: string,
    reasoning: string,
    expected_travel_distance: number
}
export interface RXYWing {
    id: number,
    license_plate: string,
    brand: string,
    model: string,
    chassis_number: string,
    mileage: number,
    expected_mileage: number,
    annual_performance: number,
    insurance_id: number,
    type: string,
    pricing_id: number
}

//Pilot and administrative staff
export interface RPilot {
    id: number,
    name: string
//    role  varchar(8) check( role in ('admin', 'user', 'security')),
//    is_driver tinyint(1) not null
}

export async function getFlights(): Promise<RFlight[]> {
    return await getAsJson('booking') as RFlight[]
}

export async function getVehicles(): Promise<RXYWing[]> {
    return await getAsJson('xywing') as RXYWing[];
}

export async function getPilots(): Promise<RPilot[]> {
    return await getAsJson('user') as RPilot[];
}

export async function getAsJson(url : string): Promise<any[]> {

    headers.set('Content-Type', 'application/json')
    headers.set('Accept', 'application/json')

    let username = 'nsimon';
    let password = '123';
    let auth = btoa(`${username}:${password}`);

    headers.set('Authorization', `Basic ${auth}`);

    const request: RequestInfo = new Request(`http://127.0.0.1:8080/${url}?user=nsimon&secret=123`, {
        method: 'GET',
        headers: headers,
    })
    console.log("hi");

    let response = await fetch(request);
    let jsonResponse: any = await response.json();
    if(jsonResponse.code !== undefined && jsonResponse !== 200) {
        console.error(jsonResponse)
    }
    return jsonResponse;
}

