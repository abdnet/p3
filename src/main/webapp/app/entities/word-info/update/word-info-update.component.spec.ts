import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WordInfoService } from '../service/word-info.service';
import { IWordInfo, WordInfo } from '../word-info.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';
import { UtilisateurService } from 'app/entities/utilisateur/service/utilisateur.service';

import { WordInfoUpdateComponent } from './word-info-update.component';

describe('WordInfo Management Update Component', () => {
  let comp: WordInfoUpdateComponent;
  let fixture: ComponentFixture<WordInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wordInfoService: WordInfoService;
  let utilisateurService: UtilisateurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WordInfoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WordInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WordInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wordInfoService = TestBed.inject(WordInfoService);
    utilisateurService = TestBed.inject(UtilisateurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Utilisateur query and add missing value', () => {
      const wordInfo: IWordInfo = { id: 456 };
      const contributeur: IUtilisateur = { id: 42868 };
      wordInfo.contributeur = contributeur;
      const validateur: IUtilisateur = { id: 68622 };
      wordInfo.validateur = validateur;

      const utilisateurCollection: IUtilisateur[] = [{ id: 54479 }];
      jest.spyOn(utilisateurService, 'query').mockReturnValue(of(new HttpResponse({ body: utilisateurCollection })));
      const additionalUtilisateurs = [contributeur, validateur];
      const expectedCollection: IUtilisateur[] = [...additionalUtilisateurs, ...utilisateurCollection];
      jest.spyOn(utilisateurService, 'addUtilisateurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wordInfo });
      comp.ngOnInit();

      expect(utilisateurService.query).toHaveBeenCalled();
      expect(utilisateurService.addUtilisateurToCollectionIfMissing).toHaveBeenCalledWith(utilisateurCollection, ...additionalUtilisateurs);
      expect(comp.utilisateursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const wordInfo: IWordInfo = { id: 456 };
      const contributeur: IUtilisateur = { id: 30472 };
      wordInfo.contributeur = contributeur;
      const validateur: IUtilisateur = { id: 21122 };
      wordInfo.validateur = validateur;

      activatedRoute.data = of({ wordInfo });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(wordInfo));
      expect(comp.utilisateursSharedCollection).toContain(contributeur);
      expect(comp.utilisateursSharedCollection).toContain(validateur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WordInfo>>();
      const wordInfo = { id: 123 };
      jest.spyOn(wordInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wordInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wordInfo }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(wordInfoService.update).toHaveBeenCalledWith(wordInfo);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WordInfo>>();
      const wordInfo = new WordInfo();
      jest.spyOn(wordInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wordInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wordInfo }));
      saveSubject.complete();

      // THEN
      expect(wordInfoService.create).toHaveBeenCalledWith(wordInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WordInfo>>();
      const wordInfo = { id: 123 };
      jest.spyOn(wordInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wordInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wordInfoService.update).toHaveBeenCalledWith(wordInfo);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUtilisateurById', () => {
      it('Should return tracked Utilisateur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUtilisateurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
