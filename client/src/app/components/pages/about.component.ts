import { Component, inject, OnInit } from '@angular/core';
import { PaymentService } from '../../services/payment.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-about',
  standalone: false,
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent implements OnInit {
  private fb = inject(FormBuilder)
  private paymentSvc = inject(PaymentService)

  protected open = false;

  protected form!: FormGroup

  //protected readonly amount = new FormControl<number | null>(null, Validators.required, Validators.min(1));

  ngOnInit(): void {
    this.form = this.fb.group({
      amount: this.fb.control<number>(0, [Validators.required, Validators.min(1)])
    })
  }

  protected showDialog(): void {
      this.open = true;
  }

  donate(observer: any) {
    observer.complete()
    
    if (this.form.valid) {
      this.paymentSvc.checkout(this.form.value['amount']).subscribe(
        response => {
          console.info("response.sessionId: ", response.sessionId)
          this.paymentSvc.redirectToCheckout(response.sessionId)
        }
      )
    }      
  }
}
