import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {ApplicationConfigService} from 'app/core/config/application-config.service';
import {IPortStatistics} from "../model/dashboard.model";
import {MostPortDataSummary} from "../model/IMostPortDataSummary.model";

export type EntityResponseType = HttpResponse<IPortStatistics>;
export type EntityArrayResponseType = HttpResponse<IPortStatistics[]>;

@Injectable({providedIn: 'root'})
export class DashboardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dashboard/port');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
  }

  getPorts(): Observable<IPortStatistics[]> {
    return this.http.get<IPortStatistics[]>(this.resourceUrl)
  }

  getMostTrafficOutgoingPortsYesterdaySegregated(): Observable<MostPortDataSummary[]> {
    return this.http.get<IPortStatistics[]>(this.resourceUrl.concat('/outgoing'))
  }

}
