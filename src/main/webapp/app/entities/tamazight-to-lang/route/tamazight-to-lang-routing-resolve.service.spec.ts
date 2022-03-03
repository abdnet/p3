import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ITamazightToLang, TamazightToLang } from '../tamazight-to-lang.model';
import { TamazightToLangService } from '../service/tamazight-to-lang.service';

import { TamazightToLangRoutingResolveService } from './tamazight-to-lang-routing-resolve.service';

describe('TamazightToLang routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: TamazightToLangRoutingResolveService;
  let service: TamazightToLangService;
  let resultTamazightToLang: ITamazightToLang | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(TamazightToLangRoutingResolveService);
    service = TestBed.inject(TamazightToLangService);
    resultTamazightToLang = undefined;
  });

  describe('resolve', () => {
    it('should return ITamazightToLang returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTamazightToLang = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTamazightToLang).toEqual({ id: 123 });
    });

    it('should return new ITamazightToLang if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTamazightToLang = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultTamazightToLang).toEqual(new TamazightToLang());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TamazightToLang })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultTamazightToLang = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultTamazightToLang).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
