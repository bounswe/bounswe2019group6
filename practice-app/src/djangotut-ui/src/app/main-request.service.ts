import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';

@Injectable()
export class MainRequestService {

  protected options: any = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
      // 'X-Socket-ID': ''
    }),
    params: {
      token: null
    }
  };

  constructor(
    protected http: HttpClient,
  ) { }

  protected handleError(error: any, router: any = null): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only

    switch (error.status) {
      case 401:
        break;
      case 421:
        break;
      default:
    }
    return Promise.reject(error.message || error);
  }
}
