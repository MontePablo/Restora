package com.example.restora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import daos.CartDishDao;
import daos.DishDao;
import models.Dish;

public class HomeActivity extends AppCompatActivity implements AddToCartFunctionInterface {
    RecyclerView buyerDishesRecyclerView;
    FloatingActionButton floatingNewDishButton;
    FloatingActionButton floatingCustomDish;
    BuyerDishAdapter buyerDishAdapter;
    DishDao dishDao;
    CartDishDao cartDishDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        floatingNewDishButton=findViewById(R.id.addNewDish);
        floatingCustomDish=findViewById(R.id.addCustomDish);
        cartDishDao=new CartDishDao();
        dishDao=new DishDao();
        floatingCustomDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),newCustomDishActivity.class));
            }
        });
        floatingNewDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NewDishAdding.class));
            }
        });
        setupBuyerRecyclerView();
    }
    public void setupBuyerRecyclerView(){
        Log.d("TAG","setupBuyerRecyclerView start");
        buyerDishesRecyclerView=findViewById(R.id.buyerDishesRecyclerView);
        CollectionReference dishCollection=dishDao.dishCollections;
//        dishCollection.orderBy("name", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d("TAG 2", "onSuccess"+document.getId() + " => " + document.getData());
//                    }
//                } else {
//                    Log.d("TAG 2", "Error getting documents: ", task.getException());
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("TAG","Query onFailure:"+e.getMessage());
//            }
//        });
        Query query=dishCollection.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Dish> recyclerOptions=new FirestoreRecyclerOptions.Builder<Dish>().setQuery(query,Dish.class).build();

        buyerDishAdapter=new BuyerDishAdapter(recyclerOptions,this);
        buyerDishesRecyclerView.setAdapter(buyerDishAdapter);
        buyerDishesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("TAG","setupBuyerRecyclerView finish");

    }

    @Override
    protected void onStart() {
        super.onStart();
        buyerDishAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        buyerDishAdapter.stopListening();
    }

    @Override
    public void addToCart(String dishId) {
        Toast.makeText(getApplicationContext(), "Added to Cart!",Toast.LENGTH_SHORT).show();
        cartDishDao.addDishFromHomeToCart(dishId);
    }

    @Override
    public void delete(String dishId) {
        Toast.makeText(getApplicationContext(), "Item deleted!",Toast.LENGTH_SHORT).show();
        dishDao.deleteDish(dishId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogoutButton:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,SignInActivity.class));
                finish();
                return true;
            case R.id.menuCartButton:
                startActivity(new Intent(this,Cart.class));
                return true;
            case R.id.menuMyListingsButton:
                Toast.makeText(getApplicationContext(),"My dishes!",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}