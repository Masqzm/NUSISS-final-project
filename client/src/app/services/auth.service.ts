import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import { User, UserAuthForm } from '../models';
import { UserStore } from '../user.store';

@Injectable()
export class AuthService {
  private http = inject(HttpClient)
  private userStore = inject(UserStore)

  // Google sign-in ref: https://medium.com/@sallu-salman/implementing-sign-in-with-google-in-spring-boot-application-5f05a34905a8
  private googleClientId = '965456258956-72p2vtec27mcvk17d586ola7v5b2dp0c.apps.googleusercontent.com'
  private googleRedirectUri = 'http://localhost:8080/api/oauth2/code'  // For Google sign-in to redirect & send OAuth2 code to backend

  // returns 'success' for successful registration/login => redirect to homepage
  // returns <msg> for 500 errors (incl. email unavailable errors for reg, invalid user/pw)
  async postAuthForm(authMtd: string, form: UserAuthForm): Promise<string> {
    try {
      const result = await firstValueFrom(this.http.post<any>(`/api/${authMtd}`, form))
      console.info('result: ', result)
      if(result.userId) {
        // User json is returned on registration success (hence User.userId available)
        this.userStore.setUser(result)

        return 'success'
      }

      return result.message ? result.message : 'unknown server error'
    } catch (err: any) {
      console.error('err: ', err)
      return err.message ? err.message : 'unknown server error'
    }
  }

  async register(form: UserAuthForm): Promise<string> {
    return this.postAuthForm('register', form)
  }

  async login(form: UserAuthForm): Promise<string> {
    return this.postAuthForm('login', form)
  }
  
  loginGoogle() {
    // Scopes to access user's data from google (https://www.googleapis.com/auth/userinfo.email, https://www.googleapis.com/auth/userinfo.profile, openid)
    const google_scope_uri = '&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline'
    // Google sign in link
    const google_uri = `https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=${this.googleRedirectUri}&response_type=code&client_id=${this.googleClientId}` + google_scope_uri
    // Redirect to google sign in
    window.location.href = google_uri
  }

  logout() {
    this.userStore.resetStore()
  }
}