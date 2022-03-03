import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GroupeService } from '../service/groupe.service';

import { GroupeComponent } from './groupe.component';

describe('Groupe Management Component', () => {
  let comp: GroupeComponent;
  let fixture: ComponentFixture<GroupeComponent>;
  let service: GroupeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [GroupeComponent],
    })
      .overrideTemplate(GroupeComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(GroupeComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(GroupeService);

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
    expect(comp.groupes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
