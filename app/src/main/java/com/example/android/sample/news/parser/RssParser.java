package com.example.android.sample.news.parser;

import com.example.android.sample.news.data.Link;
import com.example.android.sample.news.data.Site;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * RSSの入力ストリームを受け取って、SiteとList<Link>を生成する
 */

public class RssParser {

    //サイト情報
    private Site site;

    //リンク情報
    private List<Link> links;

    //RSSフィードの入力ストリームを解析
    public boolean parse(InputStream inputStream){

        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        dbfactory.setNamespaceAware(false);

        try{
            DocumentBuilder builder = dbfactory.newDocumentBuilder();
            Document document = builder.parse(inputStream);
            inputStream.close();
            //RSSのバージョンを判定氏、適切なパーサーを得る
            FeedParser parser = getParser(document);

            if(parser != null && parser.parse(document)){
                //解析成功
                this.site = parser.getSite();//サイト情報
                this.links = parser.getLinkList();//リンク情報
                return true;
            }

        }catch(ParserConfigurationException | IOException | SAXException e){
            // 設定エラーでDocumentBuilderが生成できなかった場合
            // parseできなかった場合
            // フィードの文法がおかしい場合
            e.printStackTrace();
        }
        return false;
    }

    public Site getSite() {
        return site;
    }

    public List<Link> getLinkList() {
        return links;
    }

    private FeedParser getParser(Document document){
        NodeList children = document.getChildNodes();
        FeedParser parser = null;

        for(int i = 0;i<children.getLength();i++){
            String childName = children.item(i).getNodeName();

            //「rdf:RDF」はRSS1.0、「rss」はRSS2.0とする
            if(childName.equals("rdf:RDF")){
                parser = new Rss1Parser();
            }else if(childName.equals("rss")){
                parser = new Rss2Parser();
            }
        }
        return parser;
    }
}
