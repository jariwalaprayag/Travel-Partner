package com.androidbroadcast.reciever;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GooglePlacesExample extends ListActivity {
    ArrayList<GooglePlace> venuesList;
    public static String placeId;
    public static final int REQ_VOICE = 1;

    final static String GOOGLE_KEY = "AIzaSyB2NP1cNPXmksIfAvBVioGmatLilKdIxn8";


    ArrayAdapter<String> myAdapter;
    ArrayList<GooglePlace> outputObtained;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //execute the async task
        new googleplaces(new AsyncResponse() {
            public void processFinish(ArrayList output) {
                // Log.d("", "processFinish: "+output);
                outputObtained = new ArrayList<GooglePlace>(output);
                // Log.d("Output", "processFinish: "+outputObtained.get(0).getPlaceId());

            }
        }).execute();


        ListView lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                // String value = (String)adapter.getItemAtPosition(position);
                Log.d("Value", "onItemClick: " + outputObtained.get(position).getPlaceId());
                Log.d("Value PlaceId", "onItemClick: " + outputObtained.get(position).getLatitude());
                Intent i = new Intent(GooglePlacesExample.this, PlaceDetail.class);
                i.putExtra("placeId", outputObtained.get(position).getPlaceId());
                i.putExtra("photoReferenceId", outputObtained.get(position).getPhotoRefId());
                i.putExtra("name", outputObtained.get(position).getName());
                i.putExtra("location", outputObtained.get(position).getFormatted_address());
                i.putExtra("rating", outputObtained.get(position).getRating());
                i.putExtra("category", outputObtained.get(position).getCategory());
                i.putExtra("openNow", outputObtained.get(position).getOpenNow());
                i.putExtra("lat", outputObtained.get(position).getLatitude());
                i.putExtra("lon", outputObtained.get(position).getLongitude());
                i.putExtra("openNow", outputObtained.get(position).getOpenNow());
                startActivity(i);
            }
        });
    }

    public void go(View view) {
        new googleplaces(new AsyncResponse() {
            public void processFinish(ArrayList output) {
                // Log.d("", "processFinish: "+output);
                outputObtained = new ArrayList<GooglePlace>(output);
                // Log.d("Output", "processFinish: "+outputObtained.get(0).getPlaceId());

            }
        }).execute();
    }

    private class googleplaces extends AsyncTask<View, Void, ArrayList> {

        String temp;
        public AsyncResponse delegate = null;

        public googleplaces(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interface through constructor
        }


        @Override
        protected ArrayList doInBackground(View... urls) {
            EditText query = (EditText) findViewById(R.id.searchEdit);

            @SuppressLint("WrongThread") String q = query.getText().toString();
            // make Call to the url
            temp = Util.makeCall("https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + Util.queryModifier(q) + "+point+of+interest+&language=en&key=" + GOOGLE_KEY);
            //Log.d("temp", "doInBackground: "+temp);
            return new ArrayList();
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }


        @Override
        protected void onPostExecute(ArrayList result) {
            if (temp == null) {
                Toast.makeText(getApplicationContext(), "No search result available", Toast.LENGTH_SHORT).show();
            } else {
                // all things went right

                // parse Google places search result
                venuesList = (ArrayList<GooglePlace>) Util.parseGoogleParse(temp);

                Collections.sort(venuesList, new Comparator<GooglePlace>() {
                    public int compare(GooglePlace lhs, GooglePlace rhs) {
                        Float a = Float.parseFloat(lhs.getRating());
                        Float b = Float.parseFloat(rhs.getRating());
                        return b.compareTo(a);
                    }
                });

                List<String> listTitle = new ArrayList<String>();

                for (int i = 0; i < venuesList.size(); i++) {
                    // make a list of the venus that are loaded in the list.
                    // show the name, the category and the city
                    Log.d("Array", "onPostExecute: " + venuesList.get(i).getCategory());
                    listTitle.add(i, venuesList.get(i).getName() + "\n" + venuesList.get(i).getRating() + "\n" + venuesList.get(i).getFormatted_address());

                }

                // set the results to the list
                // and show them in the xml
                Log.d("Output is : ", "onPostExecute: " + listTitle.toString());
                ListView simpleList = getListView();

                MyAdapter myAdapter = new MyAdapter(getBaseContext(), R.layout.row_layout, venuesList);
                simpleList.setAdapter(myAdapter);

                    /*myAdapter = new ArrayAdapter<String>(GooglePlacesExample.this, R.layout.row_layout, R.id.Name, listTitle);
                    setListAdapter(myAdapter);
                    */
                delegate.processFinish(venuesList);
            }
        }


    }

    public void voice(View view) {
        Log.d("Inside voice", "voice: " + "started");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {
            Log.d("Inside try", "voice: " + "started");
            startActivityForResult(intent, REQ_VOICE);
        } catch (Exception exception) {
            Toast.makeText(getApplicationContext(), "Error initializing speech to text engine.", Toast.LENGTH_LONG).show();
        }
    }

    public void listClicked(View view) {
        Toast.makeText(getApplicationContext(), "Currently Viewing HomePage", Toast.LENGTH_SHORT).show();
    }

    public void favClicked(View view) {
        Intent launchActivity1 = new Intent(GooglePlacesExample.this, FavMain.class);
        startActivity(launchActivity1);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && null != data) {
            // handles voice results
            if (requestCode == GooglePlacesExample.REQ_VOICE) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String query = result.get(0);
                Log.d("Recognizer result", "onActivityResult: " + query);
                Util.queryModifier(query);
                EditText actv = (EditText) findViewById(R.id.searchEdit);
                actv.setText(query);
            }

        }

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
                Intent l1 = new Intent(GooglePlacesExample.this, AboutUs.class);
                startActivity(l1);
                return (true);
            case R.id.menu_logout:
                //add the function to perform here
                Intent l2 = new Intent(GooglePlacesExample.this, SignInActivity.class);
                startActivity(l2);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

}

