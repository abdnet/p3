import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AmawalWordComponent } from './list/amawal-word.component';
import { AmawalWordDetailComponent } from './detail/amawal-word-detail.component';
import { AmawalWordUpdateComponent } from './update/amawal-word-update.component';
import { AmawalWordDeleteDialogComponent } from './delete/amawal-word-delete-dialog.component';
import { AmawalWordRoutingModule } from './route/amawal-word-routing.module';

@NgModule({
  imports: [SharedModule, AmawalWordRoutingModule],
  declarations: [AmawalWordComponent, AmawalWordDetailComponent, AmawalWordUpdateComponent, AmawalWordDeleteDialogComponent],
  entryComponents: [AmawalWordDeleteDialogComponent],
})
export class AmawalWordModule {}
