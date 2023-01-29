package SmartPDF;
  
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfArray;
import com.itextpdf.kernel.pdf.PdfDictionary;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfNumber;
import com.itextpdf.kernel.pdf.PdfString;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.annot.PdfLineAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;

class AnnotatingMapTest {

	static final String ANNOTATEDPINMAPFILE = "AnnotatedPinNewportMap.pdf";
	static final String ANNOTATEDARROWMAPFILE = "AnnotatedArrowNewportMap.pdf";
	static final String NEWPORTMAP = "NewportMap.png";
	
	URL newportMapPath;
	
	
	@BeforeEach
	void setUp() throws Exception {
		
		newportMapPath = getClass().getClassLoader().getResource(NEWPORTMAP);
	}

	@Test
	void testAddingPinDrop() throws FileNotFoundException, MalformedURLException {
		
		File file = new File(newportMapPath.getPath());
		String pdfPath = file.getParent() + "\\" + ANNOTATEDPINMAPFILE;
		
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfPath));
		Image img = new Image(ImageDataFactory.create(newportMapPath.getPath()));
		Document doc = new Document(pdfDoc, new PageSize(img.getImageWidth(), img.getImageHeight()));
		
		img.setFixedPosition(0, 0);
		doc.add(img);
		
		// add a custom shape on top of a image
		PdfCanvas canvas =  new PdfCanvas(pdfDoc.getFirstPage());
		canvas.setStrokeColor(ColorConstants.RED)
        .setLineWidth(3)
        .moveTo(220, 330)
        .lineTo(240, 370)
        .arc(200, 350, 240, 390, 0, 180)
        .lineTo(220, 330)
        .closePathStroke()
        .setFillColor(ColorConstants.RED)
        .circle(220, 370, 10)
        .fill();
		 
		doc.close();
	}
	
	@Test
	void testAddingArrowAndTextbox() throws MalformedURLException, FileNotFoundException {
		
		File file = new File(newportMapPath.getPath());
		String pdfPath = file.getParent() + "\\" + ANNOTATEDARROWMAPFILE;
	
		PdfDocument pdfDoc = new PdfDocument(new PdfWriter(pdfPath));
		Image img = new Image(ImageDataFactory.create(newportMapPath.getPath()));
		Document doc = new Document(pdfDoc, new PageSize(img.getImageWidth(), img.getImageHeight()));
		
		img.setFixedPosition(0, 0);
		doc.add(img);
		
		Rectangle rect = new Rectangle(100, 200, 100, 100);
        PdfLineAnnotation lineAnnotation = new PdfLineAnnotation(rect,
                new float[] {220 + 5, 250 + 5, 120 + 255 - 5, 250 + 145 - 5});
        lineAnnotation.setTitle(new PdfString("You are here:"));

        // This method sets the text that will be displayed for the annotation or the alternate description,
        // if this type of annotation does not display text.
        lineAnnotation.setContents("Malbone Road");
        lineAnnotation.setColor(ColorConstants.RED);

        // Set to print the annotation when the page is printed
        lineAnnotation.setFlag(PdfAnnotation.PRINT);

        // Set arrow's border style
        PdfDictionary borderStyle = new PdfDictionary();
        borderStyle.put(PdfName.S, PdfName.S);
        borderStyle.put(PdfName.W, new PdfNumber(3));
        lineAnnotation.setBorderStyle(borderStyle);

        PdfArray le = new PdfArray();
        le.add(PdfName.OpenArrow);
        le.add(PdfName.None);
        lineAnnotation.put(PdfName.LE, le);
        lineAnnotation.put(PdfName.IT, PdfName.LineArrow);

        pdfDoc.getFirstPage().addAnnotation(lineAnnotation);
		
		doc.close();
	}

}
