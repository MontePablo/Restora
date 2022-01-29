package com.example.restora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import daos.CartDishDao;
import daos.OrderDao;
import daos.UserDao;
import models.Address;
import models.CartDish;
import models.Order;

public class Cart extends AppCompatActivity implements CartDishButtonsInterface{
    RecyclerView cartRecyclerView;
    CartDishDao cartDishDao;
    CartAdapter cartAdapter;
    Button cartPlaceOrder;
    TextView cartTotalPrice;
    Hashtable<String,Integer> hash;
    int totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartPlaceOrder=findViewById(R.id.cartPlaceOrder);
        hash=new Hashtable<>();
        cartTotalPrice=findViewById(R.id.cartTotalPrice);
        cartDishDao=new CartDishDao();
        setupCartRecyclerView();
        totalPrice=0;
        cartPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartDishDao.getAllCartDish().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<CartDish> allCartDishArray=task.getResult().toObjects(CartDish.class);
                            Order order=new Order();
                            order.setCartDishArray(allCartDishArray);
                            order.setTotalCartPrice(totalPrice);
                            order.setOrderId(String.valueOf(System.currentTimeMillis()));
                            (new OrderDao()).uploadOrder(order);
                            Intent intent=new Intent(getApplicationContext(), AddressPage.class);
                            intent.putExtra("orderId",order.orderId);
                            startActivity(intent);
                        }else{
                            Log.d("TAG ", "getAllCartDish task not succeed", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG","getAllCartDish Failed:"+e.getMessage());
                    }
                });
            }
        });
    }

    public void setupCartRecyclerView(){
        Log.d("TAG","cartRecyclerVIew Started");
        cartRecyclerView=findViewById(R.id.cartRecyclerView);
        Query query=cartDishDao.cartDishIntoCartReference.orderBy("name", Query.Direction.ASCENDING);
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("TAG", "Cart Query onSuccess"+document.getId() + " => " + document.getData());
//                    }
//                }else{
//                    Log.d("TAG ", "Error getting Cart Query: ", task.getException());
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("TAG","Cart Query failed :"+e.getMessage());
//
//            }
//        });
        FirestoreRecyclerOptions<CartDish> options=new FirestoreRecyclerOptions.Builder<CartDish>().setQuery(query,CartDish.class).build();
        cartAdapter=new CartAdapter(options,this);
        cartRecyclerView.setAdapter(cartAdapter);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG","cartRecyclerVIew finished");
    }

    @Override
    protected void onStart() {
        super.onStart();
        cartAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cartAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
    }

    @Override
    public void cartDishQuantityIncrease(String cartDishId) {
        cartDishDao.increaseQuantity(cartDishId);
    }

    @Override
    public void cartDishQuantityDecrease(String cartDishId) {
       cartDishDao.decreaseQuantity(getApplicationContext(),cartDishId);

    }

    @Override
    public void cartDishDelete(String cartDishId) {

        cartDishDao.getCartDishById(cartDishId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                CartDish cartDish=task.getResult().toObject(CartDish.class);
                int price=cartDish.price;int quantity=cartDish.quantity;
                totalPrice-=(price*quantity);
                cartTotalPrice.setText(String.valueOf(totalPrice));
                cartDishDao.cartDishIntoCartReference.document(cartDishId).delete()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG","Deleting cartDish failed:"+e.getMessage());
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","cart delete failed :"+e.getMessage());
            }
        });

    }

    @Override
    public void sendPriceToCartTotal(int price,int quantity,String id) {
        hash.put(id,(price*quantity));
        totalPrice=calculator(hash);
        cartTotalPrice=findViewById(R.id.cartTotalPrice);
        cartTotalPrice.setText(String.valueOf(totalPrice));
    }
    public int calculator(Hashtable<String,Integer> hash){
        Iterator<Integer> iter=hash.values().iterator();
        int sum = 0;
        while(iter.hasNext()){
            sum+=iter.next();
        }
        return sum;
    }
}