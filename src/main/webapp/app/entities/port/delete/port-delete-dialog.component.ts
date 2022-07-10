import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPort } from '../port.model';
import { PortService } from '../service/port.service';

@Component({
  templateUrl: './port-delete-dialog.component.html',
})
export class PortDeleteDialogComponent {
  port?: IPort;

  constructor(protected portService: PortService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.portService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
