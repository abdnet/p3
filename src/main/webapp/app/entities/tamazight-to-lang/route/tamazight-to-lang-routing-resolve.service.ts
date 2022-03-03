import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITamazightToLang, TamazightToLang } from '../tamazight-to-lang.model';
import { TamazightToLangService } from '../service/tamazight-to-lang.service';

@Injectable({ providedIn: 'root' })
export class TamazightToLangRoutingResolveService implements Resolve<ITamazightToLang> {
  constructor(protected service: TamazightToLangService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITamazightToLang> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tamazightToLang: HttpResponse<TamazightToLang>) => {
          if (tamazightToLang.body) {
            return of(tamazightToLang.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TamazightToLang());
  }
}
