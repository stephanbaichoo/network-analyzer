import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INetflow } from '../netflow.model';
import { NetflowService } from '../service/netflow.service';

@Component({
  templateUrl: './netflow-delete-dialog.component.html',
})
export class NetflowDeleteDialogComponent {
  netflow?: INetflow;

  constructor(protected netflowService: NetflowService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.netflowService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
