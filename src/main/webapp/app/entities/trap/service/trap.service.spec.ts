import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ITrap, Trap } from '../trap.model';

import { TrapService } from './trap.service';

describe('Trap Service', () => {
  let service: TrapService;
  let httpMock: HttpTestingController;
  let elemDefault: ITrap;
  let expectedResult: ITrap | ITrap[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TrapService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      time: 'AAAAAAA',
      trap: 'AAAAAAA',
      values: 'AAAAAAA',
      trigger: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Trap', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new Trap()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Trap', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          time: 'BBBBBB',
          trap: 'BBBBBB',
          values: 'BBBBBB',
          trigger: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Trap', () => {
      const patchObject = Object.assign(
        {
          time: 'BBBBBB',
          trap: 'BBBBBB',
          values: 'BBBBBB',
        },
        new Trap()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Trap', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          time: 'BBBBBB',
          trap: 'BBBBBB',
          values: 'BBBBBB',
          trigger: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Trap', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addTrapToCollectionIfMissing', () => {
      it('should add a Trap to an empty array', () => {
        const trap: ITrap = { id: 123 };
        expectedResult = service.addTrapToCollectionIfMissing([], trap);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trap);
      });

      it('should not add a Trap to an array that contains it', () => {
        const trap: ITrap = { id: 123 };
        const trapCollection: ITrap[] = [
          {
            ...trap,
          },
          { id: 456 },
        ];
        expectedResult = service.addTrapToCollectionIfMissing(trapCollection, trap);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Trap to an array that doesn't contain it", () => {
        const trap: ITrap = { id: 123 };
        const trapCollection: ITrap[] = [{ id: 456 }];
        expectedResult = service.addTrapToCollectionIfMissing(trapCollection, trap);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trap);
      });

      it('should add only unique Trap to an array', () => {
        const trapArray: ITrap[] = [{ id: 123 }, { id: 456 }, { id: 90654 }];
        const trapCollection: ITrap[] = [{ id: 123 }];
        expectedResult = service.addTrapToCollectionIfMissing(trapCollection, ...trapArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const trap: ITrap = { id: 123 };
        const trap2: ITrap = { id: 456 };
        expectedResult = service.addTrapToCollectionIfMissing([], trap, trap2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(trap);
        expect(expectedResult).toContain(trap2);
      });

      it('should accept null and undefined values', () => {
        const trap: ITrap = { id: 123 };
        expectedResult = service.addTrapToCollectionIfMissing([], null, trap, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(trap);
      });

      it('should return initial array if no Trap is added', () => {
        const trapCollection: ITrap[] = [{ id: 123 }];
        expectedResult = service.addTrapToCollectionIfMissing(trapCollection, undefined, null);
        expect(expectedResult).toEqual(trapCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
