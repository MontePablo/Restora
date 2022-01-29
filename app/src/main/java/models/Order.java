package models;


import java.util.ArrayList;
import java.util.List;

public class Order {
    public String orderId;
    public   List<CartDish> allCartDishArray;
    public   int totalCartPrice;
    public   Address address;
    public Order(){
        address=new Address();
        allCartDishArray=new ArrayList<>();
    }
    public void setCartDishArray(List<CartDish> allCartDishArray){
        this.allCartDishArray=allCartDishArray;
    }
    public void setAddress(Address address){
        this.address=address;
    }
    public void setTotalCartPrice(int totalCartPrice){
        this.totalCartPrice=totalCartPrice;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
