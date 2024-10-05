package com.example.topwise.emv;

import android.content.Context;
import android.os.ConditionVariable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.example.topwise.TopwisePlugin;
import com.example.topwise.action.ActionEnterPin;
import com.example.topwise.core.AAction;
import com.example.topwise.core.ActionResult;
import com.example.topwise.emv.api.IEmv;
import com.example.topwise.emv.entity.Amounts;
import com.example.topwise.emv.entity.EmvEntity;
import com.example.topwise.emv.enums.EOnlineResult;
import com.example.topwise.emv.impl.ETransProcessListenerImpl;
import com.example.topwise.emv.utlis.EmvDefinition;
import com.example.topwise.entity.TransData;
import com.example.topwise.entity.TransResult;
import com.example.topwise.transmit.Online;
import com.topwise.cloudpos.aidl.emv.level2.EmvCandidateItem;
import com.topwise.cloudpos.aidl.pinpad.AidlPinpad;
import com.topwise.cloudpos.struct.BytesUtil;
import com.topwise.cloudpos.struct.TlvList;
import com.topwise.toptool.api.convert.IConvert;
import com.topwise.toptool.api.packer.ITlv;
import com.topwise.toptool.api.packer.TlvException;

import java.io.UnsupportedEncodingException;

/**
 * Creation date：2021/6/23 on 16:30
 * Describe:
 * Author:wangweicheng
 */
public class EmvTransProcessImpl extends ETransProcessListenerImpl {
    private static final String TAG = EmvTransProcessImpl.class.getSimpleName();

    private AidlPinpad mPinpad = TopwisePlugin.usdkManage.getPinpad(0);

    private Context context;
    private TransData transData;
    private ConditionVariable cv;
    private boolean isConfirm =false;
    private EmvEntity emvEntity;
    private int intResult;
    private IEmv emv;
    private Handler handler;
    private IConvert convert = TopwisePlugin.convert;

    public EmvTransProcessImpl(Context context, TransData transData, IEmv emv,Handler handler) {
        this.context = context;
        this.transData = transData;
        this.emv = emv;
        this.handler = handler;
    }

    /**
     * Multiple Aid options
     * @param aids
     * @return
     */
    @Override
    public int requestAidSelect(final String[] aids) {
        // TODO: show dialog to user to choose multiples aids
        return 0;
//        Log.d(TAG, "requestAidSelect = ");
//        if (aids.length == 1) {
//            return 0;
//        }
//
//        cv = new ConditionVariable();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                List<SingleBean> selects = new ArrayList<>();
//                SingleBean singleBean;
//                for (int i = 0;i < aids.length;i++) {
//                        if (i == 0) {
//                            singleBean = new SingleBean(true, aids[i]);
//                        } else {
//                            singleBean = new SingleBean(false, aids[i]);
//                        }
//                    selects.add(singleBean);
//                }
//                DialogSingleChoice dialogSingleChoice =new DialogSingleChoice(TransContext.getInstance().getCurrentContext());
//                dialogSingleChoice.setTitle("Select Aid");
//                dialogSingleChoice.setListdata(selects);
//                dialogSingleChoice.setMyListener(new IkeyListener() {
//                    @Override
//                    public void onConfirm(String text) {
//                        intResult = Integer.parseInt(text);
//                        if (cv != null) {
//                            cv.open();
//                            cv = null;
//                        }
//                    }
//
//                    @Override
//                    public void onCancel(int res) {
//                        if (cv != null) {
//                            cv.open();
//                        }
//                        intResult = 0;
//                    }
//                });
//                dialogSingleChoice.show();
//            }
//        });
//        if (cv != null) {
//            cv.block();
//        }
//        AppLog.d(TAG, "intResult = " + intResult);
//        return intResult;
    }

    /**
     * Synchronous kernel type
     * EMV 0x00
     *KERNTYPE_MC = 0x02;
     *KERNTYPE_VISA = 0x03;
     *KERNTYPE_AMEX = 0x04;
     *KERNTYPE_JCB = 0x05;
     *KERNTYPE_ZIP = 0x06; //Discover ZIP or 16
     *KERNTYPE_DPAS = 0x06;//Discover DPAS
     *KERNTYPE_QPBOC = 0x07;
     *KERNTYPE_RUPAY = 0x0D;
     *KERNTYPE_PURE = 0x12;
     * @param kernelType
     */
    @Override
    public void onUpdateKernelType(byte kernelType) {
        transData.setKernelType(kernelType);
    }

