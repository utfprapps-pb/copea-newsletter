import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FrontendConfigService {

  constructor(private http: HttpClient) {
  }

  update(config: any): Observable<void> {
    return this.http.post<void>(`${environment.api}/frontend-config`, config);
  }

}
