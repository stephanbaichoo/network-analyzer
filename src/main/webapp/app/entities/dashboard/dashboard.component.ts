import { Component, OnInit } from '@angular/core';
import { DashboardService } from './service/dashboard.service';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  constructor(protected dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.find(1);
  }
}
