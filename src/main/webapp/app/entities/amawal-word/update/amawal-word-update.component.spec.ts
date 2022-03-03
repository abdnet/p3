import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AmawalWordService } from '../service/amawal-word.service';
import { IAmawalWord, AmawalWord } from '../amawal-word.model';
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

import { AmawalWordUpdateComponent } from './amawal-word-update.component';

describe('AmawalWord Management Update Component', () => {
  let comp: AmawalWordUpdateComponent;
  let fixture: ComponentFixture<AmawalWordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let amawalWordService: AmawalWordService;
  let wordInfoService: WordInfoService;
  let tamazightToLangService: TamazightToLangService;
  let wordTypeService: WordTypeService;
  let themeService: ThemeService;
  let dialecteService: DialecteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AmawalWordUpdateComponent],
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
      .overrideTemplate(AmawalWordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AmawalWordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    amawalWordService = TestBed.inject(AmawalWordService);
    wordInfoService = TestBed.inject(WordInfoService);
    tamazightToLangService = TestBed.inject(TamazightToLangService);
    wordTypeService = TestBed.inject(WordTypeService);
    themeService = TestBed.inject(ThemeService);
    dialecteService = TestBed.inject(DialecteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call wordInfo query and add missing value', () => {
      const amawalWord: IAmawalWord = { id: 456 };
      const wordInfo: IWordInfo = { id: 86806 };
      amawalWord.wordInfo = wordInfo;

      const wordInfoCollection: IWordInfo[] = [{ id: 15762 }];
      jest.spyOn(wordInfoService, 'query').mockReturnValue(of(new HttpResponse({ body: wordInfoCollection })));
      const expectedCollection: IWordInfo[] = [wordInfo, ...wordInfoCollection];
      jest.spyOn(wordInfoService, 'addWordInfoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      expect(wordInfoService.query).toHaveBeenCalled();
      expect(wordInfoService.addWordInfoToCollectionIfMissing).toHaveBeenCalledWith(wordInfoCollection, wordInfo);
      expect(comp.wordInfosCollection).toEqual(expectedCollection);
    });

    it('Should call TamazightToLang query and add missing value', () => {
      const amawalWord: IAmawalWord = { id: 456 };
      const tamazightToLang: ITamazightToLang = { id: 99202 };
      amawalWord.tamazightToLang = tamazightToLang;

      const tamazightToLangCollection: ITamazightToLang[] = [{ id: 17006 }];
      jest.spyOn(tamazightToLangService, 'query').mockReturnValue(of(new HttpResponse({ body: tamazightToLangCollection })));
      const additionalTamazightToLangs = [tamazightToLang];
      const expectedCollection: ITamazightToLang[] = [...additionalTamazightToLangs, ...tamazightToLangCollection];
      jest.spyOn(tamazightToLangService, 'addTamazightToLangToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      expect(tamazightToLangService.query).toHaveBeenCalled();
      expect(tamazightToLangService.addTamazightToLangToCollectionIfMissing).toHaveBeenCalledWith(
        tamazightToLangCollection,
        ...additionalTamazightToLangs
      );
      expect(comp.tamazightToLangsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WordType query and add missing value', () => {
      const amawalWord: IAmawalWord = { id: 456 };
      const wordType: IWordType = { id: 21473 };
      amawalWord.wordType = wordType;

      const wordTypeCollection: IWordType[] = [{ id: 6953 }];
      jest.spyOn(wordTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: wordTypeCollection })));
      const additionalWordTypes = [wordType];
      const expectedCollection: IWordType[] = [...additionalWordTypes, ...wordTypeCollection];
      jest.spyOn(wordTypeService, 'addWordTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      expect(wordTypeService.query).toHaveBeenCalled();
      expect(wordTypeService.addWordTypeToCollectionIfMissing).toHaveBeenCalledWith(wordTypeCollection, ...additionalWordTypes);
      expect(comp.wordTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Theme query and add missing value', () => {
      const amawalWord: IAmawalWord = { id: 456 };
      const theme: ITheme = { id: 90336 };
      amawalWord.theme = theme;

      const themeCollection: ITheme[] = [{ id: 99595 }];
      jest.spyOn(themeService, 'query').mockReturnValue(of(new HttpResponse({ body: themeCollection })));
      const additionalThemes = [theme];
      const expectedCollection: ITheme[] = [...additionalThemes, ...themeCollection];
      jest.spyOn(themeService, 'addThemeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      expect(themeService.query).toHaveBeenCalled();
      expect(themeService.addThemeToCollectionIfMissing).toHaveBeenCalledWith(themeCollection, ...additionalThemes);
      expect(comp.themesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Dialecte query and add missing value', () => {
      const amawalWord: IAmawalWord = { id: 456 };
      const dialectes: IDialecte[] = [{ id: 25078 }];
      amawalWord.dialectes = dialectes;

      const dialecteCollection: IDialecte[] = [{ id: 59215 }];
      jest.spyOn(dialecteService, 'query').mockReturnValue(of(new HttpResponse({ body: dialecteCollection })));
      const additionalDialectes = [...dialectes];
      const expectedCollection: IDialecte[] = [...additionalDialectes, ...dialecteCollection];
      jest.spyOn(dialecteService, 'addDialecteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      expect(dialecteService.query).toHaveBeenCalled();
      expect(dialecteService.addDialecteToCollectionIfMissing).toHaveBeenCalledWith(dialecteCollection, ...additionalDialectes);
      expect(comp.dialectesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const amawalWord: IAmawalWord = { id: 456 };
      const wordInfo: IWordInfo = { id: 81623 };
      amawalWord.wordInfo = wordInfo;
      const tamazightToLang: ITamazightToLang = { id: 85243 };
      amawalWord.tamazightToLang = tamazightToLang;
      const wordType: IWordType = { id: 27091 };
      amawalWord.wordType = wordType;
      const theme: ITheme = { id: 12532 };
      amawalWord.theme = theme;
      const dialectes: IDialecte = { id: 39514 };
      amawalWord.dialectes = [dialectes];

      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(amawalWord));
      expect(comp.wordInfosCollection).toContain(wordInfo);
      expect(comp.tamazightToLangsSharedCollection).toContain(tamazightToLang);
      expect(comp.wordTypesSharedCollection).toContain(wordType);
      expect(comp.themesSharedCollection).toContain(theme);
      expect(comp.dialectesSharedCollection).toContain(dialectes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmawalWord>>();
      const amawalWord = { id: 123 };
      jest.spyOn(amawalWordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amawalWord }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(amawalWordService.update).toHaveBeenCalledWith(amawalWord);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmawalWord>>();
      const amawalWord = new AmawalWord();
      jest.spyOn(amawalWordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: amawalWord }));
      saveSubject.complete();

      // THEN
      expect(amawalWordService.create).toHaveBeenCalledWith(amawalWord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AmawalWord>>();
      const amawalWord = { id: 123 };
      jest.spyOn(amawalWordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ amawalWord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(amawalWordService.update).toHaveBeenCalledWith(amawalWord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackWordInfoById', () => {
      it('Should return tracked WordInfo primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWordInfoById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackTamazightToLangById', () => {
      it('Should return tracked TamazightToLang primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackTamazightToLangById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackWordTypeById', () => {
      it('Should return tracked WordType primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackWordTypeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackThemeById', () => {
      it('Should return tracked Theme primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackThemeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackDialecteById', () => {
      it('Should return tracked Dialecte primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDialecteById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedDialecte', () => {
      it('Should return option if no Dialecte is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedDialecte(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Dialecte for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedDialecte(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Dialecte is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedDialecte(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
