import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HostComponent } from './list/host.component';
import { HostDetailComponent } from './detail/host-detail.component';
import { HostUpdateComponent } from './update/host-update.component';
import { HostDeleteDialogComponent } from './delete/host-delete-dialog.component';
import { HostRoutingModule } from './route/host-routing.module';

@NgModule({
  imports: [SharedModule, HostRoutingModule],
  declarations: [HostComponent, HostDetailComponent, HostUpdateComponent, HostDeleteDialogComponent],
  entryComponents: [HostDeleteDialogComponent],
})
export class HostModule {}
