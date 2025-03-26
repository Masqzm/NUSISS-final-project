import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { loadStripe } from '@stripe/stripe-js';
import { Observable } from "rxjs";

@Injectable()
export class PaymentService {
    private http = inject(HttpClient);

    // Set Stripe publishable key
    private stripePromise = loadStripe('pk_test_51R6wEX2f0zVXIolaLoadjEbrQw4m4DX8Mq3j0gNNKpUKNAHpWBPIA7hJv4JnYWyPTczyRHaQ43m8dTbVaGEZmiCf00TUl7yrf8'); 

    // Checkout and return sessionId
    checkout(cost_cents: number): Observable<{ sessionId: string }> {
        //console.log('cost_cents: ', cost_cents)
        return this.http.post<{ sessionId: string }>('/api/payment/checkout', { cost_cents });
    }

    // Sends users to stripe client for payment
    async redirectToCheckout(sessionId: string) {
        const stripe = await this.stripePromise;
        await stripe?.redirectToCheckout({ sessionId });
    }
}