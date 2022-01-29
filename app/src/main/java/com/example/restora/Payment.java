package com.example.restora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import daos.OrderDao;
import daos.UserDao;
import models.Order;
import models.User;

public class Payment extends AppCompatActivity {
    Order order;
    Button buttonPay;
    OrderDao orderDao;
    TextView textView12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        buttonPay=findViewById(R.id.buttonPay);
        textView12=findViewById(R.id.textView12);
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
                Log.d("TAG","getOrderById failed:"+e.getMessage());
            }
        });

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDao.uploadOrder(order);
                Toast.makeText(getApplicationContext(), "Order Done", Toast.LENGTH_LONG).show();
//                startActivity(new Intent(getApplicationContext(),OrdersPage.class));
                textView12.setVisibility(View.VISIBLE);
                buttonPay.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(this,HomeActivity.class));
    }
}