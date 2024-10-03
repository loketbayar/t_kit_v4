package com.example.topwise;

import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.topwise.cloudpos.aidl.camera.AidlCameraScanCode;
import com.topwise.cloudpos.aidl.camera.AidlCameraScanCodeListener;
import com.topwise.cloudpos.data.AidlScanParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Created by topwise on 17-11-6.
 */

public class QrCodeScannerActivity   {
    private AidlCameraScanCode iScanner = null;
    private static final String TAG = "TPW-QrCodeScanner";
    private boolean HAS_CAMERA_FACING_FRONT = false;
    String data="";

    private static final String qrScanner= "qrScanner";



    public QrCodeScannerActivity(AidlCameraScanCode aidlCameraScanCode){
        iScanner = aidlCameraScanCode;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                HAS_CAMERA_FACING_FRONT = true;
                break;
            }
        }
    }

    public interface QrCodeScannerCallback {
        void onEventFinish(String value);
    }




    public void frontScan(QrCodeScannerCallback callback) {
        Log.d(TAG,"frontScan");
        if(HAS_CAMERA_FACING_FRONT) {

            Bundle bundle = new Bundle();
            bundle.putSerializable(AidlScanParam.SCAN_CODE,new AidlScanParam(Camera.CameraInfo.CAMERA_FACING_FRONT,10));
            try {
                iScanner.scanCode(bundle, new AidlCameraScanCodeListener.Stub() {
                    @Override
                    public void onResult(String s) throws RemoteException {
                        data = s;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onEventFinish(data);
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancel() throws RemoteException {
                        data = "Scan Cancelled";
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onEventFinish(data);
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(int i) throws RemoteException {
                        data = "Scan Error Code "+i;
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onEventFinish(data);
                                }
                            }
                        });
                    }

                    @Override
                    public void onTimeout() throws RemoteException {
                        data = "Scan Time Out";
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onEventFinish(data);
                                }
                            }
                        });
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
                data = e.toString();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
        } else {
            data = "Scan no front camera";
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onEventFinish(data);
                    }
                }
            });
        }
    }

    public void backScan(QrCodeScannerCallback callback) {
        Log.d(TAG,"backScan");

        Bundle bundle = new Bundle();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String amt = dtf.format(now);
        AidlScanParam param = new AidlScanParam(Camera.CameraInfo.CAMERA_FACING_BACK,60, "QR Scanner","Please scan the code",amt);
        bundle.putSerializable(AidlScanParam.SCAN_CODE, param);
        try {
            iScanner.scanCode(bundle, new AidlCameraScanCodeListener.Stub() {
                @Override
                public void onResult(String s) throws RemoteException {
                    data = s;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onEventFinish(data);
                            }
                        }
                    });
                }

                @Override
                public void onCancel() throws RemoteException {
                    data = "Scan Cancelled";
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onEventFinish(data);
                            }
                        }
                    });
                }

                @Override
                public void onError(int i) throws RemoteException {
                    data = "Scan Error Code "+i;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onEventFinish(data);
                            }
                        }
                    });
                }

                @Override
                public void onTimeout() throws RemoteException {
                    data = "Scan Time Out";
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onEventFinish(data);
                            }
                        }
                    });;
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            data = e.toString();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onEventFinish(data);
                    }
                }
            });
        }
    }

    public void stopScan(QrCodeScannerCallback callback) {
        Log.d(TAG,"stopScan");
        try {
            iScanner.stopScan();
            data = "Scanner Stopped";
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onEventFinish(data);
                    }
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            data = e.toString();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onEventFinish(data);
                    }
                }
            });
        }
    }

}
