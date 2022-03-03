import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { WordTypeService } from '../service/word-type.service';

import { WordTypeComponent } from './word-type.component';

describe('WordType Management Component', () => {
  let comp: WordTypeComponent;
  let fixture: ComponentFixture<WordTypeComponent>;
  let service: WordTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [WordTypeComponent],
    })
      .overrideTemplate(WordTypeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WordTypeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(WordTypeService);

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
    expect(comp.wordTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
