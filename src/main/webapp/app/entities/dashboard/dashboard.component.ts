import { Component, Input, OnInit } from '@angular/core';
import { DashboardService } from './service/dashboard.service';
import { IPortStatistics } from './model/dashboard.model';
import { Data, MostPortDataSummary } from './model/IMostPortDataSummary.model';
import * as Highcharts from 'highcharts';
import HC_exporting from 'highcharts/modules/exporting';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  chartOptions: any;
  @Input() data: any = [];

  Highcharts = Highcharts;

  iPortStatistics?: IPortStatistics[];

  stats?: Data[];

  iMostPortDataSummary?: MostPortDataSummary[];

  bigChart = [];

  chartOptionsCard = {};

  constructor(protected dashboardService: DashboardService) {}

  ngOnInit(): void {
    /* eslint-disable no-console */

    // Area Chart for Outgoing Traffic

    this.dashboardService.getMostTrafficOutgoingPortsYesterdaySegregated().subscribe(result => {
      this.stats = result;
      const map = this.stats?.map((value: Data) => {
        const a = {
          name: this.giveProperAcronym(value?.portName),
          data: value?.bytesPerHour,
        };
        return a;
      });

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
        series: map,
      };

      HC_exporting(Highcharts);
    });

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

  giveProperAcronym(value: string | undefined): string {
    if (value === undefined) {
      return 'Others';
    }

    if (value.includes('(HTTPS)')) {
      return 'HTTPS (443)';
    }

    if (value.includes('(HTTP)')) {
      return 'HTTP (80)';
    }

    if (value.includes('(DNS)')) {
      return 'DNS (53)';
    }

    if (value.includes('(SNMP)')) {
      return 'SNMP (161)';
    }

    if (value.includes('(SNMPTRAP)')) {
      return 'SNMPTRAP (162)';
    }

    if (value.includes('Telnet')) {
      return 'Telnet (23)';
    }
    if (value.includes('(SSH)')) {
      return 'SSH (22)';
    }
    return 'Others';
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
