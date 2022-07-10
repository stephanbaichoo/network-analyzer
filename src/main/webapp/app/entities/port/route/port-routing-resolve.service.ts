import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPort, Port } from '../port.model';
import { PortService } from '../service/port.service';

@Injectable({ providedIn: 'root' })
export class PortRoutingResolveService implements Resolve<IPort> {
  constructor(protected service: PortService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPort> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((port: HttpResponse<Port>) => {
          if (port.body) {
            return of(port.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Port());
  }
}
