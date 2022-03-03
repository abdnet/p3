import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDialecte } from '../dialecte.model';
import { DialecteService } from '../service/dialecte.service';
import { DialecteDeleteDialogComponent } from '../delete/dialecte-delete-dialog.component';

@Component({
  selector: 'jhi-dialecte',
  templateUrl: './dialecte.component.html',
})
export class DialecteComponent implements OnInit {
  dialectes?: IDialecte[];
  isLoading = false;

  constructor(protected dialecteService: DialecteService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dialecteService.query().subscribe({
      next: (res: HttpResponse<IDialecte[]>) => {
        this.isLoading = false;
        this.dialectes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDialecte): number {
    return item.id!;
  }

  delete(dialecte: IDialecte): void {
    const modalRef = this.modalService.open(DialecteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dialecte = dialecte;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
