import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHost, Host } from '../host.model';
import { HostService } from '../service/host.service';

@Component({
  selector: 'jhi-host-update',
  templateUrl: './host-update.component.html',
})
export class HostUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    hostName: [],
    ipAddress: [],
    asname: [],
    org: [],
  });

  constructor(protected hostService: HostService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ host }) => {
      this.updateForm(host);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const host = this.createFromForm();
    if (host.id !== undefined) {
      this.subscribeToSaveResponse(this.hostService.update(host));
    } else {
      this.subscribeToSaveResponse(this.hostService.create(host));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHost>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(host: IHost): void {
    this.editForm.patchValue({
      id: host.id,
      hostName: host.hostName,
      ipAddress: host.ipAddress,
      asname: host.asname,
      org: host.org,
    });
  }

  protected createFromForm(): IHost {
    return {
      ...new Host(),
      id: this.editForm.get(['id'])!.value,
      hostName: this.editForm.get(['hostName'])!.value,
      ipAddress: this.editForm.get(['ipAddress'])!.value,
      asname: this.editForm.get(['asname'])!.value,
      org: this.editForm.get(['org'])!.value,
    };
  }
}
