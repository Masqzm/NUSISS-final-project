import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import {tuiMarkControlAsTouchedAndValidate} from '@taiga-ui/cdk';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  private fb = inject(FormBuilder)
  private router = inject(Router)

  protected form!: FormGroup

  ngOnInit(): void {
      this.form = this.createForm()
  }
  private createForm(): FormGroup {
    return this.fb.group({
      email: this.fb.control<string>('', [Validators.required, Validators.email]),
      password: this.fb.control<string>('', [Validators.required])
    })
  }

  login() {
    tuiMarkControlAsTouchedAndValidate(this.form)

    if(this.form.invalid)
      return

    console.info('login: ', this.form.value)

    //this.router.navigate(['/search'], { queryParams: { q } })
  }

  loginGoogle() {
    console.info('login w google')
  }
}