import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrapComponent } from '../list/trap.component';
import { TrapDetailComponent } from '../detail/trap-detail.component';
import { TrapUpdateComponent } from '../update/trap-update.component';
import { TrapRoutingResolveService } from './trap-routing-resolve.service';

const trapRoute: Routes = [
  {
    path: '',
    component: TrapComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrapDetailComponent,
    resolve: {
      trap: TrapRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrapUpdateComponent,
    resolve: {
      trap: TrapRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrapUpdateComponent,
    resolve: {
      trap: TrapRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trapRoute)],
  exports: [RouterModule],
})
export class TrapRoutingModule {}
