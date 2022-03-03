import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWordType } from '../word-type.model';
import { WordTypeService } from '../service/word-type.service';
import { WordTypeDeleteDialogComponent } from '../delete/word-type-delete-dialog.component';

@Component({
  selector: 'jhi-word-type',
  templateUrl: './word-type.component.html',
})
export class WordTypeComponent implements OnInit {
  wordTypes?: IWordType[];
  isLoading = false;

  constructor(protected wordTypeService: WordTypeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.wordTypeService.query().subscribe({
      next: (res: HttpResponse<IWordType[]>) => {
        this.isLoading = false;
        this.wordTypes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWordType): number {
    return item.id!;
  }

  delete(wordType: IWordType): void {
    const modalRef = this.modalService.open(WordTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.wordType = wordType;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
