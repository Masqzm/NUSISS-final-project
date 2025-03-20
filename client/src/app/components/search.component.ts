import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import {tuiMarkControlAsTouchedAndValidate} from '@taiga-ui/cdk';

@Component({
  selector: 'app-search',
  standalone: false,
  templateUrl: './search.component.html',
  styleUrl: './search.component.css'
})
export class SearchComponent implements OnInit {
  private fb = inject(FormBuilder)

  protected form!: FormGroup

  ngOnInit(): void {
      this.form = this.createForm()
  }
  private createForm(): FormGroup {
    return this.fb.group({
      restaurant: this.fb.control<string>('', [Validators.required])
    })
  }

  search() {
    tuiMarkControlAsTouchedAndValidate(this.form)

    if(this.form.invalid)
      return

    console.info('>>> search: ', this.form.value['restaurant'])
  }
}