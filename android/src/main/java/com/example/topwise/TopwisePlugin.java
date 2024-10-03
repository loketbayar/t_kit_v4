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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;



/** TopwisePlugin */
public class TopwisePlugin implements FlutterPlugin,
        MethodCallHandler,
        PluginRegistry.RequestPermissionsResultListener,
        PluginRegistry.ActivityResultListener,
        ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
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

    /*
      End Magnetic Stripe Swipe
     */

    /*
      IC Card
     */

    if (call.method.equals("openICCard")){
      AidlICCard icCard = DeviceServiceManager.getInstance().getICCardReader();

      new ICCardActivity(icCard).open(new ICCardActivity.ICCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("cardReset")){
      AidlICCard icCard = DeviceServiceManager.getInstance().getICCardReader();

      new ICCardActivity(icCard).cardReset(new ICCardActivity.ICCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("apduComm")){
      AidlICCard icCard = DeviceServiceManager.getInstance().getICCardReader();

      new ICCardActivity(icCard).apduComm(new ICCardActivity.ICCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("closeICCard")){
      AidlICCard icCard = DeviceServiceManager.getInstance().getICCardReader();

      new ICCardActivity(icCard).close(new ICCardActivity.ICCardCallback() {
        @Override
        public void onEventFinish(String value) {
          new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                  .invokeMethod(universaDartChannellCallback, value);
        }
      });

      result.success(null);
      return;
    }

    if (call.method.equals("isICCardExist")){
      AidlICCard icCard = DeviceServiceManager.getInstance().getICCardReader();

      new ICCardActivity(icCard).isExists(new ICCardActivity.ICCardCallback() {
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
      End IC Card
     */

    /*
      RF Card
     */

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

//       Map<String, Object> base64String = call.argument<String>("base64image");
//       val decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
//       val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size);

      testPrint(aidlPrinter);

       // Second choice if testPrint() is not working
       // see https://github.com/devmedtz/flutter_topwise/blob/main/android/src/main/kotlin/com/ubx/flutter_topwise/MethodCallHandlerImpl.kt#L48
       // https://github.com/devmedtz/flutter_topwise/blob/main/android/src/main/kotlin/com/ubx/flutter_topwise/MethodCallHandlerImpl.kt#L430
//       PrintTemplate printTemplate = com.topwise.cloudpos.aidl.printer.PrintTemplate();
//       printTemplate.add(new TextUnit("Test", TextUnit.TextSize.NORMAL, Align.CENTER));

//      aidlPrinter.addText(29, , PrinterConstant.FontSize.NORMAL, "Test");

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

  public void testPrint(AidlPrinter aidlPrinter){
    if(aidlPrinter!=null) {
      try {
        List<PrintItemObj> data = new ArrayList<PrintItemObj>();
        PrintItemObj printItemObj1 = new PrintItemObj("Test Print 1");
        PrintItemObj printItemObj2 = new PrintItemObj("Test Print 1");

//        Log.i("PrinterState:" + aidlPrinter.getPrinterState());
        data.add(printItemObj1);
        data.add(printItemObj2);
        aidlPrinter.printText(data, printListener);
      } catch (RemoteException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };

}
