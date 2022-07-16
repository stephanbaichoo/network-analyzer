import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITrap, Trap } from '../trap.model';
import { TrapService } from '../service/trap.service';

@Component({
  selector: 'jhi-trap-update',
  templateUrl: './trap-update.component.html',
})
export class TrapUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    date: [],
    time: [],
    trap: [],
    values: [],
    trigger: [],
  });

  constructor(protected trapService: TrapService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trap }) => {
      this.updateForm(trap);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trap = this.createFromForm();
    if (trap.id !== undefined) {
      this.subscribeToSaveResponse(this.trapService.update(trap));
    } else {
      this.subscribeToSaveResponse(this.trapService.create(trap));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrap>>): void {
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

  protected updateForm(trap: ITrap): void {
    this.editForm.patchValue({
      id: trap.id,
      date: trap.date,
      time: trap.time,
      trap: trap.trap,
      values: trap.values,
      trigger: trap.trigger,
    });
  }

  protected createFromForm(): ITrap {
    return {
      ...new Trap(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      time: this.editForm.get(['time'])!.value,
      trap: this.editForm.get(['trap'])!.value,
      values: this.editForm.get(['values'])!.value,
      trigger: this.editForm.get(['trigger'])!.value,
    };
  }
}
