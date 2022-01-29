package models;

public class CartDish {
    public String image,name,extras,userId,dishId;
    public int price,quantity;
    public CartDish(String image,String name,String extras,int price,int quantity,String userId,String dishId){
        this.extras=extras;
        this.name=name;
        this.image=image;
        this.price=price;
        this.userId=userId;
        this.quantity=quantity;
        this.dishId=dishId;
    }
    public CartDish(){

    }
}
