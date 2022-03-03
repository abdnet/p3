import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TamazightToLangDetailComponent } from './tamazight-to-lang-detail.component';

describe('TamazightToLang Management Detail Component', () => {
  let comp: TamazightToLangDetailComponent;
  let fixture: ComponentFixture<TamazightToLangDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TamazightToLangDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ tamazightToLang: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(TamazightToLangDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TamazightToLangDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load tamazightToLang on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.tamazightToLang).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
