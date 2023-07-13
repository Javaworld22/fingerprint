/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Abc
 */
public class User {
    private String id;
    private String fname;
    private String lname;
    private String picture;
    private String finger_extract;
    
    public User(String id, String fname, String lname, String picture, String finger_extract){
        this.finger_extract = finger_extract;
        this.fname = fname;
        this.id = id;
        this.picture = picture;
        this.lname = lname;
    }
    public class UserArray{
     private User[] userArray;
     
     public UserArray(User[] userArray){
         this.userArray = userArray;
     }
         
         public User[] getUserArray(){
             return userArray;
         }
 
  }
}


