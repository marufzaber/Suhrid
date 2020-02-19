package com.zaber.helpseeking;

public class RecentCall {

    
    //private variables
    int _id;
    String _name;
    String _phone_number;
    String _recent_number;
    String _type;
     
    // Empty constructor
    public RecentCall(){
         
    }
    // constructor
    public RecentCall(int id, String name, String _phone_number, String _recent_number, String _type){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._recent_number = _recent_number;
        this._type = _type;
    }
     
    // constructor
    public RecentCall(String name, String _phone_number, String _recent_number, String _type){
        this._name = name;
        this._phone_number = _phone_number;
        this._recent_number = _recent_number;
        this._type = _type;
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
    
    // getting recent number
    public String getRecentNumber(){
        return this._recent_number;
    }
     
    // setting recent number
    public void setRecentNumber(String _recent_number){
        this._recent_number = _recent_number;
    }
    
    // getting type
    public String getType(){
        return this._type;
    }
     
    // setting type
    public void setType(String _type){
        this._type = _type;
    }
}
