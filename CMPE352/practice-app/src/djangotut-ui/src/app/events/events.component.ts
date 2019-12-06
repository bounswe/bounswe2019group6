import { Component, OnInit } from '@angular/core';
import { MainRequestService } from '../main-request.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent implements OnInit {

  public get isPageReady(): boolean {
    return this.events && true;
  }

  events: Array<any>;

  constructor(
    private requestService: MainRequestService
  ) { }

  ngOnInit() {
    this.requestService.getEvents().subscribe( response => this.events = response);
  }

}
