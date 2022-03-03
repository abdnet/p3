import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDialecte, Dialecte } from '../dialecte.model';

import { DialecteService } from './dialecte.service';

describe('Dialecte Service', () => {
  let service: DialecteService;
  let httpMock: HttpTestingController;
  let elemDefault: IDialecte;
  let expectedResult: IDialecte | IDialecte[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DialecteService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      dialecteId: 0,
      dialecte: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dialecte', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Dialecte()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dialecte', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dialecteId: 1,
          dialecte: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dialecte', () => {
      const patchObject = Object.assign(
        {
          dialecte: 'BBBBBB',
        },
        new Dialecte()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dialecte', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dialecteId: 1,
          dialecte: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dialecte', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDialecteToCollectionIfMissing', () => {
      it('should add a Dialecte to an empty array', () => {
        const dialecte: IDialecte = { id: 123 };
        expectedResult = service.addDialecteToCollectionIfMissing([], dialecte);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dialecte);
      });

      it('should not add a Dialecte to an array that contains it', () => {
        const dialecte: IDialecte = { id: 123 };
        const dialecteCollection: IDialecte[] = [
          {
            ...dialecte,
          },
          { id: 456 },
        ];
        expectedResult = service.addDialecteToCollectionIfMissing(dialecteCollection, dialecte);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dialecte to an array that doesn't contain it", () => {
        const dialecte: IDialecte = { id: 123 };
        const dialecteCollection: IDialecte[] = [{ id: 456 }];
        expectedResult = service.addDialecteToCollectionIfMissing(dialecteCollection, dialecte);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dialecte);
      });

      it('should add only unique Dialecte to an array', () => {
        const dialecteArray: IDialecte[] = [{ id: 123 }, { id: 456 }, { id: 32723 }];
        const dialecteCollection: IDialecte[] = [{ id: 123 }];
        expectedResult = service.addDialecteToCollectionIfMissing(dialecteCollection, ...dialecteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dialecte: IDialecte = { id: 123 };
        const dialecte2: IDialecte = { id: 456 };
        expectedResult = service.addDialecteToCollectionIfMissing([], dialecte, dialecte2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dialecte);
        expect(expectedResult).toContain(dialecte2);
      });

      it('should accept null and undefined values', () => {
        const dialecte: IDialecte = { id: 123 };
        expectedResult = service.addDialecteToCollectionIfMissing([], null, dialecte, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dialecte);
      });

      it('should return initial array if no Dialecte is added', () => {
        const dialecteCollection: IDialecte[] = [{ id: 123 }];
        expectedResult = service.addDialecteToCollectionIfMissing(dialecteCollection, undefined, null);
        expect(expectedResult).toEqual(dialecteCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
