import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrap, Trap } from '../trap.model';
import { TrapService } from '../service/trap.service';

@Injectable({ providedIn: 'root' })
export class TrapRoutingResolveService implements Resolve<ITrap> {
  constructor(protected service: TrapService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrap> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trap: HttpResponse<Trap>) => {
          if (trap.body) {
            return of(trap.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Trap());
  }
}
