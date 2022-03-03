import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAmawalWord, getAmawalWordIdentifier } from '../amawal-word.model';

export type EntityResponseType = HttpResponse<IAmawalWord>;
export type EntityArrayResponseType = HttpResponse<IAmawalWord[]>;

@Injectable({ providedIn: 'root' })
export class AmawalWordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/amawal-words');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(amawalWord: IAmawalWord): Observable<EntityResponseType> {
    return this.http.post<IAmawalWord>(this.resourceUrl, amawalWord, { observe: 'response' });
  }

  update(amawalWord: IAmawalWord): Observable<EntityResponseType> {
    return this.http.put<IAmawalWord>(`${this.resourceUrl}/${getAmawalWordIdentifier(amawalWord) as number}`, amawalWord, {
      observe: 'response',
    });
  }

  partialUpdate(amawalWord: IAmawalWord): Observable<EntityResponseType> {
    return this.http.patch<IAmawalWord>(`${this.resourceUrl}/${getAmawalWordIdentifier(amawalWord) as number}`, amawalWord, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAmawalWord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAmawalWord[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAmawalWordToCollectionIfMissing(
    amawalWordCollection: IAmawalWord[],
    ...amawalWordsToCheck: (IAmawalWord | null | undefined)[]
  ): IAmawalWord[] {
    const amawalWords: IAmawalWord[] = amawalWordsToCheck.filter(isPresent);
    if (amawalWords.length > 0) {
      const amawalWordCollectionIdentifiers = amawalWordCollection.map(amawalWordItem => getAmawalWordIdentifier(amawalWordItem)!);
      const amawalWordsToAdd = amawalWords.filter(amawalWordItem => {
        const amawalWordIdentifier = getAmawalWordIdentifier(amawalWordItem);
        if (amawalWordIdentifier == null || amawalWordCollectionIdentifiers.includes(amawalWordIdentifier)) {
          return false;
        }
        amawalWordCollectionIdentifiers.push(amawalWordIdentifier);
        return true;
      });
      return [...amawalWordsToAdd, ...amawalWordCollection];
    }
    return amawalWordCollection;
  }
}
