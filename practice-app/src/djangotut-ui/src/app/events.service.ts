import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { MainRequestService } from './main-request.service';
import { environment } from '../environments/environment';

import { catchError } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventsService extends MainRequestService {

  constructor(
    protected http: HttpClient
  ) { super(http); }

  public getEvents(): Observable<any> {
    const url = `${environment.apiUri}/events/`;

    return this.http.get(url, { headers: this.options.headers})
      .pipe(catchError(error => this.handleError(error)));
  }
}
