import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DialecteDetailComponent } from './dialecte-detail.component';

describe('Dialecte Management Detail Component', () => {
  let comp: DialecteDetailComponent;
  let fixture: ComponentFixture<DialecteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DialecteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dialecte: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DialecteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DialecteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dialecte on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dialecte).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
