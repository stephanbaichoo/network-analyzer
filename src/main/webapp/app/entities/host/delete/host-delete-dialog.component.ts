import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHost } from '../host.model';
import { HostService } from '../service/host.service';

@Component({
  templateUrl: './host-delete-dialog.component.html',
})
export class HostDeleteDialogComponent {
  host?: IHost;

  constructor(protected hostService: HostService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hostService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
