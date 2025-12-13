/*
 * @(#)XMLReferer.java
 *
 *
 * NOTICE !      
 * You can copy or redistribute this code freely except commercial use,
 * If you want to use this program for commercial use, you must contact to me.
 *
 * And, you should not remove the information about the copyright notice 
 * and the author.
 * 
 */

package jdf.framework.core.xml;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jdf.framework.core.util.SmartStringArray;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * <b><code>XMLReferer</code> </b>
 * <p>
 * XML 문서를 xPath의 원리에 아이디어를 받아 가장 쉽게 탐색할 수 있는 Class. 쓰다 보면 정말 편리한 Class
 * </p>
 * 
 * example
 * <pre>
 * XMLReferer xmlDoc = new XMLReferer("c:/test.xml");
 * 
 * xmlDoc.lookup("/transaction/sql");
 * 
 * while(xmlDoc.next())
 * {
 *    String query = xmlDoc.getText();
 *    String id = xmlDoc.getString("id");
 *    
 * }
 * </pre>
 * 
 * 
 * @author
 * @version 1.0
 */

public final class XMLReferer
{

    private Document _doc;

    /**
     * lookup method를 통해 찾은 Node 객체를 가지고 있다. #text값을 참조하거나, attr값을 참조해도 이 객체를 계속
     * 유지한다.
     */
    private Node selectedNodeByLookup;

    /**
     * find method를 통해 찾은 Node 객체를 가지고 있다. #text값을 참조하거나, attr값을 참조하면 이 변수는
     * null로 된다.
     */
    private Node selectedNodeByFind;

    private String selectedNodeByFindName;

    private boolean isFounded = false;

    private Node sibling;

    private LinkedList tmpList = new LinkedList();

    private StringBuffer pathBuff = new StringBuffer();

    /**
     * file 명으로 XMLReferer를 생성한다.
     * 
     * @param filename
     */
    public XMLReferer(String filename)
    {
        this(new File(filename));
    }

    /**
     * directory 경로와 xml화일명으로 XMLReferer를 생성한다.
     * 
     * @param dir
     * @param filename
     */
    public XMLReferer(String dir, String filename)
    {
        this(new File(dir, filename));
    }

