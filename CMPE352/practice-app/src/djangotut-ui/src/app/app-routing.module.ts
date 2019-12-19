import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GoogleMapComponent } from './google-map/google-map.component';
import { GoogleOauthComponent } from './google-oauth/google-oauth.component';
import { EventsComponent } from './events/events.component';
import { NewsComponent } from './news/news.component';
import { CurrencyComponent } from './currency/currency.component';
import { TwitterComponent } from './twitter/twitter.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'google/map', component: GoogleMapComponent },
  { path: 'google/oauth', component: GoogleOauthComponent },
  { path: 'events', component: EventsComponent },
  { path: 'news', component: NewsComponent },
  { path: 'currency', component: CurrencyComponent },
  { path: 'twitter', component: TwitterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
