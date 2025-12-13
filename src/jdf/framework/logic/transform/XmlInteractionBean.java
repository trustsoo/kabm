package jdf.framework.logic.transform;

import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;

import jdf.framework.core.data.DataSet;


/**
 * 
 * jdf.framework.core.data.InteractionBean의 XML transform 기능을 확장한 interaction bean 이다.
 * 
 * 
 * 
 * @author
 * @version 0.8
 * @since 2003-12-17 오후 2:31:27
 *
 */
public class XmlInteractionBean extends jdf.framework.core.data.InteractionBean
{
    private TransformerFactory tFactory= TransformerFactory.getInstance();

    public XmlInteractionBean()
    {}

    private URL xsl_url;

    
    /**
     * XSL stylesheet 의 경로를 정의한다.
     * 이것을 세팅하지 않고 transform할 경우는 DataSet을 XML로만 변환시킨다. 
     * 
     * @param xsl_url
     */
    public void setStylesheet(URL xsl_url)
    {
        this.xsl_url= xsl_url;
    }

    /**
     * DataSet output을 XML로 변환한뒤 XSLT 처리를 하여
     * Writer로 출력한다.
     * 
     * 
     * @param trcode
     * @param input
     * @return
     * @throws Exception
     */
    public void transform(DataSet source, Writer writer) throws TransformerException
    {
        XmlTransformer tf= tFactory.newXmlTransformer();
        tf.setXmlType(XmlTransformer.XML_ATTRIBUTE_CENTRIC);
        if (this.xsl_url != null)
            tf.setStylesheet(xsl_url);

        tf.transform(source, writer);

    }

    /**
     * DataSet output을 XML로 변환한뒤 XSLT 처리를 하여
     * OutputStream으로 출력한다.
     * 
     * @param source
     * @param out
     * @throws TransformerException
     */
    public void transform(DataSet source, OutputStream out) throws TransformerException
    {
        XmlTransformer tf= tFactory.newXmlTransformer();
        tf.setXmlType(XmlTransformer.XML_ATTRIBUTE_CENTRIC);
        if (this.xsl_url != null)
            tf.setStylesheet(xsl_url);

        tf.transform(source, out);

    }

}
