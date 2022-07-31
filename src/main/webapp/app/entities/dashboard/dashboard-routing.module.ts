import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard.component';
import { HostComponent } from './host/host.component';
import { GeneralComponent } from './general/general.component';
import { SnmpComponent } from './snmp/snmp.component';

// const routes: Routes = [dashboardRoute];

const routes: Routes = [
  {
    path: '',
    component: DashboardComponent,
  },
  {
    path: 'host',
    component: HostComponent,
  },
  {
    path: 'general',
    component: GeneralComponent,
  },
  {
    path: 'snmp',
    component: SnmpComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DashboardRoutingModule {}
