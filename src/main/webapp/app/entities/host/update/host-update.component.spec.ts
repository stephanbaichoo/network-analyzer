import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HostService } from '../service/host.service';
import { IHost, Host } from '../host.model';

import { HostUpdateComponent } from './host-update.component';

describe('Host Management Update Component', () => {
  let comp: HostUpdateComponent;
  let fixture: ComponentFixture<HostUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let hostService: HostService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HostUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(HostUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HostUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    hostService = TestBed.inject(HostService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const host: IHost = { id: 456 };

      activatedRoute.data = of({ host });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(host));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Host>>();
      const host = { id: 123 };
      jest.spyOn(hostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ host });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: host }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(hostService.update).toHaveBeenCalledWith(host);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Host>>();
      const host = new Host();
      jest.spyOn(hostService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ host });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: host }));
      saveSubject.complete();

      // THEN
      expect(hostService.create).toHaveBeenCalledWith(host);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Host>>();
      const host = { id: 123 };
      jest.spyOn(hostService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ host });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(hostService.update).toHaveBeenCalledWith(host);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
