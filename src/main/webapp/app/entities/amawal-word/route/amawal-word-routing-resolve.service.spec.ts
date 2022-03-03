import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAmawalWord, AmawalWord } from '../amawal-word.model';
import { AmawalWordService } from '../service/amawal-word.service';

import { AmawalWordRoutingResolveService } from './amawal-word-routing-resolve.service';

describe('AmawalWord routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AmawalWordRoutingResolveService;
  let service: AmawalWordService;
  let resultAmawalWord: IAmawalWord | undefined;

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
    routingResolveService = TestBed.inject(AmawalWordRoutingResolveService);
    service = TestBed.inject(AmawalWordService);
    resultAmawalWord = undefined;
  });

  describe('resolve', () => {
    it('should return IAmawalWord returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmawalWord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAmawalWord).toEqual({ id: 123 });
    });

    it('should return new IAmawalWord if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmawalWord = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAmawalWord).toEqual(new AmawalWord());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AmawalWord })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAmawalWord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAmawalWord).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
