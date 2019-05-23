package com.romanport.rpws;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpTool {

    public static <T> void SendGet(final Context c, String url, final Class<T> ty, final Response.Listener<T> callback, final Response.Listener<VolleyError> failCallback) {
        SendAuthenticatedRequest(c, url, callback, failCallback, ty, null, Request.Method.GET );
    }

    ///Generic send request
    static <T> void SendAuthenticatedRequest(Context c, final String url, final Response.Listener<T> callback, final Response.Listener<VolleyError> failCallback, final Class<T> ty, final byte[] body, final int method) {
        //Send request
        RequestQueue queue = Volley.newRequestQueue(c);
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Decode this as JSON

                        try {
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            T ee = gson.fromJson(response, ty);
                            callback.onResponse(ee);
                        } catch (Exception ex) {
                            RpwsLog.LogException("network-failure-json", "Failed to deserialize JSON data. URL: "+url+" Error: ", ex);
                            failCallback.onResponse(null);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //There was an error.
                RpwsLog.Log("network-failure-net", "Server error. URL: "+url);
                if(failCallback != null) {
                    failCallback.onResponse(error);
                }
            }


        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return body;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Get access token
                String token = "8zpZMKoa2JGJGw6SNOl9KiCLgc8Td56zChL5MXWprZ"; //TODO: Add auth. This is just a token for my testing account. Not the first time it's been published...

                //Set auth headers
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+token);

                return params;
            }
        };

        queue.add(stringRequest);
    }
}

