import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, Observable, tap } from "rxjs";
import { Rsvp } from "../models";

@Injectable()
export class RsvpService {
  private http = inject(HttpClient)

  getFormTopics(): Promise<string[]> {
    return firstValueFrom(this.http.get<string[]>('/api/rsvpTopics'))
  }

  postRsvp(rsvp: Rsvp): Promise<string> {
    return firstValueFrom(this.http.post<string>('/api/rsvp', rsvp))
  }

  getRsvps(): Observable<Rsvp[]> {
    return this.http.get<Rsvp[]>('/api/rsvps').pipe(
        tap(rsvps => console.log('RSVPs: ', rsvps))
    )
  }
}