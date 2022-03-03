import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILangue, getLangueIdentifier } from '../langue.model';

export type EntityResponseType = HttpResponse<ILangue>;
export type EntityArrayResponseType = HttpResponse<ILangue[]>;

@Injectable({ providedIn: 'root' })
export class LangueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/langues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(langue: ILangue): Observable<EntityResponseType> {
    return this.http.post<ILangue>(this.resourceUrl, langue, { observe: 'response' });
  }

  update(langue: ILangue): Observable<EntityResponseType> {
    return this.http.put<ILangue>(`${this.resourceUrl}/${getLangueIdentifier(langue) as number}`, langue, { observe: 'response' });
  }

  partialUpdate(langue: ILangue): Observable<EntityResponseType> {
    return this.http.patch<ILangue>(`${this.resourceUrl}/${getLangueIdentifier(langue) as number}`, langue, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILangue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILangue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLangueToCollectionIfMissing(langueCollection: ILangue[], ...languesToCheck: (ILangue | null | undefined)[]): ILangue[] {
    const langues: ILangue[] = languesToCheck.filter(isPresent);
    if (langues.length > 0) {
      const langueCollectionIdentifiers = langueCollection.map(langueItem => getLangueIdentifier(langueItem)!);
      const languesToAdd = langues.filter(langueItem => {
        const langueIdentifier = getLangueIdentifier(langueItem);
        if (langueIdentifier == null || langueCollectionIdentifiers.includes(langueIdentifier)) {
          return false;
        }
        langueCollectionIdentifiers.push(langueIdentifier);
        return true;
      });
      return [...languesToAdd, ...langueCollection];
    }
    return langueCollection;
  }
}
