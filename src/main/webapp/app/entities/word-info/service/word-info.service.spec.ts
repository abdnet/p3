import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IWordInfo, WordInfo } from '../word-info.model';

import { WordInfoService } from './word-info.service';

describe('WordInfo Service', () => {
  let service: WordInfoService;
  let httpMock: HttpTestingController;
  let elemDefault: IWordInfo;
  let expectedResult: IWordInfo | IWordInfo[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WordInfoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      wordInfoId: 0,
      contributionDate: currentDate,
      validationDate: currentDate,
      etat: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          contributionDate: currentDate.format(DATE_TIME_FORMAT),
          validationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a WordInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          contributionDate: currentDate.format(DATE_TIME_FORMAT),
          validationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          contributionDate: currentDate,
          validationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new WordInfo()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WordInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          wordInfoId: 1,
          contributionDate: currentDate.format(DATE_TIME_FORMAT),
          validationDate: currentDate.format(DATE_TIME_FORMAT),
          etat: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          contributionDate: currentDate,
          validationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WordInfo', () => {
      const patchObject = Object.assign(
        {
          validationDate: currentDate.format(DATE_TIME_FORMAT),
          etat: 1,
        },
        new WordInfo()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          contributionDate: currentDate,
          validationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WordInfo', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          wordInfoId: 1,
          contributionDate: currentDate.format(DATE_TIME_FORMAT),
          validationDate: currentDate.format(DATE_TIME_FORMAT),
          etat: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          contributionDate: currentDate,
          validationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a WordInfo', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addWordInfoToCollectionIfMissing', () => {
      it('should add a WordInfo to an empty array', () => {
        const wordInfo: IWordInfo = { id: 123 };
        expectedResult = service.addWordInfoToCollectionIfMissing([], wordInfo);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wordInfo);
      });

      it('should not add a WordInfo to an array that contains it', () => {
        const wordInfo: IWordInfo = { id: 123 };
        const wordInfoCollection: IWordInfo[] = [
          {
            ...wordInfo,
          },
          { id: 456 },
        ];
        expectedResult = service.addWordInfoToCollectionIfMissing(wordInfoCollection, wordInfo);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WordInfo to an array that doesn't contain it", () => {
        const wordInfo: IWordInfo = { id: 123 };
        const wordInfoCollection: IWordInfo[] = [{ id: 456 }];
        expectedResult = service.addWordInfoToCollectionIfMissing(wordInfoCollection, wordInfo);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wordInfo);
      });

      it('should add only unique WordInfo to an array', () => {
        const wordInfoArray: IWordInfo[] = [{ id: 123 }, { id: 456 }, { id: 31333 }];
        const wordInfoCollection: IWordInfo[] = [{ id: 123 }];
        expectedResult = service.addWordInfoToCollectionIfMissing(wordInfoCollection, ...wordInfoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wordInfo: IWordInfo = { id: 123 };
        const wordInfo2: IWordInfo = { id: 456 };
        expectedResult = service.addWordInfoToCollectionIfMissing([], wordInfo, wordInfo2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wordInfo);
        expect(expectedResult).toContain(wordInfo2);
      });

      it('should accept null and undefined values', () => {
        const wordInfo: IWordInfo = { id: 123 };
        expectedResult = service.addWordInfoToCollectionIfMissing([], null, wordInfo, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wordInfo);
      });

      it('should return initial array if no WordInfo is added', () => {
        const wordInfoCollection: IWordInfo[] = [{ id: 123 }];
        expectedResult = service.addWordInfoToCollectionIfMissing(wordInfoCollection, undefined, null);
        expect(expectedResult).toEqual(wordInfoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
