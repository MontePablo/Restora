package com.example.restora;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import daos.AddressDao;
import models.Address;
import models.Order;

public class EditAddress extends AppCompatActivity {
    EditText editAddressName,editAddressMobile,editAddressPincode,editAddressLine1,
            editAddressLine2,editAddressState,editAddressTown,editAddressLandmark;
    Button editAddressSave;
    Address selectedAddress,newAddress;
    String addressId;

    AddressDao addressDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        editAddressSave=findViewById(R.id.editAddressSave);
        editAddressName=findViewById(R.id.editAddressName);
        editAddressMobile=findViewById(R.id.editAddressMobile);
        editAddressLine1=findViewById(R.id.editAddressLine1);
        editAddressLine2=findViewById(R.id.editAddressLine2);
        editAddressPincode=findViewById(R.id.editAddressPincode);
        editAddressState=findViewById(R.id.editAddressState);
        editAddressTown=findViewById(R.id.editAddressTown);
        editAddressLandmark=findViewById(R.id.editAddressLandmark);
        getSupportActionBar().hide();

        selectedAddress= (Address) getIntent().getSerializableExtra("selectedAddress");
        addressId=(String) getIntent().getSerializableExtra("addressId");
        setData();

        addressDao=new AddressDao();
        editAddressSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                addressDao.allAddressReference.document(addressId).set(newAddress);
                Toast.makeText(getApplicationContext(),"saved!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),AddressPage.class));
                finish();
            }
        });
    }
    public void setData(){
        editAddressName.setText(selectedAddress.fullName);
        editAddressMobile.setText(selectedAddress.mobileNo);
        editAddressLine1.setText(selectedAddress.addressLine1);
        editAddressLine2.setText(selectedAddress.addressLine2);
        editAddressLandmark.setText(selectedAddress.landmark);
        editAddressState.setText(selectedAddress.state);
        editAddressPincode.setText(selectedAddress.pincode);
        editAddressTown.setText(selectedAddress.townCity);
    }
    public void getData(){
        newAddress=new Address(editAddressName.getText().toString(),editAddressMobile.getText().toString(),
                                editAddressPincode.getText().toString(),editAddressLine1.getText().toString(),
                                editAddressLine2.getText().toString(),editAddressLandmark.getText().toString(),
                                editAddressTown.getText().toString(),editAddressState.getText().toString());
    }
}