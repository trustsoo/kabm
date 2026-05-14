package com.kabm.util;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.log.Logger;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class PartnerLoader
{
	
	private static final String LOG_ID="<at:PartnerLoader> ";
	
	private static ConcurrentHashMap<String, List<Map<String, DataSet>>> map = null;
	
	
	public static ConcurrentHashMap<String, List<Map<String, DataSet>>> getMap()
	{
		return map;
	}
	
	public static List<Map<String, DataSet>> getCompanyPartnerList(String cmpny_no)
	{
		return map.get(cmpny_no);
	}
	
	public static String searchJson(String cmpny_no, String keyword)
	{
		DataSet result = search(cmpny_no, keyword);
		JSONObject jObject = new JSONObject();
		JSONArray jArray = new JSONArray();
		
		for(int idx=0; idx<result.getMaxDataSize();idx++)
		{
			JSONObject atom = new JSONObject();
			String addStr = "";
			if("Y".equals(result.getText("del_yn", idx)))
			{
				addStr = " (삭제됨)";
			}
			else
			{
				if("ISMPPT0201".equals(result.getText("status_div_cd", idx))){
					addStr = addStr + " (승인대기)";
				}
			}	
			
			atom.put("prtn_nm", result.getText("prtn_nm", idx)+addStr);
			atom.put("prtn_no", result.getText("prtn_no", idx));
						
			jArray.add(atom);
		}
		
		jObject.put("partnerList", jArray);
		jObject.put("count", result.getMaxDataSize());
		
		
		//System.out.println(jObject.toString());
		return jObject.toString();
		
	}
	
	
	public static DataSet search(String cmpny_no, String keyword)
	{		
		Logger.debug.println(LOG_ID+"search:"+cmpny_no);
		DataSet result = new DataSet();
		
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
		
		
		List<Map<String, DataSet>> list = map.get(cmpny_no);
		
		for(int idx=0; idx<list.size();idx++)
		{
			Map<String, DataSet> m = list.get(idx);
			Iterator<String> ite = m.keySet().iterator();
			while(ite.hasNext())
			{
				String k = ite.next();		
				
				if(k.indexOf(seperateKeywod) != -1)
				{
					Logger.debug.println(LOG_ID+m.get(k).getText("prtn_nm")+":"+k);
					result.append(m.get(k));
				}
			}			
		}
		
		return result;
	}
	
	
	public static void doLoad(String cmpny_no)
	{	
		Logger.debug.println(LOG_ID+"doLoad:"+cmpny_no);
		DataSet input = new DataSet();
		input.put("cmd", "getPartnerListCmpnyNo");
		input.put("cmpny_no", cmpny_no);
		InteractionBean interact = new InteractionBean();
		DataSet output = new DataSet();
		
		try
		{
			output = interact.execute("/partner/Partner", input);	
		} catch(Exception ex)
		{
			ex.getStackTrace();
		}
		
		String _cmpny_no = "";
		List<Map<String, DataSet>> list = null;
		for(int idx=0; idx<output.getMaxDataSize();idx++)
		{
			if(!output.getText("cmpny_no", idx).equals(_cmpny_no))
			{
				if(!"".equals(_cmpny_no))
				{					
					map.put(output.getText("cmpny_no", idx), list);
				}
				list = new ArrayList<Map<String, DataSet>>();
			} else
			{
				//map.put(output.getText("cmpny_no", idx), list);
				//list = new ArrayList<String>();
			}			
			
			Map<String, DataSet> atomMap = new HashMap<String, DataSet>();
			
			//System.out.println(output.getText("prtn_nm", idx));
			StringBuffer sb = new StringBuffer();
			for(int kdx=0; kdx<output.getText("prtn_nm", idx).length();kdx++)
			{				
				char c = output.getText("prtn_nm", idx).charAt(kdx);
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
			DataSet d = new DataSet();
			d.put("prtn_nm", output.getText("prtn_nm", idx));
			d.put("prtn_no", output.getText("prtn_no", idx));
			d.put("del_yn", output.getText("del_yn", idx));
			d.put("status_div_cd", output.getText("status_div_cd", idx));
			atomMap.put(key, d);
			Logger.info.println(LOG_ID+key+"==>"+d.toString()+" is putted.");
						
			list.add(atomMap);
			
			if(idx == output.getMaxDataSize() -1)
			{
				map.put(output.getText("cmpny_no", idx), list);
			}
			_cmpny_no = output.getText("cmpny_no", idx);			
			
		}
		
	}
	
	
	public static void dbLoad()
	{
		DataSet input = new DataSet();
		input.put("cmd", "getPartnerListAll");
		InteractionBean interact = new InteractionBean();
		DataSet output = new DataSet();
		
		try
		{
			output = interact.execute("/partner/Partner", input);	
		} catch(Exception ex)
		{
			ex.getStackTrace();
		}
		
		
		map = new ConcurrentHashMap<String, List<Map<String, DataSet>>>();
		
		/*javax.naming.Context initCtx = null;
		javax.naming.Context envCtx = null;
		try
		{	
			
			initCtx = new javax.naming.InitialContext();
			envCtx = (javax.naming.Context) initCtx.lookup("java:comp/env");
			map = (ConcurrentHashMap<String, List<Map<String, DataSet>>>)envCtx.lookup("storage/partner");
			
		} catch(Exception ex){
			Logger.err.println(LOG_ID+Utility.getStackTrace(ex));			
		}*/
		
		
		String _cmpny_no = "";
		List<Map<String, DataSet>> list = null;
		for(int idx=0; idx<output.getMaxDataSize();idx++)
		{
			if(!output.getText("cmpny_no", idx).equals(_cmpny_no))
			{
				if(!"".equals(_cmpny_no))
				{
					/*Comparator<String> cmpr = new Comparator<String>(){
						
						public int compare(String o1, String o2)
						{
							if(o1 == null || o2 == null) return 0;
							
							return o1.compareTo(o2);
						}
					};
					
					Collections.sort(list, cmpr);*/
					map.put(output.getText("cmpny_no", idx), list);
				}
				list = new ArrayList<Map<String, DataSet>>();
			} else
			{
				//map.put(output.getText("cmpny_no", idx), list);
				//list = new ArrayList<String>();
			}			
			
			Map<String, DataSet> atomMap = new HashMap<String, DataSet>();
			
			//System.out.println(output.getText("prtn_nm", idx));
			StringBuffer sb = new StringBuffer();
			for(int kdx=0; kdx<output.getText("prtn_nm", idx).length();kdx++)
			{				
				char c = output.getText("prtn_nm", idx).charAt(kdx);
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
			DataSet d = new DataSet();
			d.put("prtn_nm", output.getText("prtn_nm", idx));
			d.put("prtn_no", output.getText("prtn_no", idx));
			d.put("del_yn", output.getText("del_yn", idx));
			d.put("status_div_cd", output.getText("status_div_cd", idx));
			atomMap.put(key, d);
			Logger.info.println(LOG_ID+key+"==>"+d.toString()+" is putted.");
						
			list.add(atomMap);
			
			if(idx == output.getMaxDataSize() -1)
			{
				map.put(output.getText("cmpny_no", idx), list);
			}
			_cmpny_no = output.getText("cmpny_no", idx);			
			
		}
		
		
	}

	public static void main(String[] args)
	{
		
		dbLoad();
		searchJson("1", "고");
		//DataSet result = search("1", "고");
		
		
			//Logger.debug.println(result.toString());
		
		
		/*for(int idx=0; idx<list.size();idx++)
		{
			
			Map<String, DataSet> m = list.get(idx);
			Iterator<String> ite = m.keySet().iterator();
			while(ite.hasNext())
			{
				String k = ite.next();
				System.out.println(m.get(k).getText("prtn_nm")+":"+k);
			}			
		}
		System.out.println("array size:"+list.size());*/
	}
}