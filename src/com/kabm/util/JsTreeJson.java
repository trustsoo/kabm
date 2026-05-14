package com.kabm.util;

/******************************************************************************** 
 * Program ID	: JsTreeJson
 * FileName		: JsTreeJson.java
 * @author		: 이정동
 * @version		: 1.0
 * Comment		: 화면에 Tree를 표현하기위한 자료구조를 만들어 내는 Class.
 *
 * Modified
 * No       Date       Author	      Comment
 * ---   ----------   ----------   -----------------------------------------------
 * 01     2009-05-03   이정동       최초작성
 ********************************************************************************/

import jdf.framework.core.data.DataSet;
import jdf.framework.core.util.StringUtil;
import net.sf.json.JSONArray;

import java.util.*;

public class JsTreeJson 
{
	List<JsTreeJson> jsTreeNode;
	List<JsTreeJson> children; 
	List<JsTreeJson> root; 
	
	Attributes attributeNode;
	Data dataNode;
	List<Attributes> attributeList;
	
	public List<Attributes> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(List<Attributes> attributeList) {
		this.attributeList = attributeList;
	}

	public List<JsTreeJson> getRoot() {
		return root;
	}

	public void setRoot(List<JsTreeJson> root) {
		this.root = root;
	}

	public List<JsTreeJson> getChildren() {
		return children;
	}

	public void setChildren(List<JsTreeJson> children) {
		this.children = children;
	}

	public List<JsTreeJson> getJsTreeNode() {
		return jsTreeNode;
	}

	public void setJsTreeNode(List<JsTreeJson> jsTreeNode) {
		this.jsTreeNode = jsTreeNode;
	}

	public Attributes getAttributeNode() {
		return attributeNode;
	}

	public void setAttributeNode(Attributes attributeNode) {
		this.attributeNode = attributeNode;
	}

	public Data getDataNode() {
		return dataNode;
	}

