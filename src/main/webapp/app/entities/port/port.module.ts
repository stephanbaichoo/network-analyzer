import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PortComponent } from './list/port.component';
import { PortDetailComponent } from './detail/port-detail.component';
import { PortUpdateComponent } from './update/port-update.component';
import { PortDeleteDialogComponent } from './delete/port-delete-dialog.component';
import { PortRoutingModule } from './route/port-routing.module';

@NgModule({
  imports: [SharedModule, PortRoutingModule],
  declarations: [PortComponent, PortDetailComponent, PortUpdateComponent, PortDeleteDialogComponent],
  entryComponents: [PortDeleteDialogComponent],
})
export class PortModule {}
