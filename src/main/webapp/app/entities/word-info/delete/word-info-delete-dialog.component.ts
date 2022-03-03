import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWordInfo } from '../word-info.model';
import { WordInfoService } from '../service/word-info.service';

@Component({
  templateUrl: './word-info-delete-dialog.component.html',
})
export class WordInfoDeleteDialogComponent {
  wordInfo?: IWordInfo;

  constructor(protected wordInfoService: WordInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wordInfoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
