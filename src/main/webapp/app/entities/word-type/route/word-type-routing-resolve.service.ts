import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWordType, WordType } from '../word-type.model';
import { WordTypeService } from '../service/word-type.service';

@Injectable({ providedIn: 'root' })
export class WordTypeRoutingResolveService implements Resolve<IWordType> {
  constructor(protected service: WordTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IWordType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((wordType: HttpResponse<WordType>) => {
          if (wordType.body) {
            return of(wordType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new WordType());
  }
}
