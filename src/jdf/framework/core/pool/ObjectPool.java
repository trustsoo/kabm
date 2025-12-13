/*
 * @(#)ObjectPool.java
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author
 */

package jdf.framework.core.pool;

/**
 * <p>
 * 한번생성한 객체를 재활용하기 위한 Class
 * 
 * </p>
 * 
 * @author
 * @version 1.0
 */

public final class ObjectPool
{
	// 최대 Object Pool 사이즈
	private final static int MAX_POOL_SIZE = Integer.MAX_VALUE / 1000;

	private Object objects[];

	private Class cls;

	private int head; // 보유

	private int init_size;

	private int max_size;

	private int increase;

	private final static int DEFAULT_INCREASE = 20;

	private final static int DEFAULT_TRACE = 20;

	/**
     * 기본생성자
     * 
     * 
     * @param className
     * @param init_size
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     */
	public ObjectPool(String className, int init_size)
			throws ClassNotFoundException, IllegalArgumentException {
		this(Class.forName(className), init_size, MAX_POOL_SIZE,
				DEFAULT_INCREASE);
	}

	/**
     * 
     * 기본생성자
     * 
     * @param className
     * @param init_size
     * @param max_size
     * @throws ClassNotFoundException
     * @throws IllegalArgumentException
     */
	public ObjectPool(String className, int init_size, int max_size)
			throws ClassNotFoundException, IllegalArgumentException {

		this(Class.forName(className), init_size, max_size, DEFAULT_INCREASE);
	}

	/**
     * 기본생성자
     * 
     * 
     * @param cls
     * @param init_size
     * @throws IllegalArgumentException
     */
	public ObjectPool(Class cls, int init_size) throws IllegalArgumentException {
		this(cls, init_size, MAX_POOL_SIZE, DEFAULT_INCREASE);
	}

	/**
     * 기본생성자
     * 
     * @param cls
     * @param init_size
     * @param max_size
     * @throws IllegalArgumentException
     */
	public ObjectPool(Class cls, int init_size, int max_size)
			throws IllegalArgumentException {

		this(cls, init_size, max_size, DEFAULT_INCREASE);
	}

	/**
     * 
     * 기본생성자
     * 
     * 
     * @param cls
     * @param init_size
     * @param max_size
     * @param increase
     * @throws IllegalArgumentException
     */
	public ObjectPool(Class cls, int init_size, int max_size, int increase)
			throws IllegalArgumentException {
		if (cls == null || init_size < 1 || init_size > max_size
				|| increase <= 0)
			throw new IllegalArgumentException("invalid arguments");

		this.init_size = init_size;
		this.max_size = max_size;
		this.increase = increase;
		this.cls = cls;

		clear();

	}

	/**
     * 초기화 한다.
     * 
     */
	public synchronized void clear()
	{
		head = 0;
		objects = new Object[init_size];
	}

	private boolean isInit = false;

	/**
     * 초기화
     * 
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
	public void initialize() throws InstantiationException,
			IllegalAccessException
	{
		isInit = true;

		/** 초기 사이즈만큼 미리 만듬 * */
		for (int j = 0; j < init_size; j++) {
			head = j;
			objects[j] = (Object) cls.newInstance();
		}
	}

	private int requestUseNum = 0; // 현재 동작중인 객체수

	private int requestNum = 0; // getObject() 계속적인 요청횟수

	private int requestMaxNum = 0; // getObject() 요청시 대기중인 최대값

	private boolean IsGet = false; // 연속적인 Get상태인지아닌지

	private int requestArray[] = new int[DEFAULT_TRACE]; // getObject() 요청횟수을

	// 담고 있는 배열

	private int count = 0;

	/**
     * 객체를 가져온다.
     * 
     */
	public synchronized Object getObject() throws InstantiationException,
			IllegalAccessException
	{
		Object obj = null;

		if (!isInit)
			initialize();

		if (head > 0) {
			head--;
			obj = objects[head];
			objects[head] = null;
		} else {
			obj = (Object) cls.newInstance();
		}

		IsGet = true;
		requestNum++;
		requestArray[(count % requestArray.length)] = requestNum;
		if (requestNum > requestMaxNum)
			requestMaxNum = requestNum;

		requestUseNum++;

		return obj;
	}

	/**
     * 다쓴 객체를 다시 반납한다.
     * 
     */
	public synchronized void returnObject(Object pObject)
	{

		if (objects.length > head) {
			objects[head] = pObject;
			head++;

		} else {
			if (!isMaximun()) {
				expandObjectPool();
				objects[head] = pObject;
				head++;
			}

		}

		if (IsGet) {
			requestArray[(count % requestArray.length)] = requestNum;
			count = ((count % requestArray.length) == 0) ? 0 : count++;
			requestNum = 0;
			IsGet = false;
		}

		requestUseNum--;

	}

	/**
     * Pooling 용량을 확장시킨다.
     * 
     * 
     */
	public synchronized void expandObjectPool()
	{
		Object newObjectPool[];

		int newSize = objects.length + increase;

		if (newSize > max_size)
			newSize = max_size;

		newObjectPool = new Object[newSize];

		System.arraycopy(objects, 0, newObjectPool, 0, objects.length);

		objects = newObjectPool;
	}

	/**
     * Pooling 용량을 최적화 시킨다.
     * 
     * 
     */
	public synchronized void optimizeObjectPool()
	{
		int total = 0;
		int num = 0;

		for (int i = 0; i < requestArray.length; i++)
			if (requestArray[i] != 0) {
				total = total + requestArray[i];
				num++;
			}

		int newSize = 0;

		if (total == 0)
			return;
		else
			newSize = (int) ((float) total / (float) num);

		if (newSize > max_size)
			newSize = max_size;

		Object newObjectPool[];

		newObjectPool = new Object[newSize];

		if (newSize > objects.length)
			newSize = objects.length;

		System.arraycopy(objects, 0, newObjectPool, 0, newSize);

		objects = newObjectPool;

		head = objects.length;
	}

	/**
     * 현재 이 Object Pool이 최대값에 도달했는지 여부판단.
     */
	public boolean isMaximun()
	{
		return (objects.length == max_size);
	}

	public int getUsingObjectNum()
	{
		return requestUseNum;
	}

	public synchronized int getPoolSize()
	{
		return objects.length;

	}

}
