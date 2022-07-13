import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHost, Host } from '../host.model';

import { HostService } from './host.service';

describe('Host Service', () => {
  let service: HostService;
  let httpMock: HttpTestingController;
  let elemDefault: IHost;
  let expectedResult: IHost | IHost[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HostService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      hostName: 'AAAAAAA',
      ipAddress: 'AAAAAAA',
      asname: 'AAAAAAA',
      org: 'AAAAAAA',
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

    it('should create a Host', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Host()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Host', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          hostName: 'BBBBBB',
          ipAddress: 'BBBBBB',
          asname: 'BBBBBB',
          org: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Host', () => {
      const patchObject = Object.assign(
        {
          hostName: 'BBBBBB',
          asname: 'BBBBBB',
          org: 'BBBBBB',
        },
        new Host()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Host', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          hostName: 'BBBBBB',
          ipAddress: 'BBBBBB',
          asname: 'BBBBBB',
          org: 'BBBBBB',
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

    it('should delete a Host', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHostToCollectionIfMissing', () => {
      it('should add a Host to an empty array', () => {
        const host: IHost = { id: 123 };
        expectedResult = service.addHostToCollectionIfMissing([], host);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(host);
      });

      it('should not add a Host to an array that contains it', () => {
        const host: IHost = { id: 123 };
        const hostCollection: IHost[] = [
          {
            ...host,
          },
          { id: 456 },
        ];
        expectedResult = service.addHostToCollectionIfMissing(hostCollection, host);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Host to an array that doesn't contain it", () => {
        const host: IHost = { id: 123 };
        const hostCollection: IHost[] = [{ id: 456 }];
        expectedResult = service.addHostToCollectionIfMissing(hostCollection, host);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(host);
      });

      it('should add only unique Host to an array', () => {
        const hostArray: IHost[] = [{ id: 123 }, { id: 456 }, { id: 19545 }];
        const hostCollection: IHost[] = [{ id: 123 }];
        expectedResult = service.addHostToCollectionIfMissing(hostCollection, ...hostArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const host: IHost = { id: 123 };
        const host2: IHost = { id: 456 };
        expectedResult = service.addHostToCollectionIfMissing([], host, host2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(host);
        expect(expectedResult).toContain(host2);
      });

      it('should accept null and undefined values', () => {
        const host: IHost = { id: 123 };
        expectedResult = service.addHostToCollectionIfMissing([], null, host, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(host);
      });

      it('should return initial array if no Host is added', () => {
        const hostCollection: IHost[] = [{ id: 123 }];
        expectedResult = service.addHostToCollectionIfMissing(hostCollection, undefined, null);
        expect(expectedResult).toEqual(hostCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
