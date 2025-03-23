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