import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWordType, getWordTypeIdentifier } from '../word-type.model';

export type EntityResponseType = HttpResponse<IWordType>;
export type EntityArrayResponseType = HttpResponse<IWordType[]>;

@Injectable({ providedIn: 'root' })
export class WordTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/word-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wordType: IWordType): Observable<EntityResponseType> {
    return this.http.post<IWordType>(this.resourceUrl, wordType, { observe: 'response' });
  }

  update(wordType: IWordType): Observable<EntityResponseType> {
    return this.http.put<IWordType>(`${this.resourceUrl}/${getWordTypeIdentifier(wordType) as number}`, wordType, { observe: 'response' });
  }

  partialUpdate(wordType: IWordType): Observable<EntityResponseType> {
    return this.http.patch<IWordType>(`${this.resourceUrl}/${getWordTypeIdentifier(wordType) as number}`, wordType, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWordType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWordType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWordTypeToCollectionIfMissing(wordTypeCollection: IWordType[], ...wordTypesToCheck: (IWordType | null | undefined)[]): IWordType[] {
    const wordTypes: IWordType[] = wordTypesToCheck.filter(isPresent);
    if (wordTypes.length > 0) {
      const wordTypeCollectionIdentifiers = wordTypeCollection.map(wordTypeItem => getWordTypeIdentifier(wordTypeItem)!);
      const wordTypesToAdd = wordTypes.filter(wordTypeItem => {
        const wordTypeIdentifier = getWordTypeIdentifier(wordTypeItem);
        if (wordTypeIdentifier == null || wordTypeCollectionIdentifiers.includes(wordTypeIdentifier)) {
          return false;
        }
        wordTypeCollectionIdentifiers.push(wordTypeIdentifier);
        return true;
      });
      return [...wordTypesToAdd, ...wordTypeCollection];
    }
    return wordTypeCollection;
  }
}
