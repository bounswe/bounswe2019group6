import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { MainRequestService } from '../main-request.service';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-google-map',
  templateUrl: './google-map.component.html',
  styleUrls: ['./google-map.component.scss']
})
export class GoogleMapComponent implements OnInit {

  lat = 39.0905013;
  lng = 35.4270658;
  zoom = 5;
  address = '';
  marker = false;

  @ViewChild('input')
  input: ElementRef;

  private searchTerms = new Subject<string>();

  options = [];

  constructor(
    private requestService: MainRequestService
  ) { }

  ngOnInit() {
    this.searchTerms.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      switchMap((term: string) => this.requestService.placeAutocomplete(term))
    ).subscribe((response: any) => this.options = response.predictions);
  }

  search(term: string): void {
    this.searchTerms.next(this.address);
  }

  geoDecode(selectedPlace: any) {

    const place = this.options.find(_place => _place.place_id === selectedPlace.place_id);

    this.address = place.description;

    this.marker = false;

    this.requestService.decodeGeocode(place.place_id)
      .subscribe(response => {
        this.lat = response.results[0].geometry.location.lat;
        this.lng = response.results[0].geometry.location.lng;
        this.zoom = 15;
        this.marker = true;
      });
  }

  dblClick($event: any) {
    if (this.zoom > 13) {
      console.log($event);
      this.lat = $event.coords.lat;
      this.lng = $event.coords.lng;
      this.marker = true;
    }
  }
}
