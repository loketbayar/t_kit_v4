package com.example.topwise;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.RemoteException;
import android.print.PrinterInfo;
import android.text.TextUtils;
import android.util.Log;
import android.util.Printer;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.topwise.action.ActionEmvProcess;
import com.example.topwise.action.ActionEnterPin;
import com.example.topwise.action.ActionOnline;
import com.example.topwise.core.AAction;
import com.example.topwise.core.ActionResult;
import com.example.topwise.device.Device;
import com.example.topwise.emv.entity.EinputType;
import com.example.topwise.emv.entity.EmvAidParam;
import com.example.topwise.emv.entity.EmvCapkParam;
import com.example.topwise.entity.TransData;
import com.example.topwise.param.AidParam;
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
import com.example.topwise.card.api.ICardReader;
import com.example.topwise.TopUsdkManage;
import com.example.topwise.card.impl.CardReader;
//import com.example.topwise.card.action.ActionEmvProcess;
//import com.example.topwise.card.action.ActionEnterPin;
//import com.example.topwise.card.action.ActionOnline;

// import com.example.topwise.action.ActionEmvProcess;
// import com.example.topwise.action.ActionEnterPin;
// import com.example.topwise.action.ActionOnline;
// import com.example.topwise.core.AAction;
// import com.example.topwise.core.ActionResult;
// import com.example.topwise.core.TransContext;
// import com.example.topwise.device.ConfiUtils;
// import com.example.topwise.device.Device;
// import com.example.topwise.entity.TransData;

// import com.example.topwise.emv.entity.EmvAidParam;
// import com.example.topwise.emv.entity.EmvCapkParam;

