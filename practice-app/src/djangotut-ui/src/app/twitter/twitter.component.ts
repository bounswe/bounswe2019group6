import { Component, OnInit } from '@angular/core';
import { MainRequestService } from '../main-request.service';

@Component({
  selector: 'app-twitter',
  templateUrl: './twitter.component.html',
  styleUrls: ['./twitter.component.scss']
})
export class TwitterComponent implements OnInit {

  links: Array<string>;

  get isPageReady(): boolean {
    return this.links && true;
  }

  constructor(
    private requestService: MainRequestService
  ) { }

  ngOnInit() {
    this.requestService.getTwitterLinks().subscribe((response: any) => this.links = response.all_links);
  }

}
