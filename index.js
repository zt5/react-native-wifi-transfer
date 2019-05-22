import {NativeModules, NativeEventEmitter} from "react-native";

const {RNWifiTransfer} = NativeModules;

const WifiTransfer = {
    /**打开错误 wifi没有打开*/
    ERROR_WIFI_NOT_OPEN: RNWifiTransfer.ERROR_WIFI_NOT_OPEN,
    /**打开错误 打开连接错误*/
    ERROR_CONNECT_OPEN: RNWifiTransfer.ERROR_CONNECT_OPEN,
    /**端口被占用*/
    ERROR_PORT_ALREADY_BIND: RNWifiTransfer.ERROR_PORT_ALREADY_BIND,
    /**接收到新文件 结构{name: string,path: string}*/
    FILE_UPLOAD_NEW: RNWifiTransfer.FILE_UPLOAD_NEW,
    /**事件侦听器*/
    Emitter: new NativeEventEmitter(RNWifiTransfer),
    /**关闭方法*/
    close: () => {
        RNWifiTransfer.close();
    },
    /**开启方法*/
    start: (port: number): Promise<string> => {
        return RNWifiTransfer.start(port);
    },
};
export default WifiTransfer;