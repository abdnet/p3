import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { WordInfoComponent } from '../list/word-info.component';
import { WordInfoDetailComponent } from '../detail/word-info-detail.component';
import { WordInfoUpdateComponent } from '../update/word-info-update.component';
import { WordInfoRoutingResolveService } from './word-info-routing-resolve.service';

const wordInfoRoute: Routes = [
  {
    path: '',
    component: WordInfoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WordInfoDetailComponent,
    resolve: {
      wordInfo: WordInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WordInfoUpdateComponent,
    resolve: {
      wordInfo: WordInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WordInfoUpdateComponent,
    resolve: {
      wordInfo: WordInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(wordInfoRoute)],
  exports: [RouterModule],
})
export class WordInfoRoutingModule {}
