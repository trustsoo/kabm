package jdf.framework.core.pool;

import jdf.framework.core.log.Logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteBufferPool {

	private static final int MEMORY_SIZE = 1024*10;
	private static final int BUFFER_COUNT = 100;
	private static int inUse = 0;
	private static final List queue = new ArrayList();
	static
	{		
		initBuffer(BUFFER_COUNT);
	}
	
	
	
	/*public ByteBufferPool() throws IOException {
		initBuffer(BUFFER_COUNT);
	}*/
	
	private static void initBuffer(int count) {
		
		if (queue.isEmpty() != true) {
			queue.clear();
		}
		Logger.debug.println("initBuffer");
		for(int i = 0; i < count; i++) {
			// Direct memory difficulty in handle(2007.11.27)
			//ByteBuffer buffer = ByteBuffer.allocateDirect(MEMORY_SIZE);
			ByteBuffer buffer = ByteBuffer.allocate(MEMORY_SIZE);
			queue.add(buffer);
			Logger.debug.println("initBuffer : "+i+" allocate :"+MEMORY_SIZE+" bytes.");
		}
	}
	
	public static ByteBuffer getBuffer() {
		synchronized (queue) {
			if (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException e) {
					return null;
				}
			}

			inUse++;
			//Logger.debug.println("[ByteBufferPool] used Pool count:"+usedBufferCount());
			return (ByteBuffer)queue.remove(0);
		}
	}

	public static void putBuffer(ByteBuffer buffer) {
		buffer.clear();
		synchronized (queue) {
			queue.add(buffer);
			queue.notify();
			inUse--;
		}
		//Logger.debug.println("[ByteBufferPool] used Pool count:"+usedBufferCount());
	}
	
	public static void addBuffer(int size) {
		synchronized(queue) {
			// in fact, this method will never use in running
			for(int i = 0; i < size; i++) {
				ByteBuffer buffer = ByteBuffer.allocate(MEMORY_SIZE);
				queue.add(buffer);
			}
		}
	}
	
	public static void reduceBuffer(int size) {
		synchronized(queue) {
			// in fact, this method will never use in running
			for(int i = 0; i < size; i++) {
				ByteBuffer buffer = (ByteBuffer)queue.remove(0);
				buffer.clear();
				buffer = null;
			}
		}
	}
	
	public static synchronized int usedBufferCount() {
		return inUse;
	}
	
	public static synchronized int getBufferSize() {
		return queue.size();
	}
}
