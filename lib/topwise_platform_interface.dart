import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'topwise_method_channel.dart';

abstract class TopwisePlatform extends PlatformInterface {
  /// Constructs a TopwisePlatform.
  TopwisePlatform() : super(token: _token);

  static final Object _token = Object();

  static TopwisePlatform _instance = MethodChannelTopwise();

  /// The default instance of [TopwisePlatform] to use.
  ///
  /// Defaults to [MethodChannelTopwise].
  static TopwisePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [TopwisePlatform] when
  /// they register themselves.
  static set instance(TopwisePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion();

  Future<void> requestPermission();

  // Magnetic Stripe Feature
  Future<String?> swipeCard();

  Future<String?> cancelSwipe();
  // end Magnetic Stripe Feature

  // IC Card Feature
  Future<String?> openICCard();

  Future<String?> closeICCard();

  Future<String?> onFindMagCard();

  Future<String?> isICCardExist();
  // End IC Card Feature

  // RF Card Feature
  Future<String?> openRFCard();

  Future<String?> closeRFCard();

  Future<String?> apduComm();

  Future<String?> cardReset();

  Future<String?> isRFCardExists() {
    throw UnimplementedError('isRFCardExists() has not been implemented.');
  }

  Future<String?> getUidRFCard();

  Future<String?> readRFCardType();
  // End RF Card Feature

  // QR Scanner Feature
  Future<String?> openQRScanner();

  Future<String?> stopQRScanner();
  // End QR Scanner Feature

  // Printer Feature
  Future<String?> getPrintState();

  Future<String?> printTickertape();

  Future<String?> printBalanceInformation(Map<String, dynamic> data);

  Future<String?> printBalancePendingInformation(Map<String, dynamic> data);
  // End Printer Feature

  // Shell CMD
  Future<String?> getHardwareSN();
  // End Shell CMD

  //universal
  void universalCallback(Function callback);
}
