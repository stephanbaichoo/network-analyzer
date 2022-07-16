import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrapComponent } from './list/trap.component';
import { TrapDetailComponent } from './detail/trap-detail.component';
import { TrapUpdateComponent } from './update/trap-update.component';
import { TrapDeleteDialogComponent } from './delete/trap-delete-dialog.component';
import { TrapRoutingModule } from './route/trap-routing.module';

@NgModule({
  imports: [SharedModule, TrapRoutingModule],
  declarations: [TrapComponent, TrapDetailComponent, TrapUpdateComponent, TrapDeleteDialogComponent],
  entryComponents: [TrapDeleteDialogComponent],
})
export class TrapModule {}
