import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrapDetailComponent } from './trap-detail.component';

describe('Trap Management Detail Component', () => {
  let comp: TrapDetailComponent;
  let fixture: ComponentFixture<TrapDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TrapDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ trap: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TrapDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TrapDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load trap on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.trap).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
