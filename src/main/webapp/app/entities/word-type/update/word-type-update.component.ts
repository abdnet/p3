import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IWordType, WordType } from '../word-type.model';
import { WordTypeService } from '../service/word-type.service';

@Component({
  selector: 'jhi-word-type-update',
  templateUrl: './word-type-update.component.html',
})
export class WordTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    typeId: [],
    type: [],
  });

  constructor(protected wordTypeService: WordTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wordType }) => {
      this.updateForm(wordType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wordType = this.createFromForm();
    if (wordType.id !== undefined) {
      this.subscribeToSaveResponse(this.wordTypeService.update(wordType));
    } else {
      this.subscribeToSaveResponse(this.wordTypeService.create(wordType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWordType>>): void {
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

  protected updateForm(wordType: IWordType): void {
    this.editForm.patchValue({
      id: wordType.id,
      typeId: wordType.typeId,
      type: wordType.type,
    });
  }

  protected createFromForm(): IWordType {
    return {
      ...new WordType(),
      id: this.editForm.get(['id'])!.value,
      typeId: this.editForm.get(['typeId'])!.value,
      type: this.editForm.get(['type'])!.value,
    };
  }
}
