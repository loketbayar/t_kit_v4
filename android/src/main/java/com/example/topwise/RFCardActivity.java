package com.example.topwise;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.View;

import com.topwise.cloudpos.aidl.rfcard.AidlRFCard;

/**
 * RF card test
 *
 * @author Tianxiaobo
 */
public class RFCardActivity {

    public AidlRFCard rfcard = null;
    String data = "";
    private static final String rfCard= "rfCard";



    public RFCardActivity(AidlRFCard aidlRFCard) {
        this.rfcard = aidlRFCard;
    }

    public interface RFCardCallback {
        void onEventFinish(String value);
    }

    /**
     * open RF card
     */
    public void open(RFCardCallback callback) {
        try {
            boolean flag = rfcard.open();
            if (flag) {
                data = "RF Card Success Opened";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {
                data = "RF Card Failed Opened";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
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

    /**
     * close RF card
     */
    public void close(RFCardCallback callback) {
        try {
            boolean flag = rfcard.close();
            if (flag) {
                data = "RF Card Success Closed";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {
                data = "RF Card Failed Closed";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
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

    /**
     * RF card is exist
     */
    public void isExists(RFCardCallback callback) {
        try {
            boolean flag = rfcard.isExist();
            if (flag) {
                data = "RF Card Is Exist";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {
                data = "RF Card Not Exist";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
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

    /**
     * RF card reset
     */
    public void reset(RFCardCallback callback) {
        try {
            int type = rfcard.getCardType();
            byte[] dataInternal = rfcard.reset(type);
            if (null != dataInternal) {
                data = HexUtil.bcd2str(dataInternal) +"   type " +type;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {
                data = "Reset RF Card Failed";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
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

    /**
     * halt
     */
    public void halt(RFCardCallback callback) {
        try {
            int ret = rfcard.halt();
            if (ret == 0x00) {

                data = "Halt RF Card Success";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {

                data = "Halt RF Card Failed";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
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

    /**
     *  read card type
     */
    public void readCardType(RFCardCallback callback) {
        try {
            int type = rfcard.getCardType();
            if (type == 0) {

                data = "Read RF Card Type Failed";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {

                data = "Read RF Card Type Success "+type;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            data = "Read RF Card Type Failed";
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
     *  send apdu  data
     */
    public void apduComm(RFCardCallback callback) {
        try {
            byte[] apdu = HexUtil
                    .hexStringToByte("00A404000E315041592E5359532E4444463031");
            byte[] dataInternal = rfcard.apduComm(apdu);
            if (dataInternal != null) {

                data = HexUtil.bcd2str(dataInternal);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {
                data ="APDU failed" ;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }
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

    /**
     * auth
     * @param v
     */
    public void auth(View v) {
        authCard();
    }

    /**
     * add value
     */
    public void addValue() {
        try {
            authCard();
            int retCode = rfcard.addValue((byte) 0x08, new byte[]{(byte) 0x00, (byte) 0x00,
                    (byte) 0x01, (byte) 0x00});
             // TODO : RF Card add value
            if (retCode == 0x00) {
//                showMessage(getResources().getString(R.string.rf_card_addvalue_success));
            } else {
//                showMessage(getResources().getString(R.string.rf_card_addvalue_fail));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * reduce Value
     */
    public void reduceValue() {
        try {
            // TODO : RF Card reduce value
            authCard();
            int retCode = rfcard.reduceValue((byte) 0x08, new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01});
//            showMessage(getResources().getString(R.string.rf_card_reducevalue_result) + retCode);
            if (retCode == 0x00) {
//                showMessage(getResources().getString(R.string.rf_card_reducevalue_success));
            } else {
//                showMessage(getResources().getString(R.string.rf_card_reducevalue_fail));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * read block data
     */
    public void readBlockData() {
        try {
            // TODO : RF Card readBlockData
            authCard();
            byte[] data = new byte[16];
            int length = rfcard.readBlock((byte) 0x08, data);
            if (length == 0x00) {
//                showMessage(getResources().getString(R.string.rf_card_blockdata_success) + HexUtil.bcd2str(data));
            } else {
//                showMessage(getResources().getString(R.string.rf_card_blockdata_fail));
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * write block
     */
    public void writeBlock() {
        try {
            // TODO : RF Card writeBlock

            authCard();
            int retCode = rfcard.writeBlock((byte) 0x08, new byte[]{
                    (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                    (byte) 0xFF, (byte) 0xFE, (byte) 0xFF, (byte) 0xFF,
                    (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                    (byte) 0x01, (byte) 0xFE, (byte) 0x01, (byte) 0xFE
            });
//            showMessage(getResources().getString(R.string.rf_card_write_blockdata_result) + retCode);
            if (retCode == 0x00) {
//                showMessage(getResources().getString(R.string.rf_card_write_blockdata_success));
            } else {
//                showMessage(getResources().getString(R.string.rf_card_write_blockdata_fail));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }



    /**
     * test get ATQA data
     *
     */
    public void getATQA() {
        // TODO : RF Card getATQA

        try {
            byte[] data = rfcard.getATQA();
            if (data == null ||data.length == 0) {
//                showMessage("getATQA FAIL");

            } else {
//                showMessage("getATQA SUCCESS. " + HexUtil.bcd2str(data));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * test activity ID card
     *
     */
    public void activateIDCard() {
        // TODO : RF Card activateIDCard

        try {
            int type = rfcard.getCardType();
            byte[] data = rfcard.activateTypeAOrIDCard(type);
            if (null != data) {
//                showMessage("activateIDCard SUCCESS. " + HexUtil.bcd2str(data));
            } else {
//                showMessage("activateIDCard FAIL");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getUid(RFCardCallback callback) {
        try {
            byte[] cardCode = rfcard.getUID();
            if (null != cardCode) {
                data =HexUtil.bcd2str(cardCode);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            } else {
                data ="Failed to get Uid";
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onEventFinish(data);
                        }
                    }
                });
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            data =e.toString();
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

    private void authCard(){
        // TODO : RF Card authCard
        try {
            int cardType = rfcard.getCardType();
            byte[] resetData = rfcard.reset(cardType);
            if (resetData == null) {
//                showMessage(getResources().getString(R.string.rf_card_auth_not_exist));
                return;
            }
            int retCode = 0;
            if (resetData.length > 4) {
                byte[] newData = new byte[4];
                System.arraycopy(resetData, resetData.length - 5, newData, 0, 4);
                retCode = rfcard.auth((byte) 0x00, (byte) 0x08, new byte[]{
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, newData);
            } else {
                retCode = rfcard.auth((byte) 0x00, (byte) 0x08, new byte[]{
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}, resetData);
            }

//            showMessage(getResources().getString(R.string.rf_card_auth_result) + retCode);
            if (0x00 == retCode) {
//                showMessage(getResources().getString(R.string.rf_card_auth_success));
//            } else {
//                showMessage(getResources().getString(R.string.rf_card_auth_fail));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
