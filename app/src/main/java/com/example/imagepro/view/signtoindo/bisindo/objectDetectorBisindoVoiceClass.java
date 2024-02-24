package com.example.imagepro.view.signtoindo.bisindo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class objectDetectorBisindoVoiceClass {
    private final Interpreter interpreter;
    private final int INPUT_SIZE;
    private final int PIXEL_SIZE=3;
    private final int IMAGE_MEAN=0;
    private final float IMAGE_STD=255.0f;
    private int height=0;
    private  int width=0;
    private int SignInputSize = 96;

    public objectDetectorBisindoVoiceClass(AssetManager assetManager, String modelPath, int inputSize) throws IOException{
        INPUT_SIZE = inputSize;
        interpreter = new Interpreter(loadModelFile(assetManager, modelPath), new Interpreter.Options());
    }

    private ByteBuffer loadModelFile(AssetManager assetManager, String modelPath) throws IOException {
        AssetFileDescriptor fileDescriptor=assetManager.openFd(modelPath);
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset =fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();

        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }

    public Mat recognizeImage(Mat mat_image){
        Mat rotated_mat_image = new Mat();
        Mat a = mat_image.t();
        Core.flip(a, rotated_mat_image, 1);
        a.release();

        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(rotated_mat_image.cols(), rotated_mat_image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rotated_mat_image, bitmap);
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        ByteBuffer byteBuffer = convertBitmapToByteBuffer1(scaledBitmap);
        Object[] input = new Object[1];
        input[0] = byteBuffer;

        Map<Integer, Object> output_map = new TreeMap<>();
        float[][] output_class_value = new float[1][1];

        output_map.put(0, output_class_value);

        interpreter.runForMultipleInputsOutputs(input, output_map);

        String sign_val = get_alphabets(output_class_value[0][0]);

        Imgproc.putText(rotated_mat_image, sign_val, new Point(10, 40), 2, 1.5, new Scalar(255, 255, 255, 255), 2);

        Mat b = rotated_mat_image.t();
        Core.flip(b, mat_image, 0);
        b.release();
        return mat_image;
    }

    public String stringImage(Mat mat_image) {
        Mat rotated_mat_image = new Mat();
        Mat a = mat_image.t();
        Core.flip(a, rotated_mat_image, 1);
        a.release();

        Bitmap bitmap = Bitmap.createBitmap(rotated_mat_image.cols(), rotated_mat_image.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(rotated_mat_image, bitmap);
        height = bitmap.getHeight();
        width = bitmap.getWidth();

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
        ByteBuffer byteBuffer = convertBitmapToByteBuffer1(scaledBitmap);
        Object[] input = new Object[1];
        input[0] = byteBuffer;

        Map<Integer, Object> output_map = new TreeMap<>();
        float[][] output_class_value = new float[1][1];
        output_map.put(0, output_class_value);

        interpreter.runForMultipleInputsOutputs(input, output_map);
        String sign_val = get_alphabets(output_class_value[0][0]);

        Mat b = rotated_mat_image.t();
        Core.flip(b, mat_image, 0);
        b.release();

        return sign_val;
    }


    private String get_alphabets(float v) {
        String[] alphabets = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        int index = Math.round(v);

        if (index >= 0 && index < alphabets.length) {
            return alphabets[index];
        }
        return "";
    }

    private ByteBuffer convertBitmapToByteBuffer1(Bitmap bitmap) {
        ByteBuffer byteBuffer;
        int quant=1;
        int size_images=SignInputSize;
        if(quant==0){
            byteBuffer=ByteBuffer.allocateDirect(size_images * size_images * 3);
        }
        else {
            byteBuffer=ByteBuffer.allocateDirect(4 * size_images * size_images * 3);
        }
        byteBuffer.order(ByteOrder.nativeOrder());
        int[] intValues=new int[size_images*size_images];
        bitmap.getPixels(intValues,0,bitmap.getWidth(),0,0,bitmap.getWidth(),bitmap.getHeight());
        int pixel=0;

        for (int i=0;i<size_images;++i){
            for (int j=0;j<size_images;++j){
                final  int val=intValues[pixel++];
                if(quant==0){
                    byteBuffer.put((byte) ((val>>16)&0xFF));
                    byteBuffer.put((byte) ((val>>8)&0xFF));
                    byteBuffer.put((byte) (val&0xFF));
                }
                else {
                    byteBuffer.putFloat((((val >> 16) & 0xFF)));
                    byteBuffer.putFloat((((val >> 8) & 0xFF)));
                    byteBuffer.putFloat((((val) & 0xFF)));
                }
            }
        }
        return byteBuffer;
    }
}