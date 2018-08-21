import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.PackagePartName;
import org.apache.poi.openxml4j.opc.PackagingURIHelper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComments;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CommentsDocument;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.OutputStream;

import static org.apache.poi.POIXMLTypeLoader.DEFAULT_XML_OPTIONS;


public class CreateWordWithComments {

    //a method for creating the CommentsDocument /word/comments.xml in the *.docx ZIP archive
    public static MyXWPFCommentsDocument createCommentsDocument(XWPFDocument document) throws Exception {
        OPCPackage oPCPackage = document.getPackage();
        PackagePartName partName = PackagingURIHelper.createPartName("/word/comments.xml");
        PackagePart part = oPCPackage.createPart(partName, "application/vnd.openxmlformats-officedocument.wordprocessingml.comments+xml");
        MyXWPFCommentsDocument myXWPFCommentsDocument = new MyXWPFCommentsDocument(part);

        String rId = "rId" + (document.getRelationParts().size()+1);
        document.addRelation(rId, XWPFRelation.COMMENT, myXWPFCommentsDocument);

        return myXWPFCommentsDocument;
    }


    //a wrapper class for the CommentsDocument /word/comments.xml in the *.docx ZIP archive
    public static class MyXWPFCommentsDocument extends POIXMLDocumentPart {

        public CTComments comments;

        public MyXWPFCommentsDocument(PackagePart part) throws Exception {
            super(part);
            comments = CommentsDocument.Factory.newInstance().addNewComments();
        }

        public CTComments getComments() {
            return comments;
        }

        @Override
        public void commit() throws IOException {
            XmlOptions xmlOptions = new XmlOptions(DEFAULT_XML_OPTIONS);
            xmlOptions.setSaveSyntheticDocumentElement(new QName(CTComments.type.getName().getNamespaceURI(), "comments"));
            PackagePart part = getPackagePart();
            OutputStream out = part.getOutputStream();
            comments.save(out, xmlOptions);
            out.close();
        }

    }
}