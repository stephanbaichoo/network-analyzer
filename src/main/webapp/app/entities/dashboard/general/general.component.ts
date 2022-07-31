import { Component, Input, OnInit } from '@angular/core';
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
  lastFourPacketsSum: number[] = [];
  lastFourSNMPSum: number[] = [];

  lastHourUDPTCP: number[] = [];
  lastYesterdayUDPTCP: number[] = [];
  lastFourUDPTCP: number[] = [];

  label: string | undefined;
  total: string | undefined;
  percentage: string | undefined;
  @Input() data: number[] = [];

  label0: string | undefined;
  total0: string | undefined;
  percentage0: string | undefined;
  @Input() data0: number[] = [];

  label1: string | undefined;
  total1: string | undefined;
  percentage1: string | undefined;
  @Input() data1: number[] = [];

  Highcharts: any = Highcharts;
  chartOptionsCard: any;
  chartOptionsCard0: any;
  chartOptionsCard1: any;

  chartOptions: any;
  chartOptions0: any;
  chartOptions1: any;

  constructor(protected dashboardService: DashboardService) {}

  ngOnInit(): void {
    this.dashboardService.getLastFourBytesSum().subscribe(result => {
      if (result !== undefined) {
        this.lastFourBytesSum = result;

        this.chartOptionsCard = {
          chart: {
            type: 'area',
            backgroundColor: null,
            borderWidth: 0,
            margin: [2, 2, 2, 2],
            height: 60,
          },
          title: {
            text: '',
          },
          subtitle: {
            text: 'Bytes Per Day',
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
      }

      const difference = this.lastFourBytesSum[3] - this.lastFourBytesSum[2];

      const division = this.lastFourBytesSum[3] === 0 ? 1 : difference / this.lastFourBytesSum[3];

      this.label = this.createLabel();
      this.percentage = String(Math.floor(division * 100));
      this.total = String(Math.floor(difference / 1000000)).concat(' Mbytes');
      this.data = this.lastFourBytesSum;

      HC_exporting(Highcharts);
    });

    this.dashboardService.getLastFourPacketsSum().subscribe(result => {
      if (result !== undefined) {
        this.lastFourPacketsSum = result;

        console.log(result);

        this.chartOptionsCard0 = {
          chart: {
            type: 'area',
            backgroundColor: null,
            borderWidth: 0,
            margin: [2, 2, 2, 2],
            height: 60,
          },
          title: {
            text: '',
          },
          subtitle: {
            text: 'Packets Per Day',
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
              data: this.lastFourPacketsSum,
            },
          ],
        };
      }

      console.log('valuuee', this.lastFourPacketsSum[2]);

      const difference = this.lastFourPacketsSum[3] - this.lastFourPacketsSum[2];

      const division = this.lastFourPacketsSum[3] === 0 ? 1 : difference / this.lastFourPacketsSum[3];

      this.label0 = this.createLabel0();
      this.percentage0 = String(Math.floor(division * 100));
      this.total0 = String(Math.floor(difference)).concat(' Packets No');
      this.data0 = this.lastFourPacketsSum;

      HC_exporting(Highcharts);
    });

    this.dashboardService.getLastFourSNMPLogsCount().subscribe(result => {
      if (result !== undefined) {
        this.lastFourSNMPSum = result;

        console.log(result);

        this.chartOptionsCard1 = {
          chart: {
            type: 'area',
            backgroundColor: null,
            borderWidth: 0,
            margin: [2, 2, 2, 2],
            height: 60,
          },
          title: {
            text: '',
          },
          subtitle: {
            text: 'SNMP Logs Per Day',
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
              data: this.lastFourSNMPSum,
            },
          ],
        };
      }

      console.log('valuuee', this.lastFourSNMPSum[2]);

      const difference = this.lastFourSNMPSum[3] - this.lastFourSNMPSum[2];

      const division = this.lastFourSNMPSum[3] === 0 ? 1 : difference / this.lastFourSNMPSum[3];

      this.label1 = this.createLabel1();
      this.percentage1 = String(Math.floor(division * 100));
      this.total1 = String(Math.floor(difference)).concat(' SNMP No');
      this.data1 = this.lastFourSNMPSum;

      HC_exporting(Highcharts);
    });

    this.dashboardService.getUDPTCPDataPerHour().subscribe(result => {
      this.lastHourUDPTCP = result;

      const sum = this.lastHourUDPTCP[0] + this.lastHourUDPTCP[1];

      const udpPercentage = (this.lastHourUDPTCP[0] / sum) * 100;
      const tcpPercentage = (this.lastHourUDPTCP[1] / sum) * 100;

      const udpBytes = String(Math.floor(this.lastHourUDPTCP[0] / 1000000)).concat(' Mbytes');
      const tcpBytes = String(Math.floor(this.lastHourUDPTCP[1] / 1000000)).concat(' Mbytes');

      const name = 'UDP('.concat(udpBytes.concat(') TCP(').concat(tcpBytes)).concat(')');

      const newVar = [
        {
          name: 'UDP',
          y: udpPercentage,
          sliced: true,
          selected: true,
        },
        {
          name: 'TCP',
          y: tcpPercentage,
        },
      ];

      this.chartOptions = {
        chart: {
          plotBackgroundColor: null,
          plotBorderWidth: null,
          plotShadow: false,
          type: 'pie',
        },
        title: {
          text: 'TCP / UDP Traffic during the Last Hour',
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              format: '<b>{point.name}</b>: {point.percentage:.1f} %',
            },
          },
        },
        exporting: {
          enabled: true,
        },
        credits: {
          enabled: false,
        },
        series: [
          {
            name: ''.concat(name),
            colorByPoint: true,
            data: newVar,
          },
        ],
      };

      HC_exporting(Highcharts);
    });

    this.dashboardService.getUDPTCPDataYesterday().subscribe(result => {
      this.lastYesterdayUDPTCP = result;

      console.log('new', this.lastYesterdayUDPTCP);

      const sum = this.lastYesterdayUDPTCP[0] + this.lastYesterdayUDPTCP[1];

      const udpPercentage = (this.lastYesterdayUDPTCP[0] / sum) * 100;
      const tcpPercentage = (this.lastYesterdayUDPTCP[1] / sum) * 100;

      const udpBytes = String(Math.floor(this.lastYesterdayUDPTCP[0] / 1000000)).concat(' Mbytes');
      const tcpBytes = String(Math.floor(this.lastYesterdayUDPTCP[1] / 1000000)).concat(' Mbytes');

      const name = 'UDP('.concat(udpBytes.concat(') TCP(').concat(tcpBytes)).concat(')');

      const newVar = [
        {
          name: 'UDP',
          y: udpPercentage,
          sliced: true,
          selected: true,
        },
        {
          name: 'TCP',
          y: tcpPercentage,
        },
      ];

      this.chartOptions0 = {
        chart: {
          plotBackgroundColor: null,
          plotBorderWidth: null,
          plotShadow: false,
          type: 'pie',
        },
        title: {
          text: 'TCP / UDP Traffic since Yesterday',
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              format: '<b>{point.name}</b>: {point.percentage:.1f} %',
            },
          },
        },
        exporting: {
          enabled: true,
        },
        credits: {
          enabled: false,
        },
        series: [
          {
            name: ''.concat(name),
            colorByPoint: true,
            data: newVar,
          },
        ],
      };

      HC_exporting(Highcharts);
    });

    this.dashboardService.getUDPTCPDataFourDays().subscribe(result => {
      this.lastFourUDPTCP = result;

      console.log('lastFourUDPTCP', this.lastFourUDPTCP);

      const sum = this.lastFourUDPTCP[0] + this.lastFourUDPTCP[1];

      const udpPercentage = (this.lastFourUDPTCP[0] / sum) * 100;
      const tcpPercentage = (this.lastFourUDPTCP[1] / sum) * 100;

      const udpBytes = String(Math.floor(this.lastFourUDPTCP[0] / 1000000)).concat(' Mbytes');
      const tcpBytes = String(Math.floor(this.lastFourUDPTCP[1] / 1000000)).concat(' Mbytes');

      const name = 'UDP('.concat(udpBytes.concat(') TCP(').concat(tcpBytes)).concat(')');

      const newVar = [
        {
          name: 'UDP',
          y: udpPercentage,
          sliced: true,
          selected: true,
        },
        {
          name: 'TCP',
          y: tcpPercentage,
        },
      ];

      this.chartOptions1 = {
        chart: {
          plotBackgroundColor: null,
          plotBorderWidth: null,
          plotShadow: false,
          type: 'pie',
        },
        title: {
          text: 'TCP / UDP Traffic since Four Days',
        },
        tooltip: {
          pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
        },
        plotOptions: {
          pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
              enabled: true,
              format: '<b>{point.name}</b>: {point.percentage:.1f} %',
            },
          },
        },
        exporting: {
          enabled: true,
        },
        credits: {
          enabled: false,
        },
        series: [
          {
            name: ''.concat(name),
            colorByPoint: true,
            data: newVar,
          },
        ],
      };

      HC_exporting(Highcharts);
    });
  }

  createLabel(): string {
    if (this.lastFourBytesSum[2] > this.lastFourBytesSum[3]) {
      return 'Decreasing';
    }
    if (this.lastFourBytesSum[2] < this.lastFourBytesSum[3]) {
      return 'Increasing';
    }
    return '';
  }

  createLabel0(): string {
    if (this.lastFourPacketsSum[2] > this.lastFourPacketsSum[3]) {
      return 'Decreasing';
    }
    if (this.lastFourPacketsSum[2] < this.lastFourPacketsSum[3]) {
      return 'Increasing';
    }
    return '';
  }

  createLabel1(): string {
    if (this.lastFourSNMPSum[2] > this.lastFourSNMPSum[3]) {
      return 'Decreasing';
    }
    if (this.lastFourSNMPSum[2] < this.lastFourSNMPSum[3]) {
      return 'Increasing';
    }
    return '';
  }
}
