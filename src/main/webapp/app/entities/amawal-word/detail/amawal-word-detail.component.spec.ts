import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AmawalWordDetailComponent } from './amawal-word-detail.component';

describe('AmawalWord Management Detail Component', () => {
  let comp: AmawalWordDetailComponent;
  let fixture: ComponentFixture<AmawalWordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AmawalWordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ amawalWord: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AmawalWordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AmawalWordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load amawalWord on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.amawalWord).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
