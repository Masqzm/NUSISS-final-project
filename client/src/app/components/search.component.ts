import { Component, inject, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import {tuiMarkControlAsTouchedAndValidate} from '@taiga-ui/cdk';

@Component({
  selector: 'app-search',
  standalone: false,
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit {
  private fb = inject(FormBuilder)
  private router = inject(Router)

  protected form!: FormGroup

  @Input({ required: true })
  inlineSearchBtn!: boolean

  ngOnInit(): void {
      this.form = this.createForm()
  }
  private createForm(): FormGroup {
    return this.fb.group({
      keyword: this.fb.control<string>('', [Validators.required])
    })
  }

  search() {
    tuiMarkControlAsTouchedAndValidate(this.form)

    if(this.form.invalid)
      return

    const q = this.form.value['keyword']

    this.router.navigate(['/search'], { queryParams: { q } })
  }
}