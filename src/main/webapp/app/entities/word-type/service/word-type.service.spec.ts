import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWordType, WordType } from '../word-type.model';

import { WordTypeService } from './word-type.service';

describe('WordType Service', () => {
  let service: WordTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: IWordType;
  let expectedResult: IWordType | IWordType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WordTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      typeId: 0,
      type: 'AAAAAAA',
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

    it('should create a WordType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new WordType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WordType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeId: 1,
          type: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WordType', () => {
      const patchObject = Object.assign(
        {
          typeId: 1,
        },
        new WordType()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WordType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeId: 1,
          type: 'BBBBBB',
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

    it('should delete a WordType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWordTypeToCollectionIfMissing', () => {
      it('should add a WordType to an empty array', () => {
        const wordType: IWordType = { id: 123 };
        expectedResult = service.addWordTypeToCollectionIfMissing([], wordType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wordType);
      });

      it('should not add a WordType to an array that contains it', () => {
        const wordType: IWordType = { id: 123 };
        const wordTypeCollection: IWordType[] = [
          {
            ...wordType,
          },
          { id: 456 },
        ];
        expectedResult = service.addWordTypeToCollectionIfMissing(wordTypeCollection, wordType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WordType to an array that doesn't contain it", () => {
        const wordType: IWordType = { id: 123 };
        const wordTypeCollection: IWordType[] = [{ id: 456 }];
        expectedResult = service.addWordTypeToCollectionIfMissing(wordTypeCollection, wordType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wordType);
      });

      it('should add only unique WordType to an array', () => {
        const wordTypeArray: IWordType[] = [{ id: 123 }, { id: 456 }, { id: 56923 }];
        const wordTypeCollection: IWordType[] = [{ id: 123 }];
        expectedResult = service.addWordTypeToCollectionIfMissing(wordTypeCollection, ...wordTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wordType: IWordType = { id: 123 };
        const wordType2: IWordType = { id: 456 };
        expectedResult = service.addWordTypeToCollectionIfMissing([], wordType, wordType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wordType);
        expect(expectedResult).toContain(wordType2);
      });

      it('should accept null and undefined values', () => {
        const wordType: IWordType = { id: 123 };
        expectedResult = service.addWordTypeToCollectionIfMissing([], null, wordType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wordType);
      });

      it('should return initial array if no WordType is added', () => {
        const wordTypeCollection: IWordType[] = [{ id: 123 }];
        expectedResult = service.addWordTypeToCollectionIfMissing(wordTypeCollection, undefined, null);
        expect(expectedResult).toEqual(wordTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
