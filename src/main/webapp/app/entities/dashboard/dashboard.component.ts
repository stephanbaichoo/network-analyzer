import { Component, Input, OnInit } from '@angular/core';
import { DashboardService } from './service/dashboard.service';
import { IPortStatistics } from './model/dashboard.model';
import { MostPortDataSummary } from './model/IMostPortDataSummary.model';
import * as Highcharts from 'highcharts';
import HC_exporting from 'highcharts/modules/exporting';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  chartOptions: any = {};
  @Input() data: any = [];

  Highcharts = Highcharts;

  iPortStatistics?: IPortStatistics[];

  iMostPortDataSummary?: MostPortDataSummary[];

  bigChart = [];

  chartOptionsCard = {};

  constructor(protected dashboardService: DashboardService) {}

  ngOnInit(): void {
    /* eslint-disable no-console */

    // Area Chart for Outgoing Traffic
    this.chartOptions = {
      chart: {
        type: 'area',
      },
      title: {
        text: 'Outgoing Traffic From The most Popular Ports',
      },
      subtitle: {
        text: 'Port Data Usage Taken As from Yesterday. Bytes vs Last Hours',
      },
      tooltip: {
        split: true,
        valueSuffix: ' Mbytes',
      },
      credits: {
        enabled: false,
      },
      exporting: {
        enabled: true,
      },
      series: this.bigCharts(),
    };

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
          data: [23, 45, 67, 87],
        },
      ],
    };

    this.getMostTrafficOutgoingPortsYesterdaySegregated();

    this.bigChart = this.bigCharts();

    console.log(this.getRandomData());

    console.log(this.bigChart);

    HC_exporting(Highcharts);

    /* eslint-disable no-console */
  }

  getPorts(): void {
    this.dashboardService.getPorts().subscribe(result => {
      this.iPortStatistics = result;
    });
  }

  getMostTrafficOutgoingPortsYesterdaySegregated(): void {
    this.dashboardService.getMostTrafficOutgoingPortsYesterdaySegregated().subscribe(result => {
      this.iMostPortDataSummary = result;
      console.log(...result);
    });
  }

  getYesterdayDate(): Date {
    const yesterdayDate = new Date(new Date().getTime());
    yesterdayDate.setDate(yesterdayDate.getDate() - 1);
    return yesterdayDate;
  }

  getTodayDate(): Date {
    return new Date(new Date().getTime());
  }

  getRandomData(): number[] {
    return [...Array(this.getTodayDate().getHours() + 2)].map(() => Math.floor(Math.random() * 3000));
  }

  bigCharts(): any {
    return [
      {
        name: 'DNS',
        data: this.getRandomData(),
      },
      {
        name: 'HTTPS',
        data: this.getRandomData(),
      },
      {
        name: 'HTTP',
        data: this.getRandomData(),
      },
      {
        name: 'SSH',
        data: this.getRandomData(),
      },
      {
        name: 'SNMP',
        data: this.getRandomData(),
      },
    ];
  }
}
