import { Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { ActivatedRoute, Params } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { GoogleService } from '../google.service';

@Component({
  selector: 'app-google-oauth',
  templateUrl: './google-oauth.component.html',
  styleUrls: ['./google-oauth.component.scss']
})
export class GoogleOauthComponent implements OnInit {

  constructor(
    private oauthService: OAuthService,
    private googleService: GoogleService,
    private activatedRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.activatedRoute.queryParams.pipe(switchMap((params: Params) => {
      if (typeof params.code !== 'undefined') {
        return this.googleService.exchangeToken(params.code);
      }
      return of();
    })).subscribe((params: Params) => console.log(params));
  }

  public login() {
    this.oauthService.initImplicitFlow();
  }

  public logoff() {
    this.oauthService.logOut();
  }

  public get name() {
    const claims = this.oauthService.getIdentityClaims();
    if (!claims) {
      return null;
    }

    return claims;
  }
}
