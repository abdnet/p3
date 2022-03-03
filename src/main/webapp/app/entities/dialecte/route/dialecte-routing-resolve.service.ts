import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDialecte, Dialecte } from '../dialecte.model';
import { DialecteService } from '../service/dialecte.service';

@Injectable({ providedIn: 'root' })
export class DialecteRoutingResolveService implements Resolve<IDialecte> {
  constructor(protected service: DialecteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDialecte> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dialecte: HttpResponse<Dialecte>) => {
          if (dialecte.body) {
            return of(dialecte.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dialecte());
  }
}
