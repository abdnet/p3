import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DialecteComponent } from './list/dialecte.component';
import { DialecteDetailComponent } from './detail/dialecte-detail.component';
import { DialecteUpdateComponent } from './update/dialecte-update.component';
import { DialecteDeleteDialogComponent } from './delete/dialecte-delete-dialog.component';
import { DialecteRoutingModule } from './route/dialecte-routing.module';

@NgModule({
  imports: [SharedModule, DialecteRoutingModule],
  declarations: [DialecteComponent, DialecteDetailComponent, DialecteUpdateComponent, DialecteDeleteDialogComponent],
  entryComponents: [DialecteDeleteDialogComponent],
})
export class DialecteModule {}
