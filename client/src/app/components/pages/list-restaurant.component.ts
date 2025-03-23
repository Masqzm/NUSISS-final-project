import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { SearchService } from '../../search.service';
import { Restaurant } from '../../models';

@Component({
  selector: 'app-list-restaurant',
  standalone: false,
  templateUrl: './list-restaurant.component.html',
  styleUrl: './list-restaurant.component.css'
})
export class ListRestaurantComponent implements OnInit, OnDestroy {
  private activatedRoute = inject(ActivatedRoute)
  private searchSvc = inject(SearchService)

  query!: string
  subParams!: Subscription
  results$!: Observable<Restaurant[]>

  // TEST PLACEHOLDER
  testRestaurant!: Restaurant
  testData: Restaurant[] = []

  ngOnInit(): void {
    this.subParams = this.activatedRoute.queryParams.subscribe(
      params => {
        this.query = this.activatedRoute.snapshot.queryParams['q']
        console.info('>>> q: ', this.query)

        this.results$ = this.searchSvc.search(this.query)
      }
    )

    // TEST PLACEHOLDER
    this.testRestaurant = {
        id: "restID123",
        name: "Restoname",
        address: "Resto address Rd, #01-123 Some Center, S123456",
        googleMapsURI: "https://maps.google.com/?cid=12345678",
        googlePhotosURI: "https://photos.google.com/?cid=12345678",
        websiteURI: "https://maps.google.com/?cid=12345678",
    
        startPrice: 1,
        endPrice: 10,
        rating: 4.5,
        userRatingCount: 420,
        
        openingHours: [
          "Monday: 11:00AM - 8:00PM", 
          "Tuesday: 11:00AM - 8:00PM",
          "Wednesday: 11:00AM - 8:00PM",
          "Thursday: 11:00AM - 8:00PM",
          "Friday: 11:00AM - 8:00PM",
          "Saturday: Closed",
          "Sunday: Closed"],
        rsvpIds: []
    }
    for (let i = 0; i < 10; i++) {
      this.testData.push(this.testRestaurant)
    }
  }

  ngOnDestroy(): void {
      this.subParams.unsubscribe()
  }
}
