import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDialecte } from '../dialecte.model';
import { DialecteService } from '../service/dialecte.service';

@Component({
  templateUrl: './dialecte-delete-dialog.component.html',
})
export class DialecteDeleteDialogComponent {
  dialecte?: IDialecte;

  constructor(protected dialecteService: DialecteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dialecteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
