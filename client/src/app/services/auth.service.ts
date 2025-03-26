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
  async postAuthForm(authMtd: string, form: UserAuthForm): Promise<string> {
    try {
      const result = await firstValueFrom(this.http.post<any>(`/api/${authMtd}`, form))
      //console.info('result: ', result)
      if(result.userId) {
        // User json is returned on registration success (hence User.userId available)
        this.userStore.setUser(result)

        return 'success'
      }

      return result.message ? result.message : 'unknown server error'
    } catch (err: any) {
      //console.error('err: ', err)
      return err.error.message ? err.error.message : 'unknown server error' //err.error.message
    }
  }

  async register(form: UserAuthForm): Promise<string> {
    return this.postAuthForm('register', form)
  }

  async login(form: UserAuthForm): Promise<string> {
    return this.postAuthForm('login', form)
  }
  
  loginGoogle() {
    localStorage.setItem('processingGoogleLogin', JSON.stringify(true))

    window.location.href = 'http://localhost:8080/api/oauth2/google'
  }

  async loginGoogleCheckStatus() {
    const isProcessingGoogleLogin = localStorage.getItem('processingGoogleLogin') === 'true';

    if(!isProcessingGoogleLogin)
      return

    firstValueFrom(this.http.get<any>('/api/oauth2/user')).then(
      (response) => {
        //console.info('Received response on google login: ', response)
        if(response.userId)
          // User json is returned on registration success (hence User.userId available)
          this.userStore.setUser(response)

        localStorage.setItem('processingGoogleLogin', JSON.stringify(false))

        // reload page
        location: Location 
        location.reload()
      },
      error => {
        //console.error('Error fetching user data:', error); // Handle error

        localStorage.setItem('processingGoogleLogin', JSON.stringify(false))

        error.error.message ? alert(`Error signing in: ${error.error.message}`) : alert('Error signing in: unknown server error')
      }
    );
  }

  logout() {
    this.userStore.resetStore()
  }
}