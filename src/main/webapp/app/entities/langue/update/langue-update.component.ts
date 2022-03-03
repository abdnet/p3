import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ILangue, Langue } from '../langue.model';
import { LangueService } from '../service/langue.service';

@Component({
  selector: 'jhi-langue-update',
  templateUrl: './langue-update.component.html',
})
export class LangueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    langueId: [],
    langue: [],
  });

  constructor(protected langueService: LangueService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ langue }) => {
      this.updateForm(langue);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const langue = this.createFromForm();
    if (langue.id !== undefined) {
      this.subscribeToSaveResponse(this.langueService.update(langue));
    } else {
      this.subscribeToSaveResponse(this.langueService.create(langue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILangue>>): void {
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

  protected updateForm(langue: ILangue): void {
    this.editForm.patchValue({
      id: langue.id,
      langueId: langue.langueId,
      langue: langue.langue,
    });
  }

  protected createFromForm(): ILangue {
    return {
      ...new Langue(),
      id: this.editForm.get(['id'])!.value,
      langueId: this.editForm.get(['langueId'])!.value,
      langue: this.editForm.get(['langue'])!.value,
    };
  }
}
