package com.example.json;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParase {
	public static List<String> parase(String jsonobj) throws JSONException{
		List<String> mList = new ArrayList<String>();
		JSONObject json1 = new JSONObject(jsonobj);
		String aa = json1.getString("serverinfo");
		JSONObject json2 = new JSONObject(aa);
		Iterator<String> it = json2.keys();
		while(it.hasNext()){
			String key = it.next();
			String value = json2.getString(key);
			mList.add(value);
		}
		return mList;
	}
}
