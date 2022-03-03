import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { WordInfoService } from '../service/word-info.service';

import { WordInfoComponent } from './word-info.component';

describe('WordInfo Management Component', () => {
  let comp: WordInfoComponent;
  let fixture: ComponentFixture<WordInfoComponent>;
  let service: WordInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WordInfoComponent],
    })
      .overrideTemplate(WordInfoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WordInfoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(WordInfoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.wordInfos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
