import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHost, getHostIdentifier } from '../host.model';

export type EntityResponseType = HttpResponse<IHost>;
export type EntityArrayResponseType = HttpResponse<IHost[]>;

@Injectable({ providedIn: 'root' })
export class HostService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hosts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(host: IHost): Observable<EntityResponseType> {
    return this.http.post<IHost>(this.resourceUrl, host, { observe: 'response' });
  }

  update(host: IHost): Observable<EntityResponseType> {
    return this.http.put<IHost>(`${this.resourceUrl}/${getHostIdentifier(host) as number}`, host, { observe: 'response' });
  }

  partialUpdate(host: IHost): Observable<EntityResponseType> {
    return this.http.patch<IHost>(`${this.resourceUrl}/${getHostIdentifier(host) as number}`, host, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHost>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHost[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHostToCollectionIfMissing(hostCollection: IHost[], ...hostsToCheck: (IHost | null | undefined)[]): IHost[] {
    const hosts: IHost[] = hostsToCheck.filter(isPresent);
    if (hosts.length > 0) {
      const hostCollectionIdentifiers = hostCollection.map(hostItem => getHostIdentifier(hostItem)!);
      const hostsToAdd = hosts.filter(hostItem => {
        const hostIdentifier = getHostIdentifier(hostItem);
        if (hostIdentifier == null || hostCollectionIdentifiers.includes(hostIdentifier)) {
          return false;
        }
        hostCollectionIdentifiers.push(hostIdentifier);
        return true;
      });
      return [...hostsToAdd, ...hostCollection];
    }
    return hostCollection;
  }
}
