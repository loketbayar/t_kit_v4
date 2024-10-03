package com.example.topwise.emv.utlis;

import com.example.topwise.utlis.DataUtils;

/**
 * 创建日期：2021/6/17 on 10:23
 * 描述:
 * 作者:wangweicheng
 */
public class TransUtlis {
    //EMV error code definition
    public static final int EMV_OK = 0;
    public static final int EMV_APPROVED = 1;
    public static final int EMV_FORCE_APPROVED = 2;
    public static final int EMV_DECLINED = 3;
    public static final int EMV_NOT_ALLOWED = 5;
    public static final int EMV_NO_ACCEPTED = 6;
    public static final int EMV_TERMINATED = 7;
    public static final int EMV_CARD_BLOCKED = 8;
    public static final int EMV_APP_BLOCKED = 9;
    public static final int EMV_NO_APP = 10;
    public static final int EMV_FALLBACK = 11;
    public static final int EMV_CAPK_EXPIRED = 12;
    public static final int EMV_CAPK_CHECKSUM_ERROR = 13;
    public static final int EMV_AID_DUPLICATE = 14;
    public static final int EMV_CERTIFICATE_RECOVER_FAILED = 15;
    public static final int EMV_DATA_AUTH_FAILED = 16;
    public static final int EMV_UN_RECOGNIZED_TAG = 17;
    public static final int EMV_DATA_NOT_EXISTS = 18;
    public static final int EMV_DATA_LENGTH_ERROR = 19;
    public static final int EMV_INVALID_TLV = 20;
    public static final int EMV_INVALID_RESPONSE = 21;
    public static final int EMV_DATA_DUPLICATE = 22;
    public static final int EMV_MEMORY_NOT_ENOUGH = 23;
    public static final int EMV_MEMORY_OVERFLOW = 24;
    public static final int EMV_PARAMETER_ERROR = 25;
    public static final int EMV_ICC_ERROR = 26;
    public static final int EMV_NO_MORE_DATA = 27;
    public static final int EMV_CAPK_NO_FOUND = 28;
    public static final int EMV_AID_NO_FOUND = 29;
    public static final int EMV_FORMAT_ERROR = 30;
    public static final int EMV_ONLINE_REQUEST = 31;//online request -by wfh20190805
    public static final int EMV_SELECT_NEXT_AID = 32;//Select next AID
    public static final int EMV_TRY_AGAIN = 33;//Try Again. ICC read failed.
    public static final int EMV_SEE_PHONE = 34;//Status Code returned by IC card is 6986, please see phone. GPO 6986 CDCVM.
    public static final int EMV_TRY_OTHER_INTERFACE = 35;//Try other interface -by wfh20190805
    public static final int EMV_ICC_ERR_LAST_RECORD = 36;
    public static final int EMV_CANCEL = 254;
    public static final int EMV_OTHER_ERROR = 255;

    public static final byte PINTYPE_OFFLINE = 0x01;
    public static final byte PINTYPE_OFFLINE_LASTTIME = 0x02;
    public static final byte PINTYPE_ONLINE = 0x03;


    public static final int ICC_RESET_ERR = -1;
    public static final int ICC_CMD_ERR = -2;
    public static final int ICC_BLOCK = -3;
    public static final int EMV_RSP_ERR = -4;
    public static final int EMV_APP_BLOCK = -5;
    public static final int EMV_USER_CANCEL = -7;
    public static final int EMV_TIME_OUT = -8;
    public static final int EMV_DATA_ERR = -9;
    public static final int EMV_NOT_ACCEPT = -10;
    public static final int EMV_DENIAL = -11;
    public static final int EMV_KEY_EXP = -12;
    public static final int EMV_NO_PINPAD = -13;
    public static final int EMV_NO_PASSWORD = -14;
    public static final int EMV_SUM_ERR = -15;
    public static final int EMV_NOT_FOUND = -16;
    public static final int EMV_NO_DATA = -17;
    public static final int EMV_OVERFLOW = -18;
    public static final int NO_TRANS_LOG = -19;
    public static final int RECORD_NOTEXIST = -20;
    public static final int LOGITEM_NOTEXIST = -21;
    public static final int ICC_RSP_6985 = -22;

    public static final int EMV_FILE_ERR = -24;

    public static final int EMV_PARAM_ERR = -30;

