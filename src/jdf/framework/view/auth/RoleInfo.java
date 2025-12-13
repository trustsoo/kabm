package jdf.framework.view.auth;



/**
 * 
 * Role 정보를 가지고 있는 객
 * 
 * 
 * @author
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class RoleInfo
{
    private String name;
    
    private String loginUrl;
    
    private String sourceUri;
    
    public RoleInfo(String name)
    {
        this.name = name;        
    }
    
    
    
    
    /**
     * @return
     */
    public String getLoginUrl()
    {
        return loginUrl;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return
     */
    public String getSourceUri()
    {
        return sourceUri;
    }

    /**
     * @param string
     */
    public void setLoginUrl(String string)
    {
        loginUrl= string;
    }

    /**
     * @param string
     */
    public void setSourceUri(String string)
    {
        sourceUri= string;
    }
    
    
  
}