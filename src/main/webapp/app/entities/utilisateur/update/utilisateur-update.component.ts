import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IUtilisateur, Utilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';

@Component({
  selector: 'jhi-utilisateur-update',
  templateUrl: './utilisateur-update.component.html',
})
export class UtilisateurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    utilisateurId: [],
    nom: [],
    prenom: [],
    adresseEmail: [],
    avatar: [],
    dateInscription: [],
    validProfil: [],
    activateProfil: [],
    genre: [],
    password: [],
  });

  constructor(protected utilisateurService: UtilisateurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilisateur }) => {
      if (utilisateur.id === undefined) {
        const today = dayjs().startOf('day');
        utilisateur.dateInscription = today;
      }

      this.updateForm(utilisateur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const utilisateur = this.createFromForm();
    if (utilisateur.id !== undefined) {
      this.subscribeToSaveResponse(this.utilisateurService.update(utilisateur));
    } else {
      this.subscribeToSaveResponse(this.utilisateurService.create(utilisateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUtilisateur>>): void {
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

  protected updateForm(utilisateur: IUtilisateur): void {
    this.editForm.patchValue({
      id: utilisateur.id,
      utilisateurId: utilisateur.utilisateurId,
      nom: utilisateur.nom,
      prenom: utilisateur.prenom,
      adresseEmail: utilisateur.adresseEmail,
      avatar: utilisateur.avatar,
      dateInscription: utilisateur.dateInscription ? utilisateur.dateInscription.format(DATE_TIME_FORMAT) : null,
      validProfil: utilisateur.validProfil,
      activateProfil: utilisateur.activateProfil,
      genre: utilisateur.genre,
      password: utilisateur.password,
    });
  }

  protected createFromForm(): IUtilisateur {
    return {
      ...new Utilisateur(),
      id: this.editForm.get(['id'])!.value,
      utilisateurId: this.editForm.get(['utilisateurId'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      adresseEmail: this.editForm.get(['adresseEmail'])!.value,
      avatar: this.editForm.get(['avatar'])!.value,
      dateInscription: this.editForm.get(['dateInscription'])!.value
        ? dayjs(this.editForm.get(['dateInscription'])!.value, DATE_TIME_FORMAT)
        : undefined,
      validProfil: this.editForm.get(['validProfil'])!.value,
      activateProfil: this.editForm.get(['activateProfil'])!.value,
      genre: this.editForm.get(['genre'])!.value,
      password: this.editForm.get(['password'])!.value,
    };
  }
}
