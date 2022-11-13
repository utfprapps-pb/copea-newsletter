import { MatDrawer, MatDrawerContainer } from '@angular/material/sidenav';
import { Injectable } from '@angular/core';

@Injectable()
export class DrawerService {

  private matDrawer: MatDrawer;
  public matDrawerContainer: MatDrawerContainer;

  public setDrawer(drawer: MatDrawer) {
    this.matDrawer = drawer;
  }

  public get drawer() {
    return this.matDrawer;
  };

}
