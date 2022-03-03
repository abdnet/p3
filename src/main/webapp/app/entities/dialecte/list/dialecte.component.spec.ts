import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DialecteService } from '../service/dialecte.service';

import { DialecteComponent } from './dialecte.component';

describe('Dialecte Management Component', () => {
  let comp: DialecteComponent;
  let fixture: ComponentFixture<DialecteComponent>;
  let service: DialecteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DialecteComponent],
    })
      .overrideTemplate(DialecteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DialecteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DialecteService);

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
    expect(comp.dialectes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
