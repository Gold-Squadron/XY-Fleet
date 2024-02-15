const headers: Headers = new Headers()

//endregion Types

//Pilot and administrative staff
export interface RPilot {
    id: number,
    name: string,
    is_driver: boolean
//    role  varchar(8) check( role in ('admin', 'user', 'security')),
//    is_driver tinyint(1) not null
}

export async function getAllPilots(): Promise<RPilot[]> {
    return await getAsJson('user') as RPilot[]
}

// !TODO!
export async function removePilot(id: number): Promise<void> {
}

// !TODO!
export async function addPilot(data: object): Promise<void> {
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