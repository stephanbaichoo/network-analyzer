import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { TrapService } from '../service/trap.service';
import { ITrap, Trap } from '../trap.model';

import { TrapUpdateComponent } from './trap-update.component';

describe('Trap Management Update Component', () => {
  let comp: TrapUpdateComponent;
  let fixture: ComponentFixture<TrapUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let trapService: TrapService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [TrapUpdateComponent],
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
      .overrideTemplate(TrapUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TrapUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    trapService = TestBed.inject(TrapService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const trap: ITrap = { id: 456 };

      activatedRoute.data = of({ trap });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(trap));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Trap>>();
      const trap = { id: 123 };
      jest.spyOn(trapService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trap });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trap }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(trapService.update).toHaveBeenCalledWith(trap);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Trap>>();
      const trap = new Trap();
      jest.spyOn(trapService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trap });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trap }));
      saveSubject.complete();

      // THEN
      expect(trapService.create).toHaveBeenCalledWith(trap);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Trap>>();
      const trap = { id: 123 };
      jest.spyOn(trapService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trap });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(trapService.update).toHaveBeenCalledWith(trap);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
