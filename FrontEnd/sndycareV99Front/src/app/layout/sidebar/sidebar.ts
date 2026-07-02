import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/service/auth.service';
import { CommonModule } from '@angular/common';

interface NavItem {
  label: string;
  icon: string;
  route: string;
  group: string;
  badge?: number;
  children?: { label: string; route: string }[];
}

@Component({
  selector: 'app-sidebar',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, CommonModule],
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.scss'],
})
export class Sidebar {

    residence = 'Aghattas';
    user: any;
    groups = ['Main', 'People', 'Finance', 'Platform'];
    navItems: NavItem[] = [
      {
        label: 'Dashboard',
        icon: 'ti-layout-dashboard',
        route: '/dashboard',
        group: 'Main'
      },
      {
        label: 'Residences',
        icon: 'ti-building-community',
        route: '/residence',
        group: 'Main',
        children: [
          { label: 'All Residences', route: '/residence/list' },
          { label: 'Add Residence', route: '/residence/add' }
        ]
      },
      {
        label: 'Buildings',
        icon: 'ti-building',
        route: '/buildings',
        group: 'Main',
        children: [
          { label: 'All Buildings', route: '/buildings/list' },
          { label: 'Add Building', route: '/buildings/add' }
        ]
      },
      {
        label: 'Apartments',
        icon: 'ti-home',
        route: '/apartments',
        group: 'Main'
      },
      {
        label: 'Residents',
        icon: 'ti-users',
        route: '/residents',
        group: 'People'
      },
      {
        label: 'Owners',
        icon: 'ti-user',
        route: '/owners',
        group: 'People'
      },
      {
        label: 'Profile',
        icon: 'ti-user-circle',
        route: '/profile',
        group: 'People'
      },
      {
        label: 'Accounts',
        icon: 'ti-receipt',
        route: '/accounts',
        group: 'Finance',
        children: [
          { label: 'Income', route: '/accounts/income' },
          { label: 'Expenses', route: '/accounts/expenses' },
          { label: 'Transactions', route: '/accounts/transactions' }
        ]
      },
      {
        label: 'Invoices',
        icon: 'ti-file-text',
        route: '/invoices',
        group: 'Finance'
      },
      {
        label: 'Payments',
        icon: 'ti-cash',
        route: '/payments',
        group: 'Finance'
      },
      {
        label: 'Maintenance',
        icon: 'ti-tools',
        route: '/maintenance',
        group: 'Platform'
      },
      {
        label: 'Reports',
        icon: 'ti-chart-bar',
        route: '/reports',
        group: 'Platform'
      },
      {
        label: 'Notifications',
        icon: 'ti-bell',
        route: '/notifications',
        group: 'Platform'
      },
      {
        label: 'Settings',
        icon: 'ti-settings',
        route: '/settings',
        group: 'Platform'
      }
    ];
  
    constructor(private auth: AuthService) {
      this.user = this.auth.getCurrentUser();
    }
  
    getItems(group: string) {
      return this.navItems.filter(item => item.group === group);
    }

    getResidence(){
      return this.residence
    }
  
    initials() {
      const u = this.user;
      if (!u) return '??';
      return (u.username.charAt(0) + (u.username.charAt(1) || '')).toUpperCase();
    }
  
    logout() {
      localStorage.clear();
    }


}
