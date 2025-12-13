/**
 * 
 */
package com.kabm.batch;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.ResourceException;
import jdf.framework.core.log.Logger;

/**
 * @author trustsoo
 *
 */
public class MemberBatch{
		
	private final static String CLSS_NM = "[MemberBatch]";
	
	public static void main(String args[]) {
		
		MemberBatch mb = new MemberBatch();
		
		try
		{
			mb.excutePassword();
			//mb.excuteCorp();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void excutePassword() throws ResourceException, Exception
	{
		try{
			Logger.info.println(CLSS_NM+" excutePassword Start!"  );
			
			InteractionBean interact = new InteractionBean();
			
			DataSet input2 = new DataSet();
			DataSet output2 = new DataSet();
			input2.put("cmd", "getMemberList");
			input2.put("row_per_page", "20000");
			input2.put("cur_pg", "1");
			output2 = interact.execute("/member/MemberMgr", input2);
			
			if(output2.getMaxDataSize()> 0){
				int output2_len = output2.getMaxDataSize();
				Logger.info.println("output2_len : " + output2_len);
				DataSet result = new DataSet();
				String encKey = com.kabm.util.SitePropertyManager.getString("ENCRYPTION_KEY");
								
				int kdx = 0;
				for(int idx=0; idx<output2_len; idx++){
					result.put("ENCRYPTION_KEY", encKey, kdx);
					result.put("user_id", output2.getText("user_id", idx ) , kdx);
					result.put( "password" , output2.getText("password", idx ) , kdx);
					result.put("cmd", "MODIFY_PASSWORD_BATCH" , kdx);
					kdx++;
					
					if( idx > 0 && idx%100 == 0 )
					{
						interact.execute("/member/MemberMgr", result);
						kdx = 0;
						result = new DataSet();
					}
					
				}
				
				result.put("cmd", "MODIFY_PASSWORD_BATCH");
				interact.execute("/member/MemberMgr", result);
				Logger.info.println(CLSS_NM+" excute end!"  );
			}
				
		}catch(ResourceException re){
			Logger.warn.println(CLSS_NM+re.toString());
		}catch(Exception e){
			Logger.warn.println(CLSS_NM+e.toString());
		}
	}
	
	public void excuteCorp() throws ResourceException, Exception
	{
		try{
			Logger.info.println(CLSS_NM+" excuteCorp Start!"  );
			
			InteractionBean interact = new InteractionBean();
			
			DataSet input2 = new DataSet();
			DataSet output2 = new DataSet();
			input2.put("search_gbn_hcode",  "10");
			input2.put("cmd", "GET_CORP_LIST");
			input2.put("rnum", "1000");
			input2.put("page_no", "1");
			output2 = interact.execute("/common/CommonMgr", input2);
			
			if(output2.getMaxDataSize()> 0){
				int output2_len = output2.getMaxDataSize();
				Logger.info.println("output2_len : " + output2_len);
				DataSet result = new DataSet();
								
				int kdx = 0;
				for(int idx=0; idx<output2_len; idx++){
					result.put("user_id", output2.getText("mb_no", idx ) , kdx);
					result.put( "password" , output2.getText("mb_no", idx ) , kdx);
					
					result.put( "user_nm" , output2.getText("corp_nm", idx ) , kdx);
					result.put( "tel_no" , output2.getText("rp_hp", idx ) , kdx);
					result.put( "email" , output2.getText("email", idx ) , kdx);
					result.put( "mem_div" , "2" , kdx);
					
					result.put( "cp_code" , output2.getText("cp_code", idx ) , kdx);
					result.put( "ill_no" , output2.getText("ill_no", idx ) , kdx);
					result.put( "corp_tel_no" , output2.getText("cptel", idx ) , kdx);
					result.put( "corp_fax_no" , output2.getText("cpfax", idx ) , kdx);
					result.put("cmd", "MNG_INSERT_MEMBER" , kdx);
					
					kdx++;
					
					if( idx > 0 && idx%100 == 0 )
					{
						interact.execute("/member/MemberMgr", result);
						kdx = 0;
						result = new DataSet();
					}
					
				}
				
				result.put("cmd", "MNG_INSERT_MEMBER");
				interact.execute("/member/MemberMgr", result);
				Logger.info.println(CLSS_NM+" excute end!"  );
			}
				
		}catch(ResourceException re){
			Logger.warn.println(CLSS_NM+re.toString());
		}catch(Exception e){
			Logger.warn.println(CLSS_NM+e.toString());
		}
	}
}
