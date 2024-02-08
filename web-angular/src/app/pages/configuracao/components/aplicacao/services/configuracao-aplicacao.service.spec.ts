/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { ConfiguracaoAplicacaoService } from './configuracao-aplicacao.service';

describe('Service: ConfiguracaoAplicacao', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConfiguracaoAplicacaoService]
    });
  });

  it('should ...', inject([ConfiguracaoAplicacaoService], (service: ConfiguracaoAplicacaoService) => {
    expect(service).toBeTruthy();
  }));
});
