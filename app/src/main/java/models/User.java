package models;

public class User {
    public String uid,displayName,imageUrl;
    public User(String uid, String displayName, String imageUrl){
        this.uid=uid;
        this.displayName=displayName;
        this.imageUrl=imageUrl;
    }

    public User(){
        uid="nil";
        displayName="nil";
        imageUrl="nil";
    }
}
