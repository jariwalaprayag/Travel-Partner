package com.androidbroadcast.reciever;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Util {
    public static String makeCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        Log.d("url", "makeCall: " + url);
        String replyString = "";

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url
            HttpResponse response = httpclient.execute(httpget);
            InputStream is = response.getEntity().getContent();

            // buffer input stream the result
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(20);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            Log.d("Reply : ", "makeCall: " + baf.toString());
            // the result as a string is ready for parsing
            replyString = new String(baf.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(replyString);

        // trim the whitespaces
        return replyString.trim();
    }

    public static Bitmap makePhotoCall(String url) {

        // string buffers the url
        StringBuffer buffer_string = new StringBuffer(url);
        Log.d("PhotoUrl", "makePhotoCall: " + url);
        String replyString = "";
        Bitmap bmp = null;

        // instanciate an HttpClient
        HttpClient httpclient = new DefaultHttpClient();
        // instanciate an HttpGet
        HttpGet httpget = new HttpGet(buffer_string.toString());

        try {
            // get the responce of the httpclient execution of the url

            HttpResponse response = (HttpResponse) httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
           /*// Log.d("response", "makePhotoCall: "+response.getEntity().getContent().toString());

            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            InputStream instream = bufHttpEntity.getContent();
            Log.d("InStream", "makePhotoCall: "+instream.toString());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;*/
            bmp = BitmapFactory.decodeStream(entity.getContent());


        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(replyString);

        // trim the whitespaces
        return bmp;
    }
// public static makeWeatherCall()

    public static ArrayList<GooglePlace> parseGoogleParse(final String response) {

        ArrayList<GooglePlace> temp = new ArrayList<GooglePlace>();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("results")) {

                JSONArray jsonArray = jsonObject.getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    GooglePlace poi = new GooglePlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        poi.setName(jsonArray.getJSONObject(i).optString("name"));
                        poi.setRating(jsonArray.getJSONObject(i).optString("rating", " "));
                        poi.setPlaceId(jsonArray.getJSONObject(i).optString("id"));
                        poi.setLatitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lat"));
                        poi.setLongitude(jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location").optString("lng"));
                        poi.setFormatted_address(jsonArray.getJSONObject(i).optString("formatted_address"));
                        if (jsonArray.getJSONObject(i).has("opening_hours")) {
                            if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").has("open_now")) {
                                if (jsonArray.getJSONObject(i).getJSONObject("opening_hours").getString("open_now").equals("true")) {
                                    poi.setOpenNow("YES");
                                } else {
                                    poi.setOpenNow("NO");
                                }
                            }
                        } else {
                            poi.setOpenNow("Not Known");
                        }
                        if (jsonArray.getJSONObject(i).has("types")) {
                            JSONArray typesArray = jsonArray.getJSONObject(i).getJSONArray("types");
                            // poi.setCategory( "Tourist attraction" );
                            for (int j = 0; j < typesArray.length(); j++) {
                                if (!typesArray.getString(j).equalsIgnoreCase("point_of_interest") && !typesArray.getString(j).equalsIgnoreCase("establishment")) {
                                    //poi.setCategory("" + ", " + poi.getCategory());
                                    poi.setCategory(typesArray.getString(j) + ", " + poi.getCategory());

                                } else {
                                    continue;
                                }
                            }
                            if (poi.getCategory().length() == 0) {
                                poi.setCategory("Category : tourist Attraction");
                            } else {
                                char[] chrs = poi.getCategory().toCharArray();
                                char[] array2 = Arrays.copyOfRange(chrs, 0, chrs.length - 2);
                                poi.setCategory("Category : " + String.valueOf(array2));
                            }
                        }
                        if (jsonArray.getJSONObject(i).has("photos")) {
                            JSONArray photoArray = jsonArray.getJSONObject(i).getJSONArray("photos");
                            poi.setPhotoRefId(photoArray.getJSONObject(0).optString("photo_reference"));
                            Log.d("TestPhotoid", "parseGoogleParse: " + poi.getPhotoRefId());

                        }

                    }
                    temp.add(poi);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<GooglePlace>();
        }
        return temp;

    }

    public static ArrayList<WeatherModel> parseWeather(final String response) {

        ArrayList<WeatherModel> temp = new ArrayList<WeatherModel>();
        try {

            // make an jsonObject in order to parse the response
            JSONObject jsonObject = new JSONObject(response);

            // make an jsonObject in order to parse the response
            if (jsonObject.has("coord")) {
                WeatherModel poi = new WeatherModel();
                poi.setDescription(jsonObject.getJSONArray("weather").getJSONObject(0).optString("description"));
                poi.setTemperature(String.valueOf((int) Float.parseFloat(jsonObject.getJSONObject("main").optString("temp"))));
                poi.setWind(jsonObject.getJSONObject("wind").optString("speed"));
                temp.add(poi);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<WeatherModel>();
        }
        return temp;

    }

    public static String queryModifier(String query) {
        StringBuilder sb = new StringBuilder();
        query.trim();
        char[] chars = query.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != ' ') {
                sb.append(chars[i]);
            } else
                sb.append('+');
        }
        return sb.toString();

    }

    public static String getCityName(String location) {
        String[] locationSplit = location.split(",");
        String city = locationSplit[locationSplit.length - 3];
        return city;
    }
}

