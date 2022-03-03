import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LangueComponent } from '../list/langue.component';
import { LangueDetailComponent } from '../detail/langue-detail.component';
import { LangueUpdateComponent } from '../update/langue-update.component';
import { LangueRoutingResolveService } from './langue-routing-resolve.service';

const langueRoute: Routes = [
  {
    path: '',
    component: LangueComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LangueDetailComponent,
    resolve: {
      langue: LangueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LangueUpdateComponent,
    resolve: {
      langue: LangueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LangueUpdateComponent,
    resolve: {
      langue: LangueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(langueRoute)],
  exports: [RouterModule],
})
export class LangueRoutingModule {}
