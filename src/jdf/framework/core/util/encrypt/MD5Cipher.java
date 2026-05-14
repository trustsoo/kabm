package jdf.framework.core.util.encrypt;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Cipher {

	private String algorithm = null;
	private MessageDigest digest = null;
	
	public MD5Cipher() throws NoSuchAlgorithmException{
		this("MD5");
		
		
	}
	
	public MD5Cipher(String algorithm) throws NoSuchAlgorithmException{
		this.algorithm = algorithm;
		try {
			
			this.digest = MessageDigest.getInstance(algorithm);
			
		}
		catch(NoSuchAlgorithmException nsae) {
			throw new NoSuchAlgorithmException("Algorithm [" + algorithm + "] not found.");
		}
	}
	
	public byte[] encode(String data) throws NoSuchAlgorithmException {

		digest.reset();
		digest.update("naravision".getBytes());
		digest.update(data.getBytes());
		
		byte[] src = digest.digest();
		return src;
/*		
		BASE64Encoder encoder = new BASE64Encoder();
		
		String base64 = encoder.encode(src);
		
		return base64.getBytes();
*/	}
	
	public byte[] decode(String data) throws IOException {
		
		return DatatypeConverter.parseBase64Binary(data);		
	}
	
	/*-----------------------------------------------------------------------
	 For test
	 ----------------------------------------------------------------------*/

	public static void main(String[] args) {
		MD5Cipher cipher = null;
		
		try {
			cipher = new MD5Cipher("MD5");
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.out.println("[MAIN] " + e.getMessage());
			System.exit(-1);
		}
		
		String test = "stcho@naravision.net";
		System.out.println("[TEST] " + test);
		cipher.printByte(test.getBytes());
		
		byte[] enc = null;
		try {
			enc = cipher.encode(test);
		}
		catch(NoSuchAlgorithmException e) {
			System.out.println("[ENCODE ERROR] " + e.getMessage());
			System.exit(-1);
		}
		
		
		
		byte[] dec = null;
		try {
			dec = cipher.decode(new String(enc));
			System.out.println("[DECODE] " + new String(dec));
		}
		catch(IOException ioe) {
			System.out.println("[DECODE ERROR] " + ioe.getMessage());
			System.exit(-1);
		}
		
		cipher.printByte(dec);
		
		System.out.println("\n[Job Finished]");
		
		return;
	}
	
	public void printByte(byte[] data) {
		System.out.println("[Print Start]");
		
		for(int i = 0; i < data.length; i++) {
			if (i > 0 && (i % 15) == 0) 
				System.out.println();
			System.out.print("[" + toHexaValue(data[i]) + "] ");
		}
		
		System.out.println();
	}
	
	public String toHexaValue(byte b) {
		int i = (int)b;
		int mod = 0;
		int quotient = 0;
		StringBuffer sb = new StringBuffer();
		
		String[] numeric = {"0", "1", "2", "3", "4", "5", "6", "7", 
					"8", "9", "A", "B", "C", "D", "E", "F"};
		
		quotient = i;
		
		while(true) {
			
			mod = quotient % 16;
			quotient = (quotient - mod) / 16;
			if (mod < 0) mod *= (-1);
			sb.append(numeric[mod]);
			
			if (quotient < 16) {
				if (quotient < 0) quotient *= (-1);
				sb.append(numeric[quotient]);
				break;
			}
		}
		
		byte[] data = sb.toString().getBytes();
		byte[] ret = new byte[data.length];
		
		for(int j = 0, k = (data.length - 1); j < data.length; j++) {
			ret[j] = data[k--];
		}
		
		return new String(ret); 
	}
}
