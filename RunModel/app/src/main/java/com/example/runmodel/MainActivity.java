package com.example.runmodel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;

import android.app.Activity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {
    public static String MODEL_FILENAME = "cube2.tflite";

    protected Interpreter tflite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.addLogAdapter(new AndroidLogAdapter());

        try {
            tflite = new Interpreter(loadModelFile(this));
            float[][] inp = new float[][]{{1, 2, 3}};
            float[][] out = new float[][]{{0, 0, 0}};
            tflite.run(inp, out);
            Logger.d("testing");

        } catch (IOException e) {
            e.printStackTrace();
            Logger.e(Objects.requireNonNull(e.getMessage()));
        }


    }


    /**
     * Memory-map the model file in Assets.
     */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_FILENAME);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
}
