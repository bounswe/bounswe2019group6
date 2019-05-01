import { Component, OnInit } from '@angular/core';
import { EventsService } from '../events.service';

@Component({
  selector: 'app-events',
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent implements OnInit {

  public get isPageReady(): boolean {
    return this.eventsNames && this.events;
  }

  eventsNames: Array<string>;

  events: Array<any>;

  constructor(
    private eventsService: EventsService
  ) { }

  ngOnInit() {
    this.eventsService.getEvents().subscribe( response => {
      this.eventsNames = Object.keys(response);

      this.events = response;
    });
  }

}
