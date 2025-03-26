import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-home',
  standalone: false,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  private authSvc = inject(AuthService)
  
  ngOnInit(): void {
    this.authSvc.loginGoogleCheckStatus()
  }
}
