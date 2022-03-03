import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DialecteService } from '../service/dialecte.service';
import { IDialecte, Dialecte } from '../dialecte.model';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

import { DialecteUpdateComponent } from './dialecte-update.component';

describe('Dialecte Management Update Component', () => {
  let comp: DialecteUpdateComponent;
  let fixture: ComponentFixture<DialecteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dialecteService: DialecteService;
  let regionService: RegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DialecteUpdateComponent],
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
      .overrideTemplate(DialecteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DialecteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dialecteService = TestBed.inject(DialecteService);
    regionService = TestBed.inject(RegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Region query and add missing value', () => {
      const dialecte: IDialecte = { id: 456 };
      const regions: IRegion[] = [{ id: 48940 }];
      dialecte.regions = regions;

      const regionCollection: IRegion[] = [{ id: 82243 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [...regions];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dialecte });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(regionCollection, ...additionalRegions);
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dialecte: IDialecte = { id: 456 };
      const regions: IRegion = { id: 73132 };
      dialecte.regions = [regions];

      activatedRoute.data = of({ dialecte });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dialecte));
      expect(comp.regionsSharedCollection).toContain(regions);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dialecte>>();
      const dialecte = { id: 123 };
      jest.spyOn(dialecteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dialecte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dialecte }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dialecteService.update).toHaveBeenCalledWith(dialecte);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dialecte>>();
      const dialecte = new Dialecte();
      jest.spyOn(dialecteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dialecte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dialecte }));
      saveSubject.complete();

      // THEN
      expect(dialecteService.create).toHaveBeenCalledWith(dialecte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Dialecte>>();
      const dialecte = { id: 123 };
      jest.spyOn(dialecteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dialecte });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dialecteService.update).toHaveBeenCalledWith(dialecte);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRegionById', () => {
      it('Should return tracked Region primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRegionById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedRegion', () => {
      it('Should return option if no Region is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedRegion(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Region for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedRegion(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Region is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedRegion(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
