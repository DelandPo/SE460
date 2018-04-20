package ananda.com.claimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LobbyActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Button mb = (Button)findViewById(R.id.addItems);
        Button bb = (Button)findViewById(R.id.viewItems);
        Button emailButton = (Button) findViewById(R.id.emailPDF);

        FirebaseDatabase.getInstance().getReference().child("Users").child(DataAccessLayer.userId).child("Items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();

                for(DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                    Items myItems = postsnapshot.getValue(Items.class);
                    DataAccessLayer.AddItems(myItems);
                }
            }

            public void updateData(){

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go To Email Activity
            }
        });

        mb.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LobbyActivity.this, Home.class);
                LobbyActivity.this.startActivity(myIntent);
            }
        });

        bb.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(LobbyActivity.this, viewItems.class);
                LobbyActivity.this.startActivity(myIntent);
            }
        });
    }
}
