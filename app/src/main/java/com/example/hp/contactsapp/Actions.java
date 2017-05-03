package com.example.hp.contactsapp;

import android.support.annotation.NonNull;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Actions {
    // ================= POST ===========================
    public static String save(ArrayList<Pair<String,String>> data ,String url) throws IOException {
        OkHttpClient client =new OkHttpClient();
        // ======= form
        FormBody.Builder fb =new FormBody.Builder();
        for (int i = 0 ;i<data.size();i++){
            fb.add(data.get(i).first,data.get(i).second);
        }
        FormBody form = fb.build();
        //========= requete
        Request.Builder rb = new Request.Builder();
        rb.url(url);
        rb.post(form);
        Request rq = rb.build();
        //======== executer requete
        return  client.newCall(rq).execute().body().string();
    }
    // ================= GETALL / GET ===========================
    public static ArrayList<Map<String,String>> getData( String url) throws IOException, JSONException {
        OkHttpClient client =new OkHttpClient();
        String json=null;
        ArrayList<Map<String,String>> data = new ArrayList<Map<String, String>>();
        //========= requete
        Request.Builder rb = new Request.Builder();
        rb.url(url);
        Request rq = rb.build();
        //======== executer requete
        json= client.newCall(rq).execute().body().string();
        JSONObject contacts = new JSONObject(json);
        JSONArray array = new JSONArray(contacts.getString("contact"));
        for (int i = 0;i<array.length();i++){
            Map<String,String> item =new HashMap<>();
            JSONObject contact = new JSONObject(array.getString(i));
            item.put("id",contact.getString("id"));
            item.put("nomPrenom",contact.getString("nomPrenom"));
            item.put("tel",contact.getString("tel"));
            item.put("email",contact.getString("email"));
            item.put("statut",contact.getString("statut"));
            data.add(item);
        }
        return data;
    }
    // ================= DELETE ===========================
    public static String delete( String url) throws IOException {
        OkHttpClient client =new OkHttpClient();
        String json=null;
        ArrayList<Map<String,String>> data = new ArrayList<Map<String, String>>();

        //========= requete
        Request.Builder rb = new Request.Builder();
        rb.url(url);
        Request rq = rb.build();
        //======== executer requete
        return client.newCall(rq).execute().body().string();
    }



}
