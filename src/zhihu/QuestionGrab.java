package zhihu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

public class QuestionGrab {

	/**
	 * @param args
	 */
	public class subQuestion extends Thread{
		long beginIndex;
		long endIndex;
		PrintWriter pw;
		private CountDownLatch threadsSignal;
		public subQuestion(long b,long QID,CountDownLatch threadsSignal) throws FileNotFoundException{
			this.threadsSignal=threadsSignal;
			beginIndex = QID-QID/100*(b+1);
			endIndex = QID-QID/100*(b);
	        pw = new PrintWriter(new File(beginIndex+".dat"));
		}
		public void run(){

			HttpURLConnection httpUrl = null;
	        URL url = null;
			for(long i = beginIndex;i<endIndex;i++){
	    		try{
	    	    	url = new URL(prefix+"/question/"+i);
	    	    	httpUrl = (HttpURLConnection) url.openConnection();
	    	        //连接指定的资源
	    	    	if(httpUrl.getResponseCode()!=200){
	        	    	System.out.println(prefix+"/question/"+i+"\t"+"Not Found");
	    	    		continue;
	    	    	}
	    	    		
	    	    		pw.println(prefix+"/question/"+i);
	    	    		
	    	    		System.out.println(prefix+"/question/"+i);
	    	    }
	    	    catch(SocketTimeoutException e1){
	    	    	System.out.println(prefix+"/question/"+i+"\t"+"Not Found");
	    	    } catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
			pw.close();

	        threadsSignal.countDown();
		}
	}
	
	
	final static String prefix = "http://www.zhihu.com";
	public static void main(String[] args) throws Throwable, MalformedURLException, IOException {
		// TODO Auto-generated method stub
		final WebClient client1 = new WebClient(BrowserVersion.FIREFOX_3_6);
		String HtmlAddress = "http://www.zhihu.com/log/questions";
		String loginPage = "http://www.zhihu.com/#signin";
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
    	HtmlElement QIDMeta = homePage.getFirstByXPath("//a[@target='_blank']");
    	//QIDMeta = (HtmlElement) QIDMeta.getFirstChild();
    	String href = QIDMeta.getAttribute("href");
    	href = prefix+href;
    	long QID = Integer.valueOf(href.substring(href.lastIndexOf('/')+1));
    	System.out.println(href+":"+QID);
    	try{
    	homePage = client1.getPage(prefix+"/question/1");
    	}
    	catch(FailingHttpStatusCodeException e1){
    		if(e1.getStatusCode()==404){
    			System.out.println("404 NOT FOUND");
    		}
    	}
    	int fileName = 0,lineNum=0;
    	PrintWriter pw = new PrintWriter(new File(fileName+".dat"));
    	HttpURLConnection httpUrl = null;
        URL url = null;
        
        CountDownLatch threadsSignal = new CountDownLatch(100);
    	for(long i = 0;i<100;i++){
    		QuestionGrab g = new QuestionGrab();
    		Thread t = g.new subQuestion(i,QID,threadsSignal);
    		t.start();
    	}
    	threadsSignal.await();
	}

}
