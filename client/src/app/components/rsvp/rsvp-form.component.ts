import { Component, inject, OnInit } from '@angular/core';
import { RsvpService } from '../../services/rsvp.service';

@Component({
  selector: 'app-rsvp-form',
  standalone: false,
  templateUrl: './rsvp-form.component.html',
  styleUrl: './rsvp-form.component.css'
})
export class RsvpFormComponent implements OnInit {
  private rsvpSvc = inject(RsvpService)

  

  ngOnInit(): void {
    this.rsvpSvc    
  }
}
