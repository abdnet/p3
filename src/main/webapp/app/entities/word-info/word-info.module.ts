import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WordInfoComponent } from './list/word-info.component';
import { WordInfoDetailComponent } from './detail/word-info-detail.component';
import { WordInfoUpdateComponent } from './update/word-info-update.component';
import { WordInfoDeleteDialogComponent } from './delete/word-info-delete-dialog.component';
import { WordInfoRoutingModule } from './route/word-info-routing.module';

@NgModule({
  imports: [SharedModule, WordInfoRoutingModule],
  declarations: [WordInfoComponent, WordInfoDetailComponent, WordInfoUpdateComponent, WordInfoDeleteDialogComponent],
  entryComponents: [WordInfoDeleteDialogComponent],
})
export class WordInfoModule {}
