import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { UserStore } from '../../user.store';
import { firstValueFrom, Observable } from 'rxjs';
import { User } from '../../models';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit, OnDestroy {
  private userStore = inject(UserStore)

  user$!: Observable<User>

  ngOnInit(): void {
      this.user$ = this.userStore.getUser$
  }

  ngOnDestroy(): void {
      
  }

  logout() {
    this.userStore.resetStore()
  }

  async checkUser() {
    console.info("userStore: ", await firstValueFrom(this.user$))
  }
}
