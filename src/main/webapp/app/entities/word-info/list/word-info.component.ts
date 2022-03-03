import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IWordInfo } from '../word-info.model';
import { WordInfoService } from '../service/word-info.service';
import { WordInfoDeleteDialogComponent } from '../delete/word-info-delete-dialog.component';

@Component({
  selector: 'jhi-word-info',
  templateUrl: './word-info.component.html',
})
export class WordInfoComponent implements OnInit {
  wordInfos?: IWordInfo[];
  isLoading = false;

  constructor(protected wordInfoService: WordInfoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.wordInfoService.query().subscribe({
      next: (res: HttpResponse<IWordInfo[]>) => {
        this.isLoading = false;
        this.wordInfos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IWordInfo): number {
    return item.id!;
  }

  delete(wordInfo: IWordInfo): void {
    const modalRef = this.modalService.open(WordInfoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.wordInfo = wordInfo;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
