import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITamazightToLang, getTamazightToLangIdentifier } from '../tamazight-to-lang.model';

export type EntityResponseType = HttpResponse<ITamazightToLang>;
export type EntityArrayResponseType = HttpResponse<ITamazightToLang[]>;

@Injectable({ providedIn: 'root' })
export class TamazightToLangService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tamazight-to-langs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(tamazightToLang: ITamazightToLang): Observable<EntityResponseType> {
    return this.http.post<ITamazightToLang>(this.resourceUrl, tamazightToLang, { observe: 'response' });
  }

  update(tamazightToLang: ITamazightToLang): Observable<EntityResponseType> {
    return this.http.put<ITamazightToLang>(
      `${this.resourceUrl}/${getTamazightToLangIdentifier(tamazightToLang) as number}`,
      tamazightToLang,
      { observe: 'response' }
    );
  }

  partialUpdate(tamazightToLang: ITamazightToLang): Observable<EntityResponseType> {
    return this.http.patch<ITamazightToLang>(
      `${this.resourceUrl}/${getTamazightToLangIdentifier(tamazightToLang) as number}`,
      tamazightToLang,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITamazightToLang>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITamazightToLang[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTamazightToLangToCollectionIfMissing(
    tamazightToLangCollection: ITamazightToLang[],
    ...tamazightToLangsToCheck: (ITamazightToLang | null | undefined)[]
  ): ITamazightToLang[] {
    const tamazightToLangs: ITamazightToLang[] = tamazightToLangsToCheck.filter(isPresent);
    if (tamazightToLangs.length > 0) {
      const tamazightToLangCollectionIdentifiers = tamazightToLangCollection.map(
        tamazightToLangItem => getTamazightToLangIdentifier(tamazightToLangItem)!
      );
      const tamazightToLangsToAdd = tamazightToLangs.filter(tamazightToLangItem => {
        const tamazightToLangIdentifier = getTamazightToLangIdentifier(tamazightToLangItem);
        if (tamazightToLangIdentifier == null || tamazightToLangCollectionIdentifiers.includes(tamazightToLangIdentifier)) {
          return false;
        }
        tamazightToLangCollectionIdentifiers.push(tamazightToLangIdentifier);
        return true;
      });
      return [...tamazightToLangsToAdd, ...tamazightToLangCollection];
    }
    return tamazightToLangCollection;
  }
}
