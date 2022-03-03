import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAmawalWord, AmawalWord } from '../amawal-word.model';
import { AmawalWordService } from '../service/amawal-word.service';

@Injectable({ providedIn: 'root' })
export class AmawalWordRoutingResolveService implements Resolve<IAmawalWord> {
  constructor(protected service: AmawalWordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAmawalWord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((amawalWord: HttpResponse<AmawalWord>) => {
          if (amawalWord.body) {
            return of(amawalWord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AmawalWord());
  }
}
