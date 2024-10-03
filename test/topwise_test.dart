import 'package:flutter_test/flutter_test.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:topwise/topwise.dart';
import 'package:topwise/topwise_method_channel.dart';
import 'package:topwise/topwise_platform_interface.dart';

class MockTopwisePlatform with MockPlatformInterfaceMixin implements TopwisePlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');

  @override
  Future<void> requestPermission() {
    // TODO: implement requestPermission
    throw UnimplementedError();
  }

  @override
  Future<String?> swipeCard() {
    // TODO: implement swipeCard
    throw UnimplementedError();
  }

  @override
  Future<String?> cancelSwipe() {
    // TODO: implement cancelSwipe
    throw UnimplementedError();
  }

  @override
  Future<String?> closeICCard() {
    // TODO: implement closeICCard
    throw UnimplementedError();
  }

  @override
  Future<String?> openICCard() {
    // TODO: implement openICCard
    throw UnimplementedError();
  }

  @override
  Future<String?> isICCardExist() {
    // TODO: implement isICCardExist
    throw UnimplementedError();
  }

  @override
  Future<String?> onFindMagCard() {
    // TODO: implement isICCardExist
    throw UnimplementedError();
  }

  @override
  Future<String?> closeRFCard() {
    // TODO: implement closeRFCard
    throw UnimplementedError();
  }

  @override
  Future<String?> getUidRFCard() {
    // TODO: implement getUidRFCard
    throw UnimplementedError();
  }

  @override
  Future<String?> apduComm() {
    // TODO: implement isRFCardExists
    throw UnimplementedError();
  }

  @override
  Future<String?> isRFCardExists() {
    // TODO: implement isRFCardExists
    throw UnimplementedError();
  }

  @override
  Future<String?> openRFCard() {
    // TODO: implement openRFCard
    throw UnimplementedError();
  }

  @override
  Future<String?> readRFCardType() {
    // TODO: implement readRFCardType
    throw UnimplementedError();
  }

  @override
  Future<String?> openQRScanner() {
    // TODO: implement openQRScanner
    throw UnimplementedError();
  }

  @override
  Future<String?> stopQRScanner() {
    // TODO: implement stopQRScanner
    throw UnimplementedError();
  }

  @override
  Future<String?> getPrintState() {
    // TODO: implement getPrintState
    throw UnimplementedError();
  }

  @override
  Future<String?> printTickertape() {
    // TODO: implement printTickertape
    throw UnimplementedError();
  }

  @override
  Future<String?> printBalanceInformation(Map<String, dynamic> data) {
    // TODO: implement printTickertape
    throw UnimplementedError();
  }

  @override
  Future<String?> printBalancePendingInformation(Map<String, dynamic> data) {
    // TODO: implement printTickertape
    throw UnimplementedError();
  }

  @override
  Future<String?> getHardwareSN() {
    // TODO: implement getHardwareSN
    throw UnimplementedError();
  }

  @override
  Future<String?> cardReset() {
    // TODO: implement getHardwareSN
    throw UnimplementedError();
  }

  @override
  void universalCallback(Function callback) {
    // TODO: implement universalCallback
  }
}

void main() {
  final TopwisePlatform initialPlatform = TopwisePlatform.instance;

  test('$MethodChannelTopwise is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelTopwise>());
  });

  test('getPlatformVersion', () async {
    Topwise topwisePlugin = Topwise();
    MockTopwisePlatform fakePlatform = MockTopwisePlatform();
    TopwisePlatform.instance = fakePlatform;

    expect(await topwisePlugin.getPlatformVersion(), '42');
  });
}
