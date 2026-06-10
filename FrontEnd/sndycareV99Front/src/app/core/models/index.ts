export interface LoginRequest { username: string; password: string; }
export interface JwtResponse{ token: string ; type ? :string; username?:string; role?:string }

/*=========== Residance============*/
export interface ResidenceRequest{
     name : string;
     address:string;
     city:string ;
     numberUnits:number; 
     userId:number
}

export interface ResidanceResponse{
     name : string;
     address:string;
     city:string ;
     numberUnits:number; 
}
