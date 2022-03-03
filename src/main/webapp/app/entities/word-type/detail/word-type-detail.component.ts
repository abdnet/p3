import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWordType } from '../word-type.model';

@Component({
  selector: 'jhi-word-type-detail',
  templateUrl: './word-type-detail.component.html',
})
export class WordTypeDetailComponent implements OnInit {
  wordType: IWordType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wordType }) => {
      this.wordType = wordType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
