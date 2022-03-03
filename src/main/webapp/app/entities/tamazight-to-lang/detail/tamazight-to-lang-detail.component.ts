import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITamazightToLang } from '../tamazight-to-lang.model';

@Component({
  selector: 'jhi-tamazight-to-lang-detail',
  templateUrl: './tamazight-to-lang-detail.component.html',
})
export class TamazightToLangDetailComponent implements OnInit {
  tamazightToLang: ITamazightToLang | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tamazightToLang }) => {
      this.tamazightToLang = tamazightToLang;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
