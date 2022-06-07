import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NetflowComponent } from './list/netflow.component';
import { NetflowDetailComponent } from './detail/netflow-detail.component';
import { NetflowUpdateComponent } from './update/netflow-update.component';
import { NetflowDeleteDialogComponent } from './delete/netflow-delete-dialog.component';
import { NetflowRoutingModule } from './route/netflow-routing.module';

@NgModule({
  imports: [SharedModule, NetflowRoutingModule],
  declarations: [NetflowComponent, NetflowDetailComponent, NetflowUpdateComponent, NetflowDeleteDialogComponent],
  entryComponents: [NetflowDeleteDialogComponent],
})
export class NetflowModule {}
