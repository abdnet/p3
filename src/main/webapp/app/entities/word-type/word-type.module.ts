import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WordTypeComponent } from './list/word-type.component';
import { WordTypeDetailComponent } from './detail/word-type-detail.component';
import { WordTypeUpdateComponent } from './update/word-type-update.component';
import { WordTypeDeleteDialogComponent } from './delete/word-type-delete-dialog.component';
import { WordTypeRoutingModule } from './route/word-type-routing.module';

@NgModule({
  imports: [SharedModule, WordTypeRoutingModule],
  declarations: [WordTypeComponent, WordTypeDetailComponent, WordTypeUpdateComponent, WordTypeDeleteDialogComponent],
  entryComponents: [WordTypeDeleteDialogComponent],
})
export class WordTypeModule {}
