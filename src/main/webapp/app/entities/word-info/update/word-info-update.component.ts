import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IWordInfo, WordInfo } from '../word-info.model';
import { WordInfoService } from '../service/word-info.service';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

@Component({
  selector: 'jhi-word-info-update',
  templateUrl: './word-info-update.component.html',
})
export class WordInfoUpdateComponent implements OnInit {
  isSaving = false;

  utilisateursSharedCollection: IUtilisateur[] = [];

  editForm = this.fb.group({
    id: [],
    wordInfoId: [],
    contributionDate: [],
    validationDate: [],
    etat: [],
    contributeur: [],
    validateur: [],
  });

  constructor(
    protected wordInfoService: WordInfoService,
    protected utilisateurService: UtilisateurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wordInfo }) => {
      if (wordInfo.id === undefined) {
        const today = dayjs().startOf('day');
        wordInfo.contributionDate = today;
        wordInfo.validationDate = today;
      }

      this.updateForm(wordInfo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wordInfo = this.createFromForm();
    if (wordInfo.id !== undefined) {
      this.subscribeToSaveResponse(this.wordInfoService.update(wordInfo));
    } else {
      this.subscribeToSaveResponse(this.wordInfoService.create(wordInfo));
    }
  }

  trackUtilisateurById(index: number, item: IUtilisateur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWordInfo>>): void {
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

  protected updateForm(wordInfo: IWordInfo): void {
    this.editForm.patchValue({
      id: wordInfo.id,
      wordInfoId: wordInfo.wordInfoId,
      contributionDate: wordInfo.contributionDate ? wordInfo.contributionDate.format(DATE_TIME_FORMAT) : null,
      validationDate: wordInfo.validationDate ? wordInfo.validationDate.format(DATE_TIME_FORMAT) : null,
      etat: wordInfo.etat,
      contributeur: wordInfo.contributeur,
      validateur: wordInfo.validateur,
    });

    this.utilisateursSharedCollection = this.utilisateurService.addUtilisateurToCollectionIfMissing(
      this.utilisateursSharedCollection,
      wordInfo.contributeur,
      wordInfo.validateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.utilisateurService
      .query()
      .pipe(map((res: HttpResponse<IUtilisateur[]>) => res.body ?? []))
      .pipe(
        map((utilisateurs: IUtilisateur[]) =>
          this.utilisateurService.addUtilisateurToCollectionIfMissing(
            utilisateurs,
            this.editForm.get('contributeur')!.value,
            this.editForm.get('validateur')!.value
          )
        )
      )
      .subscribe((utilisateurs: IUtilisateur[]) => (this.utilisateursSharedCollection = utilisateurs));
  }

  protected createFromForm(): IWordInfo {
    return {
      ...new WordInfo(),
      id: this.editForm.get(['id'])!.value,
      wordInfoId: this.editForm.get(['wordInfoId'])!.value,
      contributionDate: this.editForm.get(['contributionDate'])!.value
        ? dayjs(this.editForm.get(['contributionDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      validationDate: this.editForm.get(['validationDate'])!.value
        ? dayjs(this.editForm.get(['validationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      etat: this.editForm.get(['etat'])!.value,
      contributeur: this.editForm.get(['contributeur'])!.value,
      validateur: this.editForm.get(['validateur'])!.value,
    };
  }
}
