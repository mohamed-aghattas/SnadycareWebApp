import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login.component';
import { authGuard } from './core/guard/auth.guard';

export const routes: Routes = [
  { path: 'login', loadComponent: () => import('./features/auth/login/login.component').then(m => m.Login) },
  {
    path: '',
    loadComponent: () => import('./layout/sidebar/sidebar').then(m => m.Sidebar),
    canActivate: [authGuard],
    children: [
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard').then(m => m.Dashboard) },
      {
        path: 'residence',
        children: [
          { path: '', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Residences', subtitle: 'Overview of all residence assets.' } },
          { path: 'list', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'All Residences', subtitle: 'View and manage all residences.' } },
          { path: 'add', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Add Residence', subtitle: 'Register a new residence into the system.' } }
        ]
      },
      {
        path: 'buildings',
        children: [
          { path: '', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Buildings', subtitle: 'Overview of all buildings.' } },
          { path: 'list', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'All Buildings', subtitle: 'View and manage your building portfolio.' } },
          { path: 'add', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Add Building', subtitle: 'Register a new building.' } }
        ]
      },
      {
        path: 'accounts',
        children: [
          { path: '', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Accounts', subtitle: 'Financial dashboard and account controls.' } },
          { path: 'income', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Income', subtitle: 'Track incoming funds and revenue.' } },
          { path: 'expenses', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Expenses', subtitle: 'Manage outgoing payments and costs.' } },
          { path: 'transactions', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Transactions', subtitle: 'Review all account transactions.' } }
        ]
      },
      { path: 'apartments', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Apartments', subtitle: 'Apartment inventory and management.' } },
      { path: 'residents', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Residents', subtitle: 'Resident directory and profiles.' } },
      { path: 'owners', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Owners', subtitle: 'Owner records and contact info.' } },
      { path: 'maintenance', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Maintenance', subtitle: 'Maintenance requests and work orders.' } },
      { path: 'invoices', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Invoices', subtitle: 'Invoice generation and tracking.' } },
      { path: 'payments', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Payments', subtitle: 'Payment processing and history.' } },
      { path: 'reports', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Reports', subtitle: 'Analytics and reporting dashboards.' } },
      { path: 'notifications', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Notifications', subtitle: 'System alerts and announcements.' } },
      { path: 'settings', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Settings', subtitle: 'Application and account settings.' } },
      { path: 'profile', loadComponent: () => import('./shared/components/feature-page/feature-page').then(m => m.FeaturePage), data: { title: 'Profile', subtitle: 'User profile and account details.' } },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  }
];