    public static final int CLSS_NO_APP = -6;
    public static final int CLSS_USE_CONTACT = -23;
    public static final int CLSS_FILE_ERR = -24;
    public static final int CLSS_TERMINATE = -25;
    public static final int CLSS_FAILED = -26;
    public static final int CLSS_DECLINE = -27;
    public static final int CLSS_TRY_ANOTHER_CARD = -28;
    public static final int CLSS_PARAM_ERR = -30;
    public static final int CLSS_RESELECT_APP = -35;
    public static final int CLSS_CARD_EXPIRED = -36;
    public static final int CLSS_NO_APP_PPSE_ERR = -37;
    public static final int CLSS_USE_VSDC = -38;
    public static final int CLSS_CVMDECLINE = -39;
    public static final int CLSS_REFER_CONSUMER_DEVICE = -40;
    public static final int CLSS_LAST_CMD_ERR = -41;
    public static final int CLSS_API_ORDER_ERR = -42;
    public static final int CLSS_TORN_CARDNUM_ERR = -43;
    public static final int CLSS_TRON_AID_ERR = -44;
    public static final int CLSS_TRON_AMT_ERR = -45;
    public static final int CLSS_CARD_EXPIRED_REQ_ONLINE = -46;
    public static final int CLSS_FILE_NOT_FOUND = -47;
    public static final int CLSS_TRY_AGAIN = -48;
    public static final int CLSS_TORN_RECOVER = -49;
    public static final int CLSS_TRON_NULL = -50;
    public static final int CLSS_DEVICE_NOT_AUTH = -51;
    public static final int ENTRY_KERNEL_6A82_ERR = -105;
    public static final int CLSS_PAYMENT_NOT_ACCEPT = -200;
    public static final int CLSS_INSERTED_ICCARD = -301;
    public static final int CLSS_SWIPED_MAGCARD = -302;
    public static final int CLSS_MORE_CARD = -303;




//#define ICC_RESET_ERR                   -1        //IC card reset is failed
//#define ICC_CMD_ERR                     -2        //IC card command is failed
//#define ICC_BLOCK                       -3        //IC card is blocked
//#define EMV_RSP_ERR                     -4        //Status Code returned by IC card is not 9000
//#define EMV_APP_BLOCK                   -5        //The Application selected is blocked
////#define EMV_NO_APP                      -6        //There is no AID matched between ICC and terminal
//#define EMV_USER_CANCEL                 -7        //The Current operation or transaction was canceled by user
//#define EMV_TIME_OUT                    -8        //User?ˉs operation is timeout
//#define EMV_DATA_ERR                    -9        //Data error is found
//#define EMV_NOT_ACCEPT                  -10       //Transaction is not accepted
//#define EMV_DENIAL                      -11       //Transaction is denied
//#define EMV_KEY_EXP                     -12       //Certification Authority Public Key is Expired
//#define EMV_NO_PINPAD                   -13       //PIN enter is required, but PIN pad is not present or not working
//#define EMV_NO_PASSWORD                 -14       //PIN enter is required, PIN pad is present, but there is no PIN entered
//#define EMV_SUM_ERR                     -15       //Checksum of CAPK is error
//#define EMV_NOT_FOUND                   -16       //Appointed Data Element can?ˉt be found
//#define EMV_NO_DATA                     -17       //The length of the appointed Data Element is 0
//#define EMV_OVERFLOW                    -18       //Memory is overflow
//#define NO_TRANS_LOG                    -19       //There is no Transaction log
//#define RECORD_NOTEXIST                 -20       //Appointed log is not existed
//#define LOGITEM_NOTEXIST                -21       //Appointed Label is not existed in current log record
//#define ICC_RSP_6985                    -22       //Status Code returned by IC card for GPO/GAC is 6985
//#define EMV_FILE_ERR                    -24       //There is file error found
//#define EMV_PARAM_ERR                   -30       //Parameter error.

//#define CLSS_NO_APP                     -6        //There is no AID matched between ICC and terminal
//#define CLSS_USE_CONTACT                -23       //Must use other interface for the transaction
//#define CLSS_FILE_ERR                   -24       //There is file error found
//#define CLSS_TERMINATE                  -25       //Must terminate the transaction
//#define CLSS_FAILED                     -26       //Contactless transaction is failed
//#define CLSS_DECLINE                    -27       //Transaction should be declined.
//#define CLSS_TRY_ANOTHER_CARD           -28       //Try another card
//#define CLSS_PARAM_ERR                  -30       //Parameter is error
//#define CLSS_RESELECT_APP               -35       //Select the next AID in candidate list
//#define CLSS_CARD_EXPIRED               -36       //IC card is expired
//#define CLSS_NO_APP_PPSE_ERR            -37       //No application is supported(Select PPSE is error)
//#define CLSS_USE_VSDC                   -38       //Switch to contactless PBOC
//#define CLSS_CVMDECLINE                 -39       //CVM result in decline for AE
//#define CLSS_REFER_CONSUMER_DEVICE      -40       //Status Code returned by IC card is 6986, please see phone
//#define CLSS_LAST_CMD_ERR               -41       //The last read record command is error(qPBOC Only)
//#define CLSS_API_ORDER_ERR              -42       //APIs are called in wrong order. Please call GetDebugInfo_xxx to get error codes.
//#define CLSS_TORN_CARDNUM_ERR           -43       //torn log's pan is different from the reselect card's pan
//#define CLSS_TRON_AID_ERR               -44       //torn log's AID is different from the reselect card's AID
//#define CLSS_TRON_AMT_ERR               -45       //torn log's amount is different from the reselect card's amount
//#define CLSS_CARD_EXPIRED_REQ_ONLINE    -46       //IC card is expired and should continue go online
//#define CLSS_FILE_NOT_FOUND             -47       //ICC return 6A82 (File not found) in response to the SELECT command
//#define CLSS_TRY_AGAIN                  -48       //Try again for AE3.1
//#define CLSS_TORN_RECOVER               -49       //1stGAC failed need torn recovery transaction
//#define CLSS_TRON_NULL                  -50       //torn log's is Null
//#define CLSS_DEVICE_NOT_AUTH       		-51       //Status Code returned by IC card is 6987, Please authenticate yourself to your device and try again
//
//#define CLSS_PAYMENT_NOT_ACCEPT         -200      // Payment Type Not Accepted for flash
//
//#define CLSS_INSERTED_ICCARD            -301      // IC card is detected during contactless transaction
//#define CLSS_SWIPED_MAGCARD             -302      // Magnetic stripe card is detected during contactless transaction
//#define CLSS_MORE_CARD                  -303      // VCAS More Card
    public static String convertorVale(String indata ,int len, char c){
        if (DataUtils.isNullString(indata) || len == 0) return null;
        StringBuffer stringBuffer = new StringBuffer();
        if (indata.length() >= len) return indata;
        int l = len - indata.length();
        for (int i = 0; i<l;i++){
            stringBuffer.append(c);
        }
        stringBuffer.append(indata);
        return stringBuffer.toString();
    }
}
