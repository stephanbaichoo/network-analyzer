import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NetflowService } from '../service/netflow.service';
import { INetflow, Netflow } from '../netflow.model';

import { NetflowUpdateComponent } from './netflow-update.component';

describe('Netflow Management Update Component', () => {
  let comp: NetflowUpdateComponent;
  let fixture: ComponentFixture<NetflowUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let netflowService: NetflowService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NetflowUpdateComponent],
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
      .overrideTemplate(NetflowUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NetflowUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    netflowService = TestBed.inject(NetflowService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const netflow: INetflow = { id: 456 };

      activatedRoute.data = of({ netflow });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(netflow));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Netflow>>();
      const netflow = { id: 123 };
      jest.spyOn(netflowService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ netflow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: netflow }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(netflowService.update).toHaveBeenCalledWith(netflow);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Netflow>>();
      const netflow = new Netflow();
      jest.spyOn(netflowService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ netflow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: netflow }));
      saveSubject.complete();

      // THEN
      expect(netflowService.create).toHaveBeenCalledWith(netflow);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Netflow>>();
      const netflow = { id: 123 };
      jest.spyOn(netflowService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ netflow });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(netflowService.update).toHaveBeenCalledWith(netflow);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
