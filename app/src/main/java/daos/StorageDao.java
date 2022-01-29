package daos;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageDao {
    StorageReference storageReference;
    UploadTask taskSnapshot;
    public StorageDao(){
        storageReference= FirebaseStorage.getInstance().getReference("images");
        Log.d("TAG","StorageDao Instance created");
    }
    public UploadTask uploadImage(Uri imagePathUri,String fileName) {

        Log.d("TAG","storageDao uploadImage start");
        return storageReference.child(fileName).putFile(imagePathUri);
    }
    public Task<Uri> getImageUrlByName(String filename){
        return storageReference.child(filename).getDownloadUrl();
    }
//    public String GetFileExtension(Context context, Uri uri){
//        ContentResolver contentResolver=context.getContentResolver();
//        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
//        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
//    }
}
