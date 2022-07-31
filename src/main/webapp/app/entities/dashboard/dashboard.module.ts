import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DashboardRoutingModule } from './dashboard-routing.module';
import { DashboardComponent } from './dashboard.component';
import { AngularMaterialModule } from '../../material.module';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { MatListModule } from '@angular/material/list';
import { SharedModule } from '../../shared/shared.module';
import { HighchartsChartModule } from 'highcharts-angular';
import { HostComponent } from './host/host.component';
import { SnmpComponent } from './snmp/snmp.component';
import { GeneralComponent } from './general/general.component';

@NgModule({
  declarations: [DashboardComponent, HostComponent, SnmpComponent],
  imports: [
    CommonModule,
    SharedModule,
    HighchartsChartModule,
    MatListModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    DashboardRoutingModule,
    FlexLayoutModule,
    MatSidenavModule,
    MatCardModule,
    MatDividerModule,
    AngularMaterialModule,
  ],
  exports: [
    HighchartsChartModule,
    MatListModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    DashboardRoutingModule,
    FlexLayoutModule,
    MatSidenavModule,
    MatCardModule,
    MatDividerModule,
    AngularMaterialModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class DashboardModule {}
