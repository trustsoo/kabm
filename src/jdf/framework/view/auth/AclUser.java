package jdf.framework.view.auth;

/**
     * 
     * 
     * 
     * 
     * @author
     *
     * To change the template for this generated type comment go to
     * Window>Preferences>Java>Code Generation>Code and Comments
     */
public class AclUser
{
    String username;
    String password;
    String roles;

    public AclUser(String username, String password, String roles)
    {
        this.username= username;
        this.password= password;
        this.roles= roles;

    }

    /**
     * @return
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @return
     */
    public String getRoles()
    {
        return roles;
    }

    /**
     * @return
     */
    public String getUsername()
    {
        return username;
    }
    
    public static void main(String[] args)
    {
        String x = "io-schema:test";
        
        System.out.println(x.substring( x.indexOf(":")+1));    
        
    
    }

}