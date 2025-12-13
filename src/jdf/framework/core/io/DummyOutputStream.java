package jdf.framework.core.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 아무짓도 하지 않는 OutputStream
 * 
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DummyOutputStream extends OutputStream {
	public DummyOutputStream() {
	}
	public void write(int b) throws IOException {
	}
	public void write(byte[] b) throws IOException {
	}
	public void write(byte[] b, int off, int len) throws IOException {
	}
}