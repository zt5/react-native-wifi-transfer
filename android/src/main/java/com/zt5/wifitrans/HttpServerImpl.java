package com.zt5.wifitrans;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HttpServerImpl extends NanoHTTPD {
    private static final Logger LOG = Logger.getLogger(HttpServerImpl.class.getName());

    private ReactApplicationContext context;


    /*
    主构造函数，也用来启动http服务
    */
    public HttpServerImpl(ReactApplicationContext context, int port) {
        super(port);
        this.context = context;
    }

    /*
    解析的主入口函数，所有请求从这里进，也从这里出
    */
    @Override
    public Response serve(IHTTPSession session) {

        String uri = session.getUri();

        if (uri.equals("/upload")) {
            return renderUpload(session);

        } else {
            return renderRes(session);
        }
    }

    private Response renderUpload(IHTTPSession session) {
        try {
            this.sendUploadFileToClient(session);
            return newFixedLengthResponse(Response.Status.OK, "text/plain", "ok");
        } catch (IOException | ResponseException e) {
            HttpServerImpl.LOG.log(Level.WARNING, "upload file error", e);
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", e.getMessage());
        }
    }

    private void sendUploadFileToClient(IHTTPSession session) throws IOException, ResponseException {

        Map<String, String> files = new HashMap<>();
        session.parseBody(files);

        Map<String, List<String>> parameters = session.getParameters();

        WritableArray params = Arguments.createArray();

        for (String key : files.keySet()) {
            List<String> nameStr = parameters.get(key);
            if (nameStr == null) continue;
            File tempFile = new File(files.get(key));
            File distFile = new File(this.context.getFilesDir().getAbsolutePath() + File.separatorChar + "wifi" + File.separatorChar + tempFile.getName());

            Utils.copyFile(tempFile, distFile);//NanoHttpd 会自动在请求完毕删除掉文件 所以克隆一份

            WritableMap map = Arguments.createMap();
            map.putString("name", nameStr.get(0));
            map.putString("path", distFile.getPath());
            params.pushMap(map);
        }
        this.context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(RNWifiTransferModule.FILE_UPLOAD_NEW, params);
    }

    private Response renderRes(IHTTPSession session) {
        String uri = session.getUri();
        String filename;
        if (uri.equals("/")) filename = "index.html";
        else filename = uri.substring(1);


        String mimeType = null;
        if (filename.endsWith(".html") || filename.endsWith(".htm")) {
            mimeType = "text/html";
        } else if (filename.endsWith(".js")) {
            mimeType = "text/javascript";
        } else if (filename.endsWith(".css")) {
            mimeType = "text/css";
        }
//        else if (filename.endsWith(".ico")) {
//            mimeType = "image/x-icon";
//        }
        if (mimeType == null) {
            return newFixedLengthResponse(Response.Status.BAD_REQUEST, "text/plain", "未知文件类型[" + filename + "]");
        }
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStreamReader = new InputStreamReader(context.getAssets().open(filename));
            reader = new BufferedReader(inputStreamReader);


            StringBuilder returnMsg = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                returnMsg.append(line);
            }
            return newFixedLengthResponse(Response.Status.OK, mimeType, returnMsg.toString());

        } catch (IOException e) {
            Utils.closeSilent(reader);
            Utils.closeSilent(inputStreamReader);
            e.printStackTrace();
            return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, "text/plain", e.getMessage());
        }

    }

}