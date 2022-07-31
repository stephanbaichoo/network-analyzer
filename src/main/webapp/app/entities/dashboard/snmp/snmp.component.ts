import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../service/dashboard.service';

@Component({
  selector: 'jhi-snmp',
  templateUrl: './snmp.component.html',
  styleUrls: ['./snmp.component.scss'],
})
export class SnmpComponent implements OnInit {
  constructor(protected dashboardService: DashboardService) {}
  ngOnInit(): void {
    this.dashboardService.getMostTrafficOutgoingPortsYesterdaySegregated();
  }
}
