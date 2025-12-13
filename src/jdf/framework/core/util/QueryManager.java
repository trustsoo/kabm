package jdf.framework.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import jdf.framework.core.Config;
import jdf.framework.core.Configuration;
import jdf.framework.core.ConfigurationException;
import jdf.framework.core.log.Logger;

/**
 * <b><code>QueryManager</code></b>
 * <p>
 * 미리 저장되어 있는 text 파일 형태의 쿼리문을 File IO로 읽어들인다.
 * setXXX와 같은 메써드를 이용해 특정 형태의 문자열을 원하는 문자열로 바꿀수 있다.
 * </p>
 * @author 조은호, <a href="mailto:advan94@cyber-i.com">advan94@cyber-i.com</a>
 * @version 1.0
 */
public class QueryManager
{	   
    public String query;
	public String lineSeparator = System.getProperty("line.separator");
	public File file ;
	public BufferedReader in;

   public QueryManager( String subsystem, String filename )
    throws Exception
   {
      try
      {
         Config conf = Configuration.lookup("/com/jdf/query");
         String querydir = System.getProperty("user.dir").substring(0, System.getProperty("user.dir").length()-3) + "webapps/sip_admin_tool/WEB-INF/jdfconf"+conf.getString(subsystem);
         
         conf = Configuration.lookup("/com/nara/jdf/tool/generator/db");
         querydir = querydir + "/" + conf.getString("type").toLowerCase();
         Logger.info.println("[FILE INFO : "+querydir+"/"+filename+"]");
         file = new File( querydir,filename );
         in = new BufferedReader( new FileReader( file ) );
         setQuery( in );
         in.close();
      } catch ( ConfigurationException ce )
      {
         throw ce;
      } catch ( FileNotFoundException fe )
      {
         throw fe;
      } catch ( IOException ie )
      {
         throw ie;
      } catch ( Exception e )
      {
         throw e;
      }
   }
   
   public QueryManager( String filename )
    throws Exception
   {
      try
      {
    	 Logger.info.println("[FILE INFO : "+filename+"]");
         file = new File(filename );
         in = new BufferedReader( new FileReader( file ) );
         setQuery( in );
         in.close();
      } catch ( ConfigurationException ce )
      {
         throw ce;
      } catch ( FileNotFoundException fe )
      {
         throw fe;
      } catch ( IOException ie )
      {
         throw ie;
      } catch ( Exception e )
      {
         throw e;
      }
   }

   public void setQuery( BufferedReader in )
      throws IOException,Exception
   {
      StringBuffer sb = new StringBuffer();
      String line = null;
      while( (line = in.readLine()) != null )
      {
         if ( line.startsWith( "#" ) ) continue;
            sb.append( line ).append( lineSeparator );
      }

      query = sb.toString();
   }



   //value 가 '' 를 필요로 할때.
   public void setString( String src, String arg )
   {
      query = StringFormater.replaceStr( query , src, "'" + arg  + "'" );
   }

   //value가 ''를 필요로 하지 않을때
   public void setString2( String src, String arg )
   {
      query = StringFormater.replaceStr( query , src, arg  );
   }


   public void setMultiString( String src, Vector vec )
   {
      StringBuffer arg = new StringBuffer();
      for( int i = 0 ; i < vec.size() ; i++ )
      {
         arg.append( "'" + (String)vec.elementAt( i ) + "'" );
         if( i != (vec.size() - 1)  )
            arg.append( "," );
      }

      query = StringFormater.replaceStr( query , src, arg.toString() );
   }


   public void setMultiInt( String src, Vector vec )
   {
      StringBuffer arg = new StringBuffer();
      for( int i = 0 ; i < vec.size() ; i++ )
      {
         arg.append((String)vec.elementAt( i ));
         if( i != (vec.size() - 1)  )
            arg.append( "," );
      }

      query = StringFormater.replaceStr( query , src, arg.toString() );
   }
   
   public void setInt( String src, String arg )
   {
      query = StringFormater.replaceStr( query , src, arg );
   }



   public String toString()
   {
      return query;
   }

   public String toDebug()
   {
		String debug = "";

		debug += lineSeparator + "----------------------------------------";
		debug += lineSeparator + query;
		debug += lineSeparator + "----------------------------------------";
		debug += lineSeparator;

		return debug;
   }
}

/*   public setQueryFile( String filename )
   {
      File newfile = new File( filename );
      File oldfile = null;

      if ( querys.get( filename ) == null ||
           ((String)querys.get( filename )).equals( "" )
         )
      {
         querys.put( filename, newfile )
      }
      else
      {
         oldfile = ((File)querys.get( filename ));
         if ( oldfile.lastModified() < newfile.lastModified() )
             querys.put( filename, newfile )
      }

	   BufferedReader br = new BufferedReader( new FileInputStream(file) );
	   String line = null;
	   while( ( line = br.readLine() ) != null )
	   {
      }
   }*/

