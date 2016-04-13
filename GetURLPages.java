import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class GetURLPages {
	
	private final static String BASE_URL = "http://sovietart.me/posters/all/page";
	
	public static String OUTPUT_DIRECTORY = "C:/Users/MEDION/Desktop/SovietPosterArt/";
	
	
	 public static void main(String[] args) throws IOException {
		 int numberOfPages = 99;
		 PrintWriter writer = new PrintWriter(OUTPUT_DIRECTORY + "sovietArtMePages.JSON");
		 	
		 
		// write info to file
		writer.println("\"soviet_art_me_pages\":[" + "\n");
		 
		 for (int i = 1; i < numberOfPages; i++) {
				int startHtmlNr = 1;
				String pageUrl = BASE_URL + i + "/";
				
				while (isValidUrl(pageUrl + startHtmlNr)) {
					 System.out.println(pageUrl + startHtmlNr);
					 writer.println("   {\"url\":\"" + pageUrl + startHtmlNr + "\"}," + "\n");
					 startHtmlNr++;
					 
					 }			
			}
		 
		 writer.println("]");
		 

	

			writer.close();
	
		 
//	        Document doc = Jsoup.connect(BASE_URL).get();
//	        String hrefAttribute = "";
//	        for (Element file : doc.getElementsByAttribute("href")) {
//	        	hrefAttribute = file.attr("href");
//	        	if (hrefAttribute.contains("page")){
//	            System.out.println(file.attr("href"));}
//	        }
	        
//	        System.out.println(doc.toString());
	    }
	 
	 public static boolean isValidUrl(String url){
		 Document doc = loadDocumentFromUrl(url);
		 if (doc==null) {
			 return false;
		 } else {
			 return true;
		 }		 
	 }
	 
	 
	 public static Document loadDocumentFromUrl(String url) {
		   try {
			   Document doc = Jsoup.connect(url).get();
			   if (doc.title().contains("Oops! Page Not Found")){
				   return null;
			   }
			   return doc;
		    }
		    catch(IOException ex){
		        System.out.println (ex.toString());
		        return null;
		      		    }
		 
		 
	
//		 Document doc = Jsoup.connect("http://example.com")
//		   .data("query", "Java")
//		   .userAgent("Mozilla")
//		   .cookie("auth", "token")
//		   .timeout(3000)
//		   .post();
	 }

}
