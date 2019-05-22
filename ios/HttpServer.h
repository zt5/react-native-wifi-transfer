//
// Created by zt on 2019-05-04.
// Copyright (c) 2019 zt. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "GCDWebUploader/GCDWebUploader.h"

typedef void(^evtCallBackBlock)(NSString *event, id body);

@interface HttpServer : NSObject <GCDWebUploaderDelegate> {
}
@property(nonatomic, strong) GCDWebUploader *webServer;

@property (nonatomic,strong) evtCallBackBlock sendEvent;

- (void)start:(NSUInteger)port resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;

- (void)close;

@end