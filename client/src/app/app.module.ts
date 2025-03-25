import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { NG_EVENT_PLUGINS } from "@taiga-ui/event-plugins";
import { TuiRoot } from "@taiga-ui/core";
import { TaigaUIModule } from './taigaUI.module';

import { AppComponent } from './app.component';

// components/pages
import { HomeComponent } from './components/pages/home.component';
import { AboutComponent } from './components/pages/about.component';
import { RestaurantComponent } from './components/pages/restaurant.component';
import { ListRestaurantComponent } from './components/pages/list-restaurant.component';
import { ViewRsvpComponent } from './components/pages/view-rsvp.component';
import { ListRsvpAllComponent } from './components/pages/list-rsvp-all.component';
import { ListRsvpSelfComponent } from './components/pages/list-rsvp-self.component';
import { LoginComponent } from './components/pages/login.component';
import { RegisterComponent } from './components/pages/register.component';

// components/layout
import { HeaderComponent } from './components/layout/header.component';
import { FooterComponent } from './components/layout/footer.component';

// components/rsvp
import { RsvpCardComponent } from './components/rsvp/rsvp-card.component';
import { RsvpFormComponent } from './components/rsvp/rsvp-form.component';

// components (generic components used here and there)
import { SearchComponent } from './components/search.component';
import { RestaurantInfoComponent } from './components/restaurant-info.component';

import { SearchService } from './services/search.service';  
import { AuthService } from './services/auth.service';
import { UserStore } from './user.store';
import { allowRegistration } from './guards';

const appRoutes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'about', component: AboutComponent },
  { path: 'search', component: ListRestaurantComponent },
  { path: 'login', component: LoginComponent },
  { path: 'restaurant/:id', component: RestaurantComponent },
  { path: 'rsvp/:rsvpId', component: ViewRsvpComponent },
  { path: 'myRsvp', component: ListRsvpSelfComponent },
  { path: 'allRsvp', component: ListRsvpAllComponent },
  
  // Route guards
  { path: 'register', component: RegisterComponent, canActivate: [allowRegistration] },

  { path: '**', redirectTo: '/', pathMatch: 'full' },  
]

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    SearchComponent,
    RegisterComponent,
    LoginComponent,
    RestaurantComponent,
    RestaurantInfoComponent,
    RsvpFormComponent,
    RsvpCardComponent,
    ViewRsvpComponent,
    ListRsvpSelfComponent,
    ListRsvpAllComponent,
    HeaderComponent,
    FooterComponent,
    ListRestaurantComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    TuiRoot,
    TaigaUIModule,
    ReactiveFormsModule,
    RouterModule.forRoot(appRoutes, { useHash: true })      
],
  providers: [ 
    SearchService,
    AuthService,
    UserStore,
    provideHttpClient(),
    NG_EVENT_PLUGINS
],
  bootstrap: [AppComponent]
})
export class AppModule { }
