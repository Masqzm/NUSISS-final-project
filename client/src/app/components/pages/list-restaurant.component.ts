import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { Restaurant } from '../../models';
import { SearchService } from '../../services/search.service';

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

  ngOnInit(): void {
    this.subParams = this.activatedRoute.queryParams.subscribe(
      params => {
        this.query = this.activatedRoute.snapshot.queryParams['q']
        //console.info('>>> q: ', this.query)

        this.results$ = this.searchSvc.search(this.query)
      }
    )
  }

  ngOnDestroy(): void {
      this.subParams.unsubscribe()
  }
}
