import { Component, signal } from '@angular/core';
import { Subject } from 'rxjs';

export type MenuItem = {
  icon: string,
  label: string,
  route?: any
}

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {

  menuItems = signal<MenuItem[]>([
    {
      icon: 'dashboard',
      label: 'Dashboard',
      route: 'dashboard'
    },
    {
      icon: 'work',
      label: 'Job',
      route: 'jobs'
    },
    {
      icon: 'groups',
      label: 'Candidate',
      route: 'candidates'
    },
    {
      icon: 'forum',
      label: 'Interview',
      route: 'interviews'
    },
    {
      icon: 'description',
      label: 'Offer',
      route: 'offers'
    },
    {
      icon: 'person',
      label: 'User',
      route: 'users'
    },
  ]);
}