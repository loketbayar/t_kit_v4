import 'dart:async';
import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:topwise/topwise.dart';
import 'package:topwise/topwise_method_channel.dart';

final ValueNotifier<String> somethingDataFromChannel = ValueNotifier('');

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  MethodChannelTopwise().universalCallback((data) {
    somethingDataFromChannel.value = data;
    log('$data', name: "universalCallback");
  });

  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _topwisePlugin = Topwise();

  @override
  void initState() {
    super.initState();

    initPlatformState();
  }

  Future<void> initPlatformState() async {
    String platformVersion;

    try {
      platformVersion = await _topwisePlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  Future<void> initPermission() async {
    try {
      await _topwisePlugin.requestPermission();
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> swipe() async {
    try {
      var data = await _topwisePlugin.swipeCard();
      log('$data', name: 'swipeCard');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> cancelSwipe() async {
    try {
      var data = await _topwisePlugin.cancelSwipe();
      log('$data', name: 'cancelSwipe');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> openICCard() async {
    try {
      var data = await _topwisePlugin.openICCard();
      log('$data', name: 'openICCard');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> closeICCard() async {
    try {
      var data = await _topwisePlugin.closeICCard();
      log('$data', name: 'closeICCard');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> onFindMagCard() async {
    try {
      var data = await _topwisePlugin.onFindMagCard();
      log('$data', name: 'onFindMagCard');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> isICCardExist() async {
    try {
      var data = await _topwisePlugin.isICCardExist();
      log('$data', name: 'isICCardExist');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> openRFCard() async {
    try {
      var data = await _topwisePlugin.openRFCard();
      log('$data', name: 'openRFCard');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> closeRFCard() async {
    try {
      var data = await _topwisePlugin.closeRFCard();
      log('$data', name: 'openRFCard');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> isRFCardExists() async {
    try {
      var data = await _topwisePlugin.isRFCardExists();
      log('$data', name: 'isRFCardExists');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> getUidRFCard() async {
    try {
      var data = await _topwisePlugin.getUidRFCard();
      log('$data', name: 'getUidRFCard');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> readRFCardType() async {
    try {
      var data = await _topwisePlugin.readRFCardType();
      log('$data', name: 'readRFCardType');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> openQRScanner() async {
    try {
      var data = await _topwisePlugin.openQRScanner();
      log('$data', name: 'openQRScanner');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> stopQRScanner() async {
    try {
      var data = await _topwisePlugin.stopQRScanner();
      log('$data', name: 'stopQRScanner');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> getPrintState() async {
    try {
      var data = await _topwisePlugin.getPrintState();
      log('$data', name: 'getPrintState');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> printTickertape() async {
    try {
      var data = await _topwisePlugin.printTickertape();
      log('$data', name: 'printTickertape');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> printBalanceInformation(Map<String, dynamic> datamodel) async {
    try {
      var data = await _topwisePlugin.printBalanceInformation(datamodel);
      log('$data', name: 'printBalanceInformation');
    } catch (e) {
      log(e.toString());
    }
  }

  Future<void> getHardwareSN() async {
    try {
      var data = await _topwisePlugin.getHardwareSN();
      log('$data', name: 'getHardwareSN');
    } catch (e) {
      log(e.toString());
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView(
          padding: const EdgeInsets.all(20),
          children: [
            Text('Running on: $_platformVersion\n'),
            ValueListenableBuilder(
              valueListenable: somethingDataFromChannel,
              builder: (_, __, ___) => Text(somethingDataFromChannel.value),
            ),
            TextButton(
                onPressed: () async {
                  await initPermission();
                },
                child: const Text('asking permission')),
            TextButton(
                onPressed: () async {
                  await swipe();
                },
                child: const Text('swipe ')),
            TextButton(
                onPressed: () async {
                  await cancelSwipe();
                },
                child: const Text('cancel swipe')),
            TextButton(
                onPressed: () async {
                  await openICCard();
                },
                child: const Text('open IC Card')),
            TextButton(
                onPressed: () async {
                  await closeICCard();
                },
                child: const Text('close IC Card')),
            TextButton(
                onPressed: () async {
                  await isICCardExist();
                },
                child: const Text('check IC Card')),
            TextButton(
                onPressed: () async {
                  await openRFCard();
                },
                child: const Text('open RF Card')),
            TextButton(
                onPressed: () async {
                  await closeRFCard();
                },
                child: const Text('close RF Card')),
            TextButton(
                onPressed: () async {
                  await isRFCardExists();
                },
                child: const Text('check RF Card ')),
            TextButton(
                onPressed: () async {
                  await getUidRFCard();
                },
                child: const Text('get Uid RF Card ')),
            TextButton(
                onPressed: () async {
                  await readRFCardType();
                },
                child: const Text('get  RF Card type')),
            TextButton(
                onPressed: () async {
                  await openQRScanner();
                },
                child: const Text('open QR Scanner')),
            TextButton(
                onPressed: () async {
                  await stopQRScanner();
                },
                child: const Text('stop QR Scanner')),
            TextButton(
                onPressed: () async {
                  await getPrintState();
                },
                child: const Text('get Printer Status')),
            TextButton(
                onPressed: () async {
                  await printTickertape();
                },
                child: const Text('print Ticker Tape')),
            TextButton(
                onPressed: () async {
                  await getHardwareSN();
                },
                child: const Text('get Hardware Serial Number')),
          ],
        ),
      ),
    );
  }
}
