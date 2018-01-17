package lugassi.wallach.client_android5778_2638_6575.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, Login.class);
        finish();
        MainActivity.this.startActivity(intent);
    }

}
