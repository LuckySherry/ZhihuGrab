package zhihu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class UserAddress {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Throwable, MalformedURLException, IOException {
		// TODO Auto-generated method stub
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
    	
    	Scanner read = new Scanner(new BufferedReader (new FileReader(QDir+"1.dat")));
    	PrintWriter pw = new PrintWriter (new File(UDir+"1.out"));
    	//while(read.hasNext()){
    		HtmlAddress = "http://m.zhihu.com/question/20874441";//read.nextLine();
    		homePage = client1.getPage(HtmlAddress);
    		HtmlElement numElement = homePage.getFirstByXPath("//h3[@id='zh-question-answer-num']");
    		int num=0;
    		if(numElement.getAttribute("data-num")!=null){
    			num = Integer.valueOf(numElement.getAttribute("data-num"));
    		}
    		List<HtmlElement> userInfos = (List<HtmlElement>) homePage.getByXPath("//h3[@class='zm-item-answer-author-wrap']");
    		client1.waitForBackgroundJavaScript(10000);
    		/*while(userInfos.size()<num){
    			
    			System.out.println(userInfos.size());
    			HtmlElement more = (HtmlElement) homePage.createElement("a");
    			more.setAttribute("aria-role", "button");
    			more.setAttribute("class", "zg-btn-white zu-button-more");
    			HtmlElement QuePart = homePage.getFirstByXPath("//div[@id='zh-single-question']");
    			QuePart.appendChild(more);
    			homePage = more.click();
    			if(more==null)
    				continue;
    			homePage = more.click();
    			userInfos = (List<HtmlElement>) homePage.getByXPath("//h3[@class='zm-item-answer-author-wrap']");
    		}*/
    		org.jsoup.nodes.Document doc = Jsoup.parse(homePage.asXml());
    		org.jsoup.select.Elements users = doc.select("h3.zm-item-answer-author-wrap");
    		for(org.jsoup.nodes.Element user:users){
    			org.jsoup.nodes.Element names = user.select("a.zm-item-link-avatar").first();
    			if(names!=null){
    				System.out.println(names.attr("href"));
    				pw.println(names.attr("href"));
    			}
    		}
    	}
	//}

}
