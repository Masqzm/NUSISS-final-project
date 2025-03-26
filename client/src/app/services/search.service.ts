import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Restaurant } from "../models";

@Injectable()
export class SearchService {
    private http = inject(HttpClient)

    search(query: string): Observable<Restaurant[]> {
        return this.http.get<Restaurant[]>(`/api/search?q=${query}`)
    }

    getRestaurant(id: string): Observable<Restaurant> {
        return this.http.get<Restaurant>(`/api/restaurant/${id}`)
    }
}