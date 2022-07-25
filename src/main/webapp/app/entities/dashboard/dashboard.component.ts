import { Component, OnInit } from '@angular/core';
import { DashboardService } from './service/dashboard.service';
import {INetflow} from "../netflow/netflow.model";
import {IPortStatistics} from "./model/dashboard.model";

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  iPortStatistics?: IPortStatistics[];

  constructor(protected dashboardService: DashboardService) {}

  ngOnInit(): void {
    /* eslint-disable no-console */
     this.dashboardService.getPorts().subscribe((result) => {
       this.iPortStatistics = result
       console.log(result);
     });
    /* eslint-disable no-console */
  }
}
