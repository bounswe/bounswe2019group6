import { Component, OnInit } from '@angular/core';
import { MainRequestService } from '../main-request.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.scss']
})
export class NewsComponent implements OnInit {

  newsKeys: Array<string>;

  news: Array<any>;

  public get isPageReady(): boolean {
    return this.news && this.newsKeys && true;
  }

  constructor(
    private requestService: MainRequestService
  ) { }

  ngOnInit() {
    this.requestService.getNews().subscribe(response => {
      this.newsKeys = Object.keys(response);
      this.news = response;
    });
  }

}
