import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDialecte, Dialecte } from '../dialecte.model';
import { DialecteService } from '../service/dialecte.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

@Component({
  selector: 'jhi-dialecte-update',
  templateUrl: './dialecte-update.component.html',
})
export class DialecteUpdateComponent implements OnInit {
  isSaving = false;

  regionsSharedCollection: IRegion[] = [];

  editForm = this.fb.group({
    id: [],
    dialecteId: [],
    dialecte: [],
    regions: [],
  });

  constructor(
    protected dialecteService: DialecteService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dialecte }) => {
      this.updateForm(dialecte);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dialecte = this.createFromForm();
    if (dialecte.id !== undefined) {
      this.subscribeToSaveResponse(this.dialecteService.update(dialecte));
    } else {
      this.subscribeToSaveResponse(this.dialecteService.create(dialecte));
    }
  }

  trackRegionById(index: number, item: IRegion): number {
    return item.id!;
  }

  getSelectedRegion(option: IRegion, selectedVals?: IRegion[]): IRegion {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDialecte>>): void {
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

  protected updateForm(dialecte: IDialecte): void {
    this.editForm.patchValue({
      id: dialecte.id,
      dialecteId: dialecte.dialecteId,
      dialecte: dialecte.dialecte,
      regions: dialecte.regions,
    });

    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing(
      this.regionsSharedCollection,
      ...(dialecte.regions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(
        map((regions: IRegion[]) =>
          this.regionService.addRegionToCollectionIfMissing(regions, ...(this.editForm.get('regions')!.value ?? []))
        )
      )
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));
  }

  protected createFromForm(): IDialecte {
    return {
      ...new Dialecte(),
      id: this.editForm.get(['id'])!.value,
      dialecteId: this.editForm.get(['dialecteId'])!.value,
      dialecte: this.editForm.get(['dialecte'])!.value,
      regions: this.editForm.get(['regions'])!.value,
    };
  }
}
