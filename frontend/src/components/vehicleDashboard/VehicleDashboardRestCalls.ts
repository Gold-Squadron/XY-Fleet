import type {User} from "@/main";
import {Vehicle} from "@/main";

const headers: Headers = new Headers()

//endregion Types

//Wings and further information
export interface RXYWing {
    id: number,
    license_plate: string,
    brand: string,
    model: string,
    chassis_number: string,
    mileage: number,
    excpected_mileage: number,
    annual_performance : number,
    type: string,
    insurance_id: number,
    pricing_id: number
}

export interface RInsurance {
    id: number,
    insurance_number: number,
    registration_date: number,
    insurance_number_expiration: number
}

export interface RPricing {
    id: number,
    purchase_date: Date,
    list_price_gross: number,
    leasing_installment_net: number
}

export async function getAllXYWings(): Promise<RXYWing[]> {
    return await getAsJson('xywing') as RXYWing[]
}

export async function getAllInsurances(): Promise<RInsurance[]> {
    return await getAsJson('insurance') as RInsurance[]
}

export async function getAllPricings(): Promise<RPricing[]> {
    return await getAsJson('pricing') as RPricing[]
}

export async function getInsurance(id: number): Promise<RInsurance> {
    return await getAsJson(`insurance/${id}`) as RInsurance
}

export async function getPricing(id: number): Promise<RPricing> {
    return await getAsJson(`pricing/${id}`) as RPricing
}

export async function removeWing(id: number): Promise<void> {
    const url: string = `xywing/${id}`

    const request: Request = new Request(`http://127.0.0.1:8080/${url}?user=nsimon&secret=123`, {
        method: 'DELETE',
        headers: headers,
    })

    await fetch(request)
}

export async function removeInsurance(id: number): Promise<void> {
    const url: string = `insurance/${id}`

    const request: Request = new Request(`http://127.0.0.1:8080/${url}?user=nsimon&secret=123`, {
        method: 'DELETE',
        headers: headers,
    })

    await fetch(request)
}

export async function removePricing(id: number): Promise<void> {
    const url: string = `pricing/${id}`

    const request: Request = new Request(`http://127.0.0.1:8080/${url}?user=nsimon&secret=123`, {
        method: 'DELETE',
        headers: headers,
    })

    await fetch(request)
}

export async function addWing(w: Vehicle): Promise<void> {

    const url: string = 'xywing'
    const dataQuery: string = `license_plate=${w.licensePlate}&brand=${w.brand}&model=${w.model}&chassis_number=${w.chassisNumber}&mileage=${w.mileage}&expected_mileage=${w.annualPerformance}&annual_performance=${w.annualPerformance}&insurance_id=100&type=${w.type}&pricing_id=100`
    const request: Request = new Request(`http://127.0.0.1:8080/${url}?${dataQuery}`, {
        method: 'PUT',
        headers: headers,
    })

    await fetch(request)
}

export async function editPilot(user: User, sameName: boolean, role: any): Promise<void> {
    const nameParam: string = sameName ? '' : `name=${user.name}`
    const url: string = `user/${user.getUiId()}`
    const dataQuery: string = `${nameParam}&is_driver=${Number(user.isDriver)}&role=${role}`

    const request: Request = new Request(`http://127.0.0.1:8080/${url}?${dataQuery}`, {
        method: 'POST',
        headers: headers,
    })

    await fetch(request)
}

export async function getAsJson(url: string): Promise<any[] | any> {

    headers.set('Content-Type', 'application/json')
    headers.set('Accept', 'application/json')

    let username = 'nsimon'
    let password = '123'
    let auth = btoa(`${username}:${password}`)

    headers.set('Authorization', `Basic ${auth}`)

    const request: RequestInfo = new Request(`http://127.0.0.1:8080/${url}?user=nsimon&secret=123`, {
        method: 'GET',
        headers: headers,
    })

    let response = await fetch(request)
    let jsonResponse: any = await response.json()
    if (jsonResponse.code !== undefined && jsonResponse !== 200) {
        console.error(jsonResponse)
    }
    return jsonResponse
}