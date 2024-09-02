import { Component, OnInit, signal } from '@angular/core';
import { SidebarService } from '../admin-layout/sidebar.service';
import { AuthService } from 'src/app/routes/session-management/auth.service';
import { take } from 'rxjs';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  fullName: string = '';
  timedOutCloser: any;

  constructor(private sidebarService: SidebarService,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.authService.currentUser$.pipe(take(1)).subscribe(user => {
      this.fullName = user?.full_name || '';
    });
  }

  toggleSidebarBtn() {
    this.sidebarService.toggleSidenav();
  }

  editProfile() {

  }

  logout() {
    this.authService.logout().subscribe({
      next: () => {
        this.authService.clearUser();
        window.location.href = '/login';
      }
    });
  }
}
