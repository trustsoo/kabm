package jdf.framework.view.layout.entity;

import java.util.HashMap;


/**
 * Layout 정보 javabean
 * 
 * @author
 *
 */
public class Layout extends HashMap
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7558652479917259797L;

	// layout 명
    private String name;
    
    // header 유무
    private boolean headerExist;

    // footer 유무
    private boolean footerExist;
    
    // columns 갯수
    private int columns;
    
    
    // template 명
    private String template;
    
    private String headerFileName;
    
    private String footerFileName;
    
    
    
    
    public Layout(String name)
    {
        super();
        this.name = name;
    }
    
    
    
    
    
    /**
     * Returns the columns.
     * @return int
     */
    public int getColumns()
    {
        return columns;
    }

    /**
     * Returns the footerExist.
     * @return boolean
     */
    public boolean isFooterExist()
    {
        return footerExist;
    }

    /**
     * Returns the headerExist.
     * @return boolean
     */
    public boolean isHeaderExist()
    {
        return headerExist;
    }

    /**
     * Returns the name.
     * @return String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the columns.
     * @param columns The columns to set
     */
    public void setColumns(int columns)
    {
        this.columns= columns;
    }

    /**
     * Sets the footerExist.
     * @param footerExist The footerExist to set
     */
    public void setFooterExist(boolean footerExist)
    {
        this.footerExist= footerExist;
    }

    /**
     * Sets the headerExist.
     * @param headerExist The headerExist to set
     */
    public void setHeaderExist(boolean headerExist)
    {
        this.headerExist= headerExist;
    }
    
    
    
    public void setParam(String key, String val)
    {
        put(key, val);
    }
    
    public String getParam(String key)
    {
        return (String) get(key);
    }
       

    /**
     * Returns the template.
     * @return String
     */
    public String getTemplate()
    {
        return template;
    }

    /**
     * Sets the template.
     * @param template The template to set
     */
    public void setTemplate(String template)
    {
        this.template= template;
    }


	/**
	 * @return
	 */
	public String getFooterFileName() {
		return footerFileName;
	}

	/**
	 * @return
	 */
	public String getHeaderFileName() {
		return headerFileName;
	}

	/**
	 * @param string
	 */
	public void setFooterFileName(String string) {
		footerFileName = string;
	}

	/**
	 * @param string
	 */
	public void setHeaderFileName(String string) {
		headerFileName = string;
	}

}

    
    