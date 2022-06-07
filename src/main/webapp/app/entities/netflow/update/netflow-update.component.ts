import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INetflow, Netflow } from '../netflow.model';
import { NetflowService } from '../service/netflow.service';

@Component({
  selector: 'jhi-netflow-update',
  templateUrl: './netflow-update.component.html',
})
export class NetflowUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dateFirstSeen: [],
    timeFirstSeen: [],
    duration: [],
    protocol: [],
    srcIp: [],
    dstIp: [],
    flags: [],
    tos: [],
    packetNo: [],
    bytes: [],
    pps: [],
    bps: [],
    bpp: [],
    flows: [],
  });

  constructor(protected netflowService: NetflowService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ netflow }) => {
      this.updateForm(netflow);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const netflow = this.createFromForm();
    if (netflow.id !== undefined) {
      this.subscribeToSaveResponse(this.netflowService.update(netflow));
    } else {
      this.subscribeToSaveResponse(this.netflowService.create(netflow));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INetflow>>): void {
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

  protected updateForm(netflow: INetflow): void {
    this.editForm.patchValue({
      id: netflow.id,
      dateFirstSeen: netflow.dateFirstSeen,
      timeFirstSeen: netflow.timeFirstSeen,
      duration: netflow.duration,
      protocol: netflow.protocol,
      srcIp: netflow.srcIp,
      dstIp: netflow.dstIp,
      flags: netflow.flags,
      tos: netflow.tos,
      packetNo: netflow.packetNo,
      bytes: netflow.bytes,
      pps: netflow.pps,
      bps: netflow.bps,
      bpp: netflow.bpp,
      flows: netflow.flows,
    });
  }

  protected createFromForm(): INetflow {
    return {
      ...new Netflow(),
      id: this.editForm.get(['id'])!.value,
      dateFirstSeen: this.editForm.get(['dateFirstSeen'])!.value,
      timeFirstSeen: this.editForm.get(['timeFirstSeen'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      protocol: this.editForm.get(['protocol'])!.value,
      srcIp: this.editForm.get(['srcIp'])!.value,
      dstIp: this.editForm.get(['dstIp'])!.value,
      flags: this.editForm.get(['flags'])!.value,
      tos: this.editForm.get(['tos'])!.value,
      packetNo: this.editForm.get(['packetNo'])!.value,
      bytes: this.editForm.get(['bytes'])!.value,
      pps: this.editForm.get(['pps'])!.value,
      bps: this.editForm.get(['bps'])!.value,
      bpp: this.editForm.get(['bpp'])!.value,
      flows: this.editForm.get(['flows'])!.value,
    };
  }
}
