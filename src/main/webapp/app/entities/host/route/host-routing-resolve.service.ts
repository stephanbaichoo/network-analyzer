import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHost, Host } from '../host.model';
import { HostService } from '../service/host.service';

@Injectable({ providedIn: 'root' })
export class HostRoutingResolveService implements Resolve<IHost> {
  constructor(protected service: HostService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHost> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((host: HttpResponse<Host>) => {
          if (host.body) {
            return of(host.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Host());
  }
}
