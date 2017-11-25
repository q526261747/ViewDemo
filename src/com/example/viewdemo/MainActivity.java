package com.example.viewdemo;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;

import com.example.httppost.HttpRequest;
import com.example.json.JsonParase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.view.WindowManager;

@SuppressLint({ "HandlerLeak", "WrongCall" }) public class MainActivity extends Activity {
	private ChartView chartView;
	private final String URL = "http://192.168.1.119:8080/transportservice/type/jason/action/GetAllSense.do";
	private Thread thread;
	private Handler handler;
	private LinkedList<String> rList;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        ScreenLighting();
        handler = new Handler(){  
            @Override  
            public void handleMessage(Message msg) {  

                if (msg.what == 0x123){//信息记号  
                	Bundle bundle = msg.getData();
                	
                	chartView.setRange(new float[]{20.0f,100.0f,250.0f});
                	if(rList.size()<7){
	                	rList.add(bundle.getString("text"));
	                	chartView.setInfo(new String[]{"0","1","2","3","4","5","6"},   //X轴刻度  
	                            new String[]{"","100","200","300","400"},   //Y轴刻度  
	                            rList, //数据  ***
	                            "PM2.5");
	                	chartView.invalidate();
                	}else{
                		rList.poll();
                		rList.add(bundle.getString("text"));
	                	chartView.setInfo(new String[]{"0","1","2","3","4","5","6"},   //X轴刻度  
	                            new String[]{"","100","200","300","400"},   //Y轴刻度  
	                            rList, //数据  ***
	                            "PM2.5");
	                	chartView.invalidate();
                	}
                       //Log.i("TAG","------------>>"+bundle.getString("text"));
                   //rList.add(bundle.getString("text"));
                   
                }  
            }  
        };
        setThread();
        
	}
	/**
	 * 设置屏幕常亮
	 */
	public void ScreenLighting(){
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	public void initData(){
		chartView = new ChartView(MainActivity.this);
    	chartView = (ChartView) findViewById(R.id.view_chart);
    	rList = new LinkedList<String>();
	}
    public void setThread(){
    	thread=new Thread(new Runnable() {  
            @Override  
            public void run() {  
            	
                while(!Thread.currentThread().isInterrupted()) {  
                String json_str = HttpRequest.HttpPostRequest(null,URL);
                try {
                List<String> mList = JsonParase.parase(json_str);
                //rList.add(mList.get(0));
                //与主线程通信  
                    Message message = new Message();  
                    message.what = 0x123;
                    Bundle bundle = new Bundle();
                    bundle.putString("text",mList.get(0));
//                    bundle.putStringArray("text1", new String[]{"1","2","3","4","5","6","7"});  
//                    bundle.putStringArray("text2",new String[]{"","50","100","150","200","250","300"});
//                    bundle.putStringArray("text3",new String[]{4.0+"",8.5+"",5.4+"",4.6+"",6.2+"",4.2+"",10.4+""});
//                    bundle.putString("title","PM2.5");
                    message.setData(bundle);  
                    handler.sendMessage(message);  
                    //线程休眠2秒  
                   
					Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}  
                }  
  
            }  
        });  
    	
        //开启新线程  
        thread.start();  
    }
    @Override
    protected void onResume() {
     /**
      * 设置为横屏
      */
     if(getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
     }
     super.onResume(); 
    }
    @Override  
    protected void onStop() {  
        super.onStop();  
        //当前Activity终止时，阻塞线程  
        thread.interrupt();  
    }  
}
