import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { LangueService } from '../service/langue.service';
import { ILangue, Langue } from '../langue.model';

import { LangueUpdateComponent } from './langue-update.component';

describe('Langue Management Update Component', () => {
  let comp: LangueUpdateComponent;
  let fixture: ComponentFixture<LangueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let langueService: LangueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [LangueUpdateComponent],
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
      .overrideTemplate(LangueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LangueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    langueService = TestBed.inject(LangueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const langue: ILangue = { id: 456 };

      activatedRoute.data = of({ langue });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(langue));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Langue>>();
      const langue = { id: 123 };
      jest.spyOn(langueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ langue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: langue }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(langueService.update).toHaveBeenCalledWith(langue);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Langue>>();
      const langue = new Langue();
      jest.spyOn(langueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ langue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: langue }));
      saveSubject.complete();

      // THEN
      expect(langueService.create).toHaveBeenCalledWith(langue);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Langue>>();
      const langue = { id: 123 };
      jest.spyOn(langueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ langue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(langueService.update).toHaveBeenCalledWith(langue);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
