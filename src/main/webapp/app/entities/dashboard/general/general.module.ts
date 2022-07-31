import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../../../shared/shared.module';
import { HighchartsChartModule } from 'highcharts-angular';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { AngularMaterialModule } from '../../../material.module';
import { DashboardModule } from '../dashboard.module';
import { GeneralComponent } from './general.component';

@NgModule({
  declarations: [GeneralComponent],
  imports: [
    CommonModule,
    SharedModule,
    DashboardModule,
    HighchartsChartModule,
    MatListModule,
    MatMenuModule,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    DashboardModule,
    FlexLayoutModule,
    MatSidenavModule,
    MatCardModule,
    MatDividerModule,
    AngularMaterialModule,
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class GeneralModule {}
