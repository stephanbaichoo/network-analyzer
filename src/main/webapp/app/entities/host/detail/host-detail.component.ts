import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHost } from '../host.model';

@Component({
  selector: 'jhi-host-detail',
  templateUrl: './host-detail.component.html',
})
export class HostDetailComponent implements OnInit {
  host: IHost | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ host }) => {
      this.host = host;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
