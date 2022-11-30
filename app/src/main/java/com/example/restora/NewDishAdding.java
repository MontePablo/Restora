package com.example.restora;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import daos.DishDao;
import daos.StorageDao;

public class NewDishAdding extends AppCompatActivity {
    ImageView newDishImageView;
    Button newDishInsertImageButton;
    Button newDishPublishButton;
    EditText newDishName,newDishExtras,newDishPrice;
    StorageDao storageDao;
    int IMAGE_REQUEST_CODE=7;
    Uri filePathUri;
    DishDao dishDao;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dish_adding);
        dishDao=new DishDao();
        newDishExtras=findViewById(R.id.newDishInsertExtra);
        newDishName=findViewById(R.id.newDIshInsertName);
        newDishPrice=findViewById(R.id.newDishInsertPrice);
        newDishPublishButton=findViewById(R.id.newDishPublishButton);
        newDishInsertImageButton=findViewById(R.id.newDIshInsertImage);
        newDishImageView=findViewById(R.id.newDishImage);
        storageDao=new StorageDao();
        getSupportActionBar().hide();

        newDishInsertImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertImage();
            }
        });
        newDishPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Log.d("TAG","publishButton start");
                    if(filePathUri!=null){
                        Log.d("TAG","FilepathUri!=null");
                        String fileName=System.currentTimeMillis()+".jpg";
                        storageDao.uploadImage(filePathUri,fileName).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("TAG","upload image success");
                                storageDao.getImageUrlByName(fileName).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl=uri.toString();
                                        String name=newDishName.getText().toString();
                                        String extras=newDishExtras.getText().toString();
                                        int price=Integer.parseInt(newDishPrice.getText().toString());
                                        dishDao.addDish(imageUrl,name,extras,price);
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        Toast.makeText(getApplicationContext(),"Posting success!",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TAG","upload image failed : "+e.getMessage());
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
                        Log.d("TAG","filePathUri==null");
                    }
            }
        });
    }

    public void insertImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //noinspection deprecation
        startActivityForResult(Intent.createChooser(intent,"Select Image"),IMAGE_REQUEST_CODE);
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(data.getData()!=null && data!=null) {
//            FilePathUri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
//                newDishImageView.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                Log.d("TAG", "onActivityResult exeption:" + e.getMessage());
//            }
//        }else{
//            Log.d("TAG","onActivityResult data==null");
//        }
//    }
//    public void insertImage(){
//        CropImage.startPickImageActivity(NewDishAdding.this);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                uri=imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCrop(imageUri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                newDishImageView.setImageURI(result.getUri());
                filePathUri=result.getUri();
                Log.d("TAG", "image uri set to imageView");
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.d("error",result.getError().toString());
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (uri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCrop(uri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
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