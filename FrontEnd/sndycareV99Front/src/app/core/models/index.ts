export interface LoginRequest { email: string; password: string; }
export interface JwtResponse{ token: string ; type ? :string; username?:string; role?:string }

/*=========== Residence============*/
export interface ResidenceRequest{
     name : string;
     address:string;
     city:string ;
     numberUnits:number; 
     userId?:number;
}

export interface ResidenceResponse{
     name : string;
     address:string;
     city:string ;
     numberUnits:number; 
}
