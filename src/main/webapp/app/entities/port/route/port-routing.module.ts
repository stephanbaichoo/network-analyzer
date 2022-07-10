import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PortComponent } from '../list/port.component';
import { PortDetailComponent } from '../detail/port-detail.component';
import { PortUpdateComponent } from '../update/port-update.component';
import { PortRoutingResolveService } from './port-routing-resolve.service';

const portRoute: Routes = [
  {
    path: '',
    component: PortComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PortDetailComponent,
    resolve: {
      port: PortRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PortUpdateComponent,
    resolve: {
      port: PortRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PortUpdateComponent,
    resolve: {
      port: PortRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(portRoute)],
  exports: [RouterModule],
})
export class PortRoutingModule {}
