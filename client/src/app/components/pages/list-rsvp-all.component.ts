import { Component, inject, OnInit } from '@angular/core';
import { RsvpService } from '../../services/rsvp.service';
import { Observable } from 'rxjs';
import { Rsvp } from '../../models';

@Component({
  selector: 'app-list-rsvp-all',
  standalone: false,
  templateUrl: './list-rsvp-all.component.html',
  styleUrl: './list-rsvp-all.component.css'
})
export class ListRsvpAllComponent implements OnInit {
  private rsvpSvc = inject(RsvpService)
  
  rsvps$!: Observable<Rsvp[]>

  ngOnInit(): void {
    this.rsvps$ = this.rsvpSvc.getRsvps()
  }
}
