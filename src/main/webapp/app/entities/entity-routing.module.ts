import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'netflow',
        data: { pageTitle: 'Netflows' },
        loadChildren: () => import('./netflow/netflow.module').then(m => m.NetflowModule),
      },
      {
        path: 'port',
        data: { pageTitle: 'Ports' },
        loadChildren: () => import('./port/port.module').then(m => m.PortModule),
      },
      {
        path: 'host',
        data: { pageTitle: 'Hosts' },
        loadChildren: () => import('./host/host.module').then(m => m.HostModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
