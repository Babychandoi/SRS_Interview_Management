import { Component, OnInit } from '@angular/core';
import { SidebarService } from './sidebar.service';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  styleUrls: ['./admin-layout.component.css']
})
export class AdminLayoutComponent implements OnInit {

  constructor(private sidebarService: SidebarService) { }

  ngOnInit(): void {

  }

  toggleSidebar() {
    return this.sidebarService.sideNavWidth();
  }
}
