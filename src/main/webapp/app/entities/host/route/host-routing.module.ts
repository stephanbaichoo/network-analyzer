import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HostComponent } from '../list/host.component';
import { HostDetailComponent } from '../detail/host-detail.component';
import { HostUpdateComponent } from '../update/host-update.component';
import { HostRoutingResolveService } from './host-routing-resolve.service';

const hostRoute: Routes = [
  {
    path: '',
    component: HostComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HostDetailComponent,
    resolve: {
      host: HostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HostUpdateComponent,
    resolve: {
      host: HostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HostUpdateComponent,
    resolve: {
      host: HostRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hostRoute)],
  exports: [RouterModule],
})
export class HostRoutingModule {}
