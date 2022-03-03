import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITamazightToLang, TamazightToLang } from '../tamazight-to-lang.model';

import { TamazightToLangService } from './tamazight-to-lang.service';

describe('TamazightToLang Service', () => {
  let service: TamazightToLangService;
  let httpMock: HttpTestingController;
  let elemDefault: ITamazightToLang;
  let expectedResult: ITamazightToLang | ITamazightToLang[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TamazightToLangService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      traductionId: 0,
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

    it('should create a TamazightToLang', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new TamazightToLang()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TamazightToLang', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          traductionId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TamazightToLang', () => {
      const patchObject = Object.assign({}, new TamazightToLang());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TamazightToLang', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          traductionId: 1,
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

    it('should delete a TamazightToLang', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTamazightToLangToCollectionIfMissing', () => {
      it('should add a TamazightToLang to an empty array', () => {
        const tamazightToLang: ITamazightToLang = { id: 123 };
        expectedResult = service.addTamazightToLangToCollectionIfMissing([], tamazightToLang);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tamazightToLang);
      });

      it('should not add a TamazightToLang to an array that contains it', () => {
        const tamazightToLang: ITamazightToLang = { id: 123 };
        const tamazightToLangCollection: ITamazightToLang[] = [
          {
            ...tamazightToLang,
          },
          { id: 456 },
        ];
        expectedResult = service.addTamazightToLangToCollectionIfMissing(tamazightToLangCollection, tamazightToLang);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TamazightToLang to an array that doesn't contain it", () => {
        const tamazightToLang: ITamazightToLang = { id: 123 };
        const tamazightToLangCollection: ITamazightToLang[] = [{ id: 456 }];
        expectedResult = service.addTamazightToLangToCollectionIfMissing(tamazightToLangCollection, tamazightToLang);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tamazightToLang);
      });

      it('should add only unique TamazightToLang to an array', () => {
        const tamazightToLangArray: ITamazightToLang[] = [{ id: 123 }, { id: 456 }, { id: 12697 }];
        const tamazightToLangCollection: ITamazightToLang[] = [{ id: 123 }];
        expectedResult = service.addTamazightToLangToCollectionIfMissing(tamazightToLangCollection, ...tamazightToLangArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tamazightToLang: ITamazightToLang = { id: 123 };
        const tamazightToLang2: ITamazightToLang = { id: 456 };
        expectedResult = service.addTamazightToLangToCollectionIfMissing([], tamazightToLang, tamazightToLang2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tamazightToLang);
        expect(expectedResult).toContain(tamazightToLang2);
      });

      it('should accept null and undefined values', () => {
        const tamazightToLang: ITamazightToLang = { id: 123 };
        expectedResult = service.addTamazightToLangToCollectionIfMissing([], null, tamazightToLang, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tamazightToLang);
      });

      it('should return initial array if no TamazightToLang is added', () => {
        const tamazightToLangCollection: ITamazightToLang[] = [{ id: 123 }];
        expectedResult = service.addTamazightToLangToCollectionIfMissing(tamazightToLangCollection, undefined, null);
        expect(expectedResult).toEqual(tamazightToLangCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