	public void setDataNode(Data dataNode) {
		this.dataNode = dataNode;
	}
	
	
	/**
	 * jsTree async module (hwado)
	 * @param dset
	 * @param header
	 * @param key
	 * @param startDepth
	 * @return JSON String
	 * @throws Exception
	 */
	public String autoJson (DataSet dset, String header, String key, int startDepth) throws RuntimeException, Exception {	
		
		return autoJson (dset, header, key, startDepth, false);
	}
	
	
	/**
	 * jsTree async module (hwado)
	 * @param dset
	 * @param header
	 * @param key
	 * @param startDepth
	 * @return JSON String
	 * @throws Exception
	 */
	public String autoJson (DataSet dset, String header, String key, int startDepth, boolean allCheck) throws RuntimeException, Exception {	
		
		JsTreeJson jsNode = null;
		StringBuffer bf = new StringBuffer();
		try{
			StringBuffer csbf = new StringBuffer();
			if(dset == null) return "''";
			int Cnt = dset.getCount(key);
			DataSet childSet = null;
			int rootDepth; 
			int rootSeq = 0;
			int childSeq = 1;
			String childStr = "";
			String[] dat = header.split(",");			
			for(int idx=0; idx < dat.length; idx++){
				dat[idx]=dat[idx].trim();
	        	int a = dat[idx].indexOf(":");
	        	if (a>0)
	        		dat[idx] = dat[idx].substring(0,a);	        	
			}
			
			HashMap rTreeMap = new HashMap();
			HashMap cTreeMap = new HashMap();
			HashMap keyMap   = new HashMap();
					
			for(int i=0; i < Cnt; i++) {
				childSet = new DataSet();
				for(int idx=0; idx < dat.length; idx++) {
					childSet.put(dat[idx], dset.getText(dat[idx], i));
				}
				rootDepth = Integer.parseInt(dset.getText(key, i));
				jsNode = getNode(childSet, header, allCheck);
				if (rootDepth==startDepth) {
					rootSeq++;
					rTreeMap.put(rootSeq, jsNode);
				} else {		
					cTreeMap.put(childSeq, jsNode);
					keyMap.put(childSeq, rootSeq);
					childSeq++;
				}
			}

			String jsonData = getJsonData(rTreeMap, cTreeMap, keyMap);
			return jsonData;
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			jdf.framework.core.log.Logger.warn.println(jdf.framework.core.util.Utility.getStackTrace(e));
			return "";
		}
	}
	
	
	/**
	 * jsTree static module (hwado)
	 * @param dset
	 * @param header
	 * @param key
	 * @param startDepth
	 * @return JSON String
	 * @throws Exception
	 */
	public String autoJsonAll(DataSet dset, String header, String key, int startDepth) throws RuntimeException, Exception {	
		
		JsTreeJson jsNode = null;
		StringBuffer bf = new StringBuffer();
		try{
			//StringBuffer csbf = new StringBuffer();
			if(dset == null) return "''";
			int Cnt = dset.getCount(key);
			DataSet childSet = null;
			int curDepth =0; 
			int preDepth = startDepth;
			int rootSeq = 0;
			int childSeq = 0;
			//int childRootSeq =1;
			//String childStr = "";
			
			String rMapKey ="";
			String cMapKey ="";
			String mapKey = "";
			
			//boolean bAddRoot = false;
			String[] dat = header.split(",");			
			for(int idx=0; idx < dat.length; idx++){
				dat[idx]=dat[idx].trim();
	        	int a = dat[idx].indexOf(":");
	        	if (a>0)
	        		dat[idx] = dat[idx].substring(0,a);	        	
			}
			
			HashMap rTreeMap = new HashMap();
			HashMap cTreeMap = new HashMap();
			HashMap keyMap   = new HashMap();
			List queMapList	 = new ArrayList();
					
			for(int i=0; i < Cnt; i++) {
				childSet = new DataSet();
				for(int idx=0; idx < dat.length; idx++) {
					childSet.put(dat[idx], dset.getText(dat[idx], i));
				}
				curDepth = Integer.parseInt(dset.getText(key, i));
				jsNode = getNode(childSet, header, false);
				
				if (curDepth==startDepth) {
					rootSeq++;
					mapKey = "R" + rootSeq;
					rMapKey = mapKey;
					rTreeMap.put(rMapKey, jsNode);
					//rTreeList.add(rMapKey, jsNode)
				} else {
					childSeq++;
					mapKey = "C" + childSeq;					
					cTreeMap.put(mapKey, jsNode);
				}
				
				queMapList.add(mapKey + "☆" + curDepth);		
				
				if(curDepth == (preDepth + 1)){
					if((curDepth-1) == startDepth) cMapKey = rMapKey;
					else {
						cMapKey = "C" + (childSeq-1); //pre child Key
					}
				} else {
					for(int kk= queMapList.size()-1; kk > 0 ; kk--){
						String tempKey = (String) queMapList.get(kk);
						int mapDepth = 0;						
						String[] arrTemp = tempKey.split("☆");
						if(arrTemp.length > 1) mapDepth = Integer.parseInt(arrTemp[1]);
						if(mapDepth == (curDepth-1)){
							 cMapKey = arrTemp[0];
							break;
						}
					}
				}	
				if(curDepth > startDepth) keyMap.put(mapKey, cMapKey);
				
				preDepth = curDepth;
			}
	

//System.out.println("rTreeMap==" + rTreeMap);			
//System.out.println("cTreeMap==" + cTreeMap);
//System.out.println("keyMap==" + keyMap);
			
			JSONArray jsonArray = JSONArray.fromObject(getJsonData2(rTreeMap, cTreeMap, keyMap) );
			return jsonArray.toString();
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			jdf.framework.core.log.Logger.warn.println(jdf.framework.core.util.Utility.getStackTrace(e));
			return "";
		}
	}
	
	
	public String getJsonData (HashMap rootMap, HashMap childMap, HashMap keyMap) throws RuntimeException, Exception {
		try{
			
			//int root;
			//String child = "";
			//int preRoot = 0;
			
			HashMap[] rootM = new HashMap[rootMap.size()];	
			HashMap[] childM = new HashMap[childMap.size()];
			List tempChild =null;
			
			for(int i=0; i < rootMap.size(); i ++){
				tempChild = new ArrayList();
				JsTreeJson rootNode = (JsTreeJson) rootMap.get(i+1);
				rootM[i] = new HashMap();
				rootM[i].put("data", getData(rootNode));
				rootM[i].put("attributes",getAttributes(rootNode) );
				if(!checkFile(rootNode)) {
					rootM[i].put("state", "closed");
				}
				
				Iterator iterator = (keyMap.keySet()).iterator();
				while(iterator.hasNext()) {
					int key = (Integer) iterator.next();
					int value = (Integer) keyMap.get(key);
					if(value == (i+1)){
						JsTreeJson childNode = (JsTreeJson) childMap.get(key);
						tempChild.add(childNode);		
					}
				}
				if(tempChild.size() > 0) {
					rootM[i].put("children", getChildNode(tempChild));
				}
				
			}
			
			
			JSONArray jsonArray = JSONArray.fromObject( rootM );  
			return jsonArray.toString();
			
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			jdf.framework.core.log.Logger.warn.println(jdf.framework.core.util.Utility.getStackTrace(e));
			throw e;
		}
	}
	
	
	public HashMap[] getJsonData2 (HashMap rootMap, HashMap childMap, HashMap keyMap) throws RuntimeException, Exception {
		try{

			//int root;
			//String child = "";
			//int preRoot = 0;
			
			if(rootMap == null) rootMap = new HashMap();
			
			rootMap = getSortedMap(rootMap);
			
			HashMap[] rootM = new HashMap[rootMap.size()];	
			HashMap[] childM = new HashMap[childMap.size()];			

			JsTreeJson rootNode = null;
			JsTreeJson childNode = null;
			
			Iterator itr =  (rootMap.keySet()).iterator();
			int rootCnt = 0;
			while(itr.hasNext()){
				String rootKey = (String) itr.next();
//System.out.println(String.format("rootKey[%1$s]",rootKey));				
				rootNode = (JsTreeJson) rootMap.get(rootKey);
				rootM[rootCnt] = new HashMap();
				rootM[rootCnt].put("data", getData(rootNode));
				rootM[rootCnt].put("attributes",getAttributes(rootNode) );
				if(!checkFile(rootNode)) {
					rootM[rootCnt].put("state", "closed");
				}
				//child search
				Iterator iterator = (keyMap.keySet()).iterator();
				HashMap rTreeMap = new HashMap();
				while(iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = (String) keyMap.get(key);
					if(value.equals(rootKey)) {
//System.out.println(String.format("key[%1$s], value[%2$s], root[%3$s]", key, value, rootKey));						
						childNode = (JsTreeJson) childMap.get(key);						
						rTreeMap.put(key, childNode);
					}
				}
				childM = getJsonData2(rTreeMap,childMap,keyMap);	
				rootM[rootCnt].put("children", childM);
				rootCnt++;
			}

			return rootM;
			
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			jdf.framework.core.log.Logger.warn.println(jdf.framework.core.util.Utility.getStackTrace(e));
			throw e;
		}
	}
	
	
	public HashMap getSortedMap(HashMap hmap)
	{
		
		HashMap map = new LinkedHashMap();
		HashMap tempMap = new HashMap();
		
		List mapKeys = new ArrayList(hmap.keySet());
		int imapKey = 0;
		List sortKey = new ArrayList();
		for(int idx=0; idx < mapKeys.size(); idx++){
			String temp = (String) mapKeys.get(idx);
			imapKey = Integer.parseInt(temp.substring(1));
			sortKey.add(imapKey);
			tempMap.put(imapKey, mapKeys.get(idx));
		}
		
		List mapValues = new ArrayList(hmap.values());
		hmap.clear();
		
		TreeSet sortedSet = new TreeSet(sortKey);
		Object[] sortedArray = sortedSet.toArray();
		int size = sortedArray.length;
		for (int i=0; i<size; i++)
		{
			map.put(tempMap.get(sortedArray[i]), mapValues.get(mapKeys.indexOf(tempMap.get(sortedArray[i]))));
		}
		return map;
	}

	
	public HashMap[] getChildNode(List childList) throws RuntimeException, Exception {
		
		try{
			HashMap[] childMap = new HashMap[childList.size()];
			for(int i=0; i < childList.size(); i++){
				JsTreeJson jsNode = (JsTreeJson) childList.get(i);
				childMap[i] = new HashMap();
				childMap[i].put("data", getData(jsNode));
				childMap[i].put("attributes",getAttributes(jsNode) );
				if(!checkFile(jsNode)) {
					childMap[i].put("state", "closed");
				}
				
			}
			return childMap;
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getStrData(JsTreeJson jsNode) throws Exception {
		try{
			Data data = jsNode.getDataNode();
			if(data == null) return "";
			return data.getTitle();
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public boolean checkFile(JsTreeJson jsNode) throws RuntimeException, Exception {
		//boolean bfile = false;
		try{
			if(jsNode == null) return false;
			Data data = jsNode.getDataNode();
			if(data == null) return false;
			String imgStr = StringUtil.nvl(data.getIcon());			
			if("D".equals(imgStr)){
				return true;
			}else
				return false;
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public HashMap getData(JsTreeJson jsNode) throws RuntimeException, Exception {
		try{
			if (jsNode == null) return null;
			Data data = jsNode.getDataNode();
			HashMap dataMap = new HashMap();
			if(data == null) return dataMap;
			dataMap.put("title", data.getTitle());
			String imgStr = StringUtil.nvl(data.getIcon());	
			String check = StringUtil.nvl(data.getCheck());
			boolean allCheck = data.getAllCheck();
			if("D".equals(imgStr)){
				dataMap.put("icon", "/img/file.png");
			}
			if("O".equals(check))
			{
				if(allCheck )
					dataMap.put("icon",  "/img/tree_folder_icons.png");
				else
					dataMap.put("icon", "/img/tree_unfolder_icons.png");				
			}
			else if("M".equals(check)) {
				if(allCheck )
					dataMap.put("icon",  "/img/tree_check_icons.png");
				else
					dataMap.put("icon", "/img/tree_uncheck_icons.png");	
			}
			return dataMap;
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getIcon(JsTreeJson jsNode) throws RuntimeException, Exception {
		try{
			Data data = jsNode.getDataNode();
			if(data == null) return "";
			String icons = StringUtil.nvl(data.getIcon());
			if("D".equals(icons)) return "/img/file.png";
			else return "";
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getCheck(JsTreeJson jsNode) throws Exception {
		try{
			Data data = jsNode.getDataNode();
			if(data == null) return "";
			String icons = StringUtil.nvl(data.getCheck());
			boolean allCheck = data.getAllCheck();
			if("D".equals(icons)){
				if(allCheck )
					return "/img/tree_folder_icons.png";
				else
					return "/img/tree_unfolder_icons.png";
			}
			else if("M".equals(icons)){
				if(allCheck )
					return "/img/tree_check_icons.png";
				else
					return "/img/tree_uncheck_icons.png";				
			}
			else return "";
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public HashMap getAttributes(JsTreeJson jsNode) throws RuntimeException, Exception {
		try{
			if(jsNode == null) return null;
			List attList = jsNode.getAttributeList();
			HashMap arrHash = new HashMap();
			for(int i=0; i < attList.size(); i++) {
				Attributes attr = (Attributes) attList.get(i);
				arrHash.put(StringUtil.nvl(attr.getId()), StringUtil.nvl(attr.getValue()));
			}
			Data data = jsNode.getDataNode();
			String gubun = data.getIcon();
			String check = data.getCheck();
			if("D".equals(gubun)){
				arrHash.put("rel","file");
			} else if ("R".equals(gubun)) {
				arrHash.put("rel","root");
			} else if("F".equals(gubun)){
				arrHash.put("rel","folder");
			}
			if("O".equals(check)){
				arrHash.put("rel","folder");
			} else if("M".equals(check)){
				arrHash.put("rel","man");
			}
			return arrHash;
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public String getStrAttributes(JsTreeJson jsNode) throws RuntimeException, Exception {
		try{
			StringBuffer sbf = new StringBuffer();
			List attList = jsNode.getAttributeList();
			HashMap[] arrHash = new HashMap[attList.size()];
			sbf.append("{ ");
			for(int i=0; i < attList.size(); i++) {
				Attributes attr = (Attributes) attList.get(i);
				sbf.append(StringUtil.nvl("\""+attr.getId()) + "\":\"" + StringUtil.nvl(attr.getValue())+"\"");
				if(i < attList.size()-1) {
					sbf.append(",");
				}				
			}
			sbf.append(" } ");
			return sbf.toString();
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public String getJson(JsTreeJson jsNode) throws RuntimeException, Exception {
		StringBuffer sbf = new StringBuffer();
		Attributes att = new Attributes();
		Data data = new Data();
		List attList = new ArrayList();
		try{
			attList = jsNode.getAttributeList();
			data = jsNode.getDataNode();
			if(data == null) return "";
			sbf.append("data : { title : \"" + data.getTitle() + "\"");
			if(data.getIcon() != null && !data.getIcon().trim().equals("")){
				sbf.append(", icon : \"" + data.getIcon() + "\"");
			}
			sbf.append("},");
			sbf.append("state : \"closed\",");
			sbf.append("attributes : {");
			for(int i=0; i < attList.size(); i++) {
				att = (Attributes) attList.get(i);
				sbf.append("\"" + att.getId() + "\" : \"" + att.getValue() + "\"");
				if(i < attList.size()-1) sbf.append(",");
			}			
			sbf.append("}");
		}catch(RuntimeException ie){
			throw ie;
		} catch (Exception e) {
			jdf.framework.core.log.Logger.warn.println(jdf.framework.core.util.Utility.getStackTrace(e));
			throw e;
		}
		return sbf.toString();		
	}
	

	public JsTreeJson getNode(DataSet dset, String str)  throws RuntimeException, Exception{
		return getNode(dset, str, false);
	}
	
	public JsTreeJson getNode(DataSet dset, String str, boolean allCheck)  throws RuntimeException, Exception{
		String[] dat = str.split(",");
		JsTreeJson node = new JsTreeJson(); 
		List attList = new ArrayList();
		try{
			
			Data data = new Data();
			Attributes attributes = null;
	        for (int i=0;i< dat.length; i++){
	        	
	        	attributes = new Attributes();
	        	dat[i]=dat[i].trim();
	        	int a = dat[i].indexOf(":");
	        	// data type
	        	String type ="";
	        	if (a>0){
	        		type = dat[i].substring(a+1);
	        		dat[i] = dat[i].substring(0,a);
	        	}
	        	if(type != null && type.equals("data")) {
	        		data.setTitle(dset.getText(dat[i]));
	        	} else if(type != null && type.equals("icon")){
	        		data.setIcon(dset.getText(dat[i]));
	        	} else if(type != null && type.equals("check")){
	        		data.setCheck(dset.getText(dat[i]),allCheck);
	        	} else {
	        		if(dset.getText(dat[i]) != null) {
		        		attributes.setId(dat[i]);
		        		attributes.setValue(dset.getText(dat[i]));
		        		attList.add(attributes);
	        		}
	        	}
	        	
	        }
	        node.setAttributeList(attList);	        
	        node.setDataNode(data);
	        
		}catch(RuntimeException ie){
			throw ie;
		}catch(Exception e){
			throw e;
		}
		return node;
	}


	
	private class Attributes {
		String id;
		String value;
		List attList;
		
		public List getAttList() {
			return attList;
		}

		public void setAttList(List attList) {
			this.attList = attList;
		}

		public Attributes() {
		}
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
	}
	
	private class Data {
		String title;
		String icon;
		String check;
		boolean allCheck = false;
		
		public Data(){			
		}
		
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		
		public String getCheck(){
			return check;
		}
		
		public boolean getAllCheck()
		{
			return allCheck;
		}
		
		public void setCheck( String check, boolean allCheck){
			this.check = check;
			this.allCheck = allCheck;
		}
		
	}
}
