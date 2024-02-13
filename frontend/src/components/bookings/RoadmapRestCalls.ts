import type {Ref} from "vue";
import {ref} from "vue";

//region Types
export class Booking {

    private refStart : Ref<Date> | null = null;
    private refEnd : Ref<Date> | null = null;

    status: string = "";
    constructor(public car: string = "", public start: Date = new Date(), public end : Date = new Date(), public reason: string = "", public driver: string = "nsimon") {
        this.refStart = null;
        this.refEnd = null;
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

export function getBookings(): Promise<Booking[]> {

    // We can use the `Headers` constructor to create headers
    // and assign it as the type of the `headers` variable
    const headers: Headers = new Headers()
    // Add a few headers
    headers.set('Content-Type', 'application/json')
    headers.set('Accept', 'application/json')
    // Add a custom header, which we can use to check
    //headers.set('X-Custom-Header', 'CustomValue')

    // Create the request object, which will be a RequestInfo type.
    // Here, we will pass in the URL as well as the options object as parameters.

    const request: RequestInfo = new Request('http://127.0.0.1:8080/booking?user=nsimon&secret=123', {
        method: 'GET',
        headers: headers,
    })
    console.log("hi")

    // For our example, the data is stored on a static `users.json` file
    return fetch(request)
        // the JSON body is taken from the response
        .then(res => res.json())
        .then(res => {
            // The response has an `any` type, so we need to cast
            // it to the `User` type, and return it from the promise
            console.log(res)
            return res as Booking[]
        })
}