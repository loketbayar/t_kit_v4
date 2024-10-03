package com.example.topwise.emv.utlis;

import android.util.Log;

import com.example.topwise.AppLog;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.TlvList;
import com.example.topwise.emv.entity.EmvCapkParam;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 交易全局数据存储类
 *
 * @author xukun
 * @version 1.0.0
 * @date 19-9-17
 */

public class PayDataUtil {

    private static final String TAG = PayDataUtil.class.getSimpleName();

    public static final String KERNAL_FILE = "kernal_data.txt";

    public static final int ORIGINAL_EMV_PROCESS = 100;
    public static final int WILDCARD_PROCESS = 101;
    public static final int DEFAULT_RETURN_CODE = -100;

    public static final int CLSS_TAG_NOT_EXIST = 0;
    public static final int CLSS_TAG_EXIST_WITHVAL = 1;
    public static final int CLSS_TAG_EXIST_NOVAL = 2;

    public static final int EMV_OK = 0;
    public static final int CLSS_USE_CONTACT = -23;
    public static final int CLSS_REFER_CONSUMER_DEVICE = -40;  //caixh added
    public static final int ENTRY_KERNEL_6A82_ERR = -105;
    public static final int CLSS_RESELECT_APP = -35;
    public static final int CLSS_TERMINATE = -25;
    public static final int ICC_CMD_ERR = -2;


    //card type
    public static final byte KERNTYPE_MC = 0x02;
    public static final byte KERNTYPE_VISA = 0x03;
    public static final byte KERNTYPE_AMEX = 0x04;
    public static final byte KERNTYPE_JCB = 0x05;
    public static final byte KERNTYPE_ZIP = 0x06; //Discover ZIP or 16
    public static final byte KERNTYPE_DPAS = 0x06;//Discover DPAS
    public static final byte KERNTYPE_QPBOC = 0x07;
    public static final byte KERNTYPE_RUPAY = 0x0D;
    public static final byte  KERNTYPE_PURE = 0x12; //add wwc
    public static final byte KERNTYPE_PAGO = 0x13;
    public static final byte KERNTYPE_MIR = 0x14;
    public static final byte KERNTYPE_PBOC = (byte) 0xE1; //Contact PBOC
    public static final byte KERNTYPE_NSICC = (byte) 0xE2;
    public static final byte KERNTYPE_RFU = (byte) 0xFF;
    /**
     * auth handle response code
     */
    public static final String AUTH_RES_CODE = "00";

    /**
     * Transaction Path
     */
    public static final int CLSS_TRANSPATH_EMV = 0;
    public static final int CLSS_TRANSPATH_MAG = 0x10;
    public static final int CLSS_TRANSPATH_LEGACY = 0x20;

    /**
     * AC Type
     */
    public static final int AC_AAC = 0x00;
    public static final int AC_TC = 0x01;
    public static final int AC_ARQC = 0x02;

    /**
     * Byte 4 bit8-5 CVM
     */
    public static final byte CLSS_OC_NO_CVM = 0x00;
    public static final byte CLSS_OC_OBTAIN_SIGNATURE = 0x10;
    public static final byte CLSS_OC_ONLINE_PIN = 0x20;
    /**
     * Byte 1 bit8-5 Status
     */
    public static final byte CLSS_OC_APPROVED = 0x10;
    public static final byte CLSS_OC_DECLINED = 0x20;
    public static final byte CLSS_OC_ONLINE_REQUEST = 0x30;
    public static final byte CLSS_OC_END_APPLICATION = 0x40;
    public static final byte CLSS_OC_SELECT_NEXT = 0x50;
    public static final byte CLSS_OC_TRY_ANOTHER_INTERFACE = 0x60;
    public static final byte CLSS_OC_TRY_AGAIN = 0x70;
    public static final byte CLSS_OC_NA = (byte) 0xF0;

    public static final byte PINTYPE_OFFLINE = 0x01;
    public static final byte PINTYPE_OFFLINE_LASTTIME = 0x02;
    public static final byte PINTYPE_ONLINE = 0x03;

    /**
     * date or time
     */
    public static final int TRANS_DATE_YYMMDD = 0;
    public static final int TRANS_TIME_HHMMSS = 1;

    /**
     * 交易过程请求APP交互类型
     */
   public enum CallbackSort {
        REQUEST_IMPORT_AMT,
        REQUEST_TIPS_CONFIRM,
        REQUEST_AID_SELECT,
        REQUEST_FINAL_AID_SELECT,
        REQUEST_ECASHTIPS_CONFIRM,
        REQUEST_CARDINFO_CONFIRM,
        REQUEST_IMPORT_PIN,
        REQUEST_USER_AUTH,
        REQUEST_ONLINE,
        ON_OFFLINE_BALANCE,
        ON_CARD_TRANSLOG,
        ON_CARD_LOADLOG,
        ON_TRANS_RESULT,
        ON_ERROR,
        ON_OFFILNE_TRANS_RESULT,
        DEFAULT_MENU
    }

