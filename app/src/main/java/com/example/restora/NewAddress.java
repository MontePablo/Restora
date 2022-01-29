package com.example.restora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import daos.AddressDao;
import models.Address;

public class NewAddress extends AppCompatActivity {
    Button newAddressSave;
    EditText newAddressName,newAddressMobile,newAddressPincode,newAddressLine1,
            newAddressLine2,newAddressState,newAddressTown,newAddressLandmark;

    AddressDao addressDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        newAddressSave=findViewById(R.id.newAddressSave);
        newAddressName=findViewById(R.id.newAddressName);
        newAddressMobile=findViewById(R.id.newAddressMobile);
        newAddressLine1=findViewById(R.id.newAddressLine1);
        newAddressLine2=findViewById(R.id.newAddressLine2);
        newAddressPincode=findViewById(R.id.newAddressPincode);
        newAddressState=findViewById(R.id.newAddressState);
        newAddressTown=findViewById(R.id.newAddressTown);
        newAddressLandmark=findViewById(R.id.newAddressLandmark);

        addressDao=new AddressDao();
        getSupportActionBar().hide();

        newAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","save button press");
                Toast.makeText(getApplicationContext(),"saved!",Toast.LENGTH_SHORT).show();
                Address address=new Address(newAddressName.getText().toString(),newAddressMobile.getText().toString(),
                                            newAddressPincode.getText().toString(),newAddressLine1.getText().toString(),
                                            newAddressLine2.getText().toString(),newAddressLandmark.getText().toString(),
                                            newAddressTown.getText().toString(),newAddressState.getText().toString());
                                            addressDao.uploadAddress(address);
                startActivity(new Intent(getApplicationContext(),AddressPage.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,AddressPage.class));
    }
}