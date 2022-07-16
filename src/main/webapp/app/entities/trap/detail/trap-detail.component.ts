import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrap } from '../trap.model';

@Component({
  selector: 'jhi-trap-detail',
  templateUrl: './trap-detail.component.html',
})
export class TrapDetailComponent implements OnInit {
  trap: ITrap | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trap }) => {
      this.trap = trap;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
