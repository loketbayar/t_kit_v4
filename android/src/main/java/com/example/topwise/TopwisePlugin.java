package com.example.topwise;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.RemoteException;
import android.print.PrinterInfo;
import android.util.Log;
import android.util.Printer;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.topwise.cloudpos.aidl.camera.AidlCameraScanCode;
import com.topwise.cloudpos.aidl.iccard.AidlICCard;
import com.topwise.cloudpos.aidl.magcard.AidlMagCard;
import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.Align;
import com.topwise.cloudpos.aidl.printer.PrintItemObj;
import com.topwise.cloudpos.aidl.printer.PrintTemplate;
import com.topwise.cloudpos.aidl.printer.TextUnit;
import com.topwise.cloudpos.aidl.rfcard.AidlRFCard;
import com.topwise.cloudpos.aidl.shellmonitor.AidlShellMonitor;
import com.topwise.cloudpos.data.PrinterConstant;

import com.example.topwise.card.entity.CardData;
import com.example.topwise.InsertCard;
import com.example.topwise.card.api.ICardReader;
import com.example.topwise.TopUsdkManage;
import com.example.topwise.card.impl.CardReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Exception;


import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;


public class TopwisePlugin implements FlutterPlugin,
        MethodCallHandler,
        PluginRegistry.RequestPermissionsResultListener,
        PluginRegistry.ActivityResultListener,
        ActivityAware {

  static String dartChannel = "topwise";
  static String universaDartChannellCallback = "universalCallback";

  private MethodChannel channel;
  private DynamicPermissionTool permissionTool;
  private FlutterPluginBinding pluginBinding;
  private ActivityPluginBinding activityBinding;
  private Context context;
  private Activity activity;
  private final int    REQUEST_CODE1          = 0;

    private interface OperationOnPermission {
    void op(boolean granted, String permission);
  }

  private void performTaskWithCallback(MyCallback callback, String data) {

    callback.onTaskCompleted(data);
  }

  interface MyCallback {
    void onTaskCompleted(String data);
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    Log.d("onAttachedToEngine", "onAttachedToEngine: Topwise Plugin Attached To Engine");

    pluginBinding = flutterPluginBinding;

    this.context = (Application) pluginBinding.getApplicationContext();

    DeviceServiceManager.getInstance().bindDeviceService(this.context);
    permissionTool = new DynamicPermissionTool(this.context);
    
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), dartChannel);
    channel.setMethodCallHandler(this);

  }

  // TODO : construct every data needed to every feature
  // TODO : construct value to Magnetic Stripe (Swipe) data
  // TODO : construct value to IC Card data
  // TODO : construct value to RF Card (NFC Contacless) data
  // TODO : construct value to Scanner Camera (QR Scanner) data
  // TODO : construct value to printer data

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);

      return;
    }

    if (call.method.equals("requestPermission")) {
      requestPermission();

      return;
    }

    /*
      Magnetic Stripe Swipe
     */

    if (call.method.equals("swipeCard")) {
      AidlMagCard magCardDev = DeviceServiceManager.getInstance().getMagCardReader();

      new SwipeCardActivity(magCardDev).getTrackData(new SwipeCardActivity.SwipeCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }
    /*
      Magnetic Stripe Swipe
     */

    if (call.method.equals("onFindMagCard")) {
      AidlMagCard magCardDev = DeviceServiceManager.getInstance().getMagCardReader();

      new SwipeCardActivity(magCardDev).getTrackData(new SwipeCardActivity.SwipeCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("cancelSwipe")){
      AidlMagCard magCardDev = DeviceServiceManager.getInstance().getMagCardReader();

      new SwipeCardActivity(magCardDev).cancelSwipe(new SwipeCardActivity.SwipeCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }


  //    THIS IS FOR INSERT CARD

    if (call.method.equals("startFindCard")) {
      Log.d("FlutterPlugin", "startFindCard method called");

      boolean isMag = true;
      boolean isIcc = true;
      boolean isRf = false;
      int timeout = 60 * 1000;

      ICardReader iCardReader = TopUsdkManage.getInstance().getCardReader();

      iCardReader.startFindCard(isMag, isIcc, isRf, timeout, new CardReader.onReadCardListener() {
        @Override
        public void getReadState(CardData cardData) {
          Log.d("FlutterPlugin", "Card data received");

//          iCardReader.close(false);

          try {
            if (CardData.EReturnType.OK == cardData.geteReturnType()) {
              Map<String, Object> cardResult = new HashMap<>();
              cardResult.put("returnType", cardData.geteReturnType().toString());
              cardResult.put("cardType", cardData.geteCardType().toString());
              cardResult.put("track1", cardData.getTrack1());
              cardResult.put("track2", cardData.getTrack2());
              cardResult.put("track3", cardData.getTrack3());

              Log.d("FlutterPlugin", "Card Result: " + cardResult.toString());
              channel.invokeMethod("onCardRead", cardResult);
            } else {
              throw new NullPointerException("Data kartu kosong atau tidak valid.");
            }
          } catch (NullPointerException e) {
            Log.e("FlutterPlugin", "Kesalahan: Data kartu tidak valid. " + e.getMessage());
            channel.invokeMethod("onCardError", e.getMessage());
          } catch (Exception e) {
            Log.e("FlutterPlugin", "Unexpected error: " + e.getMessage());
            channel.invokeMethod("onCardError", "Unexpected error occurred.");
          }
        }
      });

      Log.d("FlutterPlugin", "Card finding process started successfully");
      result.success(null);
      return;
    }



    if (call.method.equals("openRFCard")){
      AidlRFCard rfcard = DeviceServiceManager.getInstance().getRfCardReader();

      new RFCardActivity(rfcard).open(new RFCardActivity.RFCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("closeRFCard")){
      AidlRFCard rfcard = DeviceServiceManager.getInstance().getRfCardReader();

      new RFCardActivity(rfcard).close(new RFCardActivity.RFCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("isRFCardExists")){
      AidlRFCard rfcard = DeviceServiceManager.getInstance().getRfCardReader();

      new RFCardActivity(rfcard).isExists(new RFCardActivity.RFCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("getUidRFCard")){
      AidlRFCard rfcard = DeviceServiceManager.getInstance().getRfCardReader();

      new RFCardActivity(rfcard).getUid(new RFCardActivity.RFCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("readRFCardType")){
      AidlRFCard rfcard = DeviceServiceManager.getInstance().getRfCardReader();

      new RFCardActivity(rfcard).readCardType(new RFCardActivity.RFCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    /*
      End RF Card
     */

    /*
      QR Code
     */
    if (call.method.equals("openQRScanner")){
      AidlCameraScanCode cameraScanCode =DeviceServiceManager.getInstance().getCameraManager();

      new QrCodeScannerActivity(cameraScanCode).backScan(new QrCodeScannerActivity.QrCodeScannerCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("stopQRScanner")){
      AidlCameraScanCode cameraScanCode =DeviceServiceManager.getInstance().getCameraManager();

      new QrCodeScannerActivity(cameraScanCode).stopScan(new QrCodeScannerActivity.QrCodeScannerCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }
    /*
      End QR Code
    */

    /*
      Printer
    */
    if (call.method.equals("getPrintState")){
      AidlPrinter aidlPrinter =DeviceServiceManager.getInstance().getPrintManager();

      new PrintDevActivity(aidlPrinter, this.context).getPrintState(new PrintDevActivity.PrintDevCallBack() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("printTickertape")){
      AidlPrinter aidlPrinter =DeviceServiceManager.getInstance().getPrintManager();

      new PrintDevActivity(aidlPrinter, this.context).printTickertape(new PrintDevActivity.PrintDevCallBack() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      }, this.context);

      result.success(null);
      return;
    }

    if (call.method.equals("printBalanceInformation")) {
      Map<String, Object> arguments = call.arguments();

      AidlPrinter aidlPrinter = DeviceServiceManager.getInstance().getPrintManager();
      new PrintDevActivity(aidlPrinter, this.context).printBalanceInformation(new PrintDevActivity.PrintDevCallBack() {
          @Override
          public void onEventFinish(String value) {
              new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                      .invokeMethod(universaDartChannellCallback, value);
          }
      }, this.context, arguments);

      result.success(null);
      return;
    }

     if (call.method.equals("printBalancePendingInformation")){
      Map<String, Object> arguments = call.arguments();
      AidlPrinter aidlPrinter = DeviceServiceManager.getInstance().getPrintManager();

      new PrintDevActivity(aidlPrinter, this.context).printBalancePendingInformation(new PrintDevActivity.PrintDevCallBack() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      }, this.context, arguments);

      result.success(null);
      return;
    }
    
    /*
      End Printer
    */

    /*
      Shell CMD
     */
      if(call.method.equals("getHardwareSN")){
        AidlShellMonitor aidlShellMonitor = DeviceServiceManager.getInstance().getShellMonitor();

        new ShellCmdActivity(aidlShellMonitor).getHardwareSNPlaintext(new ShellCmdActivity.ShellCmdCallback() {
          @Override
          public void onEventFinish(String value) {
            new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                    .invokeMethod(universaDartChannellCallback, value);
          }
        });

        result.success(null);
        return;
      }
    /*
      End Shell CMD
     */
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    DeviceServiceManager.getInstance().unBindDeviceService();
    channel.setMethodCallHandler(null);
  }


  @Override
  public boolean onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    return false;
  }

  @Override
  public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    boolean isAllGranted = permissionTool.isAllPermissionGranted(grantResults);
    if (requestCode == REQUEST_CODE1){

      if (!isAllGranted) {
        return false;
      }else {
        return  true;
      }
    }else{
     return  false;
    }

  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    Log.d("onAttachedToActivity", "onAttachedToActivity");
    activityBinding = binding;
    activity = binding.getActivity();
    activityBinding.addRequestPermissionsResultListener(this);
    activityBinding.addActivityResultListener(this);
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  private void requestPermission() {
    boolean isAllGranted = permissionTool.isAllPermissionGranted(permissionTool.permissions);

    if (!isAllGranted) {
      String[] deniedPermissions = permissionTool.getDeniedPermissions(permissionTool.permissions);
        permissionTool.requestNecessaryPermissions(activity, deniedPermissions, REQUEST_CODE1);
    }
  }

}
