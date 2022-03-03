import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILangue } from '../langue.model';
import { LangueService } from '../service/langue.service';
import { LangueDeleteDialogComponent } from '../delete/langue-delete-dialog.component';

@Component({
  selector: 'jhi-langue',
  templateUrl: './langue.component.html',
})
export class LangueComponent implements OnInit {
  langues?: ILangue[];
  isLoading = false;

  constructor(protected langueService: LangueService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.langueService.query().subscribe({
      next: (res: HttpResponse<ILangue[]>) => {
        this.isLoading = false;
        this.langues = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILangue): number {
    return item.id!;
  }

  delete(langue: ILangue): void {
    const modalRef = this.modalService.open(LangueDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.langue = langue;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
