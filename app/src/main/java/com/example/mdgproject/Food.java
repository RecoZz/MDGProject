package com.example.mdgproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Food  {
    String[] foods={"chocolate","pizza","banana","apple","french fries", "guava", "strawberry","grapes"};
    private int index;
    private int Kcal;
    private Context context;
    private Point posFood= new Point();
    private Bitmap foodBit;
    private int bsize;
    private int bwide;
    private int bhigh;
    Food(Context context, int bhigh, int bwide, int bsize)
    {
        this.bhigh=bhigh;
        this.bwide=bwide;
        this.bsize=bsize;
        this.context=context;
        foodBit= BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        foodBit=Bitmap.createScaledBitmap(foodBit, bsize,bsize,true);

    }
    void setFoodPos()
    {
        Random random=new Random();
        index= random.nextInt(foods.length);

        RequestQueue queue= Volley.newRequestQueue(context);
        String url="https://edamam-food-and-grocery-database.p.rapidapi.com/parser?ingr="+foods[index];
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray=response.getJSONArray("parsed");
                    if(jsonArray.length()>0) {
                        JSONObject db0 = jsonArray.getJSONObject(0);
                        JSONObject food = db0.getJSONObject("food");
                        JSONObject nutrients = food.getJSONObject("nutrients");
                         int energy = nutrients.getInt("ENERC_KCAL");
                         Kcal=energy;
                        String img = food.getString("image");

                        Picasso.with(context).load(img).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                foodBit=bitmap;
                                foodBit=Bitmap.createScaledBitmap(foodBit, bsize,bsize,true);
                            }

                            @Override
                            public void onBitmapFailed(Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });


                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })

        {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("X-RapidAPI-Key", "c3936259b2msh77492d189783516p1e0b2djsna4df0e683aad");
                headers.put("X-RapidAPI-Host","edamam-food-and-grocery-database.p.rapidapi.com");
                return headers;
            }
        };
        queue.add(request);



        Random random1=new Random();
        posFood.x=random1.nextInt(bwide);
        posFood.y=random1.nextInt(bhigh);
    }
    public Point getPosFood()
    {
        return posFood;
    }
    public int getKcal(){return Kcal;}
    void drawable(Canvas canvas, Paint paint)
    {
        canvas.drawBitmap(foodBit, posFood.x *bsize, posFood.y * bsize, paint);
    }
}
