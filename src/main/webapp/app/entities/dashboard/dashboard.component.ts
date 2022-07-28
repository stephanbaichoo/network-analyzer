import {Component, OnInit} from '@angular/core';
import {DashboardService} from './service/dashboard.service';
import {IPortStatistics} from "./model/dashboard.model";
import {MostPortDataSummary} from "./model/IMostPortDataSummary.model";

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  iPortStatistics?: IPortStatistics[];

  iMostPortDataSummary?: MostPortDataSummary[];

  bigChart = [];

  constructor(protected dashboardService: DashboardService) {
  }

  ngOnInit(): void {
    /* eslint-disable no-console */

    this.getMostTrafficOutgoingPortsYesterdaySegregated();

    this.bigCharts();
    /* eslint-disable no-console */
  }

  getPorts(): void {
    this.dashboardService.getPorts().subscribe((result) => {
      this.iPortStatistics = result;
    });
  }

  getMostTrafficOutgoingPortsYesterdaySegregated(): void {
    type iiMostPortDataSummary = Array<MostPortDataSummary>;


/*    [{
        name: 'DNS',
        data: [502, 635, 809, 947, 1402, 3634, 5268]
      }, {
        name: 'HTTPS',
        data: [106, 107, 111, 133, 221, 767, 1766]
      }, {
        name: 'HTTP',
        data: [163, 203, 276, 408, 547, 729, 628]
      }, {
        name: 'SSH',
        data: [18, 31, 54, 156, 339, 818, 1201]
      }, {
        name: 'SNMP',
        data: [2, 2, 2, 6, 13, 30, 46]
      }]
    }*/

/*    var ports: iiMostPortDataSummary = [
      new MostPortDataSummary('0', 'DNS', '53', 67544),
      new MostPortDataSummary('0', 'HTTPS', '443', 67044),
      new MostPortDataSummary('0', 'HTTP', '80', 123544),
      new MostPortDataSummary('0', 'SNMP', '61', 6744),
      new MostPortDataSummary('0', 'TLS', '440', 674),
    ];*/

    this.dashboardService.getMostTrafficOutgoingPortsYesterdaySegregated().subscribe((result) => {
      this.iMostPortDataSummary = result;
      console.log(result)
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

  bigCharts() {
    return [{
      name: 'DNS',
      data: [502, 635, 809, 947, 1402, 3634, 5268]
    }, {
      name: 'HTTPS',
      data: [106, 107, 111, 133, 221, 767, 1766]
    }, {
      name: 'HTTP',
      data: [163, 203, 276, 408, 547, 729, 628]
    }, {
      name: 'SSH',
      data: [18, 31, 54, 156, 339, 818, 1201]
    }, {
      name: 'SNMP',
      data: [2, 2, 2, 6, 13, 30, 46]
    }];
  }
}
