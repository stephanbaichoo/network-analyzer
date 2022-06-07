import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INetflow, Netflow } from '../netflow.model';
import { NetflowService } from '../service/netflow.service';

@Injectable({ providedIn: 'root' })
export class NetflowRoutingResolveService implements Resolve<INetflow> {
  constructor(protected service: NetflowService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INetflow> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((netflow: HttpResponse<Netflow>) => {
          if (netflow.body) {
            return of(netflow.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Netflow());
  }
}
