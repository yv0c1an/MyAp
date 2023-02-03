package http;


import android.util.Log;

import java.util.Map;

import cn.hutool.core.net.NetUtil;
import fi.iki.elonen.NanoHTTPD;
import sys.FlyManager;

public class HttpSe extends NanoHTTPD {


    public HttpSe(int port) {
        super(port);
    }

    public HttpSe(String hostname, int port) {
        super(hostname, port);
    }

    @Override
    public Response serve(IHTTPSession session) {
        //return super.serve(session);
        if (session.getMethod().equals(Method.GET)) {
            String uri = session.getUri();
            if (uri != null && uri.equals("/fly")) {
                System.out.println("xxxxxxxxxxxxx");

                FlyManager.setSettingsOnHigh(1);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Log.e("M", e.getMessage(), e);
                }
                FlyManager.setSettingsOnHigh(2);
            }
        }

        return newFixedLengthResponse(NanoHTTPD.Response.Status.OK, "text/html", "切换成功");
    }


}
