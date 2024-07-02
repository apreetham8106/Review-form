import { Fileentity } from "./fileentity";

export interface Project {


    id?:string;
    name:string;
    designation:string;
    department:string;

    id_no:string;
    mobile_no:string;
    dob:string;
    reviews:string;

    files: Fileentity[];
   


    

}
