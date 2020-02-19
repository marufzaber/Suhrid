package com.zaber.helpseeking;

public class Contact {
    
    //private variables
    int _id;
    String _name;
    String _phone_number;
    String _contact_name;
    String _contact_number;
     
    // Empty constructor
    public Contact(){
         
    }
    // constructor
    public Contact(int id, String name, String _phone_number, String _contact_name, String _contact_number){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._contact_name = _contact_name;
        this._contact_number = _contact_number;
    }
     
    // constructor
    public Contact(String name, String _phone_number, String _contact_name, String _contact_number){
        this._name = name;
        this._phone_number = _phone_number;
        this._contact_name = _contact_name;
        this._contact_number = _contact_number;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }
     
    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }
    
    // getting contact name
    public String getContactName(){
        return this._contact_name;
    }
     
    // setting contact name
    public void setContactName(String _contact_name){
        this._contact_name = _contact_name;
    }
    
    // getting contact number
    public String getContactNumber(){
        return this._contact_number;
    }
     
    // setting contact number
    public void setContactNumber(String _contact_number){
        this._contact_number = _contact_number;
    }
    
}
