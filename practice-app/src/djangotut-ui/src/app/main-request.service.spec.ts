import { TestBed, inject } from '@angular/core/testing';

import { MainRequestService } from './main-request.service';

describe('MainRequestService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MainRequestService]
    });
  });

  it('should be created', inject([MainRequestService], (service: MainRequestService) => {
    expect(service).toBeTruthy();
  }));
});
