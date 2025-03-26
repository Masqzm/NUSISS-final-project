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
    rsvpIds: string[]
}

// User authentication
export interface User {
    userId: string
    providerId: string
    email: string
    username: string
    rsvpIds: string[]
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

// Rsvp (Jio)
export interface Rsvp {
    id: string
    posterId: string
    posterName: string
    restaurantId: string
    timestamp: number
    topcis: string[]
    attendeeIds: string[]
}