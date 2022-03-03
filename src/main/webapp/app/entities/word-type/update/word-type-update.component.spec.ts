import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WordTypeService } from '../service/word-type.service';
import { IWordType, WordType } from '../word-type.model';

import { WordTypeUpdateComponent } from './word-type-update.component';

describe('WordType Management Update Component', () => {
  let comp: WordTypeUpdateComponent;
  let fixture: ComponentFixture<WordTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wordTypeService: WordTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [WordTypeUpdateComponent],
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
      .overrideTemplate(WordTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WordTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wordTypeService = TestBed.inject(WordTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const wordType: IWordType = { id: 456 };

      activatedRoute.data = of({ wordType });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(wordType));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WordType>>();
      const wordType = { id: 123 };
      jest.spyOn(wordTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wordType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wordType }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(wordTypeService.update).toHaveBeenCalledWith(wordType);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WordType>>();
      const wordType = new WordType();
      jest.spyOn(wordTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wordType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wordType }));
      saveSubject.complete();

      // THEN
      expect(wordTypeService.create).toHaveBeenCalledWith(wordType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<WordType>>();
      const wordType = { id: 123 };
      jest.spyOn(wordTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wordType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wordTypeService.update).toHaveBeenCalledWith(wordType);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
