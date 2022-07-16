import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrap } from '../trap.model';
import { TrapService } from '../service/trap.service';

@Component({
  templateUrl: './trap-delete-dialog.component.html',
})
export class TrapDeleteDialogComponent {
  trap?: ITrap;

  constructor(protected trapService: TrapService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trapService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
