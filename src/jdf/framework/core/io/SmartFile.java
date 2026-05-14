/*
 * @(#)SmartFile.java
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

package jdf.framework.core.io;

import jdf.framework.core.util.StringFormater;

import java.io.*;
import java.util.Properties;


/**
 * <p>
 * 일반 <code>java.io.File</code> 보다 쉽게 file 객체를 다룬다.
 * </p>
 * 
 * @author
 * @version 1.0
 */

public class SmartFile extends File
{

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private BufferedReader _bReader = null;

	private static String NEWLINE = System.getProperty("line.separator");

	public SmartFile(String filename) {
		super(filename);
	}

	public SmartFile(String dir, String filename) {
		super(dir, filename);
		checkDirectory(dir);

	}

	public boolean delete()
	{
		return super.delete();
	}

	/**
     * 화일이 고유한지 체크하여 고유한 화일이면 자신의 SmartFile 객체를 넘기고, 그렇지 않으면 고유한 화일명을 만들어 SmartFile 객체로 넘긴다.
     * 
     */
	public SmartFile getUniqueFile()
	{
		if (!exists())
			return this;

		else {
			String dir = getParent();

			String filename = getUniqueFileName(dir, getName());
			return new SmartFile(dir, filename);
		}
	}

	private static String getUniqueFileName(String dir, String filename)
	{
		File file = new File(dir, filename);

		if (file.exists())
			return getUniqueFileName(dir, "!" + filename);

		else
			return filename;
	}

	/**
     * 화일을 특정 디렉토리로 복사한다.
     * 
     */
	public boolean copyTo(String dir)
	{
		return copyTo(dir, super.getName());

	}

	/**
     * 화일을 특정 디렉토리로 새로운 이름으로 복사한다.
     * 
     */
	public boolean copyTo(String dir, String newFilename)
	{

		System.out.println(dir);
		System.out.println(newFilename);

		byte[] buf = new byte[1024];

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			(new File(dir)).mkdirs();

			String destName = dir + File.separator + newFilename;

			// System.out.println(destName);

			bis = new BufferedInputStream(new FileInputStream((File) this));
			bos = new BufferedOutputStream(new FileOutputStream(destName));

			int size;

			while ((size = bis.read(buf)) > -1)
				bos.write(buf, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException ioe) {
			}
		}

