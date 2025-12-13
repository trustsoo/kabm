package jdf.framework.core.util.collection;

/**
 * <p>
 * RunnableList의 객체를 ListIterator와 비슷하게 Iterator 형식으로 받아내기 위하여 만든 클래스입니다.
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface Runnarator 
{
	public boolean hasNext();
	public Runnable next();
    public boolean hasPrevious();
	public Runnable previous();
    public int nextIndex();
    public int previousIndex();
    public void add(Runnable o);
    public void set(Runnable o);
    public void remove();
}