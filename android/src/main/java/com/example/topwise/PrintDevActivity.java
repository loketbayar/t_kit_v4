package com.example.topwise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.NumberKeyListener;

import com.topwise.cloudpos.aidl.printer.AidlPrinter;
import com.topwise.cloudpos.aidl.printer.AidlPrinterListener;
import com.topwise.cloudpos.aidl.printer.Align;
import com.topwise.cloudpos.aidl.printer.ImageUnit;
import com.topwise.cloudpos.aidl.printer.PrintItemObj;
import com.topwise.cloudpos.aidl.printer.PrintTemplate;
import com.topwise.cloudpos.aidl.printer.TextUnit;
import com.topwise.cloudpos.aidl.printer.TextUnit.TextSize;
import com.topwise.cloudpos.data.PrinterConstant.FontSize;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import android.util.Log;


/**
 *  print test
 *
 * @author Tianxiaobo
 */
public class PrintDevActivity extends  BaseUtils {
    private AidlPrinter printerDev = null;
    private final String  TAG ="PrintDevActivity";
    private boolean printRunning =  false;
    private  final  static  int BUFF_LEN = 48*2*5;

    private static final String printer= "printer";

    String data = "";


    public PrintDevActivity(AidlPrinter aidlPrinter, Context context){
        printerDev = aidlPrinter;
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"SFPRODISPLAYREGULAR.OTF");
        PrintTemplate.getInstance().init(context,typeface);
    }

    public interface PrintDevCallBack {
        void onEventFinish(String value);
    }


    /**
     *  get print status
     */
    public void getPrintState(PrintDevCallBack callback) {
        try {
            int printState = printerDev.getPrinterState();
            data = ""+printState;
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

    public void printText(PrintDevCallBack callback){
        if(printRunning){
            return;
        }

        try {
            String startTime = getCurTime();
            PrintTemplate template =PrintTemplate.getInstance();
            template.clear();
            template.add(new TextUnit("默认打印数据测试"));
            template.add(new TextUnit("默认打印数据测试"));
            template.add(new TextUnit("默认打印数据测试"));
            template.add(new TextUnit("打印数据字体放大",48));
            template.add(new TextUnit("打印数据字体放大",48));
            template.add(new TextUnit("打印数据字体放大",48));
            template.add(new TextUnit("打印数据加粗",24).setBold(true));
            template.add(new TextUnit("打印数据加粗",24).setBold(true));
            template.add(new TextUnit("打印数据加粗",24).setBold(true));
            template.add(new TextUnit("打印数据左对齐测试",24,Align.LEFT).setBold(false));
            template.add(new TextUnit("打印数据左对齐测试",24,Align.LEFT).setBold(false));
            template.add(new TextUnit("打印数据左对齐测试",24,Align.LEFT).setBold(false));
            template.add(new TextUnit("打印数据居中对齐测试",24,Align.CENTER).setBold(false));
            template.add(new TextUnit("打印数据居中对齐测试",24,Align.CENTER).setBold(false));
            template.add(new TextUnit("打印数据居中对齐测试",24,Align.CENTER).setBold(false));
            template.add(new TextUnit("打印数据右对齐测试",24,Align.RIGHT).setBold(false));
            template.add(new TextUnit("打印数据右对齐测试",24,Align.RIGHT).setBold(false));
            template.add(new TextUnit("打印数据右对齐测试",24,Align.RIGHT).setBold(false));
            template.add(new TextUnit("打印数据下划线",24,Align.LEFT).setUnderline(true));
            template.add(new TextUnit("打印数据下划线",24,Align.LEFT).setUnderline(true));
            template.add(new TextUnit("打印数据下划线",24,Align.LEFT).setUnderline(true));
            template.add(new TextUnit("打印数据不换行测试打印数据不换行测试打印数据不换行测试",24,Align.LEFT).setWordWrap(true));
            template.add(new TextUnit("打印数据不换行测试打印数据不换行测试打印数据不换行测试",24,Align.LEFT).setWordWrap(true));
            template.add(new TextUnit("打印数据不换行测试",24,Align.LEFT));
            template.add(new TextUnit("打印数据行间距测试",24,Align.LEFT).setWordWrap(true).setLineSpacing(20));
            template.add(new TextUnit("打印数据行间距测试",24,Align.LEFT).setWordWrap(true).setLineSpacing(41));
            template.add(new TextUnit("打印数据行间距测试",24,Align.LEFT).setWordWrap(true).setLineSpacing(20));
            template.add(new TextUnit("打印数据行间距测试",24,Align.LEFT).setWordWrap(true).setLineSpacing(15).setLetterSpacing(25));
            template.add(new TextUnit("打印数据行间距测试",24,Align.LEFT).setWordWrap(true).setLineSpacing(15).setLetterSpacing(25));
            template.add(new TextUnit("打印数据行间距测试",24,Align.LEFT).setWordWrap(true).setLineSpacing(15).setLetterSpacing(25));
            template.add(new TextUnit("打印数据左边距测试",24,Align.LEFT).setWordWrap(true));
            template.add(new TextUnit("打印数据左边距测试",24,Align.LEFT).setWordWrap(true));
            template.add(new TextUnit("打印数据左边距测试",24,Align.LEFT).setWordWrap(true));
            template.add(new TextUnit("\n\n"));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);
            printRunning = true;
            printerDev.printRuiQueue(mListen);
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

    /**
     * Template print
     */
    public void printTemplate(PrintDevCallBack callback) {
        if(printRunning){
            return;
        }

        try {
            String startTime = getCurTime();
            PrintTemplate template =PrintTemplate.getInstance();
            template.clear();
            int textSize = TextSize.NORMAL;
            template.add(new TextUnit("中国银联签购单",textSize,Align.CENTER));
            template.add(new TextUnit("\n"));
            template.add(new TextUnit("商户名称：鼎智开发测试专用",textSize));
            template.add(new TextUnit("商户名称：鼎智开发测试专用",textSize));
            template.add(new TextUnit("商户号：123456789012345",textSize).setBold(true));
            template.add(new TextUnit("终端号：12345678",textSize));
            template.add(new TextUnit("操作员号：01",textSize));
            template.add(new TextUnit("发卡行：招商银行",textSize));
            template.add(new TextUnit("卡号：",textSize));
            template.add(new TextUnit("6214 61** **** 6526",textSize,Align.CENTER).setBold(true));
            template.add(1,new TextUnit("交易：",textSize),
                    2,new TextUnit("消费(SALE)",textSize,Align.CENTER).setBold(true));
            template.add(new TextUnit("批次号：000001",textSize));
            template.add(new TextUnit("凭证号：000008",textSize));
            template.add(new TextUnit("授权码：",textSize));
            template.add(new TextUnit("参考号：015555323233",textSize));
            template.add(new TextUnit("清算日期：0830",textSize));
            template.add(new TextUnit("日期/时间(DATE/TIME)：",textSize));
            template.add(new TextUnit("2019/08/30 16:16:18",textSize));
            template.add(1,new TextUnit("金额(AMOUNT):",textSize),
                    1,new TextUnit("RMB 8.88",textSize).setBold(true));
            template.add(new TextUnit("--------------------------------------------------------------------").setWordWrap(false));
            template.add(new TextUnit("备注(REFERENCE)：0830",textSize));
            template.add(new TextUnit("订单编号: 3465767899",textSize));
            template.add(new TextUnit("------------------------------------------").setWordWrap(false));
            template.add(new TextUnit("本人确认将以上交易，同意将其计入本卡账户\n",textSize));
            template.add(new TextUnit("打印数据字体放大", FontSize.XLARGE));
            template.add(new TextUnit("打印数据左对齐测试", FontSize.LARGE));
            template.add(new TextUnit("打印数据居中对齐测试", FontSize.LARGE, Align.CENTER));
            template.add(new TextUnit("打印数据右对齐测试", FontSize.LARGE, Align.RIGHT));
            template.add(1,new TextUnit("数据分居两侧", FontSize.LARGE, Align.RIGHT),
                    1,new TextUnit("数据分居两侧", FontSize.LARGE, Align.RIGHT));

            template.add(1,new TextUnit("金 额：", FontSize.LARGE, Align.LEFT),
                    1,new TextUnit("1.00元", FontSize.LARGE, Align.RIGHT));//.setBold(true)
            template.add(1,new TextUnit("金 额", FontSize.LARGE, Align.LEFT),//.setBold(true)
                    1,new TextUnit("价 格", FontSize.LARGE, Align.CENTER),//.setBold(true)
                    1,new TextUnit("数 量", FontSize.LARGE, Align.RIGHT));//.setBold(true)
            template.add(1,new TextUnit("1.00", FontSize.LARGE, Align.LEFT),
                    1,new TextUnit("2.00", FontSize.LARGE, Align.CENTER),
                    1,new TextUnit("121", FontSize.LARGE, Align.RIGHT));
            template.add(1,new TextUnit("2.00", FontSize.LARGE, Align.LEFT),
                    1,new TextUnit("3.00", FontSize.LARGE, Align.CENTER),
                    1,new TextUnit("111", FontSize.LARGE, Align.RIGHT));
            template.add(new TextUnit("\n"));
            Bitmap bitmap = QRCodeUtil.createQRImage("asdfggfffffsshhheeed", 190,  190,null);
            List<TextUnit> list = new ArrayList<TextUnit>();
            list.add(new TextUnit("默认打印数据测试1"));
            list.add(new TextUnit("默认打印数据测试2", FontSize.LARGE));
            template.add(new ImageUnit(bitmap,190,190),list);
            template.add(list,new ImageUnit(bitmap,190,190));
            template.add(new TextUnit("\n\n"));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);
            printRunning = true;
            printerDev.printRuiQueue(mListen);
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
    /**
     * printBitmap
     */
    public void printBitmap(PrintDevCallBack callback, Context context) {
        if(printRunning){
            return;
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            printerDev.addRuiImage(bitmap,0);
            String startTime = getCurTime();
            PrintTemplate template =PrintTemplate.getInstance();
            template.clear();
            template.add(new TextUnit("\n\n"));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);
            printRunning = true;
            printerDev.printRuiQueue(mListen);
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

    /**
     * print barcode
     *
     */
    public void printBarCode(PrintDevCallBack callback) {
        if(printRunning){
            return;
        }

        try {
            String startTime = getCurTime();
            PrintTemplate template =PrintTemplate.getInstance();
            template.clear();
            template.add(new ImageUnit(CodeUtil.createBarcode("23418753401333", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("03400471", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("2341875340111", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("23411875", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("*23418*", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("234187534011", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("23418", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("{A23418333", 350,  160),350,160));
            template.add(new ImageUnit(CodeUtil.createBarcode("123456765432123412", 350,  160),350,160));
            template.add(new TextUnit("\n\n"));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);
            printRunning = true;
            printerDev.printRuiQueue(mListen);
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

    private String getCurTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        String time = format.format(date);
        return time;
    }



    private  void printGray(String s){
        PrintTemplate template =PrintTemplate.getInstance();
        template.clear();
        int textSize = TextSize.NORMAL;
        template.add(new TextUnit(s,textSize,Align.LEFT));
        template.add(new TextUnit("\n\n"));
        printAddLineFree(template);
        try {
            printerDev.addRuiImage(template.getPrintBitmap(),0);
            printerDev.printRuiQueue(mListen);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void printTickertape(PrintDevCallBack callback, Context context) {
        if (printerDev == null) {
            data ="Failed to get print service！";
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

        int textSize = TextSize.NORMAL;
        final String orderNo = "1234567890123456541";
        Bitmap bitmap =BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini_bmp);;
        try {
            PrintTemplate template = PrintTemplate.getInstance();
            template.init(context,null);
            template.clear();
            template.add(new ImageUnit(Align.CENTER,bitmap,bitmap.getWidth(),bitmap.getHeight()));

            template.add(new TextUnit("\n"));

            template.add(new TextUnit(getResString(R.string.print_title,context),TextSize.LARGE,Align.CENTER).setBold(false));

            template.add(new TextUnit("\n"));

            template.add(new TextUnit(getResString(R.string.print_merchantname,context),TextSize.NORMAL,Align.CENTER).setBold(true));

            template.add(new TextUnit("\n"));

            template.add(new TextUnit(getResString(R.string.print_merchantname,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_merchantno,context)+"00000000000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_operator,context)+"01",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_cardno,context)+"6214444******0095  1",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_issno,context)+"01021000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acqno,context)+"01031000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_txntype,context)+getResString(R.string.consume,context),TextSize.NORMAL-2,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_expdate,context)+"20/12",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_batchno,context)+"000001",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_voucherno,context)+"000033",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_authno,context)+"000000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_refno,context)+"1009000000033",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_datetime,context)+"2017/10/10 11:11:11",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_amount,context)+"  100.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_tips,context)+"  1.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_total,context)+"101.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            Bitmap bitmap1 =CodeUtil.createBarcode(orderNo,350,90);
            template.add(new ImageUnit(bitmap1,bitmap1.getWidth(),bitmap1.getHeight()));
            template.add(new TextUnit(getResString(R.string.print_reference,context)+"101.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("AID:A000000333010101 TVR:008004600:",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("ARQC:ABCDEFDGJHHHGA ATC:0020:",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_signature,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acknowledge,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("-----------------------------------------------------------",TextSize.NORMAL-2,Align.CENTER).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_agentcopy,context),TextSize.NORMAL,Align.CENTER).setBold(false));
            template.add(new ImageUnit(Align.CENTER,bitmap,180,180));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);

            String startTime = getCurTime();
            printerDev.printRuiQueue(new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    data =context.getResources().getString(R.string.print_error_code) + i;
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
                public void onPrintFinish() throws RemoteException {
                    printGertec(callback);
                }

            });
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
    
    public void printBalancePendingInformation(PrintDevCallBack callback, Context context, Map<String, Object> dataMap) {
        if (printerDev == null) {
            String data ="Failed to get print service";
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

        Log.d("PrintDevActivity", "Data Map: " + dataMap.toString());


        String orderNo = dataMap.get("orderNo") != null ? dataMap.get("orderNo").toString() : "0";
        String balance = dataMap.get("balance") != null ? dataMap.get("balance").toString() : "0";
        String merchantName = dataMap.get("merchantName") != null ? dataMap.get("merchantName").toString() : "0";
        String timestamp = dataMap.get("timestamp") != null ? dataMap.get("timestamp").toString() : "01 Des 2024 11:15";
        String tid = dataMap.get("tid") != null ? dataMap.get("tid").toString() : "12345678911234";
        String mid = dataMap.get("mid") != null ? dataMap.get("mid").toString() : "10000000001";
        String merchantAdress = dataMap.get("merchantAdress") != null ? dataMap.get("merchantAdress").toString() : "10000000001";
        String bankName = dataMap.get("bankName") != null ? dataMap.get("bankName").toString() : "10000000001";
        String cardNumber = dataMap.get("cardNumber") != null ? dataMap.get("cardNumber").toString() : "10000000001";
        String accountNumber = dataMap.get("accountNumber") != null ? dataMap.get("cardNumber").toString() : "10000000001";
        String noReff = dataMap.get("noReff") != null ? dataMap.get("cardNumber").toString() : "10000000001";

        int textSize = TextSize.NORMAL;
        Bitmap bitmap =BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini_bmp);;
        try {
            PrintTemplate template = PrintTemplate.getInstance();
            template.init(context,null);
            template.clear();
            template.add(new ImageUnit(Align.CENTER,bitmap,bitmap.getWidth(),bitmap.getHeight()));

            template.add(new TextUnit("\n"));

            template.add(new TextUnit(merchantName,20,Align.CENTER).setBold(false));

            template.add(new TextUnit("\n"));

            template.add(new TextUnit(getResString(R.string.print_title_type_balance,context),TextSize.LARGE,Align.CENTER).setBold(true));

            template.add(new TextUnit("\n"));

            template.add(new TextUnit(getResString(R.string.print_merchantname,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_merchantno,context)+"00000000000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_operator,context)+"01",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_cardno,context)+"6214444******0095  1",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_issno,context)+"01021000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acqno,context)+"01031000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_txntype,context)+getResString(R.string.consume,context),TextSize.NORMAL-2,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_expdate,context)+"20/12",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_batchno,context)+"000001",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_voucherno,context)+"000033",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_authno,context)+"000000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_refno,context)+"1009000000033",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_datetime,context)+"2017/10/10 11:11:11",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_amount,context)+"  100.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_tips,context)+"  1.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_total,context)+"101.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            Bitmap bitmap1 =CodeUtil.createBarcode(orderNo,350,90);
            template.add(new ImageUnit(bitmap1,bitmap1.getWidth(),bitmap1.getHeight()));
            template.add(new TextUnit(getResString(R.string.print_reference,context)+"101.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("AID:A000000333010101 TVR:008004600:",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("ARQC:ABCDEFDGJHHHGA ATC:0020:",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_signature,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acknowledge,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("-----------------------------------------------------------",TextSize.NORMAL-2,Align.CENTER).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_agentcopy,context),TextSize.NORMAL,Align.CENTER).setBold(false));
            template.add(new ImageUnit(Align.CENTER,bitmap,180,180));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);

            String startTime = getCurTime();
            printerDev.printRuiQueue(new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    data =context.getResources().getString(R.string.print_error_code) + i;
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
                public void onPrintFinish() throws RemoteException {
                    printGertec(callback);
                }

            });
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


    public void printBalanceInformation(PrintDevCallBack callback, Context context, Map<String, Object> dataMap) {
        if (printerDev == null) {
            data ="Failed to get print service!";
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
        // final String orderNo = "1234567890123456541";

        Log.d("PrintDevActivity", "Data Map: " + dataMap.toString());

        String orderNo = dataMap.get("orderNo") != null ? dataMap.get("orderNo").toString() : "0";
        String balance = dataMap.get("balance") != null ? dataMap.get("balance").toString() : "0";
        String merchantName = dataMap.get("merchantName") != null ? dataMap.get("merchantName").toString() : "0";
        String timestamp = dataMap.get("timestamp") != null ? dataMap.get("timestamp").toString() : "01 Des 2024 11:15";
        String tid = dataMap.get("tid") != null ? dataMap.get("tid").toString() : "12345678911234";
        String mid = dataMap.get("mid") != null ? dataMap.get("mid").toString() : "10000000001";
        String merchantAdress = dataMap.get("merchantAdress") != null ? dataMap.get("merchantAdress").toString() : "10000000001";
        String bankName = dataMap.get("bankName") != null ? dataMap.get("bankName").toString() : "10000000001";
        String cardNumber = dataMap.get("cardNumber") != null ? dataMap.get("cardNumber").toString() : "10000000001";
        String accountNumber = dataMap.get("accountNumber") != null ? dataMap.get("cardNumber").toString() : "10000000001";
        String noReff = dataMap.get("noReff") != null ? dataMap.get("cardNumber").toString() : "10000000001";
        
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini_bmp);

        int textSize = TextSize.NORMAL;

        try {
            PrintTemplate template = PrintTemplate.getInstance();
            template.init(context,null);
            template.clear();
            template.add(new ImageUnit(Align.CENTER,bitmap,bitmap.getWidth(),bitmap.getHeight()));
            template.add(new TextUnit("\n"));
            template.add(new TextUnit(merchantName,textSize,Align.CENTER).setBold(true));
            template.add(new TextUnit("\n"));
            template.add(new TextUnit(merchantAdress,textSize,Align.CENTER).setBold(false));
            template.add(new TextUnit("\n"));
            template.add(new TextUnit(getResString(R.string.print_title_type_balance,context),TextSize.LARGE,Align.CENTER).setBold(true));
            template.add(new TextUnit("\n"));
            template.add(new TextUnit(getResString(R.string.print_tid, context) + "\t" + tid, TextSize.NORMAL, Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_mid,context)+ "\t" + mid,TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_datetime,context) + "\t" + timestamp,TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("\n"));
            template.add(new TextUnit(getResString(R.string.print_bankname,context)+ "\t" + bankName,TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_cardnumber,context)+ "\t"+ cardNumber,TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_accountnumber,context)+ "\t" + accountNumber,TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_noreff,context)+ "\t" + noReff,TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(balance));
            template.add(new TextUnit("\n"));
        
            template.add(new TextUnit(getResString(R.string.print_balancecheck_success,context)+"Rp 031000", TextSize.NORMAL, Align.CENTER).setBold(false));

            template.add(new TextUnit("-----------------------------------------------",TextSize.NORMAL-2,Align.CENTER).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_customercopy,context),TextSize.NORMAL,Align.CENTER).setBold(false));
            template.add(new TextUnit("\n"));
            template.add(new TextUnit(getResString(R.string.print_fyi,context),TextSize.NORMAL,Align.CENTER).setBold(false));
            printAddLineFree(template);
            // printerDev.addRuiImage(template.getPrintBitmap(),0);

            String startTime = getCurTime();
            printerDev.printRuiQueue(new AidlPrinterListener.Stub() {
                @Override
                public void onError(int i) throws RemoteException {
                    data =context.getResources().getString(R.string.print_error_code) + i;
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
                public void onPrintFinish() throws RemoteException {
                    printGertec(callback);
                }

            });
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

    private void printGertec(PrintDevCallBack callback){
        try {
            // 如果是文件
            InputStream is = getClass().getClassLoader().getResourceAsStream("assets/print_data.txt");
            byte[] buffer = new byte[BUFF_LEN];
            int byteCount = 0;
            int len =0;
            int ret  =0;
            while (true) {// 循环从输入流读取 buffer字节
                byteCount = is.read(buffer);
                if(byteCount ==-1) {
                    break;
                }
                len +=byteCount;
                if(byteCount<BUFF_LEN){
                    byte[]   lastBuf = new byte[byteCount];
                    LogUtil.d(TAG, " byteCount ==== "  + byteCount);
                    System.arraycopy(buffer,0,lastBuf,0,byteCount);
                    buffer = lastBuf;
                }
                String str =  new String(buffer).toUpperCase();
                LogUtil.d(TAG, " ==== "  +str);
                byte[] printBuf  = HexUtil.hexStringToByte(str);
                ret =   printerDev.printBuf(printBuf);
                if(ret != 0 ){
                    break;
                }
                LogUtil.d(TAG, "ret  ==== "  + ret);
            }
            LogUtil.d(TAG, "len  ==== "  + len);
            is.close();
            printerDev.close();
            if(ret ==0) {
                String endTime = getCurTime();
//                showMessage(getResources().getString(R.string.print_end_time) + endTime);
            }else{
//                showMessage(getResources().getString(R.string.print_error_code) + ret);
            }

        } catch (Exception e) {
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



    public byte[] getBitmpBuff() {
        int time = 1;
        byte[] buffer = new byte[48*8*time];
        byte data = (byte) 0x80;
        for(int j =0;j<time;j++) {
            int i =0;
            for (; i < 8; i++) {
                byte[] temp = fillAndGetBuf((byte) ((data & 0xff) >> i));
                System.arraycopy(temp, 0, buffer, 384*j+i * 48, 48);
                LogUtil.d(TAG,"temp len===  "+ HexUtil.bcd2str(temp));
            }
        }
        return buffer;
    }
    private byte[] fillAndGetBuf(byte data){
        byte[] buffer= new byte[48];
        for(int i = 0;i<48;i++){
            buffer[i] = data;
        }
        return buffer;
    }

    AidlPrinterListener mListen = new AidlPrinterListener.Stub() {
        @Override
        public void onError(int i) throws RemoteException {
            printRunning = false;
        }

        @Override
        public void onPrintFinish() throws RemoteException {
            String endTime = getCurTime();
            printRunning = false;
        }
    };

    public void printBlackBlock(PrintDevCallBack callback, Context context) {
        if(printRunning){
            return;
        }

        try {
            Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            printerDev.addRuiImage(bitmap1, 0);
            printerDev.addRuiText(new ArrayList<PrintItemObj>() {
                {
                    add(new PrintItemObj("\n"));
                    if(Build.DISPLAY.contains("Z3909")) {
                        add(new PrintItemObj(""));
                    }
                }
            });
            String startTime = getCurTime();
            printRunning = true;
            printerDev.printRuiQueue(mListen);
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

    public void printSmBuy(PrintDevCallBack callback, Context context) {
        if(printRunning){
            return;
        }
        try {
            String startTime = getCurTime();


            PrintTemplate template = PrintTemplate.getInstance();
            template.init(context,null);
            template.clear();
            template.add(new TextUnit(getResString(R.string.print_title, context),TextSize.NORMAL,Align.CENTER).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_merchantname, context),TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_merchantno, context)+"00000000000",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_operator, context)+"01",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_cardno, context)+"6214444******0095  1",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_issno, context)+"01021000",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acqno, context)+"01031000",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_txntype, context)+getResString(R.string.consume, context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_expdate, context)+"20/12",TextSize.SMALL,Align.LEFT).setBold(false));

            template.add(new TextUnit(getResString(R.string.print_batchno, context)+"000001",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_voucherno, context)+"000033",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_authno, context)+"000000",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_refno, context)+"1009000000033",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_datetime, context)+"2017/10/10 11:11:11",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_amount, context)+"  100.00",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_tips, context)+"  1.00",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_total, context)+"101.00",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_reference, context),TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit("AID:A000000333010101 TVR:008004600:",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit("ARQC:ABCDEFDGJHHHGA ATC:0020:",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_signature, context),TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acknowledge, context),TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit("----------------------------------------------------------------",TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_agentcopy, context),TextSize.SMALL,Align.LEFT).setBold(false));
            template.add(new TextUnit("\n\n"));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);
            printRunning = true;
            printerDev.printRuiQueue(mListen);
        } catch (RemoteException e1) {
            e1.printStackTrace();
            data =e1.toString();
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

    public void printBigBuy(PrintDevCallBack callback, Context context) {
        if(printRunning){
            return;
        }
        try {
            String startTime = getCurTime();
            PrintTemplate template = PrintTemplate.getInstance();
            template.init(context,null);
            template.clear();
            // template.add(new TextUnit(getResString(R.string.print_title,context),TextSize.LARGE,Align.CENTER).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_merchantname,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_merchantno,context)+"00000000000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_operator,context)+"01",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_cardno,context)+"6214444******0095  1",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_issno,context)+"01021000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acqno,context)+"01031000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_txntype,context)+getResString(R.string.consume,context),TextSize.LARGE,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_expdate,context)+"20/12",TextSize.NORMAL,Align.LEFT).setBold(false));

            template.add(new TextUnit(getResString(R.string.print_batchno,context)+"000001",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_voucherno,context)+"000033",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_authno,context)+"000000",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_refno,context)+"1009000000033",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_datetime,context)+"2017/10/10 11:11:11",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_amount,context)+"  100.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_tips,context)+"  1.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_total,context)+"101.00",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_reference,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("AID:A000000333010101 TVR:008004600:",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("ARQC:ABCDEFDGJHHHGA ATC:0020:",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_signature,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_acknowledge,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("----------------------------------------------------------------",TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit(getResString(R.string.print_agentcopy,context),TextSize.NORMAL,Align.LEFT).setBold(false));
            template.add(new TextUnit("\n\n"));
            printAddLineFree(template);
            printerDev.addRuiImage(template.getPrintBitmap(),0);
            printRunning = true;
            printerDev.printRuiQueue(mListen);

        } catch (RemoteException e1) {
            e1.printStackTrace();
            data =e1.toString();
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

    public void printBitmaps(PrintDevCallBack callback, Context context) {
        if(printRunning){
            return;
        }
        try {
            Bitmap bitmap1 =BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            Bitmap bitmap4 = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            Bitmap bitmap5 =BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            Bitmap bitmap6 = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);
            Bitmap bitmap7 = BitmapFactory.decodeResource(context.getResources(), R.drawable.nobu_bank_mini);

            printerDev.addRuiImage(bitmap1, 0);
            printerDev.addRuiImage(bitmap2, 0);
            printerDev.addRuiImage(bitmap3, 0);
            printerDev.addRuiImage(bitmap4, 0);
            printerDev.addRuiImage(bitmap5, 0);
            printerDev.addRuiImage(bitmap6, 0);
            printerDev.addRuiImage(bitmap7, 0);
            printerDev.addRuiText(new ArrayList<PrintItemObj>() {
                {
                    add(new PrintItemObj("\n"));
                    if(Build.DISPLAY.contains("Z3909")) {
                        add(new PrintItemObj(""));
                    }
                }
            });

            String startTime = getCurTime();
            printRunning = true;
            printerDev.printRuiQueue(mListen);
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

    private void printAddLineFree(PrintTemplate template) {
        if(Build.DISPLAY.contains("Z3909")) {
            template.add(new TextUnit(""));
        }
    }
}
