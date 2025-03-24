export interface Restaurant {
    id: string
    name: string
    address: string
    googleMapsURI: string
    googlePhotosURI: string
    websiteURI: string

    startPrice: number          // lowest price
    endPrice: number            // highest price
    rating: number
    userRatingCount: number     // total users rated
    
    openingHours: string[]
    rsvpIds: []
}

// User authentication
export interface User {
    id: string
    email: string
    token: string
}

export interface UserAuthForm {
    email: string
    username: string
    password: string
}

// export interface UserLoginForm {
//     email: string
//     password: string
// }