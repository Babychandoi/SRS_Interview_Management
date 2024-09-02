import { computed, Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SidebarService {
  sideNavCollapsed = signal(false);
  sideNavWidth = computed(() => this.sideNavCollapsed() ? '65px' : '250px');

  toggleSidenav() {
    this.sideNavCollapsed.set(!this.sideNavCollapsed());
  }
}
