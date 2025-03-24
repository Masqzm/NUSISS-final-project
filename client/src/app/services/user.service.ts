import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import { User, UserAuthForm } from '../models';

@Injectable()
export class UserService {
  private http = inject(HttpClient)

  register(payload: UserAuthForm): Promise<User> {
      return firstValueFrom(this.http.post<User>('/api/register', payload))
  }

  login(payload: UserAuthForm): Promise<User> {
    return firstValueFrom(this.http.post<User>('/api/register', payload))
  }
}
