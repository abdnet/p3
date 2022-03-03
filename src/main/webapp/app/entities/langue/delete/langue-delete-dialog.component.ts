import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILangue } from '../langue.model';
import { LangueService } from '../service/langue.service';

@Component({
  templateUrl: './langue-delete-dialog.component.html',
})
export class LangueDeleteDialogComponent {
  langue?: ILangue;

  constructor(protected langueService: LangueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.langueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