    /**
     *final aid select
     * @return
     */
    @Override
    public boolean finalAidSelect() {
        Log.d(TAG,"finalAidSelect = ");
        byte[] aucAid;
        String aid = null;
        byte[] aucRandom;
        String random = null;
        aucAid = emv.getTlv(0x4F);
        aucRandom = emv.getTlv(0x9f37);
        if (aucAid != null) {
            aid = BytesUtil.bytes2HexString(aucAid);
            random = BytesUtil.bytes2HexString(aucRandom);
            transData.setAid(aid);
            transData.setRandom(random);
            Log.d(TAG, "aid: " + aid);
        } else {
            return false;
        }
        return true;

    }

    /**
     * confirm Card no
     * @param cardNo
     * @return
     */
    @Override
    public boolean onConfirmCardInfo(String cardNo) {
        transData.setPan(cardNo);
        return true;
    }

    /**
     * request Import PinBlock
     * @param type   0x00:online  PIN
     *               0x01 offline PIN
     * @param pinTryCount
     * @param amt
     * @return
     */
    @Override
    public EmvEntity requestImportPin(int type, int pinTryCount, String amt) {
        if(type==3){
            type =0;
        }

        return checkPin(type, pinTryCount);
    }

    /**
     * IC Request Online
     * @return
     */
    @Override
    public EmvEntity onRequestOnline() {
        //
        int commResult = -1;
        Log.i(TAG, "onRequestOnline =========== ");
        EmvEntity emvEntity = new EmvEntity();

        byte[] f55 = EmvTags.getF55(0, emv, false, false);
        byte[] tlv57 = emv.getTlv(0x57);
        byte[] tlv5a = emv.getTlv(0x5A);
        if (tlv5a !=null){
            Log.e("ActionEmvProcess","TAG5a== " + TopwisePlugin.convert.bcdToStr(tlv5a));
            transData.setTrack2(TopwisePlugin.convert.bcdToStr(tlv5a));
        }
        if (tlv57 !=null){
            Log.e("ActionEmvProcess","TAG57== " + TopwisePlugin.convert.bcdToStr(tlv57));
            transData.setTrack2(TopwisePlugin.convert.bcdToStr(tlv57));
        }
        if (f55 !=null){
            Log.e("ActionEmvProcess","f55 " + TopwisePlugin.convert.bcdToStr(f55));
            transData.setSendIccData(TopwisePlugin.convert.bcdToStr(f55));
        }

        int onlineRet =  Online.getInstance().transMit(transData);
        Log.e("ActionEmvProcess","onlineRet  " + onlineRet);
        Log.e("ActionEmvProcess","onlineRet  " + transData.getRecvIccData());
        if (onlineRet == TransResult.SUCC){
            if ("00".equals(transData.getResponseCode())){
                commResult = 1;
            }else{ //Online rejection
                commResult = 2;
            }
        }else {
            //can show tip
            emvEntity.seteOnlineResult(EOnlineResult.ABORT);
            return emvEntity;
        }
        //Online return
        String rspF55 = transData.getRecvIccData();
        try {
            if (rspF55 != null && rspF55.length() > 0){
                ITlv tlv = TopwisePlugin.packer.getTlv();

                byte[] resp55 = convert.strToBcd(rspF55, IConvert.EPaddingPosition.PADDING_LEFT);
                ITlv.ITlvDataObjList list = null;
                list = tlv.unpack(resp55);
                byte[] value91 = list.getValueByTag(0x91);
                if (value91 != null && value91.length > 0) {
                    emv.setTlv(0x91, value91);
                    emvEntity.setIssueAuthData(value91);
                }
                // set script  71
                byte[] value71 = list.getValueByTag(0x71);
                if (value71 != null && value71.length > 0) {
                    emv.setTlv(0x71, value71);

                    ITlv.ITlvDataObj obj = tlv.createTlvDataObject();
                    obj.setTag(0x71);
                    obj.setValue(value71);
                    emvEntity.setIssueScript71(tlv.pack(obj));
                }

                //  set script  72
                byte[] value72 = list.getValueByTag(0x72);
                if (value72 != null && value72.length > 0) {
                    emv.setTlv(0x72, value72);

                    ITlv.ITlvDataObj obj = tlv.createTlvDataObject();
                    obj.setTag(0x72);
                    obj.setValue(value72);
                    emvEntity.setIssueScript72(tlv.pack(obj));
                }
                //  set script  89
                byte[] value89 = list.getValueByTag(0x89);
                if (value89 != null && value89.length > 0) {
                    emv.setTlv(0x89, value89);
                    emvEntity.setAuthCode(value89);
                }

                //  set script  8A
                byte[] value8A = list.getValueByTag(0x8A);
                if (value8A != null && value8A.length > 0) {
                    emv.setTlv(0x8A, value8A);
                    emvEntity.setAuthRespCode(value8A);
                }

                //if
                if (commResult != 1){
                    //delect dup
                    emvEntity.seteOnlineResult(EOnlineResult.DENIAL);
                    return emvEntity;
                }
                emvEntity.seteOnlineResult(EOnlineResult.APPROVE);
                return emvEntity;
            }
        } catch (TlvException e) {
            e.printStackTrace();
            return emvEntity;
        }
        return emvEntity;
    }

