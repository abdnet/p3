import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWordType } from '../word-type.model';
import { WordTypeService } from '../service/word-type.service';

@Component({
  templateUrl: './word-type-delete-dialog.component.html',
})
export class WordTypeDeleteDialogComponent {
  wordType?: IWordType;

  constructor(protected wordTypeService: WordTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wordTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
