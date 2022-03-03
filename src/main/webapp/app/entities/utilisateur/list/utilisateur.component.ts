import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUtilisateur } from '../utilisateur.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { UtilisateurService } from '../service/utilisateur.service';
import { UtilisateurDeleteDialogComponent } from '../delete/utilisateur-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-utilisateur',
  templateUrl: './utilisateur.component.html',
})
export class UtilisateurComponent implements OnInit {
  utilisateurs: IUtilisateur[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected utilisateurService: UtilisateurService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.utilisateurs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.utilisateurService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IUtilisateur[]>) => {
          this.isLoading = false;
          this.paginateUtilisateurs(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.utilisateurs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IUtilisateur): number {
    return item.id!;
  }

  delete(utilisateur: IUtilisateur): void {
    const modalRef = this.modalService.open(UtilisateurDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.utilisateur = utilisateur;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateUtilisateurs(data: IUtilisateur[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.utilisateurs.push(d);
      }
    }
  }
}
