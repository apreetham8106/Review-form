import { HttpClient } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable, map } from 'rxjs';
import { ProjectService } from '../project.service';
import { Project } from '../project';
import { NgForm } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';





@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent  implements OnInit {


  // projects: Project[] = [];
  // @ViewChild('feedbackForm') form : NgForm;
  // editMode: boolean = false;
  currentid: any;
  update= false;
  upload=true;
  file: File ;
  name: any;
  designation: any;
  department: any;
  id_no:any;
  mobile_no:any;
  dob:any;
  reviews:any;


  files: File[] = [];
  newFiles!: FileList;

  

 


  // dataSource: Project[] = [];
  dataSource = new MatTableDataSource<any>();
  displayedColumns: string[] = ['id','name','designation','department','id_no','mobile_no','dob','reviews','files', 'actions'];
  constructor(private projectService:ProjectService, private snackBar: MatSnackBar){ }

  ngOnInit(){

    this.loadPersons();
    
  }
  
  

//   getAllPersons(): void {
//     this.projectService.getAllPersons().subscribe(data => {
//      this.projects = data.filter(projects => projects.deleteFlag !== 'Y');
//     //  this.personData
//      this.dataSource = [...this.projects];
     
//    });
  
//  }

loadPersons() {
  this.projectService.getAllPersons().subscribe(
    (data: any[]) => {
      // Filter out records with delete flag set to 'Y'
      this.dataSource.data = data;
    },
    error => {
      console.log('Error loading persons:', error);
    }
  );
}




 

  onSubmit(): void {




    this.projectService.savePerson(this.personData).subscribe(
      (personResponse) => {
        console.log('Person saved successfully:', personResponse);
        this.dataSource.data.push(personResponse);

        
        if (this.files.length > 0) {
          this.uploadFiles(personResponse.id, this.files);
        } else {
          console.log('No files selected.');
        }
        
        
       
      
   
  
      },
      (error) => {
        console.error('Error saving person:', error);
      }
    );
    // window.location.reload();
    
    
    
   


  }

  
  
  onFileChange(event: any) {

    this.newFiles = event.target.files;

    this.files = event.target.files;

    
    const files: FileList = event.target.files;
  this.files = [];
  for (let i = 0; i < files.length; i++) {
    const file = files.item(i);
    if (file) {
      this.files.push(file);
    }
  }
    

  }


  uploadFiles(id: string, files: File[]): void {
    // Loop through the array of files and upload each one
    files.forEach(file => {
      this.projectService.uploadFile(id, file).subscribe(
        (fileResponse) => {
          console.log('File uploaded successfully:', fileResponse);
        },
        (error) => {
          console.error('Error uploading file:', error);
          alert("File Already exist in folderpath.");
        }
      );
    });
  }

  personData:any={

  name:'',
  designation:'',
  department:'',

  id_no:'',
  mobile_no:'',
  dob:'',
  reviews:'',

   
  };

 

  getFilesString(files: any[]): string {
    return files.map(file => file.fileName).join(', ');
  }

  setUpdate(data: any) {

    this.update = true;
    this.upload = false;
    this.personData.name = data.name;
    this.personData.designation = data.designation;
    this.personData.department = data.department;
    this.personData.id_no = data.id_no;
    this.personData.mobile_no = data.mobile_no;
    this.personData.dob = data.dob;
    this.personData.reviews = data.reviews;
    this.personData.id = data.id;

    this.currentid = data.id;

     this.personData.files =data.files;
  }

  
  downloadFile(id: string, filename: string): void {
    console.log('personId:', id);
    console.log('filename:', filename);
  
    this.projectService.downloadFile(id, filename).subscribe(
      (response) => {
        const blob = new Blob([response.body as BlobPart], { type: response.headers.get('Content-Type') || 'application/octet-stream' });
  
        const link = document.createElement('a');
        link.href = window.URL.createObjectURL(blob);
        link.download = filename;  // Use the provided filename
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      },
      (error) => {
        console.error('Error downloading file:', error);
      }
    );
  }

  onNewFilesSelect(event: any): void {
    this.newFiles = event.target.files;
  }


  onEdit(): void {
    this.projectService.updatePersonDetails(this.currentid, this.personData).subscribe(
      (updatedPersonResponse) => {
        console.log('Person details updated successfully:', updatedPersonResponse);
        alert('Person details updated successfully!');
        window.location.reload();
  
  
  
  
        
        const formData = new FormData();
        for (let i = 0; i < this.newFiles.length; i++) {
          formData.append('files', this.newFiles[i]);
        }
  
        this.projectService.updateFilesForPerson(this.currentid, formData).subscribe(
          (updatedFilesResponse) => {
            console.log('Files updated successfully:', updatedFilesResponse);
            // Show a confirm alert
            if (confirm('Data updated successfully. Do you want to reload the page?')) {
              window.location.reload();
            }
          },
          (errorFiles) => {
            console.error('Error updating files:', errorFiles);
            // Show a confirm alert
            if (confirm('An error occurred while updating files. Do you want to reload the page?')) {
              window.location.reload();
            }
          }
        );
      },
      (errorPerson) => {
        console.error('Error updating person details:', errorPerson);
        // Show a confirm alert
        if (confirm('An error occurred while updating person details. Do you want to reload the page?')) {
          window.location.reload();
        }
      }
    );
  }

  onDeleteFile(file: File) {
    const index = this.files.indexOf(file);
    if (index !== -1) {
      this.files.splice(index, 1);

      
    }
  }

  filename!:string;

  delete(id: string) {
    if (confirm('Are you sure you want to delete this record?')) {
      this.projectService.delete(id).subscribe(
        () => {
          // Remove the deleted record from the data source
          this.dataSource.data = this.dataSource.data.filter(person => person.id !== id);
          // Optionally, you can reload the data after deletion
          // this.loadPersons();
        },
        error => {
          console.log('Error deleting person:', error);
          alert('Failed to delete record.');
        }
      );
    }
  }

  deletePersonAndFilesByFilename(id: string, filename: string) {
    if (confirm(`Are you sure you want to delete  this "   ${filename}  " and associated files?`)) {
      console.log('personId:', id);
      console.log('filename:', filename);
    
    this.projectService.deletePersonAndFilesByFilename(id,filename).subscribe(
      
      () => {
        console.log('Person and files deleted successfully');
      window.location.reload();
        // this.getAllPersons();
       
      },
      error => {
        console.error('Error deleting person and files', error);
      }
    );
    
    
  }
  
  
  
  }
  

  


  // delete(id: string) {

  //   if (confirm('Are you sure you want to delete this record?')) {
  //   this.projectService.delete(id).subscribe(
      
  //     (error)=>{
    
        
     
  
  //     },
  //     (data)=> {
  //       alert('failed');
      
       
        
  //   });
  
  //   window.location.reload();
  // }
  
  
  // }

  // update(id: string){

  //   this.currentid = id;
  //   let personData = this.projects.find( (p) => { return p.id === id});

    
     

  

  //   this.form.setValue({
  //     name: personData.name,
  //     designation: personData.designation,
  //     department: personData.department,

  //     id_no: personData.id_no,
  //     mobile_no: personData.mobile_no,
  //     dob: personData.dob,
  //     reviews: personData.reviews
  //   });

  //   this.editMode = true; 

    
  // }
  




}
