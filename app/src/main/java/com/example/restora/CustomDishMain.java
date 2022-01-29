package com.example.restora;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import daos.CartDishDao;
import daos.CustomDishDao;
import models.CartDish;
import models.CustomDish;


public class CustomDishMain extends AppCompatActivity implements CustomDishButtonInterface{
    RecyclerView recyclerView;
    CustomDishDao customDishDao;
    CustomDishAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dish_main);
        customDishDao=new CustomDishDao();
        setupCustomDishRecyclerView();
    }

    public void setupCustomDishRecyclerView() {
        Log.d("TAG","recyclerView start");
        recyclerView=findViewById(R.id.customDishRecyclerView);
        Query query=customDishDao.customDishReference.orderBy("recipeName", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<CustomDish> options=new FirestoreRecyclerOptions.Builder<CustomDish>().setQuery(query,CustomDish.class).build();
        adapter=new CustomDishAdapter(options,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG","recyclerView end");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,newCustomDishActivity.class));
    }

    @Override
    public void onCancelButtonClick(String customDishId) {
        customDishDao.customDishReference.document(customDishId).delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","Delete customDish failed:"+e.getMessage());
            }
        });
    }

    @Override
    public void setImagesToLayout(ArrayList<String> images, LinearLayout customDishImageLayout) {
        for(int i=0;i<images.size();i++){
            customDishImageLayout.addView(getImg(images.get(i)));
            Log.d("TAG","setImagetoLayout Running");
        }
        Log.d("TAG","setImagetoLayout finished");
    }

    @Override
    public void likeUnlike(String customDishId) {
        customDishDao.updateLikes(customDishId);
    }

    @Override
    public void gotoDialog(String recipeDetail) {
        showDialog(recipeDetail);
    }

    public ImageView getImg(String link){
        ImageView img=new ImageView(this);
        img.setLayoutParams(new LinearLayout.LayoutParams(400,400));
        Glide.with(img.getContext()).load(link).into(img);
        return  img;
    }
    public void showDialog(String recipeDetail){
        AlertDialog.Builder alert=new AlertDialog.Builder(getApplicationContext());
        View view=getLayoutInflater().inflate(R.layout.custom_dialog,null);
        TextView dialogTextView=view.findViewById(R.id.dialogTextView);

        dialogTextView.setText(recipeDetail);

        alert.setView(view);
        AlertDialog alertDialog=alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
}