    /**
     * java.io.File 를 이용하여 XMLReferer를 생성한다.
     * 
     * @param file
     */
    public XMLReferer(File file)
    {
        try
        {
            _doc = DocBuilder.getDocument(file);
        } catch (java.io.FileNotFoundException fe)
        {
            throw new IllegalArgumentException(file.toString() + " not found");
        } catch (Exception e)
        {
            // e.printStackTrace();
            jdf.framework.core.log.Logger.err.println("<XMLReferer> load error", e);
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * DOM 객체를 이용하여 XMLReferer를 생성한다.
     * 
     * @param doc
     */
    public XMLReferer(Document doc)
    {
        _doc = doc;
    }

    /**
     * 현재 검색노드 위치정보를 보관하고 있는다.
     * 
     */
    public void mark()
    {
        NodeInfo info = new NodeInfo(selectedNodeByLookup, selectedNodeByFindName, sibling, pathBuff.toString());

        tmpList.addLast(info);
    }

    /**
     * 마지막에 저장된 노드정보를 되살린다.
     * 
     * 
     */
    public void reset()
    {
        NodeInfo info = (NodeInfo) tmpList.removeLast();

        selectedNodeByLookup = info.getSelectedNodeByLookup();
        selectedNodeByFindName = info.getSelectedNodeByFindName();
        sibling = info.getSibling();
        pathBuff = new StringBuffer(info.getPathInfo());

    }

    /**
     * 저장된 검색노드 위치정보를 지운다.
     * 
     * ㅎ
     */
    private void clearMark()
    {
        tmpList.removeLast();
    }

    /**
     * XML element의 시작점을 지정한다.
     * 
     * <pre>
     * 
     *  
     *   
     *    
     *     
     *      
     *       
     *        
     *         
     *          다음 XML document에서
     *          &lt;top id=&quot;1&quot;&gt;
     *            &lt;sec id=&quot;A&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *            &lt;sec id=&quot;B&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *            &lt;sec id=&quot;C&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *          &lt;/top&gt;
     *         
     *          위와 같은 문서가 있는경우
     *          lookup(&quot;/top/sec&quot;);
     *          기준점이 &lt;sec id=&quot;A&quot;&gt; 태그(element)가 된다.
     *         
     *          
     *         
     *        
     *       
     *      
     *     
     *    
     *   
     *  
     * </pre>
     * 
     * @param path
     *            가상경로
     * @return XMLReferer 자신의 객체를 그대로 반환한다.
     */

    public XMLReferer lookup(String path)
    {
        if (path.startsWith("/"))
        {
            pathBuff = new StringBuffer();
        } else
        {
            pathBuff.append("/");
        }

        pathBuff.append(path);

        selectedNodeByFindName = null;

        // System.out.println("XMLReferer lookup=="+pathBuff.toString());

        return lookup(path, 0);
    }

    public String toString()
    {
        return pathBuff.toString();
        // return selectedNodeByLookup.toString();

    }

    public String getFullPath()
    {
        return pathBuff.toString();
    }

    /**
     * XML element의 시작점을 지정한다.
     * 
     * <pre>
     * 
     *  
     *   
     *    
     *     
     *      
     *       
     *        
     *         
     *          다음 XML document에서
     *          &lt;top id=&quot;1&quot;&gt;
     *            &lt;sec id=&quot;A&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *            &lt;sec id=&quot;B&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *            &lt;sec id=&quot;C&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *          &lt;/top&gt;
     *         
     *          위와 같은 문서가 있는경우
     *          lookup(&quot;/top/sec&quot;,1); 
     *          하면 기준점이 &lt;sec id=&quot;B&quot;&gt; 태그(element)로 선택 된다.
     *         
    *  
     * </pre>
     * 
     * @param path
     *            가상경로
     * @return XMLReferer 자신의 객체를 그대로 반환한다.
     */

    public XMLReferer lookup(String path, int seq)
    {
        if (path.startsWith("/"))
        {
            pathBuff = new StringBuffer();
        } else
        {
            pathBuff.append("/");
        }

        pathBuff.append(path);

        // System.out.println("XML REfer "+path+" " + seq);

        int crntSearchIndex = 0;
        String[] elementNames;

        isFounded = false;
        mark();

        if (path.startsWith("/")) // /first/sec/mid인 경우
        {
            selectedNodeByLookup = null;

            int startP = path.indexOf("/") < 0 ? 0 : path.indexOf("/");

            elementNames = SmartStringArray.split("/", path.substring(startP + 1, path.length()));

            // System.out.println("----------"+elementNames[0]+"
            // ----------"+seq);

            lookup(_doc, elementNames, crntSearchIndex, seq);

        } else
        // mid
        {
            elementNames = SmartStringArray.split("/", path);
            lookup(selectedNodeByLookup, elementNames, crntSearchIndex, seq);
        }

        if (isFounded)
            clearMark();
        else
            reset();

        return this;
    }

    private void lookup(Node node, String[] elementNames, int crntSearchIndex, int seq)
    {
        // System.out.println(elementNames[crntSearchIndex]+"
        // "+crntSearchIndex+" "+seq);

        Node child = node.getFirstChild();

        int mark = 0;

        for (; child != null; child = child.getNextSibling())
        {
            /*
             * System.out.println(" 1] "+child.getNodeName());
             * System.out.println(" 2] "+child.getNamespaceURI());
             * System.out.println(" 3] "+child.getPrefix());
             * System.out.println(" 4] "+child.getLocalName());
             */

            // System.out.println(" >> "+child.getNodeName());
            // System.out.println(" >> "+child.getLocalName());
            // System.out.println( elementNames[crntSearchIndex]+" 1>>>>>>>"
            // +child.getNodeName()+":"+child.getLocalName());

            // child.getNodeName() --> child.getLocalName() 로 변경
            // child.getLocalName() 은 #text인 경우에는 null 이다.
            String nodeNm = child.getLocalName();
            if (child.getNodeType() == Node.ELEMENT_NODE && nodeNm != null
                    && nodeNm.equals(elementNames[crntSearchIndex]))
            {

                if (crntSearchIndex < elementNames.length - 1)
                {
                    // Debug용
                    // pathBuff.append("/").append(elementNames[crntSearchIndex]);

                    ++crntSearchIndex;
                    lookup(child, elementNames, crntSearchIndex, seq);
                } else if (!isFounded && mark == seq)
                {

                    // Debug용
                    // pathBuff.append("/").append(elementNames[crntSearchIndex]).append("[").append(seq).append("]");

                    selectedNodeByLookup = child;
                    isFounded = true;
                    // System.out.println( "SETTING "+
                    // selectedNodeByLookup.getNodeName() );

                    break;
                } else
                    mark++;

            }
        }

    }

    /**
     * XML element의 시작점을 지정한다.
     * 
     * <pre>
     * 
     *  
     *   
     *    
     *     
     *      
     *       
     *        
     *         
     *          다음 XML document에서
     *          &lt;top id=&quot;1&quot;&gt;
     *            &lt;sec id=&quot;A&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *          &lt;/top&gt;
     *         
     *          위와 같은 문서가 있는경우
     *          find(&quot;/top/sec&quot;).getString(&quot;id&quot;); 
     *          하면 기준점이 &lt;sec id=&quot;B&quot;&gt; 태그(element)로 선택 된다.
     *          하지만 lookup method와 달리 이 지점을 기억하지는 않는다.
     *          
     * </pre>
     * 
     * @param path
     *            가상경로
     * @return XMLReferer 자신의 객체를 그대로 반환한다.
     */

    public XMLReferer find(String path)
    {

        int crntIndex = 0;
        String[] elemNames;

        if (path.startsWith("/")) // /first/sec/mid인 경우
        {
            selectedNodeByLookup = null;

            int startP = path.indexOf("/") < 0 ? 0 : path.indexOf("/");

            elemNames = SmartStringArray.split("/", path.substring(startP + 1, path.length()));

            selectedNodeByFind = find(_doc, elemNames, crntIndex);

        } else
        // mid
        {
            elemNames = SmartStringArray.split("/", path);
            selectedNodeByFind = find(selectedNodeByLookup, elemNames, crntIndex);
        }

        return this;
    }

    private Node find(Node node, String[] elementName, int sIndex)
    {

        Node child = node.getFirstChild();

        for (; child != null; child = child.getNextSibling())
        {
            if (child.getNodeName() != null && child.getNodeName().equals(elementName[sIndex]))
            {

                if (sIndex < elementName.length - 1)
                {
                    ++sIndex;
                    return find(child, elementName, sIndex);
                } else
                    return child;
            }
        }

        return null;
    }

    /**
     * XML element의 시작점을 지정한다.
     * 
     * <pre>
     * 
     *  
     *   
     *    
     *     
     *      
     *       
     *        
     *         
     *          다음 XML document에서
     *          &lt;top id=&quot;1&quot;&gt;
     *            &lt;sec id=&quot;A&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *            &lt;sec id=&quot;B&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *            &lt;sec id=&quot;C&quot;&gt;
     *               &lt;mid url=&quot;main.jsp&quot;&gt;title&lt;/mid&gt;
     *            &lt;/sec&gt;
     *          &lt;/top&gt;
     *         
     *          위와 같은 문서가 있는경우
     *          XMLReferer refer = new XMLReferer(&quot;doc.xml&quot;);
     *          refer.lookup(&quot;/top/sec&quot;);
     *          while(refer.next())
     *          {
     *               String url = refer.find(&quot;mid&quot;).getString(&quot;url&quot;);
     *          }
     *         
     *          
     *          즉 ResultSet의 next() method와 비슷한 역활을 한다.
     *         
     *          
     *         
     *        
     *       
     *      
     *     
     *    
     *   
     *  
     * </pre>
     * 
     * @param path
     *            가상경로
     * @return XMLReferer 자신의 객체를 그대로 반환한다.
     */
    public boolean next()
    {

        try
        {

            if (selectedNodeByFindName == null) // 처음 next면
            {
                selectedNodeByFindName = selectedNodeByLookup.getNodeName();
                sibling = selectedNodeByLookup.getNextSibling();

                // System.out.println("&&&&&&++++++++++++++
                // "+selectedNodeByFindName);
                // System.out.println("&&&&&&+++++++++++++2+"+sibling.getNodeName()+"+");

                if (isFounded)
                    return true;
                else
                    return false;
            }

            // else sibling = sibling.getNextSibling();

            // System.out.println(" -1");

            for (; sibling != null; sibling = sibling.getNextSibling())
            {

                // System.out.println(selectedNodeByFindName + " next = " +
                // sibling.getNodeName());
                // System.out.println(" -2");

                if (sibling.getNodeType() == Node.ELEMENT_NODE && selectedNodeByFindName.equals(sibling.getNodeName()))
                // if (selectedNodeByFindName.equals(sibling.getNodeName()))
                {
                    selectedNodeByLookup = sibling;
                    sibling = sibling.getNextSibling();
                    //System.out.print(".");
                    return true;
                }

            }
            // System.out.println(" -3");

        } catch (Exception e)
        {
        }

        return false;

    }

    /**
     * lookup 하거나 find한 Node의 #text 값을 가져온다.
     * 
     * @return String
     */

    public String getText()
    {
        Node node = getNode4Text();
        if (node == null)
        {

            return "";
        }

        String val = node.getNodeValue();
        if (val != null)
            val = val.trim();
        else
            val = "";

        /*
         * if(val==null || val.length()==0) { NodeList list =
         * node.getChildNodes();
         * 
         * for(int i=0; i <list.getLength(); i++) { Node tmp = list.item(i);
         * 
         * if( tmp.getNodeType() == Node.CDATA_SECTION_NODE) {
         * System.out.println("있군"); val = tmp.getNodeValue(); } } }
         */

        /*
         * if(val==null || val.length()==0) { Node tmp = node.getFirstChild();
         * if(tmp!=null && tmp.getNodeType() == Node.CDATA_SECTION_NODE) val =
         * tmp.getNodeValue(); }
         */
        return val;
    }

    /**
     * getText 와 다른점은 하위노드의 xml 태크까지 모두 Text 형태로 return 한다.
     * 
     * @return
     */
    public String getXmlText()
    {
        Node node = getNode();
        // Node node = getNode4Text();
        if (node == null)
        {
            return "";
        }

        StringWriter stw = new StringWriter();

        DOMWriter dWriter = new DOMWriter(stw, false);
        dWriter.printgetChildNodes(node);

        return stw.toString();
    }

    /**
     * 현재 선택된 node의 text의 값을 지정한다.
     * 
     * 
     * @param text
     */
    public void setText(String text)
    {
        Node node = getNodeForWrite();

        if (node == null)
        {
            return;
        }

        node.setNodeValue(text);
    }

    /**
     * lookup에 의해 선택된 node의 자식 element를 정의한다. <elementName>text </elementName> 과
     * 같은 식으로 들어간다.
     * 
     * 
     * @param elementName
     * @param text
     */
    public void setText(String elementName, String text)
    {
        find(elementName);

        if (selectedNodeByFind == null)
        {
            Element em = createElement(elementName, text, null);
            this.selectedNodeByLookup.appendChild(em);
            // System.out.println("null --------");
        }

        else
            setText(text);

    }

    /**
     * 특정 TEXT node와 Attribute를 가지는 element를 생성한다.
     * 
     * 
     * @param name
     * @param text
     * @param attrList
     * @return
     */
    public Element createElement(String name, String text, List attrList)
    {
        Element em = _doc.createElement(name);

        if (text != null)
            em.appendChild(_doc.createTextNode(text));

        if (attrList != null && attrList.size() > 0)
        {
            for (int i = 0; i < attrList.size(); i++)
            {
                Attr attr = (Attr) attrList.get(i);

                em.setAttributeNode(attr);
            }

        }

        return em;
    }

    /**
     * Element를 현재 lookup에 의핸 선택된 Element의 자식으로 추가시킨다.
     * 
     * 
     * @param em
     */
    public void appendChild(Node em)
    {
        selectedNodeByLookup.appendChild(em);
    }

    /**
     * lookup에 의해 선택된 Element 바로 위에 삽입
     * 
     * 
     * @param em
     * @deprecated
     */
    public void insertElement(Element em)
    {
        // System.out.println(selectedNodeByLookup+"/"+selectedNodeByLookup.getParentNode());
        selectedNodeByLookup.getParentNode().insertBefore(em, this.selectedNodeByLookup);
    }

    /**
     * 
     * Node 삽입
     * 
     * @param node
     */
    public void insertNode(Node node)
    {
        // System.out.println(selectedNodeByLookup+"/"+selectedNodeByLookup.getParentNode());
        selectedNodeByLookup.getParentNode().insertBefore(node, this.selectedNodeByLookup);
    }

    /**
     * lookup에 의해 선택된 Element를 제거한다.
     * 
     * @param em
     */
    public void removeElement()
    {
        // System.out.println(">>>>>>>>"+selectedNodeByLookup.getClass().getName());
        // System.out.println(">"+selectedNodeByLookup.getNodeName()+"<");

        if (selectedNodeByLookup.getParentNode() != null)
            System.out.println(" >>>> " + selectedNodeByLookup.getParentNode().getNodeName());

        // _doc.replaceChild(em,this.selectedNodeByLookup);
        try
        {
            selectedNodeByLookup.getParentNode().removeChild(this.selectedNodeByLookup);
        } catch (Exception e)
        {
            // System.out.println("--------"+e.toString());

            // e.printStackTrace();
        }
    }

    /**
     * lookup에 의해 선택된 Element를 다음 Element로 변경한다.
     * 
     * @param em
     */
    public void replaceElement(Element em)
    {
        // _doc.replaceChild(em,this.selectedNodeByLookup);
        selectedNodeByLookup.getParentNode().replaceChild(em, this.selectedNodeByLookup);
    }

    /**
     * 
     * 
     * 
     * @param name
     * @param value
     * @return
     */
    public Attr createAttribute(String name, String value)
    {
        Attr attr = _doc.createAttribute(name);
        attr.setValue(value);

        return attr;
    }

    /*
     * private Node getNode() { try { Node tmpNode= this.getSelectedNode();
     * 
     * for (Node child= tmpNode.getFirstChild(); child != null; child=
     * child.getNextSibling()) {
     * 
     * //System.out.println("getText-- getNodeName "+child.getNodeName()); if
     * (child.getNodeType() == Node.TEXT_NODE) //if (child.getNodeName() != null &&
     * child.getNodeName().equals("#text")) return child; } } catch (Exception
     * ex) { //System.out.println("**** getNode error"+ex); } return null; }
     */

    /**
     * 선택된 Node 에서 값을 얻어오기 위해 Text 또는 CDATA 를 선택해 준다.
     * 
     */
    private Node getNode4Text()
    {
        Node tmpNode = this.getNode();

        for (Node child = tmpNode.getFirstChild(); child != null; child = child.getNextSibling())
        {

            // System.out.println("getText-- getNodeName "+child.getNodeName());

            // CDATA가 있는 경우 TEXT노드 다음에 위치하게 된다.
            if (child.getNodeType() == Node.TEXT_NODE)
            {

                Node nextSb = child.getNextSibling();
                if (nextSb != null && nextSb.getNodeType() == Node.CDATA_SECTION_NODE)
                    return nextSb;
                else
                    return child;
            }
            // if (child.getNodeName() != null &&
            // child.getNodeName().equals("#text"))

            else if (child.getNodeType() == Node.CDATA_SECTION_NODE)
                return child;

        }

        return null;

    }

    private Node getNodeForWrite()
    {
        try
        {
            Node tmpNode = this.getNode();

            for (Node child = tmpNode.getFirstChild(); child != null; child = child.getNextSibling())
            {
                // System.out.println("getText-- getNodeName
                // "+child.getNodeName());
                if (child.getNodeType() == Node.TEXT_NODE)
                    // if (child.getNodeName() != null &&
                    // child.getNodeName().equals("#text"))
                    return child;
            }

            // write시 text 노드가 없을시 생성하여 반납한다.
            Node tmp = _doc.createTextNode("");
            tmpNode.appendChild(tmp);
            return tmp;

        } catch (Exception ex)
        {
            // System.out.println("**** getNode error"+ex);
        }
        return null;
    }

    /**
     * Document 객체를 얻는다.
     * 
     * 
     * @return document
     */
    public Document getDocument()
    {
        return this._doc;
    }

    /**
     * lookup에 의해 선택된 Node를 반납한다.
     * 
     * 
     * @return Node
     */
    public Node getNode()
    {
        Node tmpNode;

        if (selectedNodeByFind != null)
        {
            tmpNode = selectedNodeByFind;
            selectedNodeByFind = null;
        } else
            tmpNode = selectedNodeByLookup;

        return tmpNode;
    }

    /**
     * 현재 선택된 node의 Namespace URI를 가져온다.
     * 
     * @return
     */
    public String getNamespaceURI()
    {
        Node tmpNode = getNode();

        return tmpNode.getNamespaceURI();
    }

    /**
     * lookup 하거나 find한 Node의 attribute value 값을 가져온다.
     * 
     * @param attributeName
     * @return attributeValue
     */
    public String getString(String attrName)
    {
        try
        {
            Node tmpNode = getNode();

            NamedNodeMap atts = tmpNode.getAttributes();

            for (int i = 0; i < atts.getLength(); i++)
            {
                Node att = atts.item(i);

                if (att.getNodeName().equals(attrName))
                    return att.getNodeValue();

            }
        } catch (Exception ex)
        {
            // System.err.println(attrName+" XML "+ex);
        }

        return null;
    }

    /**
     * 현재 선택된 node에 Attribute를 설정한다. attribute가 있는 경우는 값만을 변경하고 없을시는 key를 생성하고
     * 값을 지정한다.
     * 
     * @param attrName
     * @param value
     */
    public void setAttribute(String attrName, String value)
    {
        setString(attrName, value);
    }

    /**
     * 현재 선택된 node에 Attribute를 설정한다. attribute가 있는 경우는 값만을 변경하고 없을시는 key를 생성하고
     * 값을 지정한다.
     * 
     * 
     * @param attrName
     * @param value
     */
    public void setString(String attrName, String value)
    {
        try
        {
            Node tmpNode = this.getNode();

            NamedNodeMap atts = tmpNode.getAttributes();

            for (int i = 0; i < atts.getLength(); i++)
            {
                Node att = atts.item(i);
                if (att.getNodeName().equals(attrName))
                {
                    att.setNodeValue(value);
                    return;
                }

            }

            Attr attr = this.createAttribute(attrName, value);
            ((Element) this.selectedNodeByLookup).setAttributeNode(attr);
        } catch (Exception ex)
        {

            // System.err.println(attrName+" XML "+ex);
        }

        return;
    }

    /**
     * 선택된 Node의 모든 Attribute 를 java.util.List 형태로 가져온다.
     * 
     * @return
     */
    public List getAttributeList()
    {
        try
        {
            Node tmpNode = this.getNode();

            NamedNodeMap atts = tmpNode.getAttributes();

            List list = new ArrayList();

            for (int i = 0; i < atts.getLength(); i++)
            {
                Node att = atts.item(i);
                list.add(att.getNodeName());

            }

            return list;
        } catch (Exception ex)
        {
        }

        return null;
    }

    
    public Map getAttributeMap()
    {
        try
        {
            Node tmpNode = this.getNode();

            NamedNodeMap atts = tmpNode.getAttributes();

            Map map = new HashMap();

            for (int i = 0; i < atts.getLength(); i++)
            {
                Node att = atts.item(i);
                map.put(att.getNodeName(), att.getNodeValue());

            }

            return map;
        } catch (Exception ex)
        {
        }

        return null;
    }

    /**
     * 현재 선택된 Node의 자식 Tag 리스트 가져온다.
     * 
     * @return
     */
    public List getTagList()
    {
        try
        {
            Node tmpNode = this.getNode();

            List list = new ArrayList();

            for (Node child = tmpNode.getFirstChild(); child != null; child = child.getNextSibling())
            {
                short type = child.getNodeType();

                if (type != Node.TEXT_NODE && type != Node.COMMENT_NODE)
                    list.add(child.getNodeName());

                /*
                 * String nodeName= child.getNodeName(); if (nodeName != null &&
                 * !nodeName.equals("#text") && !nodeName.equals("#comment"))
                 * list.add(nodeName);
                 */
            }

            return list;
        } catch (Exception ex)
        {
        }

        return null;
    }

    /**
     * 첫번째 자식 Node를 선택한다. 실제 nextSibling()를 하기전까지는 실제 선택된것이 아니다.
     * 
     * @deprecated
     * @return XMLReferer
     */
    public XMLReferer lookupFirstChild()
    {

        return lookupFirstChildElement(false);
    }

    /**
     * 현재 선택된 element node의 xpath 문자열을 얻는다.
     * 
     * @return
     */
    public String getXPathString(boolean printPrefix)
    {
        Node tmp = selectedNodeByLookup;
        if (tmp == null)
            return "";

        StringBuffer buf = new StringBuffer();

        while (tmp != null && tmp.getNodeType() == Node.ELEMENT_NODE)
        {
            if (printPrefix)
                buf.insert(0, "/" + tmp.getNodeName());
            else
                buf.insert(0, "/" + tmp.getLocalName());
            tmp = tmp.getParentNode();

        }

        return buf.toString();
    }

    public String getXPathString()
    {
        return getXPathString(false);
    }

    /**
     * 자식 Element Node를 가지고 있는가? cf) Text노드등은 제외시킨다.
     * 
     * @return
     */
    public boolean hasChildElement()
    {
        Node tmp = selectedNodeByLookup;
        if (tmp == null)
            tmp = this._doc;

        tmp = tmp.getFirstChild();

        while (tmp != null && tmp.getNodeType() != Node.ELEMENT_NODE)
        {
            tmp = tmp.getNextSibling();
        }

        if (tmp != null)
            return true;
        else
            return false;
    }

    /**
     * 
     * 
     * @param select
     * @return
     */
    public XMLReferer lookupFirstChildElement(boolean select)
    {

        if (selectedNodeByLookup == null)
            selectedNodeByLookup = this._doc;

        Node tmp = selectedNodeByLookup.getFirstChild();
        while (tmp != null && tmp.getNodeType() != Node.ELEMENT_NODE)
        {
            tmp = tmp.getNextSibling();
        }

        this.selectedNodeByLookup = tmp;

        // 바로 select를 할것인가?
        if (select)
        {
            this.selectedNodeByFindName = tmp.getNodeName();
        } else
        {
            this.selectedNodeByFindName = null;
        }

        return this;
    }

    /**
     * 현재 lookup에 의해 선택된 node의 이름를 return
     * 
     * @return
     */
    public String getNodeName()
    {
        return selectedNodeByFindName;
    }

    /**
     * lookupFirstChild()에 의해 선택된 node의 다음형재 node를 선택한다.
     * 
     * @return
     */
    public boolean nextSibling()
    {
        if (selectedNodeByLookup == null)
            return false;

        if (selectedNodeByFindName == null) // 처음 next면
        {
            selectedNodeByFindName = selectedNodeByLookup.getNodeName();
            return true;
        } else
        {
            Node tmp = selectedNodeByLookup.getNextSibling();

            while (tmp != null && tmp.getNodeType() != Node.ELEMENT_NODE)
            {
                tmp = tmp.getNextSibling();
            }

            if (tmp != null)
            {

                selectedNodeByLookup = tmp;
                selectedNodeByFindName = selectedNodeByLookup.getNodeName();
                return true;

            }

            selectedNodeByFindName = null;
            selectedNodeByFind = null;

            return false;
        }

    }

    /**
     * 
     * @author
     *
     */
    private static class NodeInfo
    {
        private Node selectedNodeByLookup;

        private String selectedNodeByFindName;

        private Node sibling;

        private String pathInfo;

        public NodeInfo(Node selectedNodeByLookup, String selectedNodeByFindName, Node sibling, String pathInfo)
        {
            this.selectedNodeByLookup = selectedNodeByLookup;
            this.selectedNodeByFindName = selectedNodeByFindName;
            this.sibling = sibling;
            this.pathInfo = pathInfo;
        }

        /**
         * Returns the pathInfo.
         * 
         * @return String
         */
        public String getPathInfo()
        {
            return pathInfo;
        }

        /**
         * Returns the selectedNodeByFindName.
         * 
         * @return Node
         */
        public String getSelectedNodeByFindName()
        {
            return selectedNodeByFindName;
        }

        /**
         * Returns the selectedNodeByLookup.
         * 
         * @return Node
         */
        public Node getSelectedNodeByLookup()
        {
            return selectedNodeByLookup;
        }

        /**
         * Returns the sibling.
         * 
         * @return Node
         */
        public Node getSibling()
        {
            return sibling;
        }

    }

    /**
     * 
     * 테스트용 code
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        // test();
        // testWrite();

        // testRead();

        testXpath();
        
        /*
         * File file= new File("d:/sitemap.xml");
         * 
         * XMLReferer doc= new XMLReferer(file);
         * 
         * doc.lookup("/site-menu/topmenu", 0); 
         * // <test>테스트 </test> 설정
         * doc.setText("test", "테스트"); 
         * // <test name="한글" key="테스트">한글 테스트 * </test> 설정 
         * List list= new ArrayList();
         * list.add(doc.createAttribute("name", "한글"));
         * list.add(doc.createAttribute("key", "테스트"));
         * 
         * Element em= doc.createElement("mmmm", "한글 테스트", list);
         * doc.appendChild(em);
         * 
         * doc.setAttribute("xxx", "한3");
         * 
         * //doc.replaceElement(em);
         * 
         * doc.lookup("/site-menu/topmenu", 1);
         * 
         * doc.insertElement(em);
         * 
         * Writer writer= new FileWriter(file);
         * 
         * DOMWriter dWriter= new DOMWriter(writer, false);
         * DOMWriter.setWriterEncoding("euc-kr");
         * 
         * dWriter.print(doc.getDocument());
         */

    }

    private static void testRead() throws Exception
    {
        File file = new File("c:/z.xml");

        XMLReferer doc = new XMLReferer(file);

        doc.lookup("/test/div");

        while (doc.next())
        {
            System.out.println("** " + doc.getText());
        }

    }

    private static void test() throws Exception
    {

        File file = new File("d:/io.xml");

        XMLReferer doc = new XMLReferer(file);

        doc.lookup("/transaction/processor-info");

        System.out.println(doc.find("script").getText());
    }

    private static void testXpath() throws Exception
    {

        File file = new File("c:/z.xml");

        XMLReferer doc = new XMLReferer(file);

        doc.lookup("/test/div");

        System.out.println(">>" + doc.getText());
        System.out.println(">>" + doc.getXmlText());
        System.out.println(">>" + doc.getXPathString());
    }

    private static void testWrite() throws Exception
    {
        File file = new File("d:/sitemap.xml");

        XMLReferer doc = new XMLReferer(file);

        StringWriter writer = new StringWriter();
        DOMWriter dWriter = new DOMWriter(writer);
        DOMWriter.setWriterEncoding("euc-kr");

        // dWriter.setCanonical(false);
        dWriter.setNewlines(true);
        dWriter.setTrim(true);
        dWriter.print(doc.lookup("/site-menu/topmenu").getNode());

        System.out.println(writer);
    }

}