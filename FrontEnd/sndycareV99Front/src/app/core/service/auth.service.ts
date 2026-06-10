import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router} from "@angular/router";
import { BehaviorSubject , Observable , tap } from "rxjs";
import { environment } from '../../environments/environment';
import { LoginRequest , JwtResponse } from "../models";

@Injectable({ providedIn: 'root' })
export class AuthService{

    private loggedIn$ = new BehaviorSubject<Boolean>(this.hasToken())
    
    constructor(private http : HttpClient , private router:Router ){}

      login(req : LoginRequest): Observable<JwtResponse> {
        return this.http.post<JwtResponse>(`${environment.apiUrl}/auth/login`, req).pipe(
        tap(r => this.setSession(r))
        );
    }


    setSession(authRes: JwtResponse){
        console.log(JSON.stringify(authRes));
        localStorage.setItem('token',authRes.token);
        localStorage.setItem('user',JSON.stringify(authRes));
        this.loggedIn$.next(true);
    }
    isLoggedInSnapshot() { return this.hasToken(); }
    getCurrentUser(): JwtResponse | null { const u = localStorage.getItem('user'); return u ? JSON.parse(u) : null; }
    getToken() : string | null{ return localStorage.getItem('token')  }
    private hasToken(){ return !!localStorage.getItem('token');}
}


