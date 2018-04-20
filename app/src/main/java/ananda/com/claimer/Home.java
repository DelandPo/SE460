package ananda.com.claimer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    int TAKE_PHOTO_CODE = 0;
    public static int count = 10110;
    final Context context = this;
    public String itemName;
    public String itemCost;
    public String itemId;
    public  String modelNumber;
    public  String sereialNumber;
    public String roomType;
    public  DatabaseReference myRef;
    public String imagePaths;
    public StorageReference storageRef;
    public Uri imageDownloadUri;
    public   Uri outputFileUri;
    public int Counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        myRef = database.getReference();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/claimer/";
        Log.d("Directory",dir);
        File newdir = new File(dir);
        newdir.mkdirs();

        Button capture = (Button) findViewById(R.id.Takepicture);
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText ItemName = (EditText) findViewById(R.id.Itemname);
                itemName = ItemName.getText().toString();
                EditText ItemCost = (EditText) findViewById(R.id.Itemcost);
                itemCost = ItemCost.getText().toString();
                EditText ItemId = (EditText) findViewById(R.id.ItemId);
                itemId = ItemId.getText().toString();
                EditText ModelNumber = (EditText) findViewById(R.id.ModelNumber);
                modelNumber = ModelNumber.getText().toString();
                EditText SereialNum = (EditText) findViewById(R.id.SereialNumber);
                sereialNumber = SereialNum.getText().toString();
                EditText RoomTyp = (EditText) findViewById(R.id.RoomType);
                roomType = RoomTyp.getText().toString();

                count++;
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                String file = dir+timeStamp+".jpg";
                imagePaths = file;
                DataAccessLayer.imagePath = imagePaths;
                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                }
                catch (IOException e)
                {
                }
                outputFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider",newfile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        });
    }

    public void UploadItemsToFirebase(){
        Uri file= Uri.fromFile(new File("/storage/emulated/0/Pictures/claimer/1.jpg"));


        try{
             file = Uri.fromFile(new File(DataAccessLayer.imagePath));

        }
        catch(Exception ex){
            Log.d("Exception",ex.toString());

        }

        StorageReference imagesRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = imagesRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Items myItem = new Items(itemName,itemCost,itemId,modelNumber,sereialNumber,roomType,downloadUrl.toString());
                Map<String,Object> updates = new HashMap<>();
                updates.put(String.valueOf(Counter),myItem);
                myRef.child("Users").child(DataAccessLayer.userId).child("Items").updateChildren(updates);
                Counter++;

            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            UploadItemsToFirebase();
        }
    }
}