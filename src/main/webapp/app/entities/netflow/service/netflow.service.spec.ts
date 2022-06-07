import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { INetflow, Netflow } from '../netflow.model';

import { NetflowService } from './netflow.service';

describe('Netflow Service', () => {
  let service: NetflowService;
  let httpMock: HttpTestingController;
  let elemDefault: INetflow;
  let expectedResult: INetflow | INetflow[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NetflowService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateFirstSeen: currentDate,
      timeFirstSeen: 'AAAAAAA',
      duration: 0,
      protocol: 'AAAAAAA',
      srcIp: 'AAAAAAA',
      dstIp: 'AAAAAAA',
      flags: 'AAAAAAA',
      tos: 0,
      packetNo: 0,
      bytes: 'AAAAAAA',
      pps: 'AAAAAAA',
      bps: 'AAAAAAA',
      bpp: 'AAAAAAA',
      flows: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateFirstSeen: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Netflow', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateFirstSeen: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFirstSeen: currentDate,
        },
        returnedFromService
      );

      service.create(new Netflow()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Netflow', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateFirstSeen: currentDate.format(DATE_FORMAT),
          timeFirstSeen: 'BBBBBB',
          duration: 1,
          protocol: 'BBBBBB',
          srcIp: 'BBBBBB',
          dstIp: 'BBBBBB',
          flags: 'BBBBBB',
          tos: 1,
          packetNo: 1,
          bytes: 'BBBBBB',
          pps: 'BBBBBB',
          bps: 'BBBBBB',
          bpp: 'BBBBBB',
          flows: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFirstSeen: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Netflow', () => {
      const patchObject = Object.assign(
        {
          dateFirstSeen: currentDate.format(DATE_FORMAT),
          timeFirstSeen: 'BBBBBB',
          protocol: 'BBBBBB',
          srcIp: 'BBBBBB',
          tos: 1,
          packetNo: 1,
          bytes: 'BBBBBB',
          pps: 'BBBBBB',
          bps: 'BBBBBB',
        },
        new Netflow()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateFirstSeen: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Netflow', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateFirstSeen: currentDate.format(DATE_FORMAT),
          timeFirstSeen: 'BBBBBB',
          duration: 1,
          protocol: 'BBBBBB',
          srcIp: 'BBBBBB',
          dstIp: 'BBBBBB',
          flags: 'BBBBBB',
          tos: 1,
          packetNo: 1,
          bytes: 'BBBBBB',
          pps: 'BBBBBB',
          bps: 'BBBBBB',
          bpp: 'BBBBBB',
          flows: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateFirstSeen: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Netflow', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNetflowToCollectionIfMissing', () => {
      it('should add a Netflow to an empty array', () => {
        const netflow: INetflow = { id: 123 };
        expectedResult = service.addNetflowToCollectionIfMissing([], netflow);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(netflow);
      });

      it('should not add a Netflow to an array that contains it', () => {
        const netflow: INetflow = { id: 123 };
        const netflowCollection: INetflow[] = [
          {
            ...netflow,
          },
          { id: 456 },
        ];
        expectedResult = service.addNetflowToCollectionIfMissing(netflowCollection, netflow);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Netflow to an array that doesn't contain it", () => {
        const netflow: INetflow = { id: 123 };
        const netflowCollection: INetflow[] = [{ id: 456 }];
        expectedResult = service.addNetflowToCollectionIfMissing(netflowCollection, netflow);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(netflow);
      });

      it('should add only unique Netflow to an array', () => {
        const netflowArray: INetflow[] = [{ id: 123 }, { id: 456 }, { id: 62378 }];
        const netflowCollection: INetflow[] = [{ id: 123 }];
        expectedResult = service.addNetflowToCollectionIfMissing(netflowCollection, ...netflowArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const netflow: INetflow = { id: 123 };
        const netflow2: INetflow = { id: 456 };
        expectedResult = service.addNetflowToCollectionIfMissing([], netflow, netflow2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(netflow);
        expect(expectedResult).toContain(netflow2);
      });

      it('should accept null and undefined values', () => {
        const netflow: INetflow = { id: 123 };
        expectedResult = service.addNetflowToCollectionIfMissing([], null, netflow, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(netflow);
      });

      it('should return initial array if no Netflow is added', () => {
        const netflowCollection: INetflow[] = [{ id: 123 }];
        expectedResult = service.addNetflowToCollectionIfMissing(netflowCollection, undefined, null);
        expect(expectedResult).toEqual(netflowCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
