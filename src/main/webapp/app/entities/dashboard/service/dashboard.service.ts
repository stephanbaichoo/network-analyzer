import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INetflow, getNetflowIdentifier } from '../../netflow/netflow.model';

export type EntityResponseType = HttpResponse<INetflow>;
export type EntityArrayResponseType = HttpResponse<INetflow[]>;

@Injectable({ providedIn: 'root' })
export class DashboardService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/netflows');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(netflow: INetflow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(netflow);
    return this.http
      .post<INetflow>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(netflow: INetflow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(netflow);
    return this.http
      .put<INetflow>(`${this.resourceUrl}/${getNetflowIdentifier(netflow) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(netflow: INetflow): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(netflow);
    return this.http
      .patch<INetflow>(`${this.resourceUrl}/${getNetflowIdentifier(netflow) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INetflow>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INetflow[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNetflowToCollectionIfMissing(netflowCollection: INetflow[], ...netflowsToCheck: (INetflow | null | undefined)[]): INetflow[] {
    const netflows: INetflow[] = netflowsToCheck.filter(isPresent);
    if (netflows.length > 0) {
      const netflowCollectionIdentifiers = netflowCollection.map(netflowItem => getNetflowIdentifier(netflowItem)!);
      const netflowsToAdd = netflows.filter(netflowItem => {
        const netflowIdentifier = getNetflowIdentifier(netflowItem);
        if (netflowIdentifier == null || netflowCollectionIdentifiers.includes(netflowIdentifier)) {
          return false;
        }
        netflowCollectionIdentifiers.push(netflowIdentifier);
        return true;
      });
      return [...netflowsToAdd, ...netflowCollection];
    }
    return netflowCollection;
  }

  protected convertDateFromClient(netflow: INetflow): INetflow {
    return Object.assign({}, netflow, {
      dateFirstSeen: netflow.dateFirstSeen?.isValid() ? netflow.dateFirstSeen.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateFirstSeen = res.body.dateFirstSeen ? dayjs(res.body.dateFirstSeen) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((netflow: INetflow) => {
        netflow.dateFirstSeen = netflow.dateFirstSeen ? dayjs(netflow.dateFirstSeen) : undefined;
      });
    }
    return res;
  }
}
