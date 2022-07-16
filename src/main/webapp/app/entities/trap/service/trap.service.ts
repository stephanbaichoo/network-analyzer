import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrap, getTrapIdentifier } from '../trap.model';

export type EntityResponseType = HttpResponse<ITrap>;
export type EntityArrayResponseType = HttpResponse<ITrap[]>;

@Injectable({ providedIn: 'root' })
export class TrapService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/traps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trap: ITrap): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trap);
    return this.http
      .post<ITrap>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(trap: ITrap): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trap);
    return this.http
      .put<ITrap>(`${this.resourceUrl}/${getTrapIdentifier(trap) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(trap: ITrap): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(trap);
    return this.http
      .patch<ITrap>(`${this.resourceUrl}/${getTrapIdentifier(trap) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITrap>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITrap[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrapToCollectionIfMissing(trapCollection: ITrap[], ...trapsToCheck: (ITrap | null | undefined)[]): ITrap[] {
    const traps: ITrap[] = trapsToCheck.filter(isPresent);
    if (traps.length > 0) {
      const trapCollectionIdentifiers = trapCollection.map(trapItem => getTrapIdentifier(trapItem)!);
      const trapsToAdd = traps.filter(trapItem => {
        const trapIdentifier = getTrapIdentifier(trapItem);
        if (trapIdentifier == null || trapCollectionIdentifiers.includes(trapIdentifier)) {
          return false;
        }
        trapCollectionIdentifiers.push(trapIdentifier);
        return true;
      });
      return [...trapsToAdd, ...trapCollection];
    }
    return trapCollection;
  }

  protected convertDateFromClient(trap: ITrap): ITrap {
    return Object.assign({}, trap, {
      date: trap.date?.isValid() ? trap.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((trap: ITrap) => {
        trap.date = trap.date ? dayjs(trap.date) : undefined;
      });
    }
    return res;
  }
}
