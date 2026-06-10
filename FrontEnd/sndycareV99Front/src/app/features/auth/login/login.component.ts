import { Component } from '@angular/core';
import { FormGroup,FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../core/service/auth.service';



@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule,CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  standalone:true
})
export class Login {

  loginForm : FormGroup;
  loadingForm = false;
  error ='';
  constructor(private fb : FormBuilder , private auth  :AuthService , private router:Router){

    this.loginForm = this.fb.group({
      email : ['',[Validators.required , Validators.email]],
      password : ['',[Validators.required , Validators.minLength(6)]]
    });
}

  onSubmit(){
    if(this.loginForm.valid){
      this.loadingForm = true;
      this.error = '';
      this.auth.login(this.loginForm.value as any).subscribe({
        next:(response:any)=>{
          this.router.navigate(['/'])
        }, 
        error:()=>{
          this.error='Invalid credentials';
          this.loadingForm=false;} }
      )
        

    }
  }



}
