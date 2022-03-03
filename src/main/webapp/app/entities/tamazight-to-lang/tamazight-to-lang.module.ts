import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TamazightToLangComponent } from './list/tamazight-to-lang.component';
import { TamazightToLangDetailComponent } from './detail/tamazight-to-lang-detail.component';
import { TamazightToLangUpdateComponent } from './update/tamazight-to-lang-update.component';
import { TamazightToLangDeleteDialogComponent } from './delete/tamazight-to-lang-delete-dialog.component';
import { TamazightToLangRoutingModule } from './route/tamazight-to-lang-routing.module';

@NgModule({
  imports: [SharedModule, TamazightToLangRoutingModule],
  declarations: [
    TamazightToLangComponent,
    TamazightToLangDetailComponent,
    TamazightToLangUpdateComponent,
    TamazightToLangDeleteDialogComponent,
  ],
  entryComponents: [TamazightToLangDeleteDialogComponent],
})
export class TamazightToLangModule {}
