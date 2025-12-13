package jdf.framework.core.naming;

import java.util.Properties;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 * Basic name parser used for java:comp naming space
 */
public class GeneralNameParser implements NameParser {

    static Properties syntax = new Properties();

    static {
        syntax.put("jndi.syntax.direction", "left_to_right");
        syntax.put("jndi.syntax.separator", "/");
        syntax.put("jndi.syntax.ignorecase", "false");
    }



    /**
     * Parse a name into its components.
     * @param  name The non-null string name to parse.
     * @return A non-null parsed form of the name using the naming convention
     * of this parser.
     * @exception NamingException If a naming exception was encountered.
     */
    public Name parse(String name) throws NamingException {
        return new CompoundName(name, syntax);
    }

}
