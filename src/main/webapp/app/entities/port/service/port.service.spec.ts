import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPort, Port } from '../port.model';

import { PortService } from './port.service';

describe('Port Service', () => {
  let service: PortService;
  let httpMock: HttpTestingController;
  let elemDefault: IPort;
  let expectedResult: IPort | IPort[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PortService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      port: 0,
      isTCP: 'AAAAAAA',
      isUDP: 'AAAAAAA',
      isSCTP: 'AAAAAAA',
      description: 'AAAAAAA',
      name: 'AAAAAAA',
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

    it('should create a Port', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Port()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Port', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          port: 1,
          isTCP: 'BBBBBB',
          isUDP: 'BBBBBB',
          isSCTP: 'BBBBBB',
          description: 'BBBBBB',
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Port', () => {
      const patchObject = Object.assign({}, new Port());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Port', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          port: 1,
          isTCP: 'BBBBBB',
          isUDP: 'BBBBBB',
          isSCTP: 'BBBBBB',
          description: 'BBBBBB',
          name: 'BBBBBB',
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

    it('should delete a Port', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPortToCollectionIfMissing', () => {
      it('should add a Port to an empty array', () => {
        const port: IPort = { id: 123 };
        expectedResult = service.addPortToCollectionIfMissing([], port);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(port);
      });

      it('should not add a Port to an array that contains it', () => {
        const port: IPort = { id: 123 };
        const portCollection: IPort[] = [
          {
            ...port,
          },
          { id: 456 },
        ];
        expectedResult = service.addPortToCollectionIfMissing(portCollection, port);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Port to an array that doesn't contain it", () => {
        const port: IPort = { id: 123 };
        const portCollection: IPort[] = [{ id: 456 }];
        expectedResult = service.addPortToCollectionIfMissing(portCollection, port);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(port);
      });

      it('should add only unique Port to an array', () => {
        const portArray: IPort[] = [{ id: 123 }, { id: 456 }, { id: 45486 }];
        const portCollection: IPort[] = [{ id: 123 }];
        expectedResult = service.addPortToCollectionIfMissing(portCollection, ...portArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const port: IPort = { id: 123 };
        const port2: IPort = { id: 456 };
        expectedResult = service.addPortToCollectionIfMissing([], port, port2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(port);
        expect(expectedResult).toContain(port2);
      });

      it('should accept null and undefined values', () => {
        const port: IPort = { id: 123 };
        expectedResult = service.addPortToCollectionIfMissing([], null, port, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(port);
      });

      it('should return initial array if no Port is added', () => {
        const portCollection: IPort[] = [{ id: 123 }];
        expectedResult = service.addPortToCollectionIfMissing(portCollection, undefined, null);
        expect(expectedResult).toEqual(portCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
