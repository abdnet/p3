import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAmawalWord, AmawalWord } from '../amawal-word.model';

import { AmawalWordService } from './amawal-word.service';

describe('AmawalWord Service', () => {
  let service: AmawalWordService;
  let httpMock: HttpTestingController;
  let elemDefault: IAmawalWord;
  let expectedResult: IAmawalWord | IAmawalWord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AmawalWordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      wordId: 0,
      orthographeTifinagh: 'AAAAAAA',
      orthographeLatin: 'AAAAAAA',
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

    it('should create a AmawalWord', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new AmawalWord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AmawalWord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          wordId: 1,
          orthographeTifinagh: 'BBBBBB',
          orthographeLatin: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AmawalWord', () => {
      const patchObject = Object.assign({}, new AmawalWord());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AmawalWord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          wordId: 1,
          orthographeTifinagh: 'BBBBBB',
          orthographeLatin: 'BBBBBB',
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

    it('should delete a AmawalWord', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAmawalWordToCollectionIfMissing', () => {
      it('should add a AmawalWord to an empty array', () => {
        const amawalWord: IAmawalWord = { id: 123 };
        expectedResult = service.addAmawalWordToCollectionIfMissing([], amawalWord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amawalWord);
      });

      it('should not add a AmawalWord to an array that contains it', () => {
        const amawalWord: IAmawalWord = { id: 123 };
        const amawalWordCollection: IAmawalWord[] = [
          {
            ...amawalWord,
          },
          { id: 456 },
        ];
        expectedResult = service.addAmawalWordToCollectionIfMissing(amawalWordCollection, amawalWord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AmawalWord to an array that doesn't contain it", () => {
        const amawalWord: IAmawalWord = { id: 123 };
        const amawalWordCollection: IAmawalWord[] = [{ id: 456 }];
        expectedResult = service.addAmawalWordToCollectionIfMissing(amawalWordCollection, amawalWord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amawalWord);
      });

      it('should add only unique AmawalWord to an array', () => {
        const amawalWordArray: IAmawalWord[] = [{ id: 123 }, { id: 456 }, { id: 36932 }];
        const amawalWordCollection: IAmawalWord[] = [{ id: 123 }];
        expectedResult = service.addAmawalWordToCollectionIfMissing(amawalWordCollection, ...amawalWordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const amawalWord: IAmawalWord = { id: 123 };
        const amawalWord2: IAmawalWord = { id: 456 };
        expectedResult = service.addAmawalWordToCollectionIfMissing([], amawalWord, amawalWord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(amawalWord);
        expect(expectedResult).toContain(amawalWord2);
      });

      it('should accept null and undefined values', () => {
        const amawalWord: IAmawalWord = { id: 123 };
        expectedResult = service.addAmawalWordToCollectionIfMissing([], null, amawalWord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(amawalWord);
      });

      it('should return initial array if no AmawalWord is added', () => {
        const amawalWordCollection: IAmawalWord[] = [{ id: 123 }];
        expectedResult = service.addAmawalWordToCollectionIfMissing(amawalWordCollection, undefined, null);
        expect(expectedResult).toEqual(amawalWordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
