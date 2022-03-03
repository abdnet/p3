import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WordInfoDetailComponent } from './word-info-detail.component';

describe('WordInfo Management Detail Component', () => {
  let comp: WordInfoDetailComponent;
  let fixture: ComponentFixture<WordInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WordInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ wordInfo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WordInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WordInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load wordInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.wordInfo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
