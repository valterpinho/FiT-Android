package fit.main;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
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

import android.util.Log;

/**
 *
 * @author Valter Pinho
 */
public class Utils {

	static String server = "http://fitec.heroku.com/";
	
	public static void setCookies(){
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
	}

	public static ArrayList<String> GET(String extension, String rootNode, String[] fields) throws ParserConfigurationException, SAXException, IOException{

		URL url = new URL(server + extension);
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		ArrayList<String> solution = new ArrayList<String>();

		if(connection.getResponseCode() == 200){

			solution = parse(connection.getInputStream(), rootNode, fields);
		}
		else{

			Log.e("Error: Response code ", ""+ connection.getResponseCode());
		}             
		return solution;
	}
	
	public static ArrayList<String> POST(String extension, String rootNode, String[] respFields, String[] fields, String[] values) throws ParserConfigurationException, SAXException, IOException{
		ArrayList<String> response = new ArrayList<String>();
	
		
		URL url = new URL(server + extension);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String charset = "UTF-8";
		
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		
		conn.setRequestProperty("Accept-Charset", charset);
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+charset);
		
		conn.setRequestMethod("POST");
		
		String query = "";
		
		for(int i = 0; i < fields.length; i++)
			query += "&" + fields[i] + "=" + values[i];
		
		query.substring(1);
		
		// Create the form content
		OutputStream out = conn.getOutputStream();
		out.write(query.getBytes(charset));
		out.close();
		
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