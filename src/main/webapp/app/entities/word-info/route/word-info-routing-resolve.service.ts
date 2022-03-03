import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWordInfo, WordInfo } from '../word-info.model';
import { WordInfoService } from '../service/word-info.service';

@Injectable({ providedIn: 'root' })
export class WordInfoRoutingResolveService implements Resolve<IWordInfo> {
  constructor(protected service: WordInfoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWordInfo> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wordInfo: HttpResponse<WordInfo>) => {
          if (wordInfo.body) {
            return of(wordInfo.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WordInfo());
  }
}
