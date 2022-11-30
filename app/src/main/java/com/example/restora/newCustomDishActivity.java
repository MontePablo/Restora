package com.example.restora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

import daos.CustomDishDao;
import daos.StorageDao;
import daos.UserDao;
import models.CustomDish;
import models.User;

public class newCustomDishActivity extends AppCompatActivity {
    private static final int IMAGE_REQUEST_CODE = 7;
    LinearLayout ingredientAddConstrantLayout;
    LinearLayout ingredientImageAddConstrantLayout;
    Button ingredientViewAddButton;
    Button ingredientImageAddButton;
    Button customDishSubmitButton;
    EditText customRecipeFullDetail;
    EditText customRecipeName;
    ArrayList<String> ingredientImageList;
    ArrayList<EditText> ingredientNameViewList;
    ArrayList<EditText> ingredientQuantityViewList;
    ImageView imageView;
    StorageDao storageDao;
    CustomDishDao customDishDao;
    Button myRecipe;
    Button gotoLeaderboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_custom_dish);
        ingredientAddConstrantLayout=findViewById(R.id.ingredientAddConstrantLayout);
        ingredientViewAddButton=findViewById(R.id.ingredientViewAddButton);
        customDishSubmitButton=findViewById(R.id.customDishSubmitButton);
        ingredientImageAddConstrantLayout=findViewById(R.id.ingredientImageAddConstrantLayout);
        ingredientImageAddButton=findViewById(R.id.ingredientImageAddButton);
        customRecipeFullDetail=findViewById(R.id.customRecipeFull);
        customRecipeName=findViewById(R.id.customRecipeName);
        myRecipe=findViewById(R.id.myRecipeButton);
        gotoLeaderboard=findViewById(R.id.gotoLeaderboard);
        ingredientNameViewList=new ArrayList<>();
        ingredientImageList=new ArrayList<>();
        ingredientQuantityViewList=new ArrayList<>();
        storageDao=new StorageDao();
        customDishDao=new CustomDishDao();
        getSupportActionBar().hide();



        ingredientViewAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addView();
            }
        });
        addView(); //1
        ingredientImageAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
        addImage();//1

        customDishSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDishSubmit();
                finish();
                startActivity(new Intent(getApplicationContext(),CustomDishMain.class));
            }
        });

        myRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomDishMain.class));
            }
        });
        gotoLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Leaderboard.class));
            }
        });

    }

    public void customDishSubmit() {
        Log.d("TAG","customDish submission start");
        CustomDish customDish=new CustomDish();
        for(int i=0;i<ingredientNameViewList.size();i++){
            String ingredient=ingredientNameViewList.get(i).getText().toString();
            String quantity=ingredientQuantityViewList.get(i).getText().toString();
            customDish.setIngredient(ingredient,quantity);
        }
        customDish.setRecipeName(customRecipeName.getText().toString());
        customDish.setImages(ingredientImageList);
        customDish.setRecipe(customRecipeFullDetail.getText().toString());
        customDish.setUserId(new UserDao().userId);
        customDishDao.uploadCustomDish(customDish);

        Log.d("TAG","customDish submission finished");
        Toast.makeText(getApplicationContext(),"success!",Toast.LENGTH_SHORT).show();
    }

    public void addView(){
        final View view= getLayoutInflater().inflate(R.layout.ingredients_dynamic_view,null,false);
        EditText ingredientName=view.findViewById(R.id.ingredientName);
        EditText ingredientQuantity=view.findViewById(R.id.ingredientQuantity);
        ingredientNameViewList.add(ingredientName);
        ingredientQuantityViewList.add(ingredientQuantity);
        ImageButton removeIngredient=view.findViewById(R.id.removeIngredient);
        removeIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeIngredient(view);
            }
        });

        ingredientAddConstrantLayout.addView(view);
    }
    public void removeIngredient(View view){
        ingredientAddConstrantLayout.removeView(view);
    }
    public void addImage(){
        final View view=getLayoutInflater().inflate(R.layout.ingredients_images_dynamic_view,null,false);
        ImageView ingredientDynamicImageView=view.findViewById(R.id.ingredientDynamicImageView);
        imageView=ingredientDynamicImageView;
        Button ingredientDynamicImageAdd=view.findViewById(R.id.ingredientDynamicImageAdd);
        ingredientDynamicImageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertImage();


            }
        });

        ImageButton ingredientImageRemove=view.findViewById(R.id.removeIngredientImage);
        ingredientImageRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeIngredientImage(view);
            }
        });
        ingredientImageAddConstrantLayout.addView(view);
    }
    public void removeIngredientImage(View view){
        ingredientImageAddConstrantLayout.removeView(view);
    }
    public void insertImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //noinspection deprecation
        startActivityForResult(Intent.createChooser(intent,"Select Image"),IMAGE_REQUEST_CODE);
    }
//    public void insertImage(ImageView imageView){
//        CropImage.startPickImageActivity(newCustomDishActivity.this);
//        this.imageView=imageView;
//
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCrop(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri=result.getUri();
                myfunc(uri);
            }
        }
    }

    private void myfunc(Uri uri) {
        if(uri!=null){
            String fileName=System.currentTimeMillis()+".jpg";
            storageDao.uploadImage(uri,fileName).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("TAG","upload image success");
                    storageDao.getImageUrlByName(fileName).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl=uri.toString();
                            ingredientImageList.add(imageUrl);
                            Glide.with(imageView.getContext()).load(imageUrl).into(imageView);
                            Log.d("TAG", "image uri set to imageView");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("TAG","upload failed:"+e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG","upload image failed : "+e.getMessage());
                }
            });
        }else{
            Log.d("TAG","image uri==null");
        }
    }

    private void startCrop(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
    public void onBackPressed() {
        startActivity(new Intent(this,HomeActivity.class));
    }
}