import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GoogleMapComponent } from './google-map/google-map.component';
import { GoogleOauthComponent } from './google-oauth/google-oauth.component';

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'google/map'},
  { path: 'google/map', component: GoogleMapComponent },
  { path: 'google/oauth', component: GoogleOauthComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
