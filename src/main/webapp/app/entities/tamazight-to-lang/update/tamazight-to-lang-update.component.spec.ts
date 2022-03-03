import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TamazightToLangService } from '../service/tamazight-to-lang.service';
import { ITamazightToLang, TamazightToLang } from '../tamazight-to-lang.model';
import { ILangue } from 'app/entities/langue/langue.model';
import { LangueService } from 'app/entities/langue/service/langue.service';
import { IAmawalWord } from 'app/entities/amawal-word/amawal-word.model';
import { AmawalWordService } from 'app/entities/amawal-word/service/amawal-word.service';

import { TamazightToLangUpdateComponent } from './tamazight-to-lang-update.component';

describe('TamazightToLang Management Update Component', () => {
  let comp: TamazightToLangUpdateComponent;
  let fixture: ComponentFixture<TamazightToLangUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tamazightToLangService: TamazightToLangService;
  let langueService: LangueService;
  let amawalWordService: AmawalWordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TamazightToLangUpdateComponent],
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
      .overrideTemplate(TamazightToLangUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TamazightToLangUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tamazightToLangService = TestBed.inject(TamazightToLangService);
    langueService = TestBed.inject(LangueService);
    amawalWordService = TestBed.inject(AmawalWordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call langue query and add missing value', () => {
      const tamazightToLang: ITamazightToLang = { id: 456 };
      const langue: ILangue = { id: 93628 };
      tamazightToLang.langue = langue;

      const langueCollection: ILangue[] = [{ id: 42536 }];
      jest.spyOn(langueService, 'query').mockReturnValue(of(new HttpResponse({ body: langueCollection })));
      const expectedCollection: ILangue[] = [langue, ...langueCollection];
      jest.spyOn(langueService, 'addLangueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tamazightToLang });
      comp.ngOnInit();

      expect(langueService.query).toHaveBeenCalled();
      expect(langueService.addLangueToCollectionIfMissing).toHaveBeenCalledWith(langueCollection, langue);
      expect(comp.languesCollection).toEqual(expectedCollection);
    });

    it('Should call AmawalWord query and add missing value', () => {
      const tamazightToLang: ITamazightToLang = { id: 456 };
      const amawalWord: IAmawalWord = { id: 66463 };
      tamazightToLang.amawalWord = amawalWord;

      const amawalWordCollection: IAmawalWord[] = [{ id: 3780 }];
      jest.spyOn(amawalWordService, 'query').mockReturnValue(of(new HttpResponse({ body: amawalWordCollection })));
      const additionalAmawalWords = [amawalWord];
      const expectedCollection: IAmawalWord[] = [...additionalAmawalWords, ...amawalWordCollection];
      jest.spyOn(amawalWordService, 'addAmawalWordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ tamazightToLang });
      comp.ngOnInit();

      expect(amawalWordService.query).toHaveBeenCalled();
      expect(amawalWordService.addAmawalWordToCollectionIfMissing).toHaveBeenCalledWith(amawalWordCollection, ...additionalAmawalWords);
      expect(comp.amawalWordsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const tamazightToLang: ITamazightToLang = { id: 456 };
      const langue: ILangue = { id: 68400 };
      tamazightToLang.langue = langue;
      const amawalWord: IAmawalWord = { id: 32735 };
      tamazightToLang.amawalWord = amawalWord;

      activatedRoute.data = of({ tamazightToLang });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(tamazightToLang));
      expect(comp.languesCollection).toContain(langue);
      expect(comp.amawalWordsSharedCollection).toContain(amawalWord);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TamazightToLang>>();
      const tamazightToLang = { id: 123 };
      jest.spyOn(tamazightToLangService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tamazightToLang });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tamazightToLang }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(tamazightToLangService.update).toHaveBeenCalledWith(tamazightToLang);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TamazightToLang>>();
      const tamazightToLang = new TamazightToLang();
      jest.spyOn(tamazightToLangService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tamazightToLang });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: tamazightToLang }));
      saveSubject.complete();

      // THEN
      expect(tamazightToLangService.create).toHaveBeenCalledWith(tamazightToLang);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<TamazightToLang>>();
      const tamazightToLang = { id: 123 };
      jest.spyOn(tamazightToLangService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ tamazightToLang });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tamazightToLangService.update).toHaveBeenCalledWith(tamazightToLang);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackLangueById', () => {
      it('Should return tracked Langue primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackLangueById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackAmawalWordById', () => {
      it('Should return tracked AmawalWord primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAmawalWordById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
