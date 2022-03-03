import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AmawalWordComponent } from '../list/amawal-word.component';
import { AmawalWordDetailComponent } from '../detail/amawal-word-detail.component';
import { AmawalWordUpdateComponent } from '../update/amawal-word-update.component';
import { AmawalWordRoutingResolveService } from './amawal-word-routing-resolve.service';

const amawalWordRoute: Routes = [
  {
    path: '',
    component: AmawalWordComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AmawalWordDetailComponent,
    resolve: {
      amawalWord: AmawalWordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AmawalWordUpdateComponent,
    resolve: {
      amawalWord: AmawalWordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AmawalWordUpdateComponent,
    resolve: {
      amawalWord: AmawalWordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(amawalWordRoute)],
  exports: [RouterModule],
})
export class AmawalWordRoutingModule {}
