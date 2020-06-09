package com.androidbroadcast.reciever;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MyAdapter extends ArrayAdapter<GooglePlace> {

    ArrayList<GooglePlace> placesList = new ArrayList<GooglePlace>();
    ImageView imageView1;
    String photoId;
    Bitmap temp1;

    public MyAdapter(Context context, int textViewResourceId, ArrayList<GooglePlace> objects) {
        super(context, textViewResourceId, objects);
        placesList = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.row_layout, null);
        imageView1 = (ImageView) v.findViewById(R.id.imageView1);
        TextView textView1 = (TextView) v.findViewById(R.id.textView1);
        /*TextView textView2 = (TextView) v.findViewById(R.id.textView2);
         */
        TextView textView3 = (TextView) v.findViewById(R.id.textView3);
        textView1.setText(placesList.get(position).getName());
        //textView2.setText(placesList.get(position).getFormatted_address());
        textView3.setText(placesList.get(position).getCategory());
        photoId = placesList.get(position).getPhotoRefId();

        try {
            temp1 = new MyAdapter.googlephoto(new AsyncResponse() {
                public void processFinish(ArrayList output) {
                }
            }).execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        imageView1.setImageBitmap(temp1);

        return v;

    }

    private class googlephoto extends AsyncTask<View, Bitmap, Bitmap> {

        Bitmap temp;
        public AsyncResponse delegate = null;

        public googlephoto(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Bitmap doInBackground(View... urls) {
            // make Call to the url BitmapFactory.decodeResource(getResources(),R.drawable.android);
            temp = Util.makePhotoCall("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoId + "&key=" + GooglePlacesExample.GOOGLE_KEY);
            //Log.d("temp", "doInBackground: " + temp.toString());
            return temp;
        }

        @Override
        protected void onPreExecute() {
            // we can start a progress bar here
        }


        protected void onPostExecute(Bitmap tempResult) {
            if (temp == null) {
                // we have an error to the call
                // we can also stop the progress bar
                // Log.d("temp+1", "onPostExecute: " + temp);
            } else {
                // all things went right
                imageView1.setImageBitmap(temp);

            }

        }
    }

}