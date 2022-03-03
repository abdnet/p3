import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILangue } from '../langue.model';

@Component({
  selector: 'jhi-langue-detail',
  templateUrl: './langue-detail.component.html',
})
export class LangueDetailComponent implements OnInit {
  langue: ILangue | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ langue }) => {
      this.langue = langue;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