   public class CardCode {
        /**
         * error code
         */
        public static final int NO_AID_ERROR = 0;

        /**
         * callback param
         */
        public static final String IMPORT_AMT_TIMES = "times";
        public static final String IMPORT_AMT_AIDS = "aids";
        public static final String CARDINFO_CARDNO = "card_no";
        public static final String IMPORT_PIN_AMOUNT = "amount";
        public static final String IMPORT_PIN_TYPE = "pin_type";
        public static final String TRANS_RESULT = "trans_result";
        public static final String ERROR_CODE = "error_code";

        /**
         * trans data
         */
        public static final String FINAL_SELECT_LEN = "select_len";
        public static final String FINAL_SELECT_DATA = "select_data";
        public static final String PREPROC_RESULT = "preproc_result";
        public static final String TRANS_PARAM = "trans_param";

        /**
         * trans type
         */
        public static final byte LKL_CONSUME = 0x00;
        public static final byte LKL_BALANCE = 0x31;
        public static final byte LKL_PREAUTH = 0x03;
        public static final byte LKL_REVOKE = 0x20;
        public static final byte LKL_ACCOUNT_DEPOSIT = 0x60;
        public static final byte LKL_UNACCOUNT_DEPOSIT = 0x62;
        public static final byte LKL_CASH_DEPOSIT = 0x63;
        public static final byte LKL_CASH_REVOKE = 0x17;
        public static final byte LKL_UNLOOP_CARD = (byte) 0xF1;
        public static final byte LKL_OFF_BALANCE = (byte) 0xF2;
        public static final byte LKL_TRANS_LOG = (byte) 0xF3;
        public static final byte LKL_TRAP_LOG = (byte) 0xF4;

        /**
         * trans result
         */
        public static final byte TRANS_APPROVAL = 0x01;
        public static final byte TRANS_REFUSE = 0x02;
        public static final byte TRANS_STOP = 0x03;
        public static final byte TRANS_FALLBACK = 0x04;
        public static final byte TRANS_USE_OTHER_INTERFACE = 0x05;
        public static final byte TRANS_SECOND_READ = 0x18;
        public static final byte TRANS_OTHER = 0x06;
        public static final byte CDCVM_SECOND_READ_CARD = 0x19;
        public static final byte TRANS_AGAIN_CHECK_CARD = 0x20;
        public static final byte TRANS_CANCEL = (byte)0xFF;
    }

    /**
     * 金额填充 eg:1.2 -> 000000000012
     *
     * @param amt 交易金额
     * @return 填充好的金额数据
     */
    public String getFixedAmount(String amt) {
        return amt;

//        StringBuilder buffer = new StringBuilder();
//        if (amt.contains(".")) {
//            String[] buf = amt.split("\\.");
//            AppLog.emvd("buf len: " + buf.length);
//            if (buf.length == 2) {
//                if (buf[1].length() >= 2) {
//                    buffer.append(buf[0]).append(buf[1].substring(0, 2));
//                } else if (buf[1].length() == 1) {
//                    buffer.append(buf[0]).append(buf[1]).append("0");
//                } else {
//                    buffer.append(buf[0]).append("00");
//                }
//            } else {
//                return null;
//            }
//        } else {
//            buffer.append(amt).append("00");
//        }
//        AppLog.emvd("amount buffer: " + buffer);
//        String bufStr = buffer.toString().length() % 2 != 0 ? 0 + buffer.toString() : buffer.toString();
//        byte[] amount = new byte[6];
//        Arrays.fill(amount, (byte) 0x00);
//        System.arraycopy(BytesUtil.hexString2Bytes(bufStr), 0, amount, 6 - bufStr.length() / 2, bufStr.length() / 2);
//        return BytesUtil.bytes2HexString(amount);
    }

    /**
     * 获取交易序列计数器
     *
     * @return 交易计数器(4个字节)
     */
    public String getSequenceCounter() {

        long counter = getSerialNumber();
        String tag9f41 = String.format("%08d",counter);
        AppLog.d(TAG, "getSequenceCounter TAG9F41== " + tag9f41);
        return tag9f41;
//        String buffer = String.valueOf(counter);
//        if (buffer.length() % 2 != 0) {
//            buffer = "0" + buffer;
//        }
//        byte[] data = new byte[4];
//        Arrays.fill(data, (byte) 0);
//        byte[] buff = BytesUtil.hexString2Bytes(buffer);
//        System.arraycopy(buff, 0, data, 4 - buff.length, buff.length);
//        buffer = BytesUtil.bytes2HexString(data);
//        AppLog.emvd("sequence counter: " + buffer);
//        return buffer;
    }

