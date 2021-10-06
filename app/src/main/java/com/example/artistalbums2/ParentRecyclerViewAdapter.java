package com.example.artistalbums2;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ParentRecyclerViewAdapter extends RecyclerView.Adapter<ParentRecyclerViewAdapter.MyViewHolder> {

    ArrayList<ParentModel> parentModelArrayList;
    Context context;
    public ParentRecyclerViewAdapter(ArrayList<ParentModel> parentModelArrayList, MainActivity mainActivity) {
        this.parentModelArrayList=parentModelArrayList;
        this.context=mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_recyclerview_items, parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView artist;
        public RecyclerView childRecyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);

            artist = itemView.findViewById(R.id.artist);
            childRecyclerView = itemView.findViewById(R.id.Child_RV);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ParentModel artist = parentModelArrayList.get(position);
        holder.artist.setText(artist.getArtist());

        LinearLayoutManager parentLayoutManager = new LinearLayoutManager(holder.childRecyclerView.getContext(),LinearLayoutManager.HORIZONTAL,false);
        holder.childRecyclerView.setLayoutManager(parentLayoutManager);

        ArrayList<ChildModel> arrayList = new ArrayList<>();
        ChildRecyclerViewAdapter childRecyclerViewAdapter = new ChildRecyclerViewAdapter(arrayList, holder.childRecyclerView.getContext());
        holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);



        class ReadFile extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(@NonNull String... params) {
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
                            //Log.e("mylogArtist",AS[0].replace(",",""));
                            for(String as : AS){
                                String albumName = " ",albumImage = " ";
                                ArrayList<String> albumItems = new ArrayList<>();
                                HashMap<String, String> albumItemLinks = new HashMap<>();

                                String[] AS1 = as.split(",");
                                if(AS1.length>1){
                                    albumName = AS1[1];
                                    albumImage = AS1[AS1.length-1];
                                    Log.e("mylogAlbum",AS1[1]);
                                    Log.e("mylogImage",AS1[AS1.length-1]);
                                }


                                for(int i =2;i<AS1.length-1;i++){
                                    if(i%2==0) {
                                        //Log.e("mylogItem " + i, AS1[i]);
                                        albumItems.add(AS1[i]);
                                    }
                                    else {
                                        //Log.e("mylogItemLink "+i,AS1[i]);
                                        albumItemLinks.put(AS1[i-1],AS1[i]);
                                    }

                                }
                                Log.e("mylogC", " "+artist.getArtist()+" "+ albumName+" "+ albumImage+" "+  albumItems+" "+  albumItemLinks);
                                if(albumItems.size()>0)
                                arrayList.add(new ChildModel(artist.getArtist(), albumName, albumImage, albumItems, albumItemLinks));
                            }
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);                            }
                        });
//                        holder.childRecyclerView.getContext().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);
//                            }
//                        });
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


        /*

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(artist.getArtist());
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(@NonNull ListResult listResult) {
                ArrayList<String> albumItems = new ArrayList<>();
                HashMap<String,String> hm = new HashMap<String,String>();
                String[] image = new String[1];

                for (StorageReference prefix : listResult.getPrefixes()) {

                    prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(@NonNull ListResult listResult) {

                            for (StorageReference item : listResult.getItems()){

                                if (!(item.getName().endsWith(".png") || item.getName().endsWith(".jpg"))){
                                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(@NonNull Uri uri) {
                                            albumItems.add(item.getName());
                                            hm.put(item.getName(),""+uri);
                                        }
                                    });
                                    Log.i("log",""+image[0]);

                                }else {
                                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(@NonNull Uri uri) {
                                            image[0] =""+uri;
                                        }
                                    });
                                }
                            }

                        }
                    });
                }
                Log.i("log",""+image[0]);
                Log.i("log",""+albumItems.size());
                for(String s:albumItems) Log.i("log",""+s);
                //arrayList.add(new ChildModel(artist.getArtist(), prefix.getName(), image[0], albumItems, hm));

                holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);
            }
        });


        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference(artist.getArtist());
        mStorageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference prefix : listResult.getPrefixes()) {

                    prefix.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                        @Override
                        public void onSuccess(ListResult listResult) {
                            ArrayList<String> albumItems = new ArrayList<>();
                            HashMap<String,String> hm = new HashMap<String,String>();
                            String[] image = new String[1];

                            for (StorageReference item : listResult.getItems()){

                                if (!(item.getName().endsWith(".png") || item.getName().endsWith(".jpg"))){
                                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            albumItems.add(item.getName());
                                            hm.put(item.getName(),""+uri);
                                        }
                                    });
                                }else {
                                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            image[0] =""+uri;
                                        }
                                    });
                                }
                            }
                            Log.i("log",""+image[0]);
                            Log.i("log",""+albumItems.size());
                            for(String s:albumItems) Log.i("log",""+s);
                            arrayList.add(new ChildModel(artist.getArtist(), prefix.getName(), image[0], albumItems, hm));
                        }
                    });

                }
                holder.childRecyclerView.setAdapter(childRecyclerViewAdapter);
            }
        });

         */
    }


    @Override
    public int getItemCount() {
        return parentModelArrayList.size();
    }
}
