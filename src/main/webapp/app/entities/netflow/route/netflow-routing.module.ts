import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NetflowComponent } from '../list/netflow.component';
import { NetflowDetailComponent } from '../detail/netflow-detail.component';
import { NetflowUpdateComponent } from '../update/netflow-update.component';
import { NetflowRoutingResolveService } from './netflow-routing-resolve.service';

const netflowRoute: Routes = [
  {
    path: '',
    component: NetflowComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NetflowDetailComponent,
    resolve: {
      netflow: NetflowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NetflowUpdateComponent,
    resolve: {
      netflow: NetflowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NetflowUpdateComponent,
    resolve: {
      netflow: NetflowRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(netflowRoute)],
  exports: [RouterModule],
})
export class NetflowRoutingModule {}
