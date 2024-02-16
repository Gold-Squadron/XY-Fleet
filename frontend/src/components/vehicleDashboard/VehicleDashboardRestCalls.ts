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

export async function editWing(w: Vehicle): Promise<any> {
    headers.set('Content-Type', 'application/json')
    headers.set('Accept', 'application/json')

    const url: string = `xywing/${w.getUiId()}`
    const dataQuery: string = `license_plate=${w.licensePlate}&brand=${w.brand}&model=${w.model}&chassis_number=${w.chassisNumber}&mileage=${w.mileage}&expected_mileage=${w.annualPerformance}&annual_performance=${w.annualPerformance}&type=${w.type}`

    // Edit wing
    const wingRequest = new Request(`http://127.0.0.1:8080/${url}?${dataQuery}`, {
        method: 'POST',
        headers: headers,
    })

    let response = await fetch(wingRequest)
    let jsonResponse: any = await response.json()
    if (jsonResponse.code !== undefined && jsonResponse !== 200) {
        console.error(jsonResponse)
    }
    return jsonResponse
}

export async function editPricing(w: Vehicle, id: number): Promise<void> {
    const url = `pricing/${id}`
    const dataQuery: string = `purchase_date=${w.pricing.purchaseDate}&list_price_gross=${w.pricing.listPriceGross}&leasing_installment_net=${w.pricing.leasingCostNet}`
    const pricingRequest = new Request(`http://127.0.0.1:8080/${url}?${dataQuery}`, {
        method: 'POST',
        headers: headers,
    })

    let response = await fetch(pricingRequest)
}

export async function editInsurance(w: Vehicle, id: number): Promise<void> {
    const url = `insurance/${id}`
    const dataQuery: string = `insurance_number=${w.insurance.number}&registration_date=${w.insurance.registrationDate}&insurance_number_expiration=${w.insurance.expiration}`
    const insuranceRequest = new Request(`http://127.0.0.1:8080/${url}?${dataQuery}`, {
        method: 'POST',
        headers: headers,
    })

    let response = await fetch(insuranceRequest)
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