    private  static long sn = 0;
    public  static long getSerialNumber() {
        if(sn > 999999) {
            sn = 0;
        }
        return ++sn;
    }

    public  static byte[] getHexRandom(int len) {
        if(len <= 0) {
            return null;
        }
        byte[] random = new byte[len];
        Random random1 = new Random();
        random1.nextBytes(random);
        return random;
    }

    /**
     *
     * @param type
     * @return
     */
   public String getTransDateTime(int type) {
        Date date = new Date();
        String result = "";
        SimpleDateFormat format = null;
        switch (type) {
            case TRANS_DATE_YYMMDD:
                format = new SimpleDateFormat("yyMMdd", Locale.CHINA);
                result = format.format(date);
                break;
            case TRANS_TIME_HHMMSS:
                format = new SimpleDateFormat("hhmmss", Locale.CHINA);
                result = format.format(date);
                break;
            default:
                break;
        }
        AppLog.emvd("getTransDateTime: " + result);
        return result;
    }

    /**
     * 填充金额
     *
     * @param amount 分
     * @return 1  -> 000000000001
     */
    String getFixedAmount(int amount) {
        byte[] amtByte = new byte[6];
        Arrays.fill(amtByte, (byte) 0);
        String amtBuf = String.valueOf(amount);
        if (amtBuf.length() % 2 != 0) {
            amtBuf = "0" + amtBuf;
        }
        System.arraycopy(BytesUtil.hexString2Bytes(amtBuf), 0, amtByte, 6 - amtBuf.length() / 2, amtBuf.length() / 2);
        return BytesUtil.bytes2HexString(amtByte);
    }


    /**
     *
     * @return
     */
    public TlvList getDefaultKernal() {
        TlvList list = new TlvList();
        list.addTlv("9F53","01");
        list.addTlv("5F36","02");
        list.addTlv("9F03","000000000000");
        list.addTlv("9F02","000000000000");
        list.addTlv("9F1E","3030303030303031");
        list.addTlv("9F15","0001");
        list.addTlv("9F09","0002");
        list.addTlv("9F40","0000000000");
        list.addTlv("DF8117","00");
        list.addTlv("DF8118","40");
        list.addTlv("DF8119","08");
        list.addTlv("DF811F","08");
        list.addTlv("DF811A","9F6A04");
        list.addTlv("9F6D","0001");
        list.addTlv("DF811E","10");
        list.addTlv("DF812C","00");
        list.addTlv("9F35","22");
        list.addTlv("DF81","0102");
//        String countryCode = String.format("%04d",Integer.parseInt("356"));
//        String countryCode = String.format("%04d",Integer.parseInt("156"));
//        list.addTlv("5F2A",countryCode);
//        list.addTlv("9F1A",countryCode);  //将在TransParam 传入设置

        //list.addTlv("DF8104","");
        //list.addTlv("DF8105","");
        return list;
    }

    public  static byte[] getSHA1(byte[] buf, int offset, int len) {
        if (buf == null) {
            return null;
        }
        if (offset < 0) {
            return null;
        }
        if (len == 0) {
            return null;
        }

        byte[] SHA1 = null;
        byte[] data = new byte[len];
        System.arraycopy(buf, offset, data, 0, len);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            SHA1 = messageDigest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return SHA1;
    }


    public  static byte[] getCAPKChecksum(EmvCapkParam capk) {

        byte[] Modulbytes = BytesUtil.hexString2Bytes(capk.getModul());
        byte[] Exponentbytes = BytesUtil.hexString2Bytes(capk.getExponent());

        byte[] data = new byte[5 + 1 + Modulbytes.length + Exponentbytes.length];
        System.arraycopy(BytesUtil.hexString2Bytes(capk.getRID()), 0, data, 0, 5);
        data[5] = (byte)capk.getKeyID();
        System.arraycopy(Modulbytes, 0, data, 6, Modulbytes.length);
        System.arraycopy(Exponentbytes, 0, data, 6 + Modulbytes.length, Exponentbytes.length);
        byte[] sha1 = PayDataUtil.getSHA1(data, 0, data.length);
        Log.d(TAG, "onSelectCapk getCAPKChecksum, sha1: " + BytesUtil.bytes2HexString(sha1));
        return sha1;
    }

}
