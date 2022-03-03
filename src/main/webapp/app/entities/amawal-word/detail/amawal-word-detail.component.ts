import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAmawalWord } from '../amawal-word.model';

@Component({
  selector: 'jhi-amawal-word-detail',
  templateUrl: './amawal-word-detail.component.html',
})
export class AmawalWordDetailComponent implements OnInit {
  amawalWord: IAmawalWord | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ amawalWord }) => {
      this.amawalWord = amawalWord;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
