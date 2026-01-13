package Scrapers;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.*;

public class WebPageScraper {
    private enum ElementType {
        BULLET,
        MAJORHEADER,
        MINORHEADER,
        PARAGRAPH,
        TABLEROW,
    }

    public static void scrape(String path, String docName, Document doc) {
        try{
            new File(path);
            FileWriter myWriter = new FileWriter(path);
            myWriter.write("# " + docName + "\n");
            Element page = doc.getElementById("page-content");
            assert page != null;
            Elements pageContent = page.getAllElements();
            boolean inTable = false;
            boolean underStrongBullet = false;

            for(Element line : pageContent){
                ElementType eType = null;

                //Set current element type
                if(line.is("tr")){eType = ElementType.TABLEROW;}
                else if(line.is("ul")){eType = ElementType.BULLET;}
                else if(line.is("h1")){eType = ElementType.MAJORHEADER;}
                else if(line.is("h2") || line.is("h3") || line.is("h4")
                        || line.is("h5")){eType = ElementType.MINORHEADER;}
                else if(line.is("p")){eType = ElementType.PARAGRAPH;}

                if(eType == ElementType.TABLEROW){
                    underStrongBullet = false;
                    if(!inTable){
                        myWriter.write("\n ```");
                        inTable = true;
                    }
                    myWriter.write(line.text() + "\n");
                }
                if(eType != null && eType != ElementType.TABLEROW){
                    if(inTable){
                        myWriter.write("``` \n");
                        inTable = false;
                    }

                    if(eType == ElementType.MAJORHEADER){
                        underStrongBullet = false;
                        myWriter.write("# " + line.text());
                    }
                    else if(eType == ElementType.MINORHEADER){
                        underStrongBullet = false;
                        myWriter.write("\n" + "## " + line.text());
                    }
                    else if(eType == ElementType.BULLET){
                        for(Element bullet : line.select("*:not(strong)*:not(em)*:not(a)*:not(sup)*:not(sub)")
                                .remove()){
                            String text = bullet.toString();
                            if(text.contains("<strong>")){
                                underStrongBullet = true;
                            }
                            text = text.replace("<li><strong>", "- **")
                                    .replace("</strong>", "**");

                            if(underStrongBullet){
                                text = text.replace("<li>", " - ");
                            }
                            else {
                                text = text.replace("<li>", "- ");
                            }

                            text = text.replace("<em>", "*")
                                    .replace("</em>", "*")
                                    .replace("\n", "")
                                    .replaceAll("<[^<>]*>", "");
                            if(!text.isEmpty()){
                                myWriter.write(text + "\n");
                            }
                        }
                    }
                    else if (eType == ElementType.PARAGRAPH){
                        underStrongBullet = false;
                        myWriter.write("\n");
                        for(Node node : line.childNodes()){
                            if(!node.toString().isEmpty()){

                                String text = node.toString();
                                text = text.replace("<strong>", "**")
                                        .replace("</strong>", "**")
                                        .replace("<em>", "*")
                                        .replace("</em>", "*")
                                        .replace("<br>", "\n")
                                        .replace("&nbsp;", " ")
                                        .replaceAll("<[^<>]*>", "")
                                        .replace("\r\n", "");
                                myWriter.write(text);
                            }
                        }

                        myWriter.write("\n");
                    }
                }
            }
            if(inTable){
                myWriter.write("```" + "\n");
            }
            myWriter.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
