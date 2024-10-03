package com.example.topwise;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.topwise.cloudpos.aidl.magcard.AidlMagCard;
import com.topwise.cloudpos.aidl.magcard.EncryptMagCardListener;
import com.topwise.cloudpos.aidl.magcard.MagCardListener;
import com.topwise.cloudpos.aidl.magcard.TrackData;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;


public class SwipeCardActivity {

    private AidlMagCard magCardDev = null; // 磁条卡设备
    private boolean isSwipeCard = false;
    private final int timeOut =15 * 1000;
    private final byte keyIndex = 0x00;
    String data = "";

    private static final String magneticStripe= "magneticStripe";


    public SwipeCardActivity(AidlMagCard magCardDev) {
        this.magCardDev = magCardDev;
    }

    public interface SwipeCardCallback {
        void onEventFinish(String value);
    }

    /**
     * get swiped track data
     * open instance SDK for getting magnetic stripe detector
     */
    public void getTrackData(SwipeCardCallback callback) {

        if (magCardDev != null) {
            try {
                isSwipeCard = true;

                magCardDev.searchCard(timeOut, new MagCardListener.Stub() {
                    @Override
                    public void onTimeout() throws RemoteException {
                        isSwipeCard = false;
                        Log.d("Swipe Timeout", "onTimeout: ");

                        data = "Swipe Timeout";
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
                    public void onSuccess(TrackData trackData)
                            throws RemoteException {
                        isSwipeCard = false;

                        Map<String, String> dataMap = new HashMap<>();

                        dataMap.put("firstTrackData", trackData.getFirstTrackData());
                        dataMap.put("secondTrackData", trackData.getSecondTrackData());
                        dataMap.put("thirdTrackData", trackData.getThirdTrackData());
                        dataMap.put("cardNo", trackData.getCardno());
                        dataMap.put("expiryDate", trackData.getExpiryDate());
                        dataMap.put("formattedTrackData", trackData.getFormatTrackData());
                        dataMap.put("serviceCode", trackData.getServiceCode());

                        ObjectMapper objectMapper = new ObjectMapper();

                        try {
                            data = objectMapper.writeValueAsString(dataMap);

                            Log.d("onSuccess", "onSuccess: "+objectMapper.writeValueAsString(dataMap));

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onEventFinish(data);
                                    }
                                }
                            });

                        } catch (JsonProcessingException e) {
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

                    @Override
                    public void onGetTrackFail() throws RemoteException {
                        isSwipeCard = false;
                        Log.d("onGetTrackFail", "onGetTrackFail: ");

                        data = "Swipe Card Failed";

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
                    public void onError(int arg0) throws RemoteException {
                        isSwipeCard = false;
                        Log.d("onError", "onError: ");

                        data = "Swipe Card Error";

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
                    public void onCanceled() throws RemoteException {
                        isSwipeCard = false;
                        Log.d("onCanceled", "onCanceled: ");

                        data = "Swipe Card Cancelled";

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
                Log.e("getTrackData", "getTrackData: " +e.toString() );
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

    /**
     * get swiped encrypted card track data
     * open instance SDK for getting magnetic stripe detector
     */

    public void getEncryptTrackData(SwipeCardCallback callback) {
        try {
            if (null != magCardDev) {
                isSwipeCard = true;
                magCardDev.searchEncryptCard(timeOut, keyIndex, (byte) 0x00, null, (byte) 0x00, new EncryptMagCardListener.Stub() {

                    @Override
                    public void onTimeout() throws RemoteException {
                        isSwipeCard = false;
                        data = "Swipe Timeout";

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
                    public void onSuccess(String[] trackData) throws RemoteException {
                        isSwipeCard = false;
                        Map<String, String> dataMap = new HashMap<>();

                        dataMap.put("encryptedFormattedTrackData", trackData[0]);
                        dataMap.put("cardData", trackData[1]);

                        ObjectMapper objectMapper = new ObjectMapper();

                        try {
                            data = objectMapper.writeValueAsString(dataMap);

                            Log.d("onSuccess", "onSuccess: "+objectMapper.writeValueAsString(dataMap));

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        callback.onEventFinish(data);
                                    }
                                }
                            });

                        } catch (JsonProcessingException e) {
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

                    @Override
                    public void onGetTrackFail() throws RemoteException {
                        isSwipeCard = false;
                        Log.d("onGetTrackFail", "onGetTrackFail: ");

                        data = "Swipe Card Failed";

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
                    public void onError(int arg0) throws RemoteException {
                        isSwipeCard = false;
                        Log.d("onError", "onError: ");

                        data = "Swipe Card Error";

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
                    public void onCanceled() throws RemoteException {
                        isSwipeCard = false;
                        Log.d("onCanceled", "onCanceled: ");

                        data = "Swipe Card Cancelled";

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
            }
        } catch (RemoteException e) {
            Log.e("getTrackData", "getTrackData: " +e.toString() );
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

    //get encrypt format track data
    public void getEncryptFormatTrackData(SwipeCardCallback callback) {
        try {
            if (null != magCardDev) {
                isSwipeCard = true;
                magCardDev.searchEncryptCard(timeOut, keyIndex, (byte) 0x01,
                        HexUtil.hexStringToByte("1122334455667788"), (byte) 0x00,
                        new EncryptMagCardListener.Stub() {

                            @Override
                            public void onTimeout() throws RemoteException {
                                isSwipeCard = false;
                                data = "Swipe Timeout";

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
                            public void onSuccess(String[] trackData) throws RemoteException {
                                isSwipeCard = false;
                                Map<String, String> dataMap = new HashMap<>();

                                dataMap.put("encryptedFormattedTrackData", trackData[0]);
                                dataMap.put("cardData", trackData[1]);

                                ObjectMapper objectMapper = new ObjectMapper();

                                try {
                                    data = objectMapper.writeValueAsString(dataMap);

                                    Log.d("onSuccess", "onSuccess: "+objectMapper.writeValueAsString(dataMap));

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (callback != null) {
                                                callback.onEventFinish(data);
                                            }
                                        }
                                    });

                                } catch (JsonProcessingException e) {
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

                            @Override
                            public void onGetTrackFail() throws RemoteException {
                                isSwipeCard = false;
                                Log.d("onGetTrackFail", "onGetTrackFail: ");

                                data = "Swipe Card Failed";

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
                            public void onError(int arg0) throws RemoteException {
                                isSwipeCard = false;
                                Log.d("onError", "onError: ");

                                data = "Swipe Card Error";

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
                            public void onCanceled() throws RemoteException {
                                isSwipeCard = false;
                                Log.d("onCanceled", "onCanceled: ");

                                data = "Swipe Card Cancelled";

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
            }
        } catch (RemoteException e) {
            Log.e("getTrackData", "getTrackData: " +e.toString() );
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


    /**
     * cancel swipe
     */
    public void cancelSwipe(SwipeCardCallback callback) {
        if (!isSwipeCard) {
            data = "No Credit Card";
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onEventFinish(data);
                    }
                }
            });

            return;
        }
        isSwipeCard = false;
        if (null != magCardDev) {
            try {
                magCardDev.stopSearch();
                data = "Cancel Swipe";
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
                data = "Failed Cancel Swipe";
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


}
