import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { Restaurant } from '../../models';
import { SearchService } from '../../services/search.service';

@Component({
  selector: 'app-restaurant',
  standalone: false,
  templateUrl: './restaurant.component.html',
  styleUrl: './restaurant.component.css'
})
export class RestaurantComponent implements OnInit , OnDestroy {    // page that loads restaurant-info, rsvp form and upcoming rsvps
  private activatedRoute = inject(ActivatedRoute)
  private searchSvc = inject(SearchService)
  
  restaurantId!: string
  subParams!: Subscription
  result$!: Observable<Restaurant>

  ngOnInit(): void {
    this.subParams = this.activatedRoute.params.subscribe(
      params => {
        this.restaurantId = params['id']
        //console.info('>>> id: ', this.restaurantId)
      }
    )

    this.result$ = this.searchSvc.getRestaurant(this.restaurantId)
  }

  ngOnDestroy(): void {
    this.subParams.unsubscribe()
  }
}
