import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTComments;

import java.io.*;
import java.math.BigInteger;
import java.util.List;

public class FileProcess {

    public static long num=0;

    public void LoadFile(String path,String save, String name){
        try {
            FileInputStream fis= new FileInputStream(path);
            //extract the content
            XWPFDocument file= new XWPFDocument(fis);
            //for comments
            CreateWordWithComments.MyXWPFCommentsDocument myXWPFCommentsDocument = CreateWordWithComments.createCommentsDocument(file);
            CTComments comments = myXWPFCommentsDocument.getComments();
            CTComment ctComment;
            //create list of paragraphs
            List<XWPFParagraph> paragraphs= file.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                StringBuilder sb=new StringBuilder();
                for (XWPFRun run : paragraph.getRuns()) {
                    if(run.isBold()) {
                            sb.append(run.toString());
                            String wiki = addWiki(sb.toString())+"\n";
                            if(wiki.contains("may refer to"))
                                wiki=" ";
                            BigInteger cId = BigInteger.valueOf(num);
                            num++;
                            wiki+="\n watch on YouTube:\n"+addYT(sb.toString());
                            ctComment = comments.addNewComment();
                            ctComment.setAuthor(sb.toString());
                            ctComment.addNewP().addNewR().addNewT().setStringValue(wiki);
                            ctComment.setId(cId);
                            paragraph.getCTP().addNewCommentRangeEnd().setId(cId);
                            paragraph.getCTP().addNewR().addNewCommentReference().setId(cId);
                            sb=new StringBuilder();
                        }
                    }
                }
            file.write(new FileOutputStream(save+"\\"+name+".docx"));
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String addWiki(String s) {
        boolean flag = false;
        StringBuilder s3 = new StringBuilder();
        try {
            Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/" + s).get();
            Element body = doc.body();
            StringBuilder s1 = new StringBuilder();
            StringBuilder s2 = new StringBuilder();
            if (body.html().contains("<h2>Contents</h2>")) {
                flag = true;
                s1.append(body.html().substring(0, body.html().indexOf("<h2>Contents</h2>")));
                s2.append(s1.toString().substring(s1.toString().indexOf("<p>")));
                doc = Jsoup.parse(s2.toString());
                s3.append(doc.text());
            }
        } catch (IOException e) {
        } catch (StringIndexOutOfBoundsException e2) {
        }
        return s3.toString();
    }

    public String addYT(String s) {
        boolean flag = false;
        StringBuilder s3 = new StringBuilder();
        try {
            Document doc = Jsoup.connect("https://www.youtube.com/results?search_query=" + s).get();
            Element body = doc.body();

            StringBuilder s1 = new StringBuilder();
            StringBuilder s2 = new StringBuilder();
            int start=body.html().indexOf("\"url\":\"/watch?v=")+7;
            int end=body.html().indexOf("\"",start);
            String url=body.html().substring(start,end);
            return "https://www.youtube.com"+url;

        } catch (IOException e) {
        } catch (StringIndexOutOfBoundsException e2) {
        }
        return s3.toString();
    }


}