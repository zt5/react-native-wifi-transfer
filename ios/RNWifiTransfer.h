
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import <Foundation/Foundation.h>
#import <React/RCTEventEmitter.h>

@interface RNWifiTransfer : NSObject <RCTBridgeModule>

@end
  
