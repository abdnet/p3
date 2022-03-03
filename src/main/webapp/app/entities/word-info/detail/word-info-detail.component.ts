import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWordInfo } from '../word-info.model';

@Component({
  selector: 'jhi-word-info-detail',
  templateUrl: './word-info-detail.component.html',
})
export class WordInfoDetailComponent implements OnInit {
  wordInfo: IWordInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wordInfo }) => {
      this.wordInfo = wordInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
