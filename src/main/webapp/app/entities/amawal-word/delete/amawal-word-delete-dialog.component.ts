import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAmawalWord } from '../amawal-word.model';
import { AmawalWordService } from '../service/amawal-word.service';

@Component({
  templateUrl: './amawal-word-delete-dialog.component.html',
})
export class AmawalWordDeleteDialogComponent {
  amawalWord?: IAmawalWord;

  constructor(protected amawalWordService: AmawalWordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.amawalWordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
