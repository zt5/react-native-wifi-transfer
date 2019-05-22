
# react-native-wifi-transfer

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
import RNWifiTransfer from 'react-native-wifi-transfer';

// TODO: What to do with the module?
RNWifiTransfer;
```
  