package com.kabm.util;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.log.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author advan94
 *
 */
public class SyllablesLoader
{
	
	private final static String LOG_ID = "<at:SyllablesLoader> ";
	
	private final static String CONTENTS_FIELD = "contents";
	private final static String DATA_FIELD = "datas";
	
	/* cmpny_no+data종류
	 * data
	 * */
	private static ConcurrentHashMap<String, List<Map<String, String>>> map = null;
	
	
	public static ConcurrentHashMap<String, List<Map<String, String>>> getMap()
	{
		return map;
	}
	
	public static List<Map<String, String>> getCompanyPartnerList(String dataType, String cmpny_no)
	{
		return map.get(cmpny_no+dataType);
	}
	
	
	public static String searchJson(String dataType, String cmpny_no, String keyword)
	{
		List<String> result = search(dataType, cmpny_no, keyword);
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();		

		for(int idx=0; idx<result.size(); idx++)
		{
			JSONObject atom = new JSONObject();
			atom.put("srch_txt", result.get(idx));
						
			jArray.add(atom);
		}
		
		jObject.put("list", jArray);
		jObject.put("count", result.size());
		
		
		return jObject.toString();
		
	}
	
	
	
	public static List<String> search(String dataType, String cmpny_no,String keyword)
	{		
		Logger.debug.println(LOG_ID+"search:"+cmpny_no+dataType);
		List<String> result = new ArrayList<String>();
		
		StringBuffer sb = new StringBuffer();
		for(int idx=0; idx<keyword.length(); idx++)
		{
			char c = keyword.charAt(idx);
			if(KoreanSyllablesSeperator.isKoreanCompatibilityJamo(c))
			{
				sb.append(c);
			} else if(KoreanSyllablesSeperator.isKoreanSyllables(c))
			{
				sb.append(KoreanSyllablesSeperator.getJamo(c));
			} else
			{
				sb.append(c);
			}
		}
		
		String seperateKeywod = sb.toString();
		
		Logger.debug.println(LOG_ID+"seperateKeywod:"+seperateKeywod);
		
		
		List<Map<String, String>> list = map.get(cmpny_no+dataType);
		
		for(int idx=0; idx<list.size();idx++)
		{
			Map<String, String> m = list.get(idx);
			Iterator<String> ite = m.keySet().iterator();
			while(ite.hasNext())
			{
				String k = ite.next();		
				
				if(k.indexOf(seperateKeywod) != -1)
				{
					Logger.debug.println(LOG_ID+m.get(k)+":"+k);
					result.add(m.get(k));
				}
			}			
		}
		
		return result;
	}
	
	
	
	
	/**
	 * data에 반드시 검색될 대상 텍스트를 contetns 이라는 키로, 텍스트의 실제 데이터 datas라는 키로 구성되어야 한다.
	 *
	 */
	public static void doLoad(String dataType, String cmpny_no, DataSet data)
	{
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for(int idx=0; idx<data.getMaxDataSize();idx++)
		{	
			Map<String, String> atomMap = new HashMap<String, String>();
			
			
			StringBuffer sb = new StringBuffer();
			for(int kdx=0; kdx<data.getText(CONTENTS_FIELD, idx).length();kdx++)
			{				
				char c = data.getText(CONTENTS_FIELD, idx).charAt(kdx);
				//System.out.println(c);
				if(KoreanSyllablesSeperator.isKoreanSyllables(c))
				{
					sb.append(KoreanSyllablesSeperator.getJamo(c));
				} else
				{
					sb.append(c);				
				}
			}
			String key = sb.toString();
			String datas = data.getText(DATA_FIELD, idx);
			
			
			atomMap.put(key, datas);
			Logger.info.println(LOG_ID+key+"==>"+datas+" is putted.");
						
			list.add(atomMap);
			
			if(idx == data.getMaxDataSize() -1)
			{
				map.put(dataType+cmpny_no, list);
			}			
			
		}
	}
}