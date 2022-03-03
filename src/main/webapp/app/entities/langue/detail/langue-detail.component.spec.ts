import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { LangueDetailComponent } from './langue-detail.component';

describe('Langue Management Detail Component', () => {
  let comp: LangueDetailComponent;
  let fixture: ComponentFixture<LangueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LangueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ langue: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(LangueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LangueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load langue on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.langue).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
