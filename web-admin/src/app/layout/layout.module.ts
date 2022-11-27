import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {LayoutComponent } from './layout.component';
import {NzLayoutModule} from "ng-zorro-antd/layout";
import {NzIconModule} from "ng-zorro-antd/icon";
import {NzMenuModule} from "ng-zorro-antd/menu";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {HttpClientModule} from "@angular/common/http";


@NgModule({
  declarations: [
    LayoutComponent
  ],
  exports: [
    LayoutComponent
  ],
  imports: [
    CommonModule,
    NzLayoutModule,
    NzIconModule,
    NzMenuModule,
    BrowserAnimationsModule,
    HttpClientModule
  ]
})
export class LayoutModule { }
