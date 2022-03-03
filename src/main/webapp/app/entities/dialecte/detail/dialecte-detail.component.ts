import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDialecte } from '../dialecte.model';

@Component({
  selector: 'jhi-dialecte-detail',
  templateUrl: './dialecte-detail.component.html',
})
export class DialecteDetailComponent implements OnInit {
  dialecte: IDialecte | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dialecte }) => {
      this.dialecte = dialecte;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
