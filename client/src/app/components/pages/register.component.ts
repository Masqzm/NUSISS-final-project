import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import {tuiMarkControlAsTouchedAndValidate, TuiValidationError} from '@taiga-ui/cdk';
import { UserAuthForm } from '../../models';
import { AuthService } from '../../services/auth.service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  private fb = inject(FormBuilder)
  private router = inject(Router)
  private authSvc = inject(AuthService)

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
      username: this.fb.control<string>('', [Validators.required, 
        Validators.minLength(5), Validators.maxLength(32), 
        Validators.pattern(/^[a-zA-Z0-9_]+$/)]),
      password: this.fb.control<string>('', [Validators.required, Validators.minLength(6)])
    })
  }

  async register() {
    tuiMarkControlAsTouchedAndValidate(this.form)

    if(this.form.invalid)
      return

    console.info('register: ', this.form.value)

    const authForm: UserAuthForm = {
      email: this.form.value['email'],
      username: this.form.value['username'],
      password: this.form.value['password']
    }

    const regAuthResponse = await this.authSvc.register(authForm)

    if(regAuthResponse == 'success') {
      // go back to prev page if there's history
      if(window.history.length > 1)
        this.location.back(); 
      else
        this.router.navigate(['/'])
    } else {
      this.authFailed = true
      console.error('regAuthResponse: ', regAuthResponse)
      if(regAuthResponse == 'invalid')
        this.authFailedTVE = new TuiValidationError('Email is already in use!')
      else 
        this.authFailedTVE = new TuiValidationError('Server error. Please try again')
    }    

    // Reset authFailed after 2s
    setTimeout(() => {
      this.authFailed = false;
    }, 2000);
  }

  regFail(): TuiValidationError | null {
    return this.authFailed ? this.authFailedTVE : null;
  }

  registerGoogle() {
    console.info('cont w google')
    this.authSvc.loginGoogle()
  }
}
