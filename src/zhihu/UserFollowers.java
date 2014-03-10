package zhihu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

public class UserFollowers {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws JSONException 
	 */
	//167000,226000
	public static void main(String[] args) throws IOException, JSONException {
		if(args.length<3){
			System.out.println("Usage: UserFollowers [input file] [StartLine] [EndLine]");
			System.exit(0);
		}
		
		int startLine = Integer.parseInt(args[1]);
        int endLine = Integer.parseInt(args[2]);
        String fileName = args[0];
		
		final WebClient client1 = new WebClient(BrowserVersion.FIREFOX_3_6);
		String HtmlAddress = "http://www.zhihu.com/log/questions";
		String loginPage = "http://www.zhihu.com/#signin";
		String QDir = "QHttp/";
		String UDir = "UHttp/";
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
	    java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(java.util.logging.Level.OFF);
	    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	    
	    client1.getOptions().setCssEnabled(false);
		client1.getOptions().setJavaScriptEnabled(true);
		client1.getOptions().setThrowExceptionOnScriptError(false);
        client1.setAjaxController(new NicelyResynchronizingAjaxController());
		
        HtmlElement h1;
        HtmlPage homePage = client1.getPage(loginPage);
        HtmlForm logForm = homePage.getFirstByXPath("//form[@class='zu-side-login-box']");
        h1 = logForm.getInputByName("email");
    	homePage.setFocusedElement(h1);
    	h1.setAttribute("value", "lxy402290553@126.com");
    	//client1.waitForBackgroundJavaScript(1000);
    	h1 = logForm.getInputByName("password");
    	homePage.setFocusedElement(h1);
    	h1.setAttribute("value", "lxy402290553");
    	//client1.waitForBackgroundJavaScript(5000);
    	HtmlButton button = logForm.getFirstByXPath("//button[@class='sign-button']");
    	homePage =button.click();
    	
    	 //
    	homePage = client1.getPage(HtmlAddress);
    	
    	long beginTime = System.currentTimeMillis();
    	Scanner read = new Scanner(new BufferedReader(new FileReader(fileName)));
    	PrintWriter pw = new PrintWriter(new File("result" + startLine + "to" + endLine + ".out"));
    	
    	String line = null;
        
    	for (int i = 1; i < startLine; i++) {
        	line = read.nextLine();
        }
        
    	for (int lineNum = startLine; lineNum <= endLine; lineNum++) {
        	line = read.nextLine();
        	String userName = line;//.substring(0,line.indexOf("\t"));
        	PrintWriter userPw = null;// = new PrintWriter(new File(userName+"out"));
        	
        	HtmlAddress = "http://www.zhihu.com"+userName+"/followers";
        	pw.print(userName);
    		try{
    			homePage = client1.getPage(HtmlAddress);
    		}catch(Exception e){
    			e.printStackTrace();
    			pw.println("\t-1");
    			continue;
    		}
    		HtmlElement hashElement = homePage.getFirstByXPath("//div[@class='zh-general-list clearfix']");
    		HtmlElement xsrfElement = homePage.getElementByName("_xsrf");
    		HtmlElement numFollow = homePage.getFirstByXPath("//span[@class='zm-profile-section-name']");
    		String s = numFollow.asText();
    		//s = s.substring(s.indexOf(" ")+1);
    		//s = s.substring(s.indexOf(" ")+1);
    		//s = s.substring(0, s.indexOf(" "));
    		
    		System.out.println(numFollow.asText());
    		Pattern pa = Pattern.compile("[^0-9 ]");
    		Matcher m = pa.matcher(s);
    		s = m.replaceAll("").trim();
    		if(s.contains(" "))
    			s = s.substring(s.indexOf(" ")+1).trim();
    		int numOfFollower = Integer.valueOf(s);
    		//int numOfFollower = Integer.valueOf(s);
    		System.out.println(numOfFollower);
    		pw.print("\t"+numOfFollower);
    		if(numOfFollower>1000){
    			pw.println("\t"+userName+".out");
    			userPw = new PrintWriter(new File(userName.substring(8)+".out"));
    		}
    		if(numOfFollower==0||numOfFollower>50000){
    			pw.println();
    			if(userPw!=null)
	    			userPw.close();
    			continue;
    		}
    		String params = hashElement.getAttribute("data-init");
    		String xsrf_code = xsrfElement.getAttribute("value");
    		System.out.println(params);
    		System.out.println(xsrf_code);
    		
    		String hash_id,order_by,nodename;
    		int offset = 0;
    		
    		JSONObject j = new JSONObject(params);
    		hash_id = (String) j.getJSONObject("params").get("hash_id");
    		order_by = (String) j.getJSONObject("params").get("order_by");
    		nodename = (String) j.get("nodename");
    		JSONObject p = j.getJSONObject("params");
    		
    		for(;offset<numOfFollower;offset+=20){
    			p.put("offset", offset);
    			//System.out.println(p.toString());
    			
    			//System.out.println(hash_id);
    			//System.out.println(order_by);
    			//System.out.println(nodename);
    			
    			WebRequest nextPageRequest = new WebRequest(new URL("http://www.zhihu.com/node/"+nodename),HttpMethod.POST);
    			nextPageRequest.setRequestParameters(new ArrayList());
    			nextPageRequest.getRequestParameters().add(new NameValuePair("method", "next"));
    			nextPageRequest.getRequestParameters().add(new NameValuePair("params", p.toString()));
    			nextPageRequest.getRequestParameters().add(new NameValuePair("_xsrf", xsrf_code));
    			Page page = null;
    			try{
    				page = client1.getPage(nextPageRequest);
    			}catch(Exception e1){
    				e1.printStackTrace();
    				offset-=20;
    				continue;
    			}
    			
    			JSONArray msg = ((new JSONObject(page.getWebResponse().getContentAsString()).getJSONArray("msg")));
    			if(msg==null){
    				offset-=20;
    				continue;
    			}
    			for(int i = 0;i<msg.length();i++){
    				//System.out.println(msg.get(i).toString());
    				org.jsoup.nodes.Document doc = Jsoup.parse(msg.get(i).toString());
    				org.jsoup.nodes.Element title = doc.getElementsByClass("zm-list-content-title").first();
    				if(title.children().size()==0)
    					continue;
    				//System.out.println(title.child(0).attr("href"));

    				org.jsoup.select.Elements details = doc.getElementsByClass("details").first().children();
    				boolean pickUpUser = true;
    				for(org.jsoup.nodes.Element detail : details){
    					//System.out.println(detail.text());
    					if(detail.text().startsWith("0")){
    						pickUpUser = false;
    						break;
    					}
    				}
    				if(pickUpUser){
    					if(numOfFollower<=1000)
    						pw.print("\t"+title.child(0).attr("href"));
    					else
    						userPw.println(title.child(0).attr("href"));
    				}
    					
    			}
    			System.out.println("offset:\t"+offset);
    			System.out.println((System.currentTimeMillis()-beginTime)/1000);
    		}
    		System.out.println((System.currentTimeMillis()-beginTime)/1000);
    		if(numOfFollower<=1000)
    			pw.println();
    		if(userPw!=null)
    			userPw.close();
        }
		pw.close();
	}
}
