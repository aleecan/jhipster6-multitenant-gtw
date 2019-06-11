import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { GtwSharedLibsModule, GtwSharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [GtwSharedLibsModule, GtwSharedCommonModule],
  declarations: [HasAnyAuthorityDirective],
  exports: [GtwSharedCommonModule, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GtwSharedModule {
  static forRoot() {
    return {
      ngModule: GtwSharedModule
    };
  }
}
