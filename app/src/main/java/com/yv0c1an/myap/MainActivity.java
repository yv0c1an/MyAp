package com.yv0c1an.myap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.log.StaticLog;
import http.HttpSe;
import http.ShellUtil;
import libfrp.Libfrp;
import sys.ALPermissionManager;
import sys.FlyManager;


/**
 *
 */
public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.txtV);
        String apkRoot = "chmod 777 " + getPackageCodePath();
        ALPermissionManager.RootCommand(apkRoot);


    }

    public static int status = 0;

    public void start(View view) {
        Button btn = findViewById(R.id.button2);
        Button btn2 = findViewById(R.id.btnclose);
        startConn();
        if (Libfrp.isFrpcRunning(Libfrp.getFrp0420())) {
            Toast.makeText(getApplicationContext(), "开启成功", Toast.LENGTH_SHORT).show();
            btn.setEnabled(false);  //设置无法点击
            btn2.setEnabled(true);
        } else {
            btn.setText("开启");
            btn.setEnabled(true);
            Toast.makeText(getApplicationContext(), "开始失败,请查看日志.", Toast.LENGTH_SHORT).show();

        }
    }

    public void close(View view) {
        Button btn = findViewById(R.id.button2);
        Button btn2 = findViewById(R.id.btnclose);
        try {
            Libfrp.stopFrpc(Libfrp.getFrp0420());
            Toast.makeText(this.getApplicationContext(), "已关闭!", Toast.LENGTH_SHORT).show();
            btn.setEnabled(true);
        } catch (Exception e) {
            StaticLog.error(e);
            btn2.setEnabled(true);
        }
    }

    public void open(View view) {
        Button btn = findViewById(R.id.btnopen);
        if (status == 0) {
            HttpSe mHttpServer = new HttpSe(8080);
            try {
                if (mHttpServer.wasStarted())
                    mHttpServer.stop();
                mHttpServer.start();
                StaticLog.info("开启成功.....");
                String ini = "# frpc.ini\n" +
                        "[common]\n" +
                        "server_addr = 43.156.134.34\n" +
                        "server_port = 7000 \n" +
                        "[http_proxy]\n" +
                        "type = tcp\n" +
                        "remote_port = 6001\n" +
                        "plugin = http_proxy\n" +
                        "[http]\n" +
                        "type = tcp\n" +
                        "remote_port = 6002\n" +
                        "local_port = 8080";
                File f = FileUtil.writeString(ini, getDataDir() + "/config.ini", "UTF-8");
                if (Libfrp.isFrpcRunning(Libfrp.getFrp0420()))
                    Libfrp.stopFrpc(Libfrp.getFrp0420());
                Libfrp.runFrpc(getDataDir() + "/config.ini", Libfrp.getFrp0420());
                setContentView(R.layout.activity_main);
                TextView textView = (TextView) findViewById(R.id.txtV);
                if (textView != null)
                    textView.setText("API：http://43.156.134.34:6002");
                status = 1;
            } catch (IOException e) {
                Log.e("open IOException", e.getMessage(), e);
            } catch (Exception e) {
                Log.e("open Exception", e.getMessage(), e);
            }
        }
    }

    public void checkIniFile() {
        String path = getDataDir() + "/config.ini";
        File f = new File(path);
        if (!FileUtil.exist(FileUtil.file(path))) {
            f = FileUtil.newFile(path);
        }
        String ini = "[common]\n" +
                "server_addr = 43.156.134.34\n" +
                "server_port = 7000 \n" +
                "[http_proxy]\n" +
                "type = tcp\n" +
                "remote_port = 6000\n" +
                "plugin = http_proxy";
        f = FileUtil.writeString(ini, f, "UTF-8");
        System.out.println(FileUtil.readString(f, "UTF-8"));
    }

    private void startConn() {

//        Frpclib.touch();
        try {
            checkIniFile();
            Libfrp.runFrpc(getDataDir() + "/config.ini", Libfrp.getFrp0420());

        } catch (Exception e) {
            StaticLog.error(e);
        }

    }

    public void fly(View view) {
        FlyManager.setSettingsOnHigh(1);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Log.e("M", e.getMessage(), e);
        }
        FlyManager.setSettingsOnHigh(2);
    }

    /**
     * 远程获取INI文件
     *
     * @return
     */
    private String getIni(View view) {
        String url = "";
        return HttpRequest.get(url).execute().body().toString();
    }
}