package com.example.tensorflowlite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.flatbuffers.Person;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.addLogAdapter(new AndroidLogAdapter());
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.68.102:5000";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            File temp = File.createTempFile("file", ".dat");
                            BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
                            bw.write(response);
                            bw.close();
                            //write data on temporary file
                            RandomAccessFile f = new RandomAccessFile(temp, "r");
                            byte[] data = new byte[(int)f.length()];
                            f.readFully(data);
                            f.close();
                            ByteBuffer bb = ByteBuffer.wrap(data);
                            Person person = Person.getRootAsPerson(bb);
                            Logger.d(person.name());
                            Logger.d("Hello");

                        } catch (IOException e) {
                            e.printStackTrace();
                            Logger.e(e.getMessage());

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.wtf("Done");
            }
        });

        queue.add(stringRequest);


    }
}
