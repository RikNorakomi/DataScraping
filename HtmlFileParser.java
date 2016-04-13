import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HtmlFileParser {

	// Resources
	// What's a delimiter?? :
	// http://pages.cs.wisc.edu/~hasti/cs302/examples/Parsing/parseString.html
	//
	// Working with the Jsoup library : Jsoup.org

	public static String TITLE = null;
	public static String CATEGORY = null;
	public static String YEAR = null;
	public static String AUTHOR = null;
	public static String FILENAME = null;
	public static String FILEPATH = null;
	public static String INPUT_DIRECTORY = "C:/Users/MEDION/Desktop/extreme picture finder exports/test/";
	public static String INPUT_DIRECTORY2 = "C:/Users/MEDION/Desktop/extreme picture finder exports/posters/";
	public static String OUTPUT_DIRECTORY = "C:/Users/MEDION/Desktop/extreme picture finder exports/";

	public static String INPUT_DIRECTORY_IMAGES = "C:/Users/MEDION/Desktop/extreme picture finder exports/treasures of soviet poster art/Landscape Images/";

	public static boolean category = false;
	public static boolean title = false;
	public static boolean year = false;
	public static boolean author = false;
	public static boolean filepath = false;
	public static boolean filename = false;

	public static ArrayList<String> filesArray = new ArrayList<>();
	public static ArrayList<String> imageArray = new ArrayList<>();
	static PrintWriter writer = null;
	static Document doc = null;

	public static void main(String[] args) throws IOException {
		writer = new PrintWriter(OUTPUT_DIRECTORY + "for real2.JSON");
		System.out.println("hi");
		createImageArray();

		readFilesFromDirectory();
		for (int i = 0; i < filesArray.size(); i++) {
			// loadDocumentFromFile(INPUT_DIRECTORY + filesArray.get(i));
			loadDocumentFromFile(filesArray.get(i));
			System.out.println("filename = " + filesArray.get(i));

			category = false;
			title = false;
			year = false;
			author = false;
			filepath = false;
			filename = false;

			getCategory();
			getTitle();
			getYear();
			getAuthor();
			getImageInfo(); // filepath filename

			if (category && title && year && author && filepath && filename) {
				
				for (int j = 0; j < imageArray.size(); j++) {
					if (imageArray.get(j).equalsIgnoreCase(FILENAME)){
						
						// write info to file
						writer.println("{");
						writer.println("	\"Title\":" + " \"" + TITLE + "\",");
						writer.println("	\"Author\":" + " \"" + AUTHOR + "\",");
						writer.println("	\"Image_filepath\":" + " \"" + FILEPATH
								+ "\",");
						writer.println("	\"Filename\":" + " \"" + FILENAME + "\",");
						writer.println("	\"Category\":" + " \"" + CATEGORY + "\",");

						writer.println("	\"Year\":" + " \"" + YEAR + "\",");

						// writer.println("	Category: " + CATEGORY);
						// writer.println("	Title: " + TITLE);
						// writer.println("	Year: " + YEAR);
						// writer.println("	Author: " + AUTHOR);
						writer.println("},");
						
						// remove arraylocation
						imageArray.remove(j);
						j--;
						
					}
				}
				
		

				System.out
						.println("------------------------------------------");
			}
		}

		writer.close();
		System.out.println("imageArray siuze after processing = " + imageArray.size());
		for (int i = 0; i < imageArray.size(); i++) {
			System.out.println(i + ". " + imageArray.get(i));
		}
		

	}

	public static void loadDocumentFromFile(String filepath) {
		String fileLocation = filepath;
		File input = new File(fileLocation);

		try {
			doc = Jsoup.parse(input, "UTF-8");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String title = doc.title();
		// System.out.println(title);

	}

	public static void loadDocumentFromURL() {
		String URL = "http://sovietart.me/posters/culture/page1/2";
		try {
			doc = Jsoup.connect(URL).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void getTitle() {
		// System.out.println("getTitleAndCategory");
		TITLE = "";
		// Title and Category are found in the h1 tag
		Elements title = doc.getElementsByTag("h1");
		// System.out.println("links size = " + title.size() + "links = " +
		// title.text());
		TITLE = title.text();
		if (!TITLE.isEmpty()) {
			HtmlFileParser.title = true;
		}
		;
		System.out.println("Title: " + TITLE);

		// for (Element h1_tag : links) {
		// String fullString = h1_tag.text();
		// String delimiter = "[()]+";
		// String[] tokens = fullString.split(delimiter);
		// System.out.println("Title = " + tokens[0]);
		//
		// TITLE = tokens[0];
		// System.out.println("post title =");
		// System.out.println("Category = " + tokens[1]);
		// CATEGORY = tokens[1];
		// }

		// TODO: Title & Category splitting will only go well if title doesn't
		// contain any ( or ) characters

	}

	public static void getCategory() {
		Elements input = doc.getElementsByTag("input");
		for (Element category : input) {
			if (category.toString().contains("checked")) {
				// System.out
				// .println("Category: " + category.attr("value"));
				CATEGORY = category.attr("value");
				System.out.println("Category: " + CATEGORY);
				HtmlFileParser.category = true;

			}
		}
	}

	public static void getYear() {
		// Year can be found in the <p> tag
		// System.out.println("getYear");
		Elements p = doc.getElementsByTag("p");

		for (Element year : p) {
			if (year.toString().contains("Year")) {
				System.out
						.println("Year: " + year.getElementsByTag("a").text());
				YEAR = year.getElementsByTag("a").text();
				HtmlFileParser.year = true;
			}

		}

	}

	public static void getAuthor() {
		// Author can be found in <p> tag
		Elements p = doc.getElementsByTag("p");
		for (Element author : p) {
			if (author.toString().contains("Author")) {
				System.out.println("Author: "
						+ author.getElementsByTag("a").text());
				AUTHOR = author.getElementsByTag("a").text();
				HtmlFileParser.author = true;
			}

		}
	}

	public static void getImageInfo() {
		Elements img = doc.getElementsByTag("img");
		for (Element images : img) {

			// System.out.println("tag src = " +
			// images.getElementsByTag("src").toString());

			if (images.toString().contains("jpg")
					&& !images.toString().contains("random")
					&& images.id().contains("poster")) {

				System.out.println("image filepath+name = "
						+ images.attr("src"));
				FILEPATH = images.attr("src");
				HtmlFileParser.filepath = true;

				String fullString = images.attr("src");
				String delimiter = "/";
				String[] tokens = fullString.split(delimiter);
				// System.out.println("token length = " + tokens.length);
				System.out.println("filename = " + tokens[tokens.length - 1]);
				FILENAME = tokens[tokens.length - 1];
				HtmlFileParser.filename = true;
				// System.out.println(images.toString());
			}

		}
	}

	public static void getJpgLinks() {
		// selector-syntax
		Elements jpg_imageLinks = doc.select("img[src$=.jpg]");
		for (Element jpgs : jpg_imageLinks) {
			System.out.println("jpg links= " + jpgs.toString());
		}
	}

	public static void createImageArray() {
		final File imageFolder = new File(INPUT_DIRECTORY_IMAGES);

		for (final File fileEntry : imageFolder.listFiles()) {

			imageArray.add(fileEntry.getName());
//			System.out.println("filename = " + fileEntry.getName()
//					+ " filepath " + fileEntry.getAbsolutePath());
		}
		
		for (int i = 0; i < imageArray.size(); i++) {
			System.out.println("filname (imageArray) = " + imageArray.get(i));
		}
	}

	public static void readFilesFromDirectory() {
		// for more info:
		// http://stackoverflow.com/questions/1844688/read-all-files-in-a-folder

		final File folder = new File(INPUT_DIRECTORY2);
		listFilesForFolder(folder);
	}

	public static void listFilesForFolder(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				// System.out.println(fileEntry.getName());
				filesArray.add(fileEntry.getAbsolutePath());
				System.out.println("filename = " + fileEntry.getName()
						+ " filepath " + fileEntry.getAbsolutePath());
			}
		}
	}

}
