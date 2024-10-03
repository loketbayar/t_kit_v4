package com.example.topwise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topwise.cloudpos.aidl.magcard.AidlMagCard;
import com.topwise.cloudpos.aidl.shellmonitor.AidlShellMonitor;

import java.util.HashMap;
import java.util.Map;

public class ShellCmdActivity  {
	private AidlShellMonitor shellMonitorInf=null;
	
	Button butExe=null;
	EditText editCmd=null;
	private EditText et_one;
	private EditText et_two;
	private EditText et_three;

	private static final String shellCMD= "shellCMD";



	String data="";

	public ShellCmdActivity(AidlShellMonitor aidlShellMonitor) {
		this.shellMonitorInf = aidlShellMonitor;
	}

	public interface ShellCmdCallback {
		void onEventFinish(String value);
	}


	/**
	 * 获取终端银联标准的硬件序列号明⽂
	 */
	public void getHardwareSNPlaintext(ShellCmdCallback callback){
		try {
			String hardwareSNPlaintext = shellMonitorInf.getHardwareSNPlaintext();
			if(hardwareSNPlaintext !=null && !hardwareSNPlaintext.isEmpty()){
				data = hardwareSNPlaintext;
				new Handler(Looper.getMainLooper()).post(new Runnable() {
					@Override
					public void run() {
						if (callback != null) {
							Map<String, Object> raw = new HashMap<>();
							raw.put("type" ,shellCMD);
							raw.put("data",hardwareSNPlaintext);

							ObjectMapper mapper = new ObjectMapper();

							try {
								data = mapper.writeValueAsString(raw);
							} catch (JsonProcessingException e) {
								throw new RuntimeException(e);
							}

							callback.onEventFinish(data);
						}
					}
				});
			} else {
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
			data=e.toString();
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
