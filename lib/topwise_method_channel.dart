import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'topwise_platform_interface.dart';

/// An implementation of [TopwisePlatform] that uses method channels.
class MethodChannelTopwise extends TopwisePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('topwise');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<void> requestPermission() async {
    await methodChannel.invokeMethod<void>('requestPermission');
  }
  // Magnetic Stripe Feature

  @override
  Future<String?> swipeCard() async {
    return await methodChannel.invokeMethod<String>('swipeCard');
  }

  @override
  Future<String?> cancelSwipe() async {
    return await methodChannel.invokeMethod<String>('cancelSwipe');
  }
  // end Magnetic Stripe Feature

  // IC Card Feature
  @override
  Future<String?> openICCard() async {
    return await methodChannel.invokeMethod<String>('openICCard');
  }

  @override
  Future<String?> cardReset() async {
    return await methodChannel.invokeMethod<String>('cardReset');
  }

  @override
  Future<String?> closeICCard() async {
    return await methodChannel.invokeMethod<String>('closeICCard');
  }

  @override
  Future<String?> apduComm() async {
    return await methodChannel.invokeMethod<String>('apduComm');
  }

  @override
  Future<String?> isICCardExist() async {
    return await methodChannel.invokeMethod<String>('isICCardExist');
  }

  @override
  Future<String?> onFindMagCard() async {
    return await methodChannel.invokeMethod<String>('onFindMagCard');
  }
  // End IC Card Feature

  // RF Card Feature
  @override
  Future<String?> openRFCard() async {
    return await methodChannel.invokeMethod<String>('openRFCard');
  }

  @override
  Future<String?> closeRFCard() async {
    return await methodChannel.invokeMethod<String>('closeRFCard');
  }

  @override
  Future<String?> isRFCardExists() async {
    return await methodChannel.invokeMethod<String>('isRFCardExists');
  }

  @override
  Future<String?> getUidRFCard() async {
    return await methodChannel.invokeMethod<String>('getUidRFCard');
  }

  @override
  Future<String?> readRFCardType() async {
    return await methodChannel.invokeMethod<String>('readRFCardType');
  }
  // END RF Card Feature

  // QR Scanner Feature

  @override
  Future<String?> openQRScanner() async {
    return await methodChannel.invokeMethod<String>('openQRScanner');
  }

  @override
  Future<String?> stopQRScanner() async {
    return await methodChannel.invokeMethod<String>('stopQRScanner');
  }

  // End QR Scanner Feature

  // Printer Feature

  @override
  Future<String?> getPrintState() async {
    return await methodChannel.invokeMethod<String>('getPrintState');
  }

  @override
  Future<String?> printTickertape() async {
    return await methodChannel.invokeMethod<String>('printTickertape');
  }

  @override
  Future<String?> printBalanceInformation(Map<String, dynamic> data) async {
    return await methodChannel.invokeMethod<String>('printBalanceInformation', data);
  }

  @override
  Future<String?> printBalancePendingInformation(Map<String, dynamic> data) async {
    return await methodChannel.invokeMethod<String>('printBalancePendingInformation', data);
  }

  // End Printer Feature

  // Shell CMD
  @override
  Future<String?> getHardwareSN() async {
    return await methodChannel.invokeMethod<String>('getHardwareSN');
  }
  // End Shell CMD

  /// Because Single Instance Method Channel Only
  /// enable for handling single "setMethodCallHandler",
  /// then the data have to be constructed.
  ///
  /// Later if the plugin become more complex maybe
  /// we can create multiple method channel for better
  /// organizational benefit
  ///
  /// but now we will use only single method channel
  ///
  // TODO : every value come from method channel must be constructed to every topwise data type
  @override
  void universalCallback(Function callback) {
    methodChannel.setMethodCallHandler((call) async {
      if (call.method == 'universalCallback') {
        callback(call.arguments);
      }
    });
  }
}
