package jdf.framework.view.auth;




/**
 * 
 *  게시판의 ACL을 체크해 주는 Class
 * 
 * Unix의 권한을 활용해 게시판의 리스트,쓰기,읽기 권한을 체크하여 준다.
 * 8 진수를 사용하는 방법은 4,2,1, 숫자를 더한 값을 100단위에서는 소유주, 10단위에는 그룹
 * 1단위에는 다른 사용자로 지정하여 사용한다.
 * 
 * 
 * 권한의미
 * 
 * 4: view권한
 * 2: write 권한
 * 1: list 권한
 * 
 * X1 X2 X3
 * X1 : 소유
 * X2 : 같은 그룹
 * X3 : 다른 사용자
 * 
 * <pre>
 * BoardAcl acl = BoardAcl.getInstance( {게시판 또는 게시물소유자 id}, {그룹명}, {권한});
 * 
 * acl.checkListPermission( {현재사용자 id}); 
 * 
 * 
 * @author 성권
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BoardAcl
{
	public static final int VIEW = 4;
	public static final int WRITE = 2;
	public static final int LIST = 1;
	
	
	public static final int OWNER = 0;
	public static final int GROUP = 1;
	public static final int OTHER = 2;
	
	
	public static final int NO_PERMISSION = -2;
	public static final int NEED_LOGIN = -1;
	
	public static final int ENABLE = 1;
	
	
	
	private String ownerId;
	private String ownerGroup;
	private String acl;
	
	private int[] aclArray = new int[3];
	
	
	private BoardAcl instance;
	

	
	
	private BoardAcl(String acl, String ownerId, String ownerGroup)
		throws IllegalArgumentException
	{
		try {
			int tmp = Integer.parseInt(acl);
			
			
			aclArray[0] = tmp/100;
			aclArray[1] = tmp/10 - aclArray[0]*10;
			aclArray[2] = tmp - aclArray[0]*100 - aclArray[1]*10;
			
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException("BoardAcl에 맞지 않는 ACL입니다. "+e.getMessage());
		}
		
		
		this.acl = acl;
		
		this.ownerId = ownerId;
		
		// gourp 명이 제대로 입력되는 경우만 set 하고, 그렇지 않은 경우는 null
		if(ownerGroup!=null && ownerGroup.length()>0)
			this.ownerGroup=ownerGroup;
		
	}
	
	public static BoardAcl getInstance(String acl, String ownerId, String ownerGroup)
		throws IllegalArgumentException
	{
		return new BoardAcl(acl, ownerId, ownerGroup);
	}
	
	
	
	
	public int checkListPermission(User user)
	{
		// OTHER 사용자에 LIST 권한이 있다면 무조건 true;
		if( isPermission(LIST, OTHER) )
			return ENABLE;
		
		
		else if( isPermission(LIST, GROUP) )
		{
			if(this.ownerGroup==null)
			{
				if(user!=null && user.isLogin())
					return ENABLE;
				else
					return NEED_LOGIN;	
			}
		}
			
		
		
		/*
		else if( isPermission(LIST, OWNER) )
		
		
		*/
		
		return NO_PERMISSION;
	}
	
	
	public int checkWritePermission(User user)
	{
		if( isPermission(WRITE, OTHER) )
			return ENABLE;
			
		else if( isPermission(WRITE, GROUP) )
		{
			if(this.ownerGroup==null)
			{
				if(user!=null && user.isLogin())
					return ENABLE;
				else
					return NEED_LOGIN;	
			}
		}
		
		return NO_PERMISSION;
	}
	


	public int checkViewPermission(User user)
	{
		if( isPermission(VIEW, OTHER) )
			return ENABLE;
			
		else if( isPermission(VIEW, GROUP) )
		{
			if(this.ownerGroup==null)
			{
				if(user!=null && user.isLogin())
					return ENABLE;
				else
					return NEED_LOGIN;	
			}
		}
		
		return NO_PERMISSION;
	}
		
	
	
	
	/**
	 * 1,2,4
	 * 
	 * 745 와 같은 acl과 2라는 position 값이 오면,
	 * 745의 세번째인 5 라는 숫자를 분석 perm에 해당하는 권한을 가지고 있는지 분석한다.
	 * 
	 * 
	 * @param perm
	 * @param position
	 * @return
	 */
	
	private boolean isPermission(int perm, int position)
	{
		if(perm==0 || position>2)
			return false;
			
			
		int x = aclArray[position]; // 0, 1, 2, 3, 4, 5,
		
		// 같은면 무조건 권한이 있다.
		if(x==perm)
			return true;
			
		//	5-4 = 1
			
		int dif = x - perm;	
		
		//  2<4 면 권한이 없다.	
		if(dif < 0)
			return false;
			
			
			
		int mod = x%2;
		
		// LIST
		if(perm==1)
		{
			if(mod==0)
				return false;
			else
				return true;	
		}
		
		else if( x>5)
			return true;
		
		else if(perm==2 && x==3)
			return true;
		
		else if(perm==4 && x==5)
			return true;	
			
		return false;	
		
		
	}
	
	
	
	public static void main(String[] args)
	{
		User user = new DefaultAuthUser();
		
		BoardAcl acl = BoardAcl.getInstance("775", "test", "");
		
		int x1 = acl.checkListPermission(user);
		int x2 = acl.checkViewPermission(user);
		int x3 = acl.checkWritePermission(user);
		
		System.out.println(x1);
		System.out.println(x2);
		System.out.println(x3);
		
	}
	

    

}
