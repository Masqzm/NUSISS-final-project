import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { PaymentService } from '../../services/payment.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

import {TuiAlertService} from '@taiga-ui/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-about',
  standalone: false,
  templateUrl: './about.component.html',
  styleUrl: './about.component.css'
})
export class AboutComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder)
  private paymentSvc = inject(PaymentService)
  private readonly alerts = inject(TuiAlertService)
  private activatedRoute = inject(ActivatedRoute)

  protected open = false;

  protected form!: FormGroup

  subParams!: Subscription

  ngOnInit(): void {
    this.form = this.fb.group({
      amount: this.fb.control<number>(0, [Validators.required, Validators.min(1)])
    })

    this.subParams = this.activatedRoute.queryParams.subscribe(
      params => {
        console.log(params)
        if(params['donateSuccess'])
          this.showNotification(params['donateSuccess'] == 'true')
      }
    )
  }
  ngOnDestroy(): void {
    this.subParams.unsubscribe()
  }
 
  protected showNotification(success: boolean): void {
    if(success) {
      this.alerts
          .open('Thanks for helping us keep the site alive!', {label: 'Donation success!'})
          .subscribe();
    } else {
      this.alerts
          .open(`Your payment didn't go through :(`, 
            {label: 'Donation failed!', appearance: 'negative'})
          .subscribe();
    }
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
