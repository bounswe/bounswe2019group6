import { Component, OnInit } from '@angular/core';
import { MainRequestService } from '../main-request.service';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-currency',
  templateUrl: './currency.component.html',
  styleUrls: ['./currency.component.scss']
})
export class CurrencyComponent implements OnInit {

  currencies: Array<string>;

  rates: any;

  loading = false;

  errorMessage: string;

  allCurrencies = [
    ['AED', 1], ['BAM', 1], ['BSD', 1], ['CLF', 1],
    ['DKK', 1], ['GEL', 1], ['HRK', 1], ['JEP', 1],
    ['KWD', 1], ['LYD', 1], ['MVR', 1], ['NZD', 1],
    ['RON', 1], ['SHP', 1], ['TMT', 1], ['UYU', 1],
    ['XDR', 1]
  ];

  constructor(
    private requestService: MainRequestService
  ) { }

  ngOnInit() {
  }

  getCurrency(f: NgForm) {

    const currency = f.value.currency;

    this.loading = true;

    this.currencies = null;

    this.errorMessage = null;

    this.requestService.getCurrencyRates([currency]).subscribe((response: any) => {
      if (typeof response.success === 'undefined') {
        this.currencies = [currency];
        this.rates = {
          [currency]: response[`EURto${currency}`]
        };

        this.errorMessage = null;
      } else if (!response.success && response.error.code === 202) {
        this.errorMessage = 'Invalid Currency Code';
      } else if (response.success) {
        this.currencies = Object.keys(response.rates);
        this.rates = response.rates;
        this.loading = false;
      } else {
        this.errorMessage = 'An Error Occured';
      }

      this.loading = false;
    });
  }

  getCurrencies() {
    const filtered = [];

    for (const currency of this.allCurrencies) {
      if (currency[1]) {
        filtered.push(currency[0]);
      }
    }

    this.errorMessage = null;

    this.currencies = null;

    this.loading = true;

    this.requestService.getCurrencyRates(filtered).subscribe(response => {
      if (response.success) {
        this.currencies = Object.keys(response.rates);
        this.rates = response.rates;
        this.loading = false;
      } else {
        this.errorMessage = 'An Error Occured';
      }

    });
  }
}
