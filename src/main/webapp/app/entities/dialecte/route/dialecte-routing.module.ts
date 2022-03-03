import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DialecteComponent } from '../list/dialecte.component';
import { DialecteDetailComponent } from '../detail/dialecte-detail.component';
import { DialecteUpdateComponent } from '../update/dialecte-update.component';
import { DialecteRoutingResolveService } from './dialecte-routing-resolve.service';

const dialecteRoute: Routes = [
  {
    path: '',
    component: DialecteComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DialecteDetailComponent,
    resolve: {
      dialecte: DialecteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DialecteUpdateComponent,
    resolve: {
      dialecte: DialecteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DialecteUpdateComponent,
    resolve: {
      dialecte: DialecteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dialecteRoute)],
  exports: [RouterModule],
})
export class DialecteRoutingModule {}
