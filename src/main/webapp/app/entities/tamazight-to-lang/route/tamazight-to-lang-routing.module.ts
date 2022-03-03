import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TamazightToLangComponent } from '../list/tamazight-to-lang.component';
import { TamazightToLangDetailComponent } from '../detail/tamazight-to-lang-detail.component';
import { TamazightToLangUpdateComponent } from '../update/tamazight-to-lang-update.component';
import { TamazightToLangRoutingResolveService } from './tamazight-to-lang-routing-resolve.service';

const tamazightToLangRoute: Routes = [
  {
    path: '',
    component: TamazightToLangComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TamazightToLangDetailComponent,
    resolve: {
      tamazightToLang: TamazightToLangRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TamazightToLangUpdateComponent,
    resolve: {
      tamazightToLang: TamazightToLangRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TamazightToLangUpdateComponent,
    resolve: {
      tamazightToLang: TamazightToLangRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tamazightToLangRoute)],
  exports: [RouterModule],
})
export class TamazightToLangRoutingModule {}
