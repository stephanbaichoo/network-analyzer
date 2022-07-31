import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IPortStatistics, MostPortDataTable } from '../model/dashboard.model';
import { Data } from '../model/IMostPortDataSummary.model';

export type EntityResponseType = HttpResponse<IPortStatistics>;
export type EntityArrayResponseType = HttpResponse<IPortStatistics[]>;

@Injectable({ providedIn: 'root' })
export class DashboardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dashboard/port');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getPorts(): Observable<IPortStatistics[]> {
    return this.http.get<IPortStatistics[]>(this.resourceUrl);
  }

  getMostTrafficOutgoingPortsYesterdaySegregated(): Observable<Data[]> {
    return this.http.get<Data[]>(this.resourceUrl.concat('/outgoing'));
  }

  getLastFourBytesSum(): Observable<number[]> {
    const endpointFor = this.applicationConfigService.getEndpointFor('api/general/traffic/bytes');
    return this.http.get<number[]>(endpointFor);
  }

  getLastFourPacketsSum(): Observable<number[]> {
    const endpointFor = this.applicationConfigService.getEndpointFor('api/general/traffic/packets');
    return this.http.get<number[]>(endpointFor);
  }

  getLastFourSNMPLogsCount(): Observable<number[]> {
    const endpointFor = this.applicationConfigService.getEndpointFor('api/general/traffic/snmp');
    return this.http.get<number[]>(endpointFor);
  }

  getUDPTCPDataPerHour(): Observable<number[]> {
    const endpointFor = this.applicationConfigService.getEndpointFor('api/general/protocol/hour');
    return this.http.get<number[]>(endpointFor);
  }

  getUDPTCPDataYesterday(): Observable<number[]> {
    const endpointFor = this.applicationConfigService.getEndpointFor('api/general/protocol/yesterday');
    return this.http.get<number[]>(endpointFor);
  }

  getUDPTCPDataFourDays(): Observable<number[]> {
    const endpointFor = this.applicationConfigService.getEndpointFor('api/general/protocol/four');
    return this.http.get<number[]>(endpointFor);
  }

  getMostOutgoingPortDataTableYesterday(): Observable<MostPortDataTable[]> {
    const endpointFor = this.applicationConfigService.getEndpointFor('api/dashboard/table/outgoing/yesterday');
    return this.http.get<MostPortDataTable[]>(endpointFor);
  }
}
