import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDialecte, getDialecteIdentifier } from '../dialecte.model';

export type EntityResponseType = HttpResponse<IDialecte>;
export type EntityArrayResponseType = HttpResponse<IDialecte[]>;

@Injectable({ providedIn: 'root' })
export class DialecteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dialectes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dialecte: IDialecte): Observable<EntityResponseType> {
    return this.http.post<IDialecte>(this.resourceUrl, dialecte, { observe: 'response' });
  }

  update(dialecte: IDialecte): Observable<EntityResponseType> {
    return this.http.put<IDialecte>(`${this.resourceUrl}/${getDialecteIdentifier(dialecte) as number}`, dialecte, { observe: 'response' });
  }

  partialUpdate(dialecte: IDialecte): Observable<EntityResponseType> {
    return this.http.patch<IDialecte>(`${this.resourceUrl}/${getDialecteIdentifier(dialecte) as number}`, dialecte, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDialecte>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDialecte[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDialecteToCollectionIfMissing(dialecteCollection: IDialecte[], ...dialectesToCheck: (IDialecte | null | undefined)[]): IDialecte[] {
    const dialectes: IDialecte[] = dialectesToCheck.filter(isPresent);
    if (dialectes.length > 0) {
      const dialecteCollectionIdentifiers = dialecteCollection.map(dialecteItem => getDialecteIdentifier(dialecteItem)!);
      const dialectesToAdd = dialectes.filter(dialecteItem => {
        const dialecteIdentifier = getDialecteIdentifier(dialecteItem);
        if (dialecteIdentifier == null || dialecteCollectionIdentifiers.includes(dialecteIdentifier)) {
          return false;
        }
        dialecteCollectionIdentifiers.push(dialecteIdentifier);
        return true;
      });
      return [...dialectesToAdd, ...dialecteCollection];
    }
    return dialecteCollection;
  }
}
