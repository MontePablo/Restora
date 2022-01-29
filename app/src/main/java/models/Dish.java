package models;

public class Dish {
    public String image,name,extras, userId;
    public int price;
    public Dish(String image,String name,String extras,int price,String userId){
        this.extras=extras;
        this.name=name;
        this.image=image;
        this.price=price;
        this.userId=userId;
    }
    public Dish(){

    }
}