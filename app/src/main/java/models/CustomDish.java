package models;

import android.util.Log;

import java.util.ArrayList;

public class CustomDish {
    public String likes;
    public String userId;
    public String recipeName;
    public ArrayList<String> ingredientsList;
    public String recipeDetail;
    public ArrayList<String> images;
    public ArrayList<String> likedBy;
    public CustomDish(){
        ingredientsList=new ArrayList<>();
        images=new ArrayList<>();
        likedBy=new ArrayList<>();
        likes="0";
    }
    public void setRecipe(String s){
        recipeDetail=s;
    }
    public void setImages(ArrayList<String> images){
        this.images.addAll(images);
        Log.d("TAG","image array added");
    }
    public void setIngredient(String name,String quantity){
        ingredientsList.add(name+" - "+quantity);
    }
    public void setRecipeName(String s){
        recipeName=s;
    }
    public void setUserId(String id){ this.userId=id; }
    public void setLikes(String userId){
        likedBy.add(userId);
        likes=Integer.toString(likedBy.size());
    }

    public void removeLikes(String currentUserId) {
        likedBy.remove(currentUserId);
        likes=Integer.toString(likedBy.size());

    }
}
