import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LangueComponent } from './list/langue.component';
import { LangueDetailComponent } from './detail/langue-detail.component';
import { LangueUpdateComponent } from './update/langue-update.component';
import { LangueDeleteDialogComponent } from './delete/langue-delete-dialog.component';
import { LangueRoutingModule } from './route/langue-routing.module';

@NgModule({
  imports: [SharedModule, LangueRoutingModule],
  declarations: [LangueComponent, LangueDetailComponent, LangueUpdateComponent, LangueDeleteDialogComponent],
  entryComponents: [LangueDeleteDialogComponent],
})
export class LangueModule {}
