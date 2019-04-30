import { Injectable } from '@angular/core';
import { MainRequestService } from './main-request.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';


import { catchError } from 'rxjs/operators';
import { Observable } from 'rxjs';

import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GoogleService extends MainRequestService {

  constructor(
    protected http: HttpClient
  ) { super(http); }

  public placeAutocomplete(searchString: string): Observable<any> {
    const url = `${environment.apiUri}/google/map/place`;

    const queryParams = new HttpParams().set('input', searchString);

    return this.http.get(url, { headers: this.options.headers, params: queryParams })
      .pipe(catchError(error => this.handleError(error)));
  }

  public decodeGeocode(placeId: string): Observable<any> {
    const url = `${environment.apiUri}/google/map/geocode`;

    const queryParams = new HttpParams().set('input', placeId);

    return this.http.get(url, { headers: this.options.headers, params: queryParams })
      .pipe(catchError(error => this.handleError(error)));
  }

  public exchangeToken(code: string): Observable<any> {
    const url = `${environment.apiUri}/google/oauth/exchange`;

    const queryParams = new HttpParams().set('code', code);

    return this.http.get(url, { headers: this.options.headers, params: queryParams })
      .pipe(catchError(error => this.handleError(error)));
  }
}
