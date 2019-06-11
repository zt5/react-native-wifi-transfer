
# react-native-wifi-transfer
手机端开启一个http服务器 可以接收局域网上传的文件 保存到app文档中 支持ios和android

`web上传页面项目地址 https://github.com/zt5/uploadweb`

## Getting started

`$ npm install react-native-wifi-transfer --save`

### Mostly automatic installation

`$ react-native link react-native-wifi-transfer`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-wifi-transfer` and add `RNWifiTransfer.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNWifiTransfer.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.zt5.wifitrans.RNWifiTransferPackage;` to the imports at the top of the file
  - Add `new RNWifiTransferPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-wifi-transfer'
  	project(':react-native-wifi-transfer').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-wifi-transfer/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-wifi-transfer')
  	```


## Usage
```javascript
//导入模块
import RNWifiTransfer from 'react-native-wifi-transfer';

//接收新文件事件
RNWifiTransfer.Emitter.addListener(RNWifiTransfer.FILE_UPLOAD_NEW, (files:{name: string,path: string}[])=>{});

//开启服务器 直到找到可用端口
let port = 10000;
openConnect = () => {
    console.warn("开启端口" + port);
    RNWifiTransfer.start(port).then((ip) => {
        console.warn("开启成功" + ip);
    }).catch(err => {
        //打开错误码
        //1. wifi没有打开 RNWifiTransfer.ERROR_WIFI_NOT_OPEN
        //2. 打开错误 打开连接错误 RNWifiTransfer.ERROR_CONNECT_OPEN
        //3. 打开错误 端口被占用 RNWifiTransfer.ERROR_PORT_ALREADY_BIND
    
        if (err.code === RNWifiTransfer.ERROR_PORT_ALREADY_BIND) {
            const newPort = port + 1;
            console.warn("端口" + port + "被占用,尝试" + newPort);
            port = newPort;
            //端口被占用了
            setTimeout(openConnect, 300);
        } else {
            console.error(err);
        }
    });
};
openConnect();

//关闭服务器
RNWifiTransfer.close();

```
  
