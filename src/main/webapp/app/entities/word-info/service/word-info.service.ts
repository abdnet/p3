import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWordInfo, getWordInfoIdentifier } from '../word-info.model';

export type EntityResponseType = HttpResponse<IWordInfo>;
export type EntityArrayResponseType = HttpResponse<IWordInfo[]>;

@Injectable({ providedIn: 'root' })
export class WordInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/word-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(wordInfo: IWordInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wordInfo);
    return this.http
      .post<IWordInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(wordInfo: IWordInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wordInfo);
    return this.http
      .put<IWordInfo>(`${this.resourceUrl}/${getWordInfoIdentifier(wordInfo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(wordInfo: IWordInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(wordInfo);
    return this.http
      .patch<IWordInfo>(`${this.resourceUrl}/${getWordInfoIdentifier(wordInfo) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IWordInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IWordInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addWordInfoToCollectionIfMissing(wordInfoCollection: IWordInfo[], ...wordInfosToCheck: (IWordInfo | null | undefined)[]): IWordInfo[] {
    const wordInfos: IWordInfo[] = wordInfosToCheck.filter(isPresent);
    if (wordInfos.length > 0) {
      const wordInfoCollectionIdentifiers = wordInfoCollection.map(wordInfoItem => getWordInfoIdentifier(wordInfoItem)!);
      const wordInfosToAdd = wordInfos.filter(wordInfoItem => {
        const wordInfoIdentifier = getWordInfoIdentifier(wordInfoItem);
        if (wordInfoIdentifier == null || wordInfoCollectionIdentifiers.includes(wordInfoIdentifier)) {
          return false;
        }
        wordInfoCollectionIdentifiers.push(wordInfoIdentifier);
        return true;
      });
      return [...wordInfosToAdd, ...wordInfoCollection];
    }
    return wordInfoCollection;
  }

  protected convertDateFromClient(wordInfo: IWordInfo): IWordInfo {
    return Object.assign({}, wordInfo, {
      contributionDate: wordInfo.contributionDate?.isValid() ? wordInfo.contributionDate.toJSON() : undefined,
      validationDate: wordInfo.validationDate?.isValid() ? wordInfo.validationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.contributionDate = res.body.contributionDate ? dayjs(res.body.contributionDate) : undefined;
      res.body.validationDate = res.body.validationDate ? dayjs(res.body.validationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((wordInfo: IWordInfo) => {
        wordInfo.contributionDate = wordInfo.contributionDate ? dayjs(wordInfo.contributionDate) : undefined;
        wordInfo.validationDate = wordInfo.validationDate ? dayjs(wordInfo.validationDate) : undefined;
      });
    }
    return res;
  }
}
