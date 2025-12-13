package jdf.framework.core.log;

public class LogInfo
{
	public final static int INFO = 0;

	public final static int DUMP = 1;

	public final static int ERR = 2;

	int mode = INFO;

	String sMode;
	String serverId;
	byte[] data;

	String strData;

	Throwable error;
	boolean isDBWrite = false;
	
	final long time = System.currentTimeMillis();

	// thread 의 id를 hex 형태로
	final String threadId = Integer.toHexString(Thread.currentThread().hashCode());

	/**
     * 기본생성자
     * 
     * @param desc
     * @param data
     * @param strData
     * @param error
     */
	public LogInfo(String sMode, String serverId, byte[] data, String strData, Throwable error, boolean isDBWrite) {

		this.sMode = sMode;
		this.serverId = serverId;
		this.data = data;
		this.strData = strData;
		this.error = error;
		this.isDBWrite =isDBWrite;
	}

	// 정상
	public LogInfo(String sMode, String serverId, String strData, boolean isDBWrite) {

		this.sMode = sMode;
		this.serverId = serverId;
		this.strData = strData;
		this.isDBWrite =isDBWrite; 
	}

	// DUMP
	public LogInfo(String sMode, String serverId, byte[] data, boolean isDBWrite) {

		this.sMode = sMode;
		this.serverId = serverId;
		this.data = data;
		this.mode = DUMP;
		this.isDBWrite =isDBWrite; 
	}

	// ERR
	public LogInfo(String sMode, String serverId, Throwable error, boolean isDBWrite) {

		this.sMode = sMode;
		this.serverId = serverId;
		this.error = error;
		this.mode = ERR;
		this.isDBWrite =isDBWrite; 
	}

}
