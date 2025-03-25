import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

import {tuiMarkControlAsTouchedAndValidate, TuiValidationError} from '@taiga-ui/cdk';
import { AuthService } from '../../services/auth.service';
import { UserAuthForm } from '../../models';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder)
  private authSvc = inject(AuthService)
  private router = inject(Router)

  protected form!: FormGroup
  
  protected authFailed = false
  protected authFailedTVE!: TuiValidationError
  
  constructor(private location: Location) {}

  ngOnInit(): void {
      this.form = this.createForm()
  }
  private createForm(): FormGroup {
    return this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required])
    })
  }

  async login() {
    tuiMarkControlAsTouchedAndValidate(this.form)

    if(this.form.invalid)
      return

    console.info('login: ', this.form.value)

    const authForm: UserAuthForm = {
      email: this.form.value['email'],
      username: '',
      password: this.form.value['password']
    }

    const loginAuthResponse = await this.authSvc.login(authForm)

    if(loginAuthResponse == 'success') {
      // go back to prev page if there's history
      if(window.history.length > 1)
        this.location.back(); 
      else
        this.router.navigate(['/'])
    } else {
      this.authFailed = true
      console.error('loginAuthResponse: ', loginAuthResponse)
      if(loginAuthResponse == 'invalid')
        this.authFailedTVE = new TuiValidationError('Invalid username or password!') 
      else
        this.authFailedTVE = new TuiValidationError('Server error. Please try again') 
    }

    // Reset authFailed after 2s
    setTimeout(() => {
      this.authFailed = false;
    }, 2000);
  }

  loginFail(): TuiValidationError | null {
    return this.authFailed ? this.authFailedTVE : null;
  }

  loginGoogle() {
    this.authSvc.loginGoogle()
  }
}