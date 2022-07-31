import { Component, OnInit } from '@angular/core';
import { DashboardService } from '../service/dashboard.service';
import * as Highcharts from 'highcharts';
import HC_exporting from 'highcharts/modules/exporting';

@Component({
  selector: 'jhi-general',
  templateUrl: './general.component.html',
  styleUrls: ['./general.component.scss'],
})
export class GeneralComponent implements OnInit {
  lastFourBytesSum: number[] = [];

  label: string | undefined;
  total: string | undefined;
  percentage: string | undefined;
  data: number[] = [];

  Highcharts = Highcharts;
  chartOptionsCard = {};

  constructor(protected dashboardService: DashboardService) {}

  ngOnInit(): void {
    console.log(this.getLastFourBytesSum());

    this.label = '';
    this.percentage = '';
    this.total = '';
    this.data = this.lastFourBytesSum;

    this.chartOptionsCard = {
      chart: {
        type: 'area',
        backgroundColor: null,
        borderWidth: 0,
        margin: [2, 2, 2, 2],
        height: 60,
      },
      title: {
        text: null,
      },
      subtitle: {
        text: null,
      },
      tooltip: {
        split: true,
        outside: true,
      },
      legend: {
        enabled: false,
      },
      credits: {
        enabled: false,
      },
      exporting: {
        enabled: false,
      },
      xAxis: {
        labels: {
          enabled: false,
        },
        title: {
          text: null,
        },
        startOnTick: false,
        endOnTick: false,
        tickOptions: [],
      },
      yAxis: {
        labels: {
          enabled: false,
        },
        title: {
          text: null,
        },
        startOnTick: false,
        endOnTick: false,
        tickOptions: [],
      },
      series: [
        {
          data: this.lastFourBytesSum,
        },
      ],
    };

    HC_exporting(Highcharts);
  }

  getLastFourBytesSum(): void {
    this.dashboardService.getLastFourBytesSum().subscribe(result => {
      this.lastFourBytesSum = result;
      console.log(result);
    });
  }

  /*  createLabel(): string {
   if (this.lastFourBytesSum?.indexOf(2) > this.lastFourBytesSum?.indexOf(3)) {
     return 'Decreasing';
   }
    if (this.lastFourBytesSum?.indexOf(2) < this.lastFourBytesSum?.indexOf(3)) {
      return 'Increasing';
    }
   return '';
  }*/
}
