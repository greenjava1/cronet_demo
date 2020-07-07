package com.tal.cronet_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import org.chromium.net.CronetEngine;
import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckAndRequestPersmission();
        CronetEngine.Builder myBuilder = new CronetEngine.Builder(getApplicationContext());
        CronetEngine cronetEngine = myBuilder.build();


// 网络状态回调
        class MyUrlRequestCallback extends UrlRequest.Callback {
            private static final String TAG = "MyUrlRequestCallback";

            @Override
            public void onFailed(UrlRequest urlRequest, UrlResponseInfo urlResponseInfo, CronetException e) {
                android.util.Log.i(TAG, "onRedirectReceived method called.");
            }

            @Override
            public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
                android.util.Log.i(TAG, "onRedirectReceived method called.");
                // You should call the request.followRedirect() method to continue
                // processing the request.
                request.followRedirect();
            }

            @Override
            public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
                android.util.Log.i(TAG, "onResponseStarted method called.");
                // You should call the request.read() method before the request can be
                // further processed. The following instruction provides a ByteBuffer object
                // with a capacity of 102400 bytes to the read() method.
                request.read(ByteBuffer.allocateDirect(102400));
            }

            @Override
            public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
                android.util.Log.i(TAG, "onReadCompleted method called.");
                // You should keep reading the request until there's no more data.
                request.read(ByteBuffer.allocateDirect(102400));
            }

            @Override
            public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
                android.util.Log.i(TAG, "onSucceeded method called.");
            }
        }
// 创建请求线程
        Executor executor = Executors.newSingleThreadExecutor();

// 创建UrlRequest
        UrlRequest.Builder requestBuilder = cronetEngine.newUrlRequestBuilder(
                "https://www.baidu.com", new MyUrlRequestCallback(), executor);
        UrlRequest request = requestBuilder.build();

// 发起请求
        request.start();


    }

    void CheckAndRequestPersmission()
    {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "请允许读写存储权限，否则APP无法正常运行！", Toast.LENGTH_SHORT).show();
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }

            //检测是否有写的权限
            int permission1 = ActivityCompat.checkSelfPermission(this,
                    "android.permission.INTERNET");
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "请允许读写存储权限，否则APP无法正常运行！", Toast.LENGTH_SHORT).show();
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},1);
            }

            //检测是否有写的权限
            int permission2 = ActivityCompat.checkSelfPermission(this,
                    "android.permission.ACCESS_NETWORK_STATE");
            if (permission2 != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "请允许读写存储权限，否则APP无法正常运行！", Toast.LENGTH_SHORT).show();
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
