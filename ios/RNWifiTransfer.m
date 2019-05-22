
#import "RNWifiTransfer.h"
#import "HttpServer.h"
#import "Reachability.h"


static NSString *ERROR_WIFI_NOT_OPEN = @"1";
static NSString *ERROR_CONNECT_OPEN = @"2";
static NSString *ERROR_PORT_ALREADY_BIND = @"4";
static NSString *FILE_UPLOAD_NEW = @"FILE_UPLOAD_NEW";


@implementation HttpServer


- (void)start:(NSUInteger)port resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    Reachability *connect = [Reachability reachabilityForInternetConnection];
    if ([connect currentReachabilityStatus] != ReachableViaWiFi) {
        reject(ERROR_WIFI_NOT_OPEN, @"必须开启wifi", nil);
        return;
    }
    
    
    // 文件存储位置
    NSString *documentsPath = [[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject] stringByAppendingPathComponent:@"wifi"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSError *error = nil;
    if (![fileManager fileExistsAtPath:documentsPath]) {
        if (![fileManager createDirectoryAtPath:documentsPath withIntermediateDirectories:YES attributes:nil error:&error]) {
            reject(ERROR_CONNECT_OPEN, @"创建目录失败", error);
            return;
        }
    }
    // 创建webServer,设置根目录
    self.webServer = [[GCDWebUploader alloc] initWithUploadDirectory:documentsPath];
    // 设置代理
    self.webServer.delegate = self;
    // 开启
    
    NSError *sererror = nil;
    
    NSMutableDictionary *options = [NSMutableDictionary dictionary];
    options[GCDWebServerOption_Port] = @(port);
    options[GCDWebServerOption_BonjourName] = nil;
    if (![self.webServer startWithOptions:options error:&sererror]) {
        NSString *errMsg = [NSString stringWithFormat:@"开启服务失败port[%zd]", port];
        reject(ERROR_CONNECT_OPEN, errMsg, sererror);
        return;
    } else {
        
        resolve(@[[self.webServer.serverURL absoluteString]]);
        return;
    }
}

- (void)close {
    if (self.webServer) {
        if (self.webServer.isRunning) {
            [self.webServer stop];
        }
        self.webServer = nil;
    }
}


#pragma mark - <GCDWebUploaderDelegate>

- (void)webUploader:(GCDWebUploader *)uploader didUploadFiles:(NSArray *)files {
    NSLog(@"[UPLOAD] %@",files);
    self.sendEvent(FILE_UPLOAD_NEW, files);
}

- (void)webUploader:(GCDWebUploader *)uploader didMoveItemFromPath:(NSString *)fromPath toPath:(NSString *)toPath {
    NSLog(@"[MOVE] %@ -> %@", fromPath, toPath);
}

- (void)webUploader:(GCDWebUploader *)uploader didDeleteItemAtPath:(NSString *)path {
    NSLog(@"[DELETE] %@", path);
}

- (void)webUploader:(GCDWebUploader *)uploader didCreateDirectoryAtPath:(NSString *)path {
    NSLog(@"[CREATE] %@", path);
}


- (void)dealloc {
    [self close];
}

@end



@implementation RNWifiTransfer{
    HttpServer *server;
}


RCT_EXPORT_METHOD(
                  start:
                  (NSUInteger) port
                  resolve:
                  (RCTPromiseResolveBlock) resolve
                  reject:
                  (RCTPromiseRejectBlock) reject
                  ) {
    [self close];
    server = [[HttpServer alloc] init];
    HttpServerModule __weak *tmp = self;
    server.sendEvent = ^(NSString *event, id body) {
        [tmp sendEventWithName:event body:body];
    };
    [server start:port resolve:resolve reject:reject];
}

RCT_EXPORT_METHOD(close) {
    if (server) {
        [server close];
        server = nil;
    }
}

- (NSArray<NSString *> *)supportedEvents {
    return @[FILE_UPLOAD_NEW];
}

- (NSDictionary *)constantsToExport {
    NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
    dic[@"ERROR_WIFI_NOT_OPEN"] = ERROR_WIFI_NOT_OPEN;
    dic[@"ERROR_CONNECT_OPEN"] = ERROR_CONNECT_OPEN;
    dic[@"ERROR_PORT_ALREADY_BIND"] = ERROR_PORT_ALREADY_BIND;
    dic[@"FILE_UPLOAD_NEW"] = FILE_UPLOAD_NEW;
    return dic;
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE()

@end
  
