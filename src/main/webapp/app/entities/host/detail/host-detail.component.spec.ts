import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HostDetailComponent } from './host-detail.component';

describe('Host Management Detail Component', () => {
  let comp: HostDetailComponent;
  let fixture: ComponentFixture<HostDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HostDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ host: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HostDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HostDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load host on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.host).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
