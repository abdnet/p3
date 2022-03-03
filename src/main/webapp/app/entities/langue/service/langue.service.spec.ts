import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILangue, Langue } from '../langue.model';

import { LangueService } from './langue.service';

describe('Langue Service', () => {
  let service: LangueService;
  let httpMock: HttpTestingController;
  let elemDefault: ILangue;
  let expectedResult: ILangue | ILangue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LangueService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      langueId: 0,
      langue: 'AAAAAAA',
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

    it('should create a Langue', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Langue()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Langue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          langueId: 1,
          langue: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Langue', () => {
      const patchObject = Object.assign(
        {
          langue: 'BBBBBB',
        },
        new Langue()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Langue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          langueId: 1,
          langue: 'BBBBBB',
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

    it('should delete a Langue', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addLangueToCollectionIfMissing', () => {
      it('should add a Langue to an empty array', () => {
        const langue: ILangue = { id: 123 };
        expectedResult = service.addLangueToCollectionIfMissing([], langue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(langue);
      });

      it('should not add a Langue to an array that contains it', () => {
        const langue: ILangue = { id: 123 };
        const langueCollection: ILangue[] = [
          {
            ...langue,
          },
          { id: 456 },
        ];
        expectedResult = service.addLangueToCollectionIfMissing(langueCollection, langue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Langue to an array that doesn't contain it", () => {
        const langue: ILangue = { id: 123 };
        const langueCollection: ILangue[] = [{ id: 456 }];
        expectedResult = service.addLangueToCollectionIfMissing(langueCollection, langue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(langue);
      });

      it('should add only unique Langue to an array', () => {
        const langueArray: ILangue[] = [{ id: 123 }, { id: 456 }, { id: 42336 }];
        const langueCollection: ILangue[] = [{ id: 123 }];
        expectedResult = service.addLangueToCollectionIfMissing(langueCollection, ...langueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const langue: ILangue = { id: 123 };
        const langue2: ILangue = { id: 456 };
        expectedResult = service.addLangueToCollectionIfMissing([], langue, langue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(langue);
        expect(expectedResult).toContain(langue2);
      });

      it('should accept null and undefined values', () => {
        const langue: ILangue = { id: 123 };
        expectedResult = service.addLangueToCollectionIfMissing([], null, langue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(langue);
      });

      it('should return initial array if no Langue is added', () => {
        const langueCollection: ILangue[] = [{ id: 123 }];
        expectedResult = service.addLangueToCollectionIfMissing(langueCollection, undefined, null);
        expect(expectedResult).toEqual(langueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
