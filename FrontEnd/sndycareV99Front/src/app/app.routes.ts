import { Routes } from '@angular/router';
import { Login } from './features/auth/login/login.component';
import { authGuard } from './core/guard/auth.guard';


export const routes: Routes = [

    { path:'login', loadComponent:()=>import('./features/auth/login/login.component').then(m=>m.Login) },
    { path:'', loadComponent:()=>import('./layout/sidebar/sidebar').then(m=>m.Sidebar)
        ,canActivate:[authGuard],children:[

        ]
    }
    

];
