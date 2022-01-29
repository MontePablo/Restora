package com.example.restora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.io.Serializable;

import daos.AddressDao;
import daos.OrderDao;
import daos.UserDao;
import models.Address;
import models.Order;

public class AddressPage extends AppCompatActivity implements AddressButtonsInterface {
    Button addressAddNew;
    Button addressContinue;
    RecyclerView addressRecyclerView;
    AddressAdapter addressAdapter;
    AddressDao addressDao;
    Order order;
    OrderDao orderDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_page);
        addressAddNew=findViewById(R.id.addressAddNew);
        addressContinue=findViewById(R.id.addressContinue);
        addressDao=new AddressDao();
        orderDao=new OrderDao();
        getSupportActionBar().hide();
        String orderId=getIntent().getStringExtra("orderId");

        orderDao.getOrderById(orderId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                order=task.getResult().toObject(Order.class);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","getOrderbyId failed:"+e.getMessage());
            }
        });
        setupAddressRecyclerView();
        addressContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order.address!=null){
                    orderDao.uploadOrder(order);
                    Intent intent=new Intent(getApplicationContext(),Payment.class);
                    intent.putExtra("orderId", orderId);
                    startActivity(intent);
                }else{
                    Log.d("TAG","address == null | need to select one");
                }

            }
        });
        addressAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NewAddress.class));
            }
        });
    }
    public void setupAddressRecyclerView(){
        Log.d("TAG","AddressRecyclerVIew started");
        addressRecyclerView=findViewById(R.id.addressRecyclerView);
        Query query=(new AddressDao()).allAddressReference.orderBy("fullName", Query.Direction.ASCENDING);
        query.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","query for address fetch failed:"+e.getMessage());
            }
        });
        FirestoreRecyclerOptions<Address> options=new FirestoreRecyclerOptions.Builder<Address>().setQuery(query,Address.class).build();
        addressAdapter=new AddressAdapter(options,this);
        addressRecyclerView.setAdapter(addressAdapter);
        addressRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        addressAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        addressAdapter.stopListening();
    }

    @Override
    public void editAddress(String addressId) {
            addressDao.getAddressById(addressId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Address selectedAddress=task.getResult().toObject(Address.class);
                    Intent intent=new Intent(getApplicationContext(),EditAddress.class);
                    intent.putExtra("addressId",addressId);
                    intent.putExtra("selectedAddress", selectedAddress);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG","selectedAddress fetch failed :"+e.getMessage());
                }
            });
    }

    @Override
    public void addressRadioClick(String addressId) {
        (addressDao).getAddressById(addressId).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Address address=task.getResult().toObject(Address.class);
                order.setAddress(address);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","getAddressById failed:"+e.getMessage());
            }
        });
    }
}