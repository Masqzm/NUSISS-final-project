import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable()
export class RsvpService {
  private http = inject(HttpClient)

  getFormTopics(): Observable<string[]> {
    return this.http.get<string[]>('/api/rsvpTopics')
  }
}