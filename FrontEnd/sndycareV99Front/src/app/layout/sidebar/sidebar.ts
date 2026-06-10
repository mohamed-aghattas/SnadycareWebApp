import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { AuthService } from '../../core/service/auth.service';
import { CommonModule } from '@angular/common';
import { LoadingSpinnerComponent } from '../../shared/components/loading-spinner/loading-spinner';

@Component({
  selector: 'app-sidebar',
  imports: [RouterOutlet,RouterLink,CommonModule,LoadingSpinnerComponent],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss',
})
export class Sidebar {

    residence = "Aghattas"
    user :any;
    groups = ['Main','Finance','Governance'];
    navItems = [
      {label:'Dashboard',  icon:'ti-layout-dashboard', route:'/dashboard', group:'Main'},
      {label:'Buildings',  icon:'ti-building',          route:'/buildings',  group:'Main'},
      {label:'Owners',     icon:'ti-users',             route:'/owners',     group:'Main'},
      {label:'Tenants',    icon:'ti-user',              route:'/tenants',    group:'Main'},
      {label:'Expenses',   icon:'ti-receipt',           route:'/expenses',   group:'Finance'},
      {label:'Payments',   icon:'ti-cash',              route:'/payments',   group:'Finance', badge:3},
      {label:'Meetings',   icon:'ti-calendar-event',    route:'/meetings',   group:'Governance'},
      {label:'Council',    icon:'ti-shield',            route:'/council',    group:'Governance'},
      {label:'Syndic',     icon:'ti-briefcase',         route:'/syndic',     group:'Governance'},
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
