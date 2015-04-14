package com.example.jum.averagefoodprices;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
/**
 * Created by James boyer
 */
public class DownloadImage extends AsyncTask<String, Void, Bitmap>{
    ImageView iv;
    public DownloadImage(ImageView iv){
        this.iv = iv;
    }
    protected Bitmap doInBackground(String ... url){

        Bitmap bitmap = null;

            String link = getImageUrl(url[0]);
            try {
                InputStream in = new URL(link).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (IOException e) {

            }

        return bitmap;
    }
    protected void onPostExecute(Bitmap res){
        iv.setImageBitmap(Bitmap.createScaledBitmap(res,iv.getWidth(),iv.getHeight(),false));
    }

    public String getImageUrl(String url){
        String all = "";
        try {

            URL site = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(site.openStream()));
            String line;
            String json= "";
            while ((line = reader.readLine()) != null) {
                json +=line;
            }
            JSONObject jsonobj = new JSONObject(json);
            jsonobj = jsonobj.getJSONObject("responseData");

            JSONArray arr = jsonobj.getJSONArray("results");
            for(int i = 0; i <arr.length(); i++) {
                JSONObject img1 = arr.getJSONObject(i);
                //This was added because it was having trouble downloading large files
                if(Integer.parseInt(img1.getString("width"))> 2500 || Integer.parseInt(img1.getString("height")) > 2500){

                }else{
                    all = img1.getString("url");
                    //added because sometimes the url wouldn't be an image, causing program to crash
                    if(all.matches("(?i).*\\.(jpe?g|png|bmp)")){
                        i = arr.length();
                    }
                }
            }

        }catch(Exception e){

        }
        return all;

    }
}
