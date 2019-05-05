import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

import { environment } from '../environments/environment';

import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

import swal from 'sweetalert2';

@Injectable()
export class MainRequestService {

  protected options: any = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
      // 'X-Socket-ID': ''
    })
  };

  constructor(
    protected http: HttpClient,
  ) { }

  public getNews(): Observable<any> {
    const url = `${environment.apiUri}/news/`;

    return this.http.get(url, { headers: this.options.headers })
      .pipe(catchError(error => this.handleError(error)));
  }

  public getEvents(): Observable<any> {
    const url = `${environment.apiUri}/events/`;

    return this.http.get(url, { headers: this.options.headers })
      .pipe(catchError(error => this.handleError(error)));
  }

  public getCurrencyRates(rates: Array<string>): Observable<any> {

    const url = `${environment.apiUri}/currencyrate/${rates.join()}`;

    return this.http.get(url, { headers: this.options.headers })
      .pipe(catchError(error => this.handleError(error)));
  }

  public getTwitterLinks(): Observable<any> {
    const url = `${environment.apiUri}/twitter/news`;

    return this.http.get(url, { headers: this.options.headers })
      .pipe(catchError(error => this.handleError(error)));
  }

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

  protected handleError(error: any, router: any = null): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only

    switch (error.status) {
      default:
        swal.fire({
          title: 'Error!',
          type: 'error',
          html: 'We have encountered with an error,<br>message: ' + error.message,
        });
    }
    return Promise.reject(error.message || error);
  }
}
