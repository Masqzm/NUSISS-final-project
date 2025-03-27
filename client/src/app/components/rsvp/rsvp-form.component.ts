import { Component, inject, Input, OnInit } from '@angular/core';
import { RsvpService } from '../../services/rsvp.service';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserStore } from '../../user.store';
import { Rsvp, User } from '../../models';
import { firstValueFrom, Observable } from 'rxjs';

import {TuiAlertService} from '@taiga-ui/core';

@Component({
  selector: 'app-rsvp-form',
  standalone: false,
  templateUrl: './rsvp-form.component.html',
  styleUrl: './rsvp-form.component.css'
})
export class RsvpFormComponent implements OnInit {
  private rsvpSvc = inject(RsvpService)
  private fb = inject(FormBuilder)
  private router = inject(Router)
  private userStore = inject(UserStore)

  private readonly alerts = inject(TuiAlertService)

  protected form!: FormGroup

  protected user$!: Observable<User>

  paxChoices = [ 1, 2, 3, 4, 5, 6 ]
  topics: string[] = []

  @Input()
  restaurantId!: string

  ngOnInit(): void {

    this.form = this.createForm()

    this.user$ = this.userStore.getUser$

    this.rsvpSvc.getFormTopics().then(
      (result: string[]) => { 
        this.topics = [...result]
      }
    )
  }
  private createForm(): FormGroup {
    return this.fb.group({
      dateTime: this.fb.control<string>('', [Validators.required]),
      capacity: this.fb.control<number>(1, [Validators.required, Validators.min(1), Validators.max(6)]),
      rsvpPromo: this.fb.control<boolean>(false),
      //topics: this.fb.array<string[]>([], [Validators.required])
      //topics: this.fb.array([], [Validators.required])
      topics: this.fb.control<string>('', [Validators.required])
    })
  }

  async processForm() {
    const user: User = await firstValueFrom(this.user$)
    // check if user logged in
    if(user.userId == '') 
      this.router.navigate(['/login'])
    
    //console.info(this.form.value.dateTime)
    const date = this.form.value.dateTime[0]
    const day = date.day
    const mth = date.month - 1 // js mths starts from 0
    const yr = date.year
    const time = this.form.value.dateTime[1]
    const hr = time.hours
    const min = time.minutes
    const sec = time.seconds
    const timestamp = new Date(yr, mth, day, hr, min, sec).getTime();

    // separate topics by comma, trim whitespaces and remove blanks
    const inputTopics = this.form.value.topics.split(",").map((item: string) => item.trim()).filter((item: string) => item !== "")
    //console.log(inputTopics)
    //console.log(timestamp)
    //console.log(this.form.value.capacity)
    //console.log(this.form.value.rsvpPromo)

    const rsvp: Rsvp = {
      id: '',
      posterId: user.userId,
      posterName: user.username,
      restaurantId: this.restaurantId,
      timestamp: timestamp,
      capacity: this.form.value.capacity,
      rsvpingForPromo: this.form.value.rsvpPromo,
      topics: inputTopics,
      attendeesEmails: [user.email]    // first user joining is poster
    }

    //console.log(rsvp)

    this.rsvpSvc.postRsvp(rsvp).then(
      (result) => {
        console.info('result: ', result)
        //if(result == '{}')
        this.alerts.open('Successfully set Jio event!', {label: ''}).subscribe()
      },
      (err) => {
        console.info('err: ', err)
        //if(result == '{}')
        this.alerts.open('Failed to set Jio event!', {label: '', appearance: 'negative'}).subscribe()
      }
    )
  }

  // get topicsArray(): FormArray {
  //   return this.form.get('topics') as FormArray;
  // }
  // getSelectedTopics(): string[] {
  //   return this.topics.filter((topic, index) => (this.topicsArray.at(index) as FormControl).value);
  // }
}
