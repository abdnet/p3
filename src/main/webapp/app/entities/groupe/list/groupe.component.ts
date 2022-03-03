import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGroupe } from '../groupe.model';
import { GroupeService } from '../service/groupe.service';
import { GroupeDeleteDialogComponent } from '../delete/groupe-delete-dialog.component';

@Component({
  selector: 'jhi-groupe',
  templateUrl: './groupe.component.html',
})
export class GroupeComponent implements OnInit {
  groupes?: IGroupe[];
  isLoading = false;

  constructor(protected groupeService: GroupeService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.groupeService.query().subscribe({
      next: (res: HttpResponse<IGroupe[]>) => {
        this.isLoading = false;
        this.groupes = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGroupe): number {
    return item.id!;
  }

  delete(groupe: IGroupe): void {
    const modalRef = this.modalService.open(GroupeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.groupe = groupe;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
