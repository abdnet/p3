import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WordTypeComponent } from '../list/word-type.component';
import { WordTypeDetailComponent } from '../detail/word-type-detail.component';
import { WordTypeUpdateComponent } from '../update/word-type-update.component';
import { WordTypeRoutingResolveService } from './word-type-routing-resolve.service';

const wordTypeRoute: Routes = [
  {
    path: '',
    component: WordTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WordTypeDetailComponent,
    resolve: {
      wordType: WordTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WordTypeUpdateComponent,
    resolve: {
      wordType: WordTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WordTypeUpdateComponent,
    resolve: {
      wordType: WordTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wordTypeRoute)],
  exports: [RouterModule],
})
export class WordTypeRoutingModule {}
