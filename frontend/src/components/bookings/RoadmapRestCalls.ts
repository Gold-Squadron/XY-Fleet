import type {Ref} from "vue";
import {ref} from "vue";

//region Types
export class Booking {

    private refStart: Ref<Date> | null = null;
    private refEnd: Ref<Date> | null = null;

    html: string = "";
    status: string = "";

    constructor(public carId: number = -1, public start: Date = new Date(), public end: Date = new Date(), public reason: string = "", public driverId: number = -1, public id = -1) {
        this.refStart = null;
        this.refEnd = null;
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

export async function getFlights(): Promise<RFlight[]> {

    const headers: Headers = new Headers()

    headers.set('Content-Type', 'application/json')
    headers.set('Accept', 'application/json')

    let username = 'nsimon';
    let password = '123';
    let auth = btoa(`${username}:${password}`);

    headers.set('Authorization', `Basic ${auth}`);

    const request: RequestInfo = new Request('http://127.0.0.1:8080/booking?user=nsimon&secret=123', {
        method: 'GET',
        headers: headers,
    })
    console.log("hi");

    let res1 = await fetch(request);
    let res2: any = await res1.json();
    console.table(res2)
    return res2 as RFlight[]
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

export async function getVehicles(): Promise<RXYWing[]> {

    const headers: Headers = new Headers()

    headers.set('Content-Type', 'application/json')
    headers.set('Accept', 'application/json')

    let username = 'nsimon';
    let password = '123';
    let auth = btoa(`${username}:${password}`);

    headers.set('Authorization', `Basic ${auth}`);

    const request: RequestInfo = new Request('http://127.0.0.1:8080/xywing?user=nsimon&secret=123', {
        method: 'GET',
        headers: headers,
    })
    console.log("hi");

    let response = await fetch(request);
    let jsonResponse: any = await response.json();
    if(jsonResponse.code !== undefined && jsonResponse !== 200) {
        console.error(jsonResponse)
    }
    return jsonResponse as RXYWing[];
}