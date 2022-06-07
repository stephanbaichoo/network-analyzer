import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NetflowDetailComponent } from './netflow-detail.component';

describe('Netflow Management Detail Component', () => {
  let comp: NetflowDetailComponent;
  let fixture: ComponentFixture<NetflowDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NetflowDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ netflow: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(NetflowDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(NetflowDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load netflow on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.netflow).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
