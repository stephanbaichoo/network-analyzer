import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../service/dashboard.service';

@Component({
  selector: 'jhi-host',
  templateUrl: './host.component.html',
  styleUrls: ['./host.component.scss'],
})
export class HostComponent implements OnInit {
  constructor(protected dashboardService: DashboardService) {}
  ngOnInit(): void {
    this.dashboardService.getMostTrafficOutgoingPortsYesterdaySegregated();
  }
}
