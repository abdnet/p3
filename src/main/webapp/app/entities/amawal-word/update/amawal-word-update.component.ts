import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAmawalWord, AmawalWord } from '../amawal-word.model';
import { AmawalWordService } from '../service/amawal-word.service';
import { IWordInfo } from 'app/entities/word-info/word-info.model';
import { WordInfoService } from 'app/entities/word-info/service/word-info.service';
import { ITamazightToLang } from 'app/entities/tamazight-to-lang/tamazight-to-lang.model';
import { TamazightToLangService } from 'app/entities/tamazight-to-lang/service/tamazight-to-lang.service';
import { IWordType } from 'app/entities/word-type/word-type.model';
import { WordTypeService } from 'app/entities/word-type/service/word-type.service';
import { ITheme } from 'app/entities/theme/theme.model';
import { ThemeService } from 'app/entities/theme/service/theme.service';
import { IDialecte } from 'app/entities/dialecte/dialecte.model';
import { DialecteService } from 'app/entities/dialecte/service/dialecte.service';

@Component({
  selector: 'jhi-amawal-word-update',
  templateUrl: './amawal-word-update.component.html',
})
export class AmawalWordUpdateComponent implements OnInit {
  isSaving = false;

  wordInfosCollection: IWordInfo[] = [];
  tamazightToLangsSharedCollection: ITamazightToLang[] = [];
  wordTypesSharedCollection: IWordType[] = [];
  themesSharedCollection: ITheme[] = [];
  dialectesSharedCollection: IDialecte[] = [];

  editForm = this.fb.group({
    id: [],
    wordId: [],
    orthographeTifinagh: [],
    orthographeLatin: [],
    wordInfo: [],
    wordType: [],
    theme: [],
    dialectes: [],
    tamazightToLang: [],
  });

  constructor(
    protected amawalWordService: AmawalWordService,
    protected wordInfoService: WordInfoService,
    protected tamazightToLangService: TamazightToLangService,
    protected wordTypeService: WordTypeService,
    protected themeService: ThemeService,
    protected dialecteService: DialecteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amawalWord }) => {
      this.updateForm(amawalWord);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const amawalWord = this.createFromForm();
    if (amawalWord.id !== undefined) {
      this.subscribeToSaveResponse(this.amawalWordService.update(amawalWord));
    } else {
      this.subscribeToSaveResponse(this.amawalWordService.create(amawalWord));
    }
  }

  trackWordInfoById(index: number, item: IWordInfo): number {
    return item.id!;
  }

  trackTamazightToLangById(index: number, item: ITamazightToLang): number {
    return item.id!;
  }

  trackWordTypeById(index: number, item: IWordType): number {
    return item.id!;
  }

  trackThemeById(index: number, item: ITheme): number {
    return item.id!;
  }

  trackDialecteById(index: number, item: IDialecte): number {
    return item.id!;
  }

  getSelectedDialecte(option: IDialecte, selectedVals?: IDialecte[]): IDialecte {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAmawalWord>>): void {
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

  protected updateForm(amawalWord: IAmawalWord): void {
    this.editForm.patchValue({
      id: amawalWord.id,
      wordId: amawalWord.wordId,
      orthographeTifinagh: amawalWord.orthographeTifinagh,
      orthographeLatin: amawalWord.orthographeLatin,
      wordInfo: amawalWord.wordInfo,
      wordType: amawalWord.wordType,
      theme: amawalWord.theme,
      dialectes: amawalWord.dialectes,
      tamazightToLang: amawalWord.tamazightToLang,
    });

    this.wordInfosCollection = this.wordInfoService.addWordInfoToCollectionIfMissing(this.wordInfosCollection, amawalWord.wordInfo);
    this.tamazightToLangsSharedCollection = this.tamazightToLangService.addTamazightToLangToCollectionIfMissing(
      this.tamazightToLangsSharedCollection,
      amawalWord.tamazightToLang
    );
    this.wordTypesSharedCollection = this.wordTypeService.addWordTypeToCollectionIfMissing(
      this.wordTypesSharedCollection,
      amawalWord.wordType
    );
    this.themesSharedCollection = this.themeService.addThemeToCollectionIfMissing(this.themesSharedCollection, amawalWord.theme);
    this.dialectesSharedCollection = this.dialecteService.addDialecteToCollectionIfMissing(
      this.dialectesSharedCollection,
      ...(amawalWord.dialectes ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.wordInfoService
      .query({ filter: 'amawalword-is-null' })
      .pipe(map((res: HttpResponse<IWordInfo[]>) => res.body ?? []))
      .pipe(
        map((wordInfos: IWordInfo[]) =>
          this.wordInfoService.addWordInfoToCollectionIfMissing(wordInfos, this.editForm.get('wordInfo')!.value)
        )
      )
      .subscribe((wordInfos: IWordInfo[]) => (this.wordInfosCollection = wordInfos));

    this.tamazightToLangService
      .query()
      .pipe(map((res: HttpResponse<ITamazightToLang[]>) => res.body ?? []))
      .pipe(
        map((tamazightToLangs: ITamazightToLang[]) =>
          this.tamazightToLangService.addTamazightToLangToCollectionIfMissing(tamazightToLangs, this.editForm.get('tamazightToLang')!.value)
        )
      )
      .subscribe((tamazightToLangs: ITamazightToLang[]) => (this.tamazightToLangsSharedCollection = tamazightToLangs));

    this.wordTypeService
      .query()
      .pipe(map((res: HttpResponse<IWordType[]>) => res.body ?? []))
      .pipe(
        map((wordTypes: IWordType[]) =>
          this.wordTypeService.addWordTypeToCollectionIfMissing(wordTypes, this.editForm.get('wordType')!.value)
        )
      )
      .subscribe((wordTypes: IWordType[]) => (this.wordTypesSharedCollection = wordTypes));

    this.themeService
      .query()
      .pipe(map((res: HttpResponse<ITheme[]>) => res.body ?? []))
      .pipe(map((themes: ITheme[]) => this.themeService.addThemeToCollectionIfMissing(themes, this.editForm.get('theme')!.value)))
      .subscribe((themes: ITheme[]) => (this.themesSharedCollection = themes));

    this.dialecteService
      .query()
      .pipe(map((res: HttpResponse<IDialecte[]>) => res.body ?? []))
      .pipe(
        map((dialectes: IDialecte[]) =>
          this.dialecteService.addDialecteToCollectionIfMissing(dialectes, ...(this.editForm.get('dialectes')!.value ?? []))
        )
      )
      .subscribe((dialectes: IDialecte[]) => (this.dialectesSharedCollection = dialectes));
  }

  protected createFromForm(): IAmawalWord {
    return {
      ...new AmawalWord(),
      id: this.editForm.get(['id'])!.value,
      wordId: this.editForm.get(['wordId'])!.value,
      orthographeTifinagh: this.editForm.get(['orthographeTifinagh'])!.value,
      orthographeLatin: this.editForm.get(['orthographeLatin'])!.value,
      wordInfo: this.editForm.get(['wordInfo'])!.value,
      wordType: this.editForm.get(['wordType'])!.value,
      theme: this.editForm.get(['theme'])!.value,
      dialectes: this.editForm.get(['dialectes'])!.value,
      tamazightToLang: this.editForm.get(['tamazightToLang'])!.value,
    };
  }
}