import com.topwise.toptool.api.ITool;
import com.topwise.toptool.api.packer.IPacker;
import com.topwise.toptool.impl.TopTool;
import com.example.topwise.param.CapkParam;
import com.example.topwise.param.SysParam;
import com.topwise.toptool.api.convert.IConvert;
import com.topwise.toptool.api.packer.ITlv;
import com.topwise.toptool.api.packer.TlvException;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private static final int NEXT_IC_PROCESS = 0x02;
    private static final int NEXT_RF_PROCESS = 0x03;
    private static final int NEXT_CHECK_PIN = 0x04;
    public final static int ONLINE = 0x05;
    public static final int SHOW = 0x99;
    public static final int INVOKE_RESULT = 0x100;
    public static final int POS_T1 = 0x01;  //0x01 T1  0x02 MP35P
    public static final int POS_MP35P = 0x02;
    public static int POS_MODE = POS_T1;

    static String dartChannel = "topwise";
    static String universaDartChannellCallback = "universalCallback";

    private MethodChannel channel;
    private DynamicPermissionTool permissionTool;
    private FlutterPluginBinding pluginBinding;
    private ActivityPluginBinding activityBinding;
    private Context context;
    private Activity activity;
    private final int REQUEST_CODE1 = 0;

    public static TopUsdkManage usdkManage;

    public static IConvert convert;

    public static IPacker packer;

    public static ITool topTool;

    public static SysParam sysParam;

    private TransData transData;

    public static List<EmvAidParam> aidList;
    public static List<EmvCapkParam> capkList;

    private MyHandler myHandler;

    class MyHandler extends Handler {
        WeakReference weakReference;

        public MyHandler(Activity activity) {
            weakReference = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
//                case NEXT_CHECK_CARD:
//                    String amount = (String) msg.obj;
//                    String format = String.format("%012d", Long.valueOf(amount));
//                    sendShow("AMOUNT: " + format + " $");
//                    transData.setAmount(format);
//                    String DataKsn = Device.autoAddDataKsn();
//                    String PinKsn = Device.autoAddPinKsn();
//                    Log.d("TAG", "DataKsn ======" + DataKsn);
//                    Log.d("TAG", "PinKsn ======" + PinKsn);
//                    transData.setDataKsn(DataKsn);
//                    transData.setPinKsn(PinKsn);
//
//                    checkCard();
//                    break;
                case NEXT_IC_PROCESS:
                case NEXT_RF_PROCESS:
                    Log.d("TopWiseLog", "gotoEmv()");
                    gotoEmv();
                    Log.d("TopWiseLog", "end gotoEmv()");
                    break;
                case NEXT_CHECK_PIN:

                    Log.d("TopWiseLog", "NEXT_CHECK_PIN PinKsn ======" + transData.getPinKsn());

                    ActionEnterPin actionEnterPin = new ActionEnterPin(new AAction.ActionStartListener() {
                        @Override
                        public void onStart(AAction action) {
                            ((ActionEnterPin) action).setParam(context, "title", transData.getPan(),
                                    transData.getAmount(), "",
                                    ActionEnterPin.ONLINE_PIN, 0);
                        }
                    });
                    actionEnterPin.setEndListener(new AAction.ActionEndListener() {
                        @Override
                        public void onEnd(AAction action, ActionResult result) {
                            if (0 == result.getRet()) {
                                String pinBlock = (String) result.getData();
                                transData.setField52(pinBlock);
                                transData.setHasPin(true);
                                if (!TextUtils.isEmpty(pinBlock)) {
//                                    sendShow("pinBlock: " + pinBlock);
                                }
                                //TODO online
//                                sendShow("GOTO online ========: ");
//                                onPrint();
                            }
                        }
                    });
                    actionEnterPin.execute();
                    break;
                case ONLINE:
                    TransData onlineTransData = (TransData) msg.obj;
                    if (onlineTransData == null) {
                        return;
                    }

                    ActionOnline actionOnline = new ActionOnline(new AAction.ActionStartListener() {
                        @Override
                        public void onStart(AAction action) {
                            ((ActionOnline) action).setParam(context, onlineTransData);
                        }
                    });
                    actionOnline.setEndListener(new AAction.ActionEndListener() {
                        @Override
                        public void onEnd(AAction action, ActionResult result) {
                            //check
                        }
                    });
                    actionOnline.execute();
                    break;
                case SHOW:
                    String show = (String) msg.obj;
                    Log.d("TopWiseLog show", show);
//                    saveMessage(show);
//                    Log.d("CARD RESULT", show);
                    break;
                case INVOKE_RESULT:
                    new MethodChannel(pluginBinding.getBinaryMessenger(), dartChannel)
                            .invokeMethod(universaDartChannellCallback, msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

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

        // Init toptool
        topTool = TopTool.getInstance();
        convert = topTool.getConvert();
        packer = topTool.getPacker();

        sysParam = SysParam.getInstance(context);

        usdkManage = TopUsdkManage.getInstance();
        Log.i(TAG, "init TopUsdkManage");
        usdkManage.init(context, new TopUsdkManage.InitListener() {
            @Override
            public void OnConnection(boolean ret) {
                Log.i(TAG, "init OnConnection " + ret);
                if (ret) {
                    Device.enableHomeAndRecent(true);
                } else {
                    Log.e(TAG, "SDK Bind Failed!");
                }
            }
        });

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

        if (call.method.equals("cancelSwipe")) {
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

            // Init capk and aidl
            List<String> list;
            CapkParam capkParam = new CapkParam();
            list = capkParam.init(context.getApplicationContext());
            capkList = capkParam.saveEmvCapkParam();
            AidParam aidParam = new AidParam();
            list = aidParam.init(context.getApplicationContext());
            aidList = aidParam.saveEmvAidParam();
            if (list == null) {
                Log.d(TAG, "InitCAPK failed");
                channel.invokeMethod("onCardError", "InitCAPK failed");
                return;
            } else {
                Log.d(TAG, "InitCAPK Success");
            }

            transData = transInit();

            // TODO: input amount from arguments?
            String amount = "10000";
            String format = String.format("%012d", Long.valueOf(amount));
            transData.setAmount(amount);
            String DataKsn = Device.autoAddDataKsn();
            String PinKsn = Device.autoAddPinKsn();
            Log.d(TAG, "DataKsn ======" + DataKsn);
            Log.d(TAG, "PinKsn ======" + PinKsn);
            transData.setDataKsn(DataKsn);
            transData.setPinKsn(PinKsn);


            boolean isMag = true;
            boolean isIcc = true;
            boolean isRf = false;
            int timeout = 60 * 1000;

            ICardReader iCardReader = usdkManage.getCardReader();

            iCardReader.startFindCard(isMag, isIcc, isRf, timeout, new CardReader.onReadCardListener() {
                @Override
                public void getReadState(CardData cardData) {
                    Log.d("FlutterPlugin", "Card data received");
                    iCardReader.close(false);
                    try {
//            setResult(cardData);
                        Log.e("TopWiseLog raw", cardData.toString());
                        if (CardData.EReturnType.OK == cardData.geteReturnType()) {
                            switch (cardData.geteCardType()) {
                                case IC:
                                    Log.d("CARD TYPE", "IC CARD");
                                    transData.setEnterMode(EinputType.IC);
                                    myHandler.sendEmptyMessage(NEXT_IC_PROCESS);
                                    break;
                                case RF:
                                    Log.d("CARD TYPE", "RF CARD");
                                    transData.setEnterMode(EinputType.RF);
                                    myHandler.sendEmptyMessage(NEXT_RF_PROCESS);
                                    break;
                                case MAG:
                                    // TODO: Mag
                                    break;
                            }
//                            // gotoEmv();
//                            Map<String, Object> cardResult = new HashMap<>();
//              cardResult.put("returnType", cardData.geteReturnType().toString());
//              cardResult.put("cardType", cardData.geteCardType().toString());
//              cardResult.put("track1", cardData.getTrack1());
//              cardResult.put("track2", cardData.getTrack2());
//              cardResult.put("track3", cardData.getTrack3());
//                            cardData = new CardData(CardData.EReturnType.OK, CardData.ECardType.IC);
//                            Log.e("data", cardData.toString());
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

//            Log.d("FlutterPlugin", "Card finding process started successfully");
//            result.success(null);
            return;
        }


        if (call.method.equals("openRFCard")) {
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

        if (call.method.equals("closeRFCard")) {
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

        if (call.method.equals("isRFCardExists")) {
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

        if (call.method.equals("getUidRFCard")) {
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

        if (call.method.equals("readRFCardType")) {
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
        if (call.method.equals("openQRScanner")) {
            AidlCameraScanCode cameraScanCode = DeviceServiceManager.getInstance().getCameraManager();

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

        if (call.method.equals("stopQRScanner")) {
            AidlCameraScanCode cameraScanCode = DeviceServiceManager.getInstance().getCameraManager();

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
        if (call.method.equals("getPrintState")) {
            AidlPrinter aidlPrinter = DeviceServiceManager.getInstance().getPrintManager();

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

        if (call.method.equals("printTickertape")) {
            AidlPrinter aidlPrinter = DeviceServiceManager.getInstance().getPrintManager();

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

        if (call.method.equals("printBalancePendingInformation")) {
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
        if (call.method.equals("getHardwareSN")) {
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
        if (requestCode == REQUEST_CODE1) {

            if (!isAllGranted) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }

    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        Log.d("onAttachedToActivity", "onAttachedToActivity");
        activityBinding = binding;
        activity = binding.getActivity();
        activityBinding.addRequestPermissionsResultListener(this);
        activityBinding.addActivityResultListener(this);
        myHandler = new MyHandler(activity);
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        sysParam = SysParam.getInstance(activity.getApplicationContext());
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

    public TransData transInit() {

        TransData transData = new TransData();
        transData.setMerchID("123456789012345");
        transData.setTermID("12345678");
        transData.setTransNo(123456);
        transData.setBatchNo(1);
        transData.setDate(getDate().substring(4));
        transData.setTime(getTime());
        transData.setDatetime(getDatetime());

        return transData;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    private String getDatetime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date);
    }

    private void gotoEmv() {
        Log.d("TopWiseLog start", "Start : " + transData.getPan());
        ActionEmvProcess actionEmvProcess = new ActionEmvProcess(new AAction.ActionStartListener() {
            @Override
            public void onStart(AAction action) {
                Log.d("TopWiseLog onStart AAction", "Start");
                ((ActionEmvProcess) action).setParam(context, myHandler, transData);
                Log.d("TopWiseLog onStart AAction", "End");
            }
        });
        Log.d("TopWiseLog start", "End");
        actionEmvProcess.setEndListener(new AAction.ActionEndListener() {
            @Override
            public void onEnd(AAction action, ActionResult result) {
                Log.d("TopWiseLog onEnd AAction", "Start");
                if (result.getData() != null) {
                    TransData emvTransData = (TransData) result.getData();
                    Log.d("TopWiseLog DE55", emvTransData.getSendIccData());
//                    sendShow("DE55: " + emvTransData.getSendIccData());
                    if (!TextUtils.isEmpty(emvTransData.getPan())) {
                        Log.d("TopWiseLog emv End PAN", emvTransData.getPan());
//                        sendShow("emv End PAN: " + emvTransData.getPan());
                    }
                    if (result.getRet() == 0) {
                        // TODO: print
//                        onPrint();
                    }

                    Message message = new Message();
                    message.what = INVOKE_RESULT;
                    message.obj = emvTransData.toJson();
                    myHandler.sendMessage(message);
                }
                Log.d("TopWiseLog onEnd AAction", "End");
            }
        });
        Log.d("TopWiseLog setEndListener", "End");
        actionEmvProcess.execute();
        Log.d("TopwWiseLog executeAction", "End");
    }

// TODO: print
//    public void onPrint() {
//        AidlPrinter printer = usdkManage.getPrinter();
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "topwise.ttf");
//        PrintTemplate.getInstance().init(this, typeface);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    int printerState = printer.getPrinterState();
//                    AppLog.d("AReceiptPrint", " printBitmap getPrinterState =" +printerState);
//                    //Printing grayscale needs to be controlled by parameters 0x01,0x02,0x03,0x04,
//                    // the larger the value, the darker the grayscale
//                    int printGray = 0x03;
//                    printer.setPrinterGray(printGray);
//                    printer.addRuiImage(generateBitmap(transData),0);
//                    start(printer);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }

}
