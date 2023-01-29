package SmartPDF;


import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.Property;
 

class AddingWebLink2ImageTest {

	static final String DEST_FILE = "add_link_labrador.pdf";
	static final String DOG = "labrador-retriever.jpg";

	String pdfPath;
	URL dogFilePath;
	
	@BeforeEach
	void setup() { 
		
		dogFilePath = getClass().getClassLoader().getResource(DOG);
		File file = new File(dogFilePath.getPath());
		pdfPath = file.getParent() + "\\" + DEST_FILE;
	}
	
	@AfterEach	
	void tearDown() {
		
		// clean up
		//File pdfFile = new File(pdfPath);
		//pdfFile.delete(); 
	}
	
	@Test
	void test() throws FileNotFoundException, MalformedURLException {
		 
		//create the PDF 
		PdfWriter pdfWriter = new PdfWriter(pdfPath);
		PdfDocument pdfDoc = new PdfDocument(pdfWriter); 
		Document doc = new Document(pdfDoc);
		
		// add the linked image
		Paragraph p = new Paragraph("Object with links");
		p.add(createImage(dogFilePath.getPath(), "https://en.wikipedia.org/wiki/I_Am_a_Cat"));
		
		
		doc.add(p);
		doc.close();
	}
	
	Image createImage(String src, String url) throws MalformedURLException {
        Image img = new Image(ImageDataFactory.create(src));

        // Create the url in the image by setting action property directly
        img.setProperty(Property.ACTION, PdfAction.createURI(url));
        return img;
    }

}
