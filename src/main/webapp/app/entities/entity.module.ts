import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'region',
        loadChildren: './srv/region/region.module#SrvRegionModule'
      },
      {
        path: 'country',
        loadChildren: './srv/country/country.module#SrvCountryModule'
      },
      {
        path: 'location',
        loadChildren: './srv/location/location.module#SrvLocationModule'
      },
      {
        path: 'department',
        loadChildren: './srv/department/department.module#SrvDepartmentModule'
      },
      {
        path: 'task',
        loadChildren: './srv/task/task.module#SrvTaskModule'
      },
      {
        path: 'employee',
        loadChildren: './srv/employee/employee.module#SrvEmployeeModule'
      },
      {
        path: 'job',
        loadChildren: './srv/job/job.module#SrvJobModule'
      },
      {
        path: 'job-history',
        loadChildren: './srv/job-history/job-history.module#SrvJobHistoryModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GtwEntityModule {}
