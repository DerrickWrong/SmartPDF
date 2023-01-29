package SmartPDF;
 

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.Property;

class RemoteGoToTest {
 
	String path;
	
	static final String REMOTEPATHPDF = "remote_goto.pdf";
	static final String SUBDIRPDF = "subdir/xyz.pdf";

	@BeforeEach
	void setUp() throws Exception {

		URL newportMapPath = getClass().getClassLoader().getResource("NewportMap.png");
		File file = new File(newportMapPath.getPath());
		path = file.getParent();
		
		// create the subdir
		File subdir = new File(path + "\\subdir/");
		subdir.mkdirs();

	}

	@Test
	void test() throws Exception {

		createLinkPdf(path + "\\" + REMOTEPATHPDF);
		createDestinationPdf(path + "\\" + SUBDIRPDF);
	}

	// This method creates a link destination pdf file.
	private void createDestinationPdf(String dest) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		Paragraph anchor = new Paragraph("This is a destination");

		// Set string destination, to which the created in the another pdf file link
		// will lead.
		anchor.setProperty(Property.DESTINATION, "dest");
		doc.add(anchor);

		doc.close();
	}

	// This method creates a pdf file, which will contain a link
	// to the page with set string destination of another pdf file.
	private static void createLinkPdf(String dest) throws Exception {
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
		Document doc = new Document(pdfDoc);

		// Create a link action, which leads to the another pdf file's page.
		// The 1st argument is the relative destination pdf file's path;
		// the 2nd argument is the string destination in the destination pdf file,
		// to which the link will lead after a click on it.
		PdfAction action = PdfAction.createGoToR(SUBDIRPDF, "dest");
		Paragraph chunk = new Paragraph(new Link("Link", action));
		doc.add(chunk);

		doc.close();
	}

}