    /**
     * set the Amount to kernal
     *
     * @return
     */
    @Override
    public Amounts requestImportAmount() {

        Amounts amounts = new Amounts();
        amounts.setTransAmount(transData.getAmount());
        return amounts;
    }

    /**
     * check Pinblock
     * @param inType
     * @return
     */
    private EmvEntity checkPin(int inType, int pinTryCount){

        EmvEntity emvEntity = new EmvEntity();


        cv = new ConditionVariable();
//    0x00:online  PIN
//    0x01 offline PIN



        ActionEnterPin actionEnterPin = new ActionEnterPin(new AAction.ActionStartListener() {
            @Override
            public void onStart(AAction action) {
                ((ActionEnterPin) action).setParam(context,"title",transData.getPan(),
                        transData.getAmount(),"",
                        inType, pinTryCount);
            }
        });
        actionEnterPin.setEndListener(new AAction.ActionEndListener() {
            @Override
            public void onEnd(AAction action, ActionResult result) {
                if (0 == result.getRet()){
                    String pinblock = (String) result.getData();

                    if (TextUtils.isEmpty(pinblock)){ //bypass
                        emvEntity.setResult(true);
                        emvEntity.setPinData(EmvDefinition.BYPASS);
                        Log.i(TAG,"actionEnterPin  BYPASS ====");
                    }else {
                        emvEntity.setResult(true);
                        if (inType == 0x00){
                            transData.setHasPin(true);
                            transData.setField52(pinblock);
                        }
                        emvEntity.setPinData(pinblock);
                        Log.i(TAG,"actionEnterPin  Pin ==== ");
                    }
                } else {
                    emvEntity.setResult(false);
                }
                cv.open();
            }
        });
        actionEnterPin.execute();

        cv.block();

        return emvEntity;
    }
 /**
     * request Kernal List TLV
     * Pass through the TAG LIST to the kernel
     * @param kernelType
     * @return
     */
    @Override
    public TlvList requestKernalListTLV(byte kernelType) {
        TlvList allList = new TlvList();
        String cvmLimit = "000000500000";
        allList.addTlv("DF4D",cvmLimit); //rupay
        allList.addTlv("DF8126",cvmLimit); //MS

        Log.i(TAG,"requestKernalListTLV  " + allList.toString()  );
        return allList;
    }

    /**
     * update App Name
     * @param emvCandidateItem
     */
    @Override
    public void onUpdateEmvCandidateItem(EmvCandidateItem emvCandidateItem) {
        if (emvCandidateItem != null && emvCandidateItem.getAucDisplayName() != null){
            String s = null;
            try {
                s = new String(emvCandidateItem.getAucDisplayName(),"gbk").trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.i(TAG,"onUpdateEmvCandidateItem  " + s  );
        }
    }


}
