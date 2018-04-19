package ananda.com.claimer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class viewItems extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference mDatabase;
    ArrayList<String> listItems=new ArrayList<String>();
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        listView = (ListView) findViewById(R.id.databaseListView);
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        listView.setAdapter(adapter);
        FirebaseDatabase.getInstance().getReference().child("Users").child(DataAccessLayer.userId).child("Items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();

                for(DataSnapshot postsnapshot: dataSnapshot.getChildren()){
                    Items myItems = postsnapshot.getValue(Items.class);
                    DataAccessLayer.AddItems(myItems);
                    Log.d("Tesla",myItems.getItemDetails());
                    listItems.add(myItems.getItemDetails());
                    adapter.notifyDataSetChanged();
                }
            }

        public void updateData(){


        }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }
}
