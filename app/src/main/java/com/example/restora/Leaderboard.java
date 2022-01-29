package com.example.restora;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import daos.CustomDishDao;
import models.CartDish;
import models.CustomDish;

public class Leaderboard extends AppCompatActivity {
    RecyclerView leaderboardRecyclerView;
    CustomDishDao customDishDao;
    LeaderboardAdapter leaderboardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        getSupportActionBar().hide();

        customDishDao=new CustomDishDao();
        setupLeaderboardRecyclerView();

    }
    public void setupLeaderboardRecyclerView(){
        Log.d("TAG","recyclerView start");
        leaderboardRecyclerView=findViewById(R.id.leaderboardRecyclerView);
        Query query=customDishDao.customDishReference.orderBy("likes", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<CustomDish> options=new FirestoreRecyclerOptions.Builder<CustomDish>().setQuery(query,CustomDish.class).build();
        leaderboardAdapter=new LeaderboardAdapter(options);
        leaderboardRecyclerView.setAdapter(leaderboardAdapter);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG","recyclerView end");
    }

    @Override
    protected void onStart() {
        super.onStart();
        leaderboardAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        leaderboardAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(this,newCustomDishActivity.class));
    }
}