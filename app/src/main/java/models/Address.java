package models;

import java.io.Serializable;

public class Address implements Serializable {
    public  String fullName,mobileNo,pincode,addressLine1,addressLine2,landmark,townCity,state;
    public Address(String fullName,String mobileNo,String pincode,String addressLine1,
                   String addressLine2,String landmark,String town,String state){
        this.fullName=fullName;this.pincode=pincode;this.state=state;this.addressLine2=addressLine2;
        this.addressLine1=addressLine1;this.townCity=town;this.mobileNo=mobileNo;this.landmark=landmark;
    }
    public Address(){

    }
}
