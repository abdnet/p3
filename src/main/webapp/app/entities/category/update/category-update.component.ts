import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICategory, Category } from '../category.model';
import { CategoryService } from '../service/category.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';
import { IGroupe } from 'app/entities/groupe/groupe.model';
import { GroupeService } from 'app/entities/groupe/service/groupe.service';

@Component({
  selector: 'jhi-category-update',
  templateUrl: './category-update.component.html',
})
export class CategoryUpdateComponent implements OnInit {
  isSaving = false;

  utilisateursSharedCollection: IUtilisateur[] = [];
  groupesSharedCollection: IGroupe[] = [];

  editForm = this.fb.group({
    id: [],
    categoryId: [],
    securityLevel: [],
    utilisateurs: [],
    groupe: [],
  });

  constructor(
    protected categoryService: CategoryService,
    protected utilisateurService: UtilisateurService,
    protected groupeService: GroupeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ category }) => {
      this.updateForm(category);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const category = this.createFromForm();
    if (category.id !== undefined) {
      this.subscribeToSaveResponse(this.categoryService.update(category));
    } else {
      this.subscribeToSaveResponse(this.categoryService.create(category));
    }
  }

  trackUtilisateurById(index: number, item: IUtilisateur): number {
    return item.id!;
  }

  trackGroupeById(index: number, item: IGroupe): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(category: ICategory): void {
    this.editForm.patchValue({
      id: category.id,
      categoryId: category.categoryId,
      securityLevel: category.securityLevel,
      utilisateurs: category.utilisateurs,
      groupe: category.groupe,
    });

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursSharedCollection,
      category.utilisateurs
    );
    this.groupesSharedCollection = this.groupeService.addGroupeToCollectionIfMissing(this.groupesSharedCollection, category.groupe);
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(utilisateurs, this.editForm.get('utilisateurs')!.value)
        )
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));

    this.groupeService
      .query()
      .pipe(map((res: HttpResponse<IGroupe[]>) => res.body ?? []))
      .pipe(map((groupes: IGroupe[]) => this.groupeService.addGroupeToCollectionIfMissing(groupes, this.editForm.get('groupe')!.value)))
      .subscribe((groupes: IGroupe[]) => (this.groupesSharedCollection = groupes));
  }

  protected createFromForm(): ICategory {
    return {
      ...new Category(),
      id: this.editForm.get(['id'])!.value,
      categoryId: this.editForm.get(['categoryId'])!.value,
      securityLevel: this.editForm.get(['securityLevel'])!.value,
      utilisateurs: this.editForm.get(['utilisateurs'])!.value,
      groupe: this.editForm.get(['groupe'])!.value,
    };
  }
}
