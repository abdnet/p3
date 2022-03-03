import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITamazightToLang, TamazightToLang } from '../tamazight-to-lang.model';
import { TamazightToLangService } from '../service/tamazight-to-lang.service';
import { ILangue } from 'app/entities/langue/langue.model';
import { LangueService } from 'app/entities/langue/service/langue.service';
import { IAmawalWord } from 'app/entities/amawal-word/amawal-word.model';
import { AmawalWordService } from 'app/entities/amawal-word/service/amawal-word.service';

@Component({
  selector: 'jhi-tamazight-to-lang-update',
  templateUrl: './tamazight-to-lang-update.component.html',
})
export class TamazightToLangUpdateComponent implements OnInit {
  isSaving = false;

  languesCollection: ILangue[] = [];
  amawalWordsSharedCollection: IAmawalWord[] = [];

  editForm = this.fb.group({
    id: [],
    traductionId: [],
    langue: [],
    amawalWord: [],
  });

  constructor(
    protected tamazightToLangService: TamazightToLangService,
    protected langueService: LangueService,
    protected amawalWordService: AmawalWordService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tamazightToLang }) => {
      this.updateForm(tamazightToLang);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tamazightToLang = this.createFromForm();
    if (tamazightToLang.id !== undefined) {
      this.subscribeToSaveResponse(this.tamazightToLangService.update(tamazightToLang));
    } else {
      this.subscribeToSaveResponse(this.tamazightToLangService.create(tamazightToLang));
    }
  }

  trackLangueById(index: number, item: ILangue): number {
    return item.id!;
  }

  trackAmawalWordById(index: number, item: IAmawalWord): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITamazightToLang>>): void {
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

  protected updateForm(tamazightToLang: ITamazightToLang): void {
    this.editForm.patchValue({
      id: tamazightToLang.id,
      traductionId: tamazightToLang.traductionId,
      langue: tamazightToLang.langue,
      amawalWord: tamazightToLang.amawalWord,
    });

    this.languesCollection = this.langueService.addLangueToCollectionIfMissing(this.languesCollection, tamazightToLang.langue);
    this.amawalWordsSharedCollection = this.amawalWordService.addAmawalWordToCollectionIfMissing(
      this.amawalWordsSharedCollection,
      tamazightToLang.amawalWord
    );
  }

  protected loadRelationshipsOptions(): void {
    this.langueService
      .query({ filter: 'tamazighttolang-is-null' })
      .pipe(map((res: HttpResponse<ILangue[]>) => res.body ?? []))
      .pipe(map((langues: ILangue[]) => this.langueService.addLangueToCollectionIfMissing(langues, this.editForm.get('langue')!.value)))
      .subscribe((langues: ILangue[]) => (this.languesCollection = langues));

    this.amawalWordService
      .query()
      .pipe(map((res: HttpResponse<IAmawalWord[]>) => res.body ?? []))
      .pipe(
        map((amawalWords: IAmawalWord[]) =>
          this.amawalWordService.addAmawalWordToCollectionIfMissing(amawalWords, this.editForm.get('amawalWord')!.value)
        )
      )
      .subscribe((amawalWords: IAmawalWord[]) => (this.amawalWordsSharedCollection = amawalWords));
  }

  protected createFromForm(): ITamazightToLang {
    return {
      ...new TamazightToLang(),
      id: this.editForm.get(['id'])!.value,
      traductionId: this.editForm.get(['traductionId'])!.value,
      langue: this.editForm.get(['langue'])!.value,
      amawalWord: this.editForm.get(['amawalWord'])!.value,
    };
  }
}
