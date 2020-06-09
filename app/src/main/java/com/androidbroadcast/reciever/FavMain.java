package com.androidbroadcast.reciever;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavMain extends ListActivity {

    List<DataController.FavsData> venuesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_main);
        ListView listView = getListView();
        DataController DC = new DataController(getBaseContext());
        DC.open();
        venuesList = DC.getFavList();


        List<String> listTitle = new ArrayList<String>();

        for (int i = 0; i < venuesList.size(); i++) {
            listTitle.add(i, venuesList.get(i).name + "\n" + venuesList.get(i).category);

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.fav_row_layout, R.id.textView1, listTitle);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent i = new Intent(FavMain.this, PlaceDetail.class);
                i.putExtra("placeId", venuesList.get(position).placeId);
                i.putExtra("photoReferenceId", venuesList.get(position).photoReferenceId);
                i.putExtra("name", venuesList.get(position).name);
                i.putExtra("location", venuesList.get(position).location);
                i.putExtra("rating", venuesList.get(position).rating);
                i.putExtra("category", venuesList.get(position).category);
                i.putExtra("openNow", venuesList.get(position).openNow);
                i.putExtra("lat", venuesList.get(position).lat);
                i.putExtra("lon", venuesList.get(position).lon);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                //add the function to perform here
                Intent l1 = new Intent(FavMain.this, AboutUs.class);
                startActivity(l1);
                return (true);
            case R.id.menu_logout:
                //add the function to perform here
                Intent l2 = new Intent(FavMain.this, SignInActivity.class);
                startActivity(l2);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    public void listClicked(View view) {
        Intent launchActivity1 = new Intent(FavMain.this, GooglePlacesExample.class);
        startActivity(launchActivity1);
    }

    public void favClicked(View view) {
        Toast.makeText(getApplicationContext(), "Currently Viewing Favourites", Toast.LENGTH_SHORT).show();
    }
}

