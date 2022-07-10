import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPort, Port } from '../port.model';
import { PortService } from '../service/port.service';

@Component({
  selector: 'jhi-port-update',
  templateUrl: './port-update.component.html',
})
export class PortUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    port: [],
    isTCP: [],
    isUDP: [],
    isSCTP: [],
    description: [],
    name: [],
  });

  constructor(protected portService: PortService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ port }) => {
      this.updateForm(port);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const port = this.createFromForm();
    if (port.id !== undefined) {
      this.subscribeToSaveResponse(this.portService.update(port));
    } else {
      this.subscribeToSaveResponse(this.portService.create(port));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPort>>): void {
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

  protected updateForm(port: IPort): void {
    this.editForm.patchValue({
      id: port.id,
      port: port.port,
      isTCP: port.isTCP,
      isUDP: port.isUDP,
      isSCTP: port.isSCTP,
      description: port.description,
      name: port.name,
    });
  }

  protected createFromForm(): IPort {
    return {
      ...new Port(),
      id: this.editForm.get(['id'])!.value,
      port: this.editForm.get(['port'])!.value,
      isTCP: this.editForm.get(['isTCP'])!.value,
      isUDP: this.editForm.get(['isUDP'])!.value,
      isSCTP: this.editForm.get(['isSCTP'])!.value,
      description: this.editForm.get(['description'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
