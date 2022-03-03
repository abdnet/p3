import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { WordTypeDetailComponent } from './word-type-detail.component';

describe('WordType Management Detail Component', () => {
  let comp: WordTypeDetailComponent;
  let fixture: ComponentFixture<WordTypeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WordTypeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ wordType: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(WordTypeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WordTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load wordType on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.wordType).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
