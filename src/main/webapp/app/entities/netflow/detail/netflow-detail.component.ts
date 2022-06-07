import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INetflow } from '../netflow.model';

@Component({
  selector: 'jhi-netflow-detail',
  templateUrl: './netflow-detail.component.html',
})
export class NetflowDetailComponent implements OnInit {
  netflow: INetflow | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ netflow }) => {
      this.netflow = netflow;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
