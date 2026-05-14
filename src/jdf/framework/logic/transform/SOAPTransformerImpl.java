package jdf.framework.logic.transform;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.InteractionBean;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.log.Logger;

import javax.xml.soap.*;
import java.io.IOException;
import java.io.OutputStream;


public class SOAPTransformerImpl extends TransformerBase
{
	private final static String LOG_ID = "<l:SOAPTransformerImpl> ";

	SOAPTransformerImpl() {
		Logger.debug.println(LOG_ID + "객체 생성");
	}
	
	public int transform(DataSet ds, OutputStream out) throws TransformerException
	{
		Block[] blocks = ds.getBlocks();
		
		transToElementSOAPSring(blocks, ds, out);
		
		return 0;
	}
	
	
	public static void main(String[] args) throws Exception
	{
		InteractionBean interact = new InteractionBean();
		DataSet input =new DataSet();
		input.put("cmd", "getDeptList");
		DataSet output = interact.execute("/sample/Code", input);
		
		DataSet dest = output.copyDataSetByBlockNames(new String[]{"dept_list"});
		
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		
		
		SOAPTransformerImpl impl = new SOAPTransformerImpl();
		impl.transform(dest, bos);
		
		System.out.println(bos.toString("utf-8"));
		
		
		
	}
	
	
	protected void transToElementSOAPSring(Block[] blocks, DataSet dataset, OutputStream out) {
		// return transToElementXmlSring(rootElementName, blocks, dataset, true,
		// blocks);
		transToElementSOAPSring(blocks, dataset, blocks, out);

	}
	
	private void setBlockData(DataSet source, Block block, SOAPElement element) throws SOAPException
	{
		Field[] fields = block.getFields();
		if (fields.length == 0)
			return;
		
		String firstKeyName = fields[0].getName();

		SOAPElement sElement2 = null;//sElement.addChildElement(block.getName());
		String keyName = null;
		for (int j = 0; j < source.getCount(firstKeyName); j++) 
		{
			sElement2 = element.addChildElement(block.getName());
			for (int i = 0; i < fields.length; i++) 
			{
				keyName = fields[i].getName();
				sElement2.addChildElement(keyName).addTextNode(source.getText(keyName, j));
			}
		}
	}
	
	
	private void transToElementSOAPSring(Block[] blocks, DataSet dataset, Block[] orgblocks, OutputStream out) throws TransformerException
	{	
		try
		{
			MessageFactory mf = MessageFactory.newInstance();
			
			
			
			SOAPMessage sMessage = mf.createMessage();			
			sMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, defaultEncoding);
			
			sMessage.getSOAPPart().getEnvelope().removeNamespaceDeclaration("SOAP-ENV");
			sMessage.getSOAPPart().getEnvelope().setPrefix("soapenv");
			SOAPHeader sHeader = sMessage.getSOAPHeader();
			sHeader.setPrefix("soapenv");			
			
			/*SOAPElement hElement = sHeader.addChildElement("result");
			hElement.addChildElement("code").addTextNode("200");
			hElement.addChildElement("msg").addTextNode("success");*/
			
			//.addChildElement("code").addTextNode("200");
			//sHeader.addChildElement("msg").addTextNode("success");
			
			
			
			SOAPBody sBody = sMessage.getSOAPBody();
			sBody.setPrefix("soapenv");
			
			//SOAPElement sElement = sBody.addChildElement("sam:result");
			SOAPElement sElement = sBody.addChildElement("result");
			sElement.addChildElement("code").addTextNode("@code");
			sElement.addChildElement("msg").addTextNode("@msg");
			
			for (int i = 0; i < blocks.length; i++) 
			{
				Block block = blocks[i];
				
				//SOAPElement sElement2 = sElement.addChildElement(block.getName());
				setBlockData(dataset, block, sElement);
				
			}	
				
			sMessage.saveChanges();
			
			sMessage.writeTo(out);
			//return sMessage.toString();
			
			
		} catch(IOException ie)
		{	
			new TransformerException(ie.getMessage(), ie);
		}catch(UnsupportedOperationException ue)
		{
			new TransformerException(ue.getMessage(), ue);
		} catch(SOAPException se)
		{
			new TransformerException(se.getMessage(), se);
		} catch(TransformerException te)
		{
			throw te;
		}	
		
	}
}