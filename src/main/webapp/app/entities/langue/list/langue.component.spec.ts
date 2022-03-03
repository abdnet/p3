import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { LangueService } from '../service/langue.service';

import { LangueComponent } from './langue.component';

describe('Langue Management Component', () => {
  let comp: LangueComponent;
  let fixture: ComponentFixture<LangueComponent>;
  let service: LangueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LangueComponent],
    })
      .overrideTemplate(LangueComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LangueComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LangueService);

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
    expect(comp.langues?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
