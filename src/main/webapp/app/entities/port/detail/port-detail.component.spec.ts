import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PortDetailComponent } from './port-detail.component';

describe('Port Management Detail Component', () => {
  let comp: PortDetailComponent;
  let fixture: ComponentFixture<PortDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PortDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ port: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PortDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PortDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load port on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.port).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
