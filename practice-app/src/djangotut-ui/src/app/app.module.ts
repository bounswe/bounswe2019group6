import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgModule } from '@angular/core';
import { AgmCoreModule } from '@agm/core';
import { HttpClientModule } from '@angular/common/http';
import { environment } from '../environments/environment';
import { FormsModule } from '@angular/forms';
import { OAuthModule } from 'angular-oauth2-oidc';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MaterialModule } from './material/material.module';
import { GoogleMapComponent } from './google-map/google-map.component';
import { GoogleOauthComponent } from './google-oauth/google-oauth.component';
import { EventsComponent } from './events/events.component';
import { CurrencyComponent } from './currency/currency.component';
import { NewsComponent } from './news/news.component';
import { MainRequestService } from './main-request.service';
import { TwitterComponent } from './twitter/twitter.component';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    GoogleMapComponent,
    GoogleOauthComponent,
    EventsComponent,
    CurrencyComponent,
    NewsComponent,
    TwitterComponent,
    HomeComponent
  ],
  imports: [
    BrowserAnimationsModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    OAuthModule.forRoot(),
    MaterialModule,
    AgmCoreModule.forRoot({
      apiKey: environment.gmapApi
    })
  ],
  providers: [
    MainRequestService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
