import { TestBed } from '@angular/core/testing';

import { HttpInterceptadorService } from './http-interceptador.service';

describe('HttpInterceptadorService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HttpInterceptadorService = TestBed.get(HttpInterceptadorService);
    expect(service).toBeTruthy();
  });
});
