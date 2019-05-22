
package com.zt5.wifitrans;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RNWifiTransferModule extends ReactContextBaseJavaModule {

  private static final String ERROR_WIFI_NOT_OPEN = "1";
  private static final String ERROR_CONNECT_OPEN = "2";
  private static final String ERROR_PORT_ALREADY_BIND = "4";

  static final String FILE_UPLOAD_NEW = "FILE_UPLOAD_NEW";

  private HttpServerImpl httpServer;

  public RNWifiTransferModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }


  /**
   * 开启方法
   */
  @ReactMethod
  public void start(int port, Promise promise) {
    String ip = Utils.getHostIp(getReactApplicationContext());
    if (ip == null) {
      promise.reject(ERROR_WIFI_NOT_OPEN, "WIFI没有打开");
      return;
    }

    this.close();

    try {
      final String host = ip + ":" + port;
      this.openHttp(port);
      promise.resolve("http://" + host);
    } catch (IOException e) {
      //端口被占用
      this.close();
      promise.reject(ERROR_PORT_ALREADY_BIND, e);
    } catch (Exception e) {
      this.close();
      promise.reject(ERROR_CONNECT_OPEN, e);
    }
  }

  private void openHttp(int port) throws IOException {
    httpServer = new HttpServerImpl(getReactApplicationContext(), port);
    httpServer.start();
  }

  /**
   * 关闭方法
   */
  @ReactMethod
  public void close() {
    if (this.httpServer != null) {
      this.httpServer.stop();
      this.httpServer = null;
    }
  }


  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("ERROR_WIFI_NOT_OPEN", ERROR_WIFI_NOT_OPEN);
    constants.put("ERROR_CONNECT_OPEN", ERROR_CONNECT_OPEN);
    constants.put("ERROR_PORT_ALREADY_BIND", ERROR_PORT_ALREADY_BIND);

    constants.put("FILE_UPLOAD_NEW", FILE_UPLOAD_NEW);
    return constants;
  }


  @Override
  public String getName() {
    return "RNWifiTransfer";
  }
}