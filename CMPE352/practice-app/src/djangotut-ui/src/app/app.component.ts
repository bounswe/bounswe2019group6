import { Component } from '@angular/core';
import { AuthConfig } from 'angular-oauth2-oidc';
import { OAuthService, JwksValidationHandler } from 'angular-oauth2-oidc';
import { environment } from '../environments/environment';

export const authConfig: AuthConfig = {
  // Url of the Identity Provider
  issuer: 'https://accounts.google.com',
  // URL of the SPA to redirect the user to after login
  redirectUri: window.location.origin + '/google/oauth',
  // The SPA's id. The SPA is registerd with this id at the auth-server
  clientId: environment.clientId,
  responseType: 'code',
  // set the scope for the permissions the client should request
  // The first three are defined by OIDC. The 4th is a usecase-specific one
  scope: 'email',
  strictDiscoveryDocumentValidation: false
};

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'tradein-ui';

  mode = 'side';

  constructor(private oauthService: OAuthService) {
    this.configureWithNewConfigApi();
  }

  private configureWithNewConfigApi() {
    this.oauthService.configure(authConfig);
    this.oauthService.tokenValidationHandler = new JwksValidationHandler();
    this.oauthService.loadDiscoveryDocumentAndTryLogin();
  }
}
