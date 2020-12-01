package com.example.project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class FindPlace {
    private String placeName;
    private ArrayList<Place> searchLocList;

    public FindPlace(String placeName) {
        this.placeName = placeName;
        this.searchLocList = new ArrayList<Place>();
    }

    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null) return null;
        return nValue.getNodeValue();
    }

    public void searchPlace() {
        String parsingUrl = "";
        String key = "340FCCC5-C1C9-31D4-B7D8-56BC7558298A";
        StringBuilder urlBuilder = new StringBuilder("http://api.vworld.kr/req/search?"); /*URL*/
        try {
            urlBuilder.append(URLEncoder.encode("service", "UTF-8") + "=" + URLEncoder.encode("search", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("request", "UTF-8") + "=" + URLEncoder.encode("search", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("version", "UTF-8") + "=" + URLEncoder.encode("2.0", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("query", "UTF-8") + "=" + URLEncoder.encode(placeName, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("place", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("format", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("errorformat", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        URL url = null;
        try {
            url = new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        parsingUrl = url.toString();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(parsingUrl);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("item"); //장소 전체 노드

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                searchLocList.add(new Place(getTagValue("title", eElement)
                        ,getTagValue("road", eElement)
                        ,Double.parseDouble(getTagValue("y", eElement))
                        ,Double.parseDouble(getTagValue("x", eElement))));
            }
        }
    }
    
    public ArrayList<Place> getSearchLocList() {
        return this.searchLocList;
    }
}
