//package com.example.topwise.action;
//
//import android.content.Context;
//
//import com.topwise.emvtest.core.AAction;
//import com.topwise.emvtest.core.ActionResult;
//import com.topwise.emvtest.entity.TransData;
//import com.topwise.emvtest.transmit.Online;
//
///**
// * Creation date：2021/9/1 on 15:22
// * Describe:
// * Author:wangweicheng
// */
//public class ActionOnline extends AAction {
//    /**
//     * 子类构造方法必须调用super设置ActionStartListener
//     * The subclass constructor must call super to set ActionStartListener
//     * @param listener {@link ActionStartListener}
//     */
//    public ActionOnline(ActionStartListener listener) {
//        super(listener);
//    }
//    private Context context;
//    private TransData transData;
//
//    public void setParam(Context context, TransData transData) {
//        this.context = context;
//        this.transData = transData;
//    }
//    @Override
//    protected void process() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                int ret = Online.getInstance().transMit(transData);
//                setResult(new ActionResult(ret, transData));
//            }
//        }).start();
//    }
//}
