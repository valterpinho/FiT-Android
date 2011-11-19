package fit.main;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Valter Pinho
 */
public class Utils {

	static String server = "http://fitec.heroku.com/api/";
	
	
	/**
	 * 
	 * @param requestType GET, POST,...
	 * @param extension path extension (/sessions.xml)
	 * @param rootNode xml root node
	 * @param respFields xml fields we want to get in the response
	 * @param fields fields to pass as arguments on a request
	 * @param values value matching each field declared on the variable fields
	 * @return a string with the content requested on respFields
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static ArrayList<String> request(String requestType, String extension, String rootNode, String[] respFields, String[] fields, String[] values) throws ParserConfigurationException, SAXException, IOException{
		ArrayList<String> response = new ArrayList<String>();
		String query = "";
		
		for(int i = 0; i < fields.length; i++)
			if(i==0)
				query += fields[i] + "=" + values[i];
			else
				query += "&" + fields[i] + "=" + values[i];		
		
		URL url = null;
		
		if(fields.length > 0)
			url = new URL(server + extension + "?" + query);
		else
			url = new URL(server + extension);
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		
		
		if(requestType.equals("POST")){
			
			String charset = "UTF-8";
			
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			
			conn.setRequestProperty("Accept-Charset", charset);
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+charset);
			
			conn.setRequestMethod(requestType);
			
			// Create the form content
			OutputStream out = conn.getOutputStream();
			out.write(query.getBytes(charset));
			out.close();
		}
		
		if (conn.getResponseCode() != 200) {
			response.add(""+conn.getResponseCode());
			return response;
		}		
		
		response = parse(conn.getInputStream(), rootNode, respFields);
		
		conn.disconnect();
		
		return response;
	}	

	/**
	 * Devolve o valor de um campo através da sua tag identificadora
	 * @param sTag tag que identifica o valor do campo a retribuir
	 * @param eElement Elemento que contém o valor do campo a retribuir
	 * @return valor do campo correspondente a tag sTag
	 */
	private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
	
	public static ArrayList<String> parse(InputStream is, String rootNode, String[] fields) throws SAXException, IOException, ParserConfigurationException{
		
		ArrayList<String> solution = new ArrayList<String>();
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(is);
		doc.getDocumentElement().normalize();
		NodeList nList = doc.getElementsByTagName(rootNode);

		for (int temp = 0; temp < nList.getLength(); temp++) {

			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				for(String field: fields){
					solution.add(getTagValue(field, eElement));
					//System.out.println(field + ": " + getTagValue(field, eElement));
				}
			}
		}
		return solution;
	}
}