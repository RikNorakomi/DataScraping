import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class JRipper {

	//in this folder will be saved all the files and subfolders
	final String directory_target = "d:\\download\\";
	String directory_local; //name of the directory of the consoles
	String subdirectory_local; //name of the directory of the games
	
	public JRipper() {
		try {
			
			//check the existency of the base folder, if it doesn't exist, create it
			if (!new File(directory_target).exists()) new File(directory_target).mkdir();
			
			//starting from the hompage, select all the elements contained in a tag <p>
			//having class="menu", in this way we get the lateral menu
			//let's filter again on the tags <a> having the attribute href starting with a dot (.)
			//in this way we get all the sub sections of the site
			//es: NES, Game Boy, SNES etc.
			for (final Element link : Jsoup.connect("http://www.vgmusic.com/").get().select("p.menu a[href^=.]")) {
				
				//let's build the folder related to the sub section
				directory_local = directory_target + remove_forbidden_characters(link.text().trim()) + "\\";
				
				//existency check of the folder, if it doesn't exist, create it
				if (!new File(directory_local).exists()) {
					new File(directory_local).mkdir();
				}
				
				//print of the current sub section
				System.out.println(link.attr("href").replaceFirst(".", "http://www.vgmusic.com"));
				
				//now that we're on the sub section page, let's extract all the <h2> tag in order to name the sub folders (the ones of the games)
				//let's extract all the tags <a> with ref attribute ending by .mid, these are the files we're going to download
				//replace the first occurence of the attribute href of the variable link with "http://www.vgmusic.com"
				//this because on the site the pages have a relative path, for us they're remote
				for (final Element inner_link : Jsoup.connect(link.attr("href").replaceFirst(".", "http://www.vgmusic.com")).get().select("h2, a[href$=.mid]")) {
					
					//if the considered element is a <h2> tag
					if (inner_link.tagName().equals("h2")) {
						//build the name of the sub folder, if it doesn't exist, create it
						subdirectory_local = directory_local + remove_forbidden_characters(inner_link.text().trim()) + "\\";
						if (!new File(subdirectory_local).exists()) {
							new File(subdirectory_local).mkdir();
							//if we entered this if we can skip the rest of the iteration
							continue;
						}
					}
					//if the element is an <a> tag with attribute href ending with .mid
					//check if it exists, if it doesn't let's save it at the builded path
					else if (!new File(subdirectory_local + inner_link.attr("href")).exists()) {
						downloadFromUrl(new URL(link.attr("href").replaceFirst(".", "http://www.vgmusic.com") + inner_link.attr("href")), subdirectory_local + inner_link.attr("href"));
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void downloadFromUrl(final URL url, final String file_target) throws IOException {
		InputStream is = null;
		FileOutputStream fos = null;

		try {
			final URLConnection urlConn = url.openConnection();

			is = urlConn.getInputStream();
			fos = new FileOutputStream(file_target);

			byte[] buffer = new byte[4096];
			int len;

			while ((len = is.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
		}
		finally {
			try {
				if (is != null) is.close();
			}
			finally {
				if (fos != null) fos.close();
			}
		}
	}
	
	private static String remove_forbidden_characters(final String input_string) {
		
		//replace of the forbidden characters (we can't create folders or files with these characters in the name)
		return input_string.replaceAll("\\\\", "-")	    //  \
						   .replaceAll("\\/", "-")		//  /
						   .replaceAll(":", "-")		//  :
						   .replaceAll("\\*", "x")		//  *
						   .replaceAll("\\?", "-")		//  ?
						   .replaceAll("\"", "'")		//  "
						   .replaceAll("<", "-")		//  <
						   .replaceAll(">", "-")		//  >
						   .replaceAll("\\|", "-")		//  |
				;
	}

	public static void main(String[] args) {

		new JRipper();
	}
}