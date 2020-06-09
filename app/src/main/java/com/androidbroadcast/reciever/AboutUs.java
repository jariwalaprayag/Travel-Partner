package com.androidbroadcast.reciever;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutUs extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_homepage:
                //add the function to perform here
                Intent l1 = new Intent(AboutUs.this, GooglePlacesExample.class);
                startActivity(l1);
                return (true);
            case R.id.menu_logout:
                //add the function to perform here
                Intent l2 = new Intent(AboutUs.this, SignInActivity.class);
                startActivity(l2);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
}