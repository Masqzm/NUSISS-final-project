import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import { User, UserAuthForm } from '../models';
import { UserStore } from '../user.store';

@Injectable()
export class AuthService {
  private http = inject(HttpClient)
  private userStore = inject(UserStore)

  // returns 'success' for successful registration/login => redirect to homepage
  // returns <msg> for 500 errors (incl. email unavailable errors for reg, invalid user/pw)
  postAuthForm(authMtd: string, form: UserAuthForm): string {
    let response = ''

    firstValueFrom(this.http.post<any>(`/api/${authMtd}`, form)).then(
      (result) => {
        if(result.token) {   
          // User json is returned on registration success (hence token available)
          this.userStore.setUser(result)

          response = 'success'
        } else {
          // response = "{ message: <error msg> }" (returned on registration failure)
          response = result.message ? result.message : 'unknown server error'

          console.info(`${authMtd} error msg: ${response}`)
        }
      }
    )

    return response
  }

  register(form: UserAuthForm): string {
    return this.postAuthForm('register', form)
  }

  login(form: UserAuthForm): string {
    return this.postAuthForm('login', form)
  }
  
  // loginGoogle(form: UserAuthForm): boolean {
  //   return firstValueFrom(this.http.post<User>('/api/register', form))
  // }

  logout() {
    this.userStore.resetStore()
  }
}