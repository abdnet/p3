import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITamazightToLang } from '../tamazight-to-lang.model';
import { TamazightToLangService } from '../service/tamazight-to-lang.service';

@Component({
  templateUrl: './tamazight-to-lang-delete-dialog.component.html',
})
export class TamazightToLangDeleteDialogComponent {
  tamazightToLang?: ITamazightToLang;

  constructor(protected tamazightToLangService: TamazightToLangService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tamazightToLangService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
