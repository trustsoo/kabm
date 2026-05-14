package jdf.framework.logic.transform;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;


/**
 * HTML 문서로 DataSet 을 변환한다.
 * 
 * @author
 * 
 */
public class HtmlTransformerImpl extends TransformerBase
{

	/**
     * 
     * HTML 로 데이터를 변환한다.
     * 
     * @see TransformerBase#transform(DataSet, OutputStream)
     */
	public int transform(DataSet ds, OutputStream out) throws TransformerException
	{
		OutputStreamWriter writer = new OutputStreamWriter(out);

		try {

			this.writerHeader(ds, writer);
			this.writerBody(ds, writer);
			this.writerFooter(ds, writer);

			writer.flush();
		} catch (IOException ioe) {
			throw new TransformerException("html transform error", ioe);

		}

		return 0;
	}

	/**
     * HTML Header 내용을 기록한다.
     * 
     * @param ds
     * @param writer
     * @throws IOException
     */
	protected void writerHeader(DataSet ds, Writer writer) throws IOException
	{

		writer.write("<html>");

		writer.write("<head>");
		writer.write("<title>anyLOGIC execute result</title>");
		writer.write("<link rel='stylesheet' type='text/css' href='/tables.css'>");
		writer.write("</head>");

	}

	/**
     * Field 의 table header title을 얻는다.
     * 
     * @param f
     * @return
     */
	private String getHeaderTitle(Field f)
	{
		String name = f.getLabel();
		if (name == null)
			name = f.getName();

		return name;
	}

	/**
     * HTML Body 부분을 기록한다.
     * 
     * @param ds
     * @param writer
     * @throws IOException
     */
	protected void writerBody(DataSet ds, Writer writer) throws IOException
	{
		writer.write("<body>");
		Block[] blocks = ds.getIOSchema().getOutputBlocks();

		for (int i = 0; i < blocks.length; i++) {
			writer.write("<p>");
			writer.write("<table class='tbl' border='1'>");
			writer.write("<caption>");
			writer.write(blocks[i].getName());
			writer.write("</caption>");

			Field[] fields = blocks[i].getFields();

			if (fields.length > 0) {
				String fisrtKeyName = null;
				writer.write("<tr>");
				for (int j = 0; j < fields.length; j++) {
					Field f = fields[j];
					if (j == 0)
						fisrtKeyName = f.getName();
					writer.write("<th>");
					writer.write(getHeaderTitle(f));
					writer.write("</th>");

				}
				writer.write("</tr>");

				int dataCount = ds.getCount(fisrtKeyName);
				for (int k = 0; k < dataCount; k++) {
					writer.write("<tr>");

					for (int p = 0; p < fields.length; p++) {
						writer.write("<td>");
						writer.write(ds.getText(fields[p].getName(), k));
						writer.write("</td>");
					}

					writer.write("</tr>");
				}

				writer.write("</table>");

			}

			writer.write("</p>");
		}

		writer.write("</body>");
	}

	protected void writerFooter(DataSet ds, Writer writer) throws IOException
	{
		writer.write("</html>");

	}

}
