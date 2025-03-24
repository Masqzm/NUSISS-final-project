import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import {tuiMarkControlAsTouchedAndValidate} from '@taiga-ui/cdk';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
  private fb = inject(FormBuilder)
  private router = inject(Router)

  protected form!: FormGroup

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

  register() {
    tuiMarkControlAsTouchedAndValidate(this.form)

    if(this.form.invalid)
      return

    console.info('register: ', this.form.value)

    //this.router.navigate(['/search'], { queryParams: { q } })
  }

  registerGoogle() {
    console.info('register w google')
  }
}
