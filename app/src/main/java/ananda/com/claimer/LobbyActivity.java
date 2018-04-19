package ananda.com.claimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Button mb = (Button)findViewById(R.id.addItems);
        Button bb = (Button)findViewById(R.id.viewItems);

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
