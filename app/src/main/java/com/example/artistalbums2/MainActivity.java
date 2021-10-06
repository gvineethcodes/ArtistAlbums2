package com.example.artistalbums2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView parentRecyclerView = findViewById(R.id.Parent_recyclerView);
        LinearLayoutManager parentLayoutManager = new LinearLayoutManager(MainActivity.this);
        parentRecyclerView.setLayoutManager(parentLayoutManager);

        ArrayList<ParentModel> parentModelArrayList = new ArrayList<>();
        ParentRecyclerViewAdapter ParentAdapter = new ParentRecyclerViewAdapter(parentModelArrayList, MainActivity.this);
        parentRecyclerView.setAdapter(ParentAdapter);

        class ReadFile extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                HttpURLConnection conn = null;
                try {
                    String urlString = "https://docs.google.com/spreadsheets/d/1VxQruR4Yt1Ive6qLqQZ2iV7qCr4x9GRL49yA9XM5GP8/export?format=csv";

                    URL url = new URL(urlString);
                    conn = (HttpURLConnection) url.openConnection();
                    InputStream in = conn.getInputStream();
                    if(conn.getResponseCode() == 200)
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String inputLine;
                        while ((inputLine = br.readLine()) != null) {

                            String[] AS = inputLine.split("ALBUM");
                            Log.e("mylogArtist",AS[0].replace(",",""));
                            parentModelArrayList.add(new ParentModel(AS[0].replace(",","")));
                        }
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                parentRecyclerView.setAdapter(ParentAdapter);

                            }
                        });
                    }

                }catch (Exception e){
                    Log.e("mylog", e.toString());
                }
                finally
                {
                    if(conn!=null)
                        conn.disconnect();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {

            }

            @Override
            protected void onPreExecute() {}
        }

        new ReadFile().execute("");


//        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
//
//        mStorageRef.listAll()
//                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
//
//                    @Override
//                    public void onSuccess(ListResult listResult) {
//                        for (StorageReference prefix : listResult.getPrefixes()) {
//                            parentModelArrayList.add(new ParentModel(prefix.getName()));
//                        }
//                        parentRecyclerView.setAdapter(ParentAdapter);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        //text=e.getMessage();
//                    }
//                });

    }
}