		return true;
	}

	public boolean copy(String newFilename)
	{

		// System.out.println( newFilename);

		byte[] buf = new byte[1024];

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(new FileInputStream((File) this));
			bos = new BufferedOutputStream(new FileOutputStream(newFilename));

			int size;

			while ((size = bis.read(buf)) > -1)
				bos.write(buf, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException ioe) {
			}
		}

		return true;
	}

	/**
     * 
     * @param newFile
     * @return
     */
	public boolean copy(File newFile)
	{

		// System.out.println( newFilename);

		byte[] buf = new byte[1024];

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {

			bis = new BufferedInputStream(new FileInputStream((File) this));
			bos = new BufferedOutputStream(new FileOutputStream(newFile));

			int size;

			while ((size = bis.read(buf)) > -1)
				bos.write(buf, 0, size);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException ioe) {
			}
		}

		return true;
	}

	public void replace(String from, String to) throws IOException
	{
		String newFileName = super.getName() + ".bak";

		copy(newFileName);

		File file = new File(newFileName);

		BufferedReader fr = null;
		FileWriter fw = null;

		try {
			fr = new BufferedReader(new FileReader(file));
			fw = new FileWriter(this);

			while (fr.ready()) {
				String readLn = fr.readLine();

				readLn = StringFormater.replaceStr(readLn, from, to);

				fw.write(readLn);
				fw.write(NEWLINE);
			}

		} catch (IOException ioe) {
			throw ioe;
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null)
					fr.close();
				if (fw != null)
					fw.close();
			} catch (IOException ioe) {
			}
		}

	}

	/*
     * 
     * port="5000"
     * 
     * replaceValue(str, "port", "6000");
     * 
     * port="6000"
     */

	private static String replaceValue(String src, String key, String value)
	{
		if (src.indexOf(key) < 0)
			return src;

		int startP = src.indexOf("\""); // 시작 "를 찾는다.
		int endP = src.indexOf("\"", startP + 1); // 끝 " 를 찾는다.

		String preValue = src.substring(startP + 1, endP);

		/*
         * System.out.println(src); System.out.println(preValue); System.out.println(value);
         */

		return StringFormater.replaceStr(src, preValue, value);
	}

	public static void main(String[] args) throws Exception
	{

		SmartFile file = new SmartFile("test.xml");

		file.removeTag("dbPool");

	}

	/**
     * findStr 다음 문자열에서 key에 새로운 value를 세팅한다.
     * 
     * <param port="5000"/>
     * 
     */
	public void replace(String findStr, String key, String value) throws IOException
	{
		String newFileName = super.getName() + ".bak";

		copy(newFileName);

		File file = new File(newFileName);

		BufferedReader fr = null;
		FileWriter fw = null;

		try {
			fr = new BufferedReader(new FileReader(file));
			fw = new FileWriter(this);

			boolean isFind = false;

			while (fr.ready()) {
				String readLn = fr.readLine();
				if (readLn == null)
					continue;

				if (readLn.indexOf(findStr) > -1)
					isFind = true;

				if (isFind) {
					if (readLn.indexOf(key) > 0) // key 문자열이 있는 경우만
					{
						readLn = replaceValue(readLn, key, value);

						isFind = false;
					}
				}

				// readLn = jdf.framework.core.util.StringFormater.replaceStr(readLn,
				// from, to);

				fw.write(readLn);
				fw.write(NEWLINE);
			}

		} catch (IOException ioe) {
			throw ioe;
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null)
					fr.close();
				if (fw != null)
					fw.close();
			} catch (IOException ioe) {
			}
		}

	}

	public void removeTag(String findStr) throws IOException
	{

		if (findStr == null)
			return;

		String newFileName = super.getName() + ".bak";

		copy(newFileName);

		File file = new File(newFileName);

		BufferedReader fr = null;
		FileWriter fw = null;

		findStr = "<" + findStr;

		try {
			fr = new BufferedReader(new FileReader(file));
			fw = new FileWriter(this);

			boolean isFind = false;

			while (fr.ready()) {
				String readLn = fr.readLine();

				if (readLn != null && readLn.indexOf(findStr) > -1) {
					isFind = true;
					continue;
				}

				if (isFind && readLn != null && readLn.indexOf("/>") > -1) {
					isFind = false;
					continue;
				}

				if (isFind)
					continue;

				fw.write(readLn);
				fw.write(NEWLINE);
			}

		} catch (IOException ioe) {
			throw ioe;
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null)
					fr.close();
				if (fw != null)
					fw.close();
			} catch (IOException ioe) {
			}
		}

	}

	/**
     * 특정 태그 자식으로 문자열을 추가시킨다.
     * 
     * 
     */
	public void appendTag(String parentTag, String appendStr) throws IOException
	{

		if (parentTag == null || appendStr == null)
			return;

		String newFileName = super.getName() + ".bak";

		copy(newFileName);

		File file = new File(newFileName);

		BufferedReader fr = null;
		FileWriter fw = null;

		try {
			fr = new BufferedReader(new FileReader(file));
			fw = new FileWriter(this);

			while (fr.ready()) {
				String readLn = fr.readLine();

				if (readLn != null && readLn.indexOf(parentTag) > -1) {
					fw.write(readLn);
					fw.write(NEWLINE);
					fw.write(appendStr);
					fw.write(NEWLINE);
					continue;
				}

				fw.write(readLn);
				fw.write(NEWLINE);
			}

		} catch (IOException ioe) {
			throw ioe;
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fr != null)
					fr.close();
				if (fw != null)
					fw.close();
			} catch (IOException ioe) {
			}
		}

	}

	/**
     * 화일의 내용을 문자열로 가져온다.
     * 
     * @return String
     */
	public String getContent()
	{

		StringBuffer content = new StringBuffer();
		BufferedReader bReader = null;

		try {

			bReader = new BufferedReader(new FileReader((File) this));

			while (bReader.ready())
				content.append(bReader.readLine() + "\n");

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (bReader != null) {
				try {
					bReader.close();
				} catch (IOException ioe) {

				}
			}
		}

		return content.toString();

	}

	/**
     * open한 화일에 새로운 문자열 내용을 입력한다.
     * <p>
     * <b>기존의 내용이 없어지므로, 추가하고 싶은 경우는 <code>appendContent</code> 메소드를 이용한다.</b>
     * </p>
     * 
     * @return String
     */
	public void setContent(String content)
	{
		FileWriter fw = null;

		try {
			fw = new FileWriter(this);
			fw.write(content);
			fw.flush();

		} catch (Exception ex) {
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
			}

		}

	}

	/**
     * byte array로 화일내용을 채운다.
     * 
     * @return String
     */
	public void setContent(byte[] content)
	{

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(this);
			fos.write(content);
			fos.flush();

		} catch (Exception ex) {
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
			}
		}
	}

	public void setContent(InputStream in) throws IOException
	{
		// OutputStream fos= new BufferedOutputStream(new
		// FileOutputStream(this),1024);

		OutputStream fos = null;
		try {
			fos = new FileOutputStream(this);
			StreamUtil.copy(in, fos);
			fos.flush();

		} finally {
			if (fos != null)
				fos.close();
		}
	}

	/**
     * open한 화일에 문자열 내용을 덧붙인다.
     * 
     * @return String
     */
	public void appendContent(String content)
	{
		Writer fw = null;

		try {
			// for jdk1.3
			fw = new OutputStreamWriter(new FileOutputStream(this.getPath(), true));
			//fw = new FileWriter(this.getPath(), true);
			fw.write(content);
			fw.flush();

		} catch (IOException ioe) {

		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (IOException e) {
			}
		}

	}

	public BufferedInputStream getBufferedInputStream() throws FileNotFoundException
	{

		return new BufferedInputStream(new FileInputStream((File) this));
	}

	public BufferedReader getBufferedReader() throws FileNotFoundException
	{

		return new BufferedReader(new FileReader((File) this));
	}

	public boolean ready()
	{

		try {
			if (_bReader == null)
				_bReader = getBufferedReader();

			return _bReader.ready();
		} catch (Exception ex) {
			return false;
		}

	}

	public String readLine()
	{

		try {

			if (_bReader == null)
				_bReader = getBufferedReader();

			if (_bReader.ready())
				return _bReader.readLine();
			else
				return null;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}

	/**
     * open한 화일에 Properties 내용을 저장한다.
     * 
     * @param Properties
     */
	public void write(Properties props)
	{

		FileOutputStream fin = null;

		try {
			fin = new FileOutputStream(this);
			props.store(fin, "property");
		} catch (Exception ex) {
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (IOException ex2) {
			}
		}

	}

	/**
     * open한 화일이 Property 화일 형식이면 키와 그 키값의 데이타를 세팅한 Properties 객체를 반환한다.
     * 
     * @return Properties
     */
	public Properties getProperties()
	{
		Properties props = new Properties();
		FileInputStream fin = null;

		try {
			fin = new FileInputStream(this);

			props.load(fin);
		} catch (Exception ex) {
		} finally {
			try {
				if (fin != null)
					fin.close();
			} catch (IOException ex2) {
			}
		}

		return props;
	}

	/**
     * rudyoh 수정 : 2003.07.24 파일 삭제
     */

	public static void deleteFile(String dir, String filename)
	{
		File file = new File(dir, filename);

		file.delete();
	}

	/**
     * rudyoh 수정 : 2003.07.25 store 파일이름 얻기
     */
	public String getFileName()
	{
		return getName();
	}

	/**
     * rudyoh 수정 : 2003.07.25 디렉토리 생성
     */
	private void checkDirectory(String dirName)
	{
		File dir = new File(dirName);

		if (!dir.exists())
			dir.mkdirs();
	}

	/**
     * @see jdf.framework.core.io.fileserver.FileHandler#close()
     */
	public void close()
	{

		try {
			os.close();
		} catch (Exception e) {
		}

	}

	/**
     * @see jdf.framework.core.io.fileserver.FileHandler#getInputStream()
     */
	public InputStream getInputStream() throws IOException
	{
		return new FileInputStream(this);
	}

	private OutputStream os = null;

	/**
     * 
     * @see jdf.framework.core.io.fileserver.FileHandler#getOutputStream()
     */
	public OutputStream getOutputStream() throws IOException
	{
		os = new FileOutputStream(this);
		return os;
	}

	/**
     * @see jdf.framework.core.io.fileserver.FileHandler#getUniqueFileName()
     */
	public String getUniqueFileName() throws IOException
	{
		String dir = this.getParent();
		String filename = this.getName();
		File file = new File(dir, filename);

		if (file.exists())
			return getUniqueFileName(dir, "!" + filename);

		else
			return filename;

	}

	/**
     * 디렉토리 생성 있으면 생성하지 않는다.
     * 
     * @param dir
     */
	public static void buildDir(final String dir)
	{
		File d = new File(dir);
		if (!d.exists())
			d.mkdirs();
	}

	/**
     * 파일 이동
     * 
     * @param source
     * @param target
     */
	public static void move(final String source, final String target)
	{
		File sourcefile = new File(source);
		File targetfile = new File(target);
		sourcefile.renameTo(targetfile);
	}
	
	/**
	 * 상대경로를 얻는다.
	 * 
	 * @param file
	 * @param relativeTo
	 * @return
	 * @throws IOException
	 */
	public static String getRelativePath(File file, File relativeTo, String outputFileSeparator) throws IOException
	{
		if(outputFileSeparator==null || outputFileSeparator.length()==0)
			outputFileSeparator = File.separator;
		
		String path1= file.getAbsolutePath();
		String path2= relativeTo.getAbsolutePath();
		
		String[] pathArray1 = jdf.framework.core.util.SmartStringArray.split(File.separator,path1);
		String[] pathArray2 = jdf.framework.core.util.SmartStringArray.split(File.separator,path2);
		
		StringBuffer result = new StringBuffer();
		
		
		int p=0;
		//경로가 틀려지는 시점을 찾는다.
		for(int i=0;i<pathArray1.length;i++)
		{
			if(i<pathArray2.length) {
				
				// 
				System.out.println( pathArray2[i] +":"+pathArray1[i]);
				if(pathArray1[i].equals(pathArray2[i])) {
					p=i;
				}
				else {
					break;
				}
			}
			
		}
		
		// .. 를 표시할 갯수
		int p1 = pathArray2.length - p-2;
		
		for(int j=0;j<p1;j++) {
			
			if(result.length()>0)
				result.append(outputFileSeparator);
			
			result.append("..");
		}
		
		
		for(int j=p+1;j<pathArray1.length;j++) {
			
			if(result.length()>0)
				result.append(outputFileSeparator);
			result.append(pathArray1[j]);
		}


		return result.toString();
	}
}