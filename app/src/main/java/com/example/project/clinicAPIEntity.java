package com.example.project;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * 선별 진료소 api와 통신할 class
 * 사용api: https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15043078
 */

public class clinicAPIEntity implements Runnable{ // 스레딩을 위해 Runnable을 implements해서 구현
    private static ArrayList<SelectedClinic> clinicsList;// 결과로 나온 clinicAPI
    int index; // 멀티 쓰레드를 위한 인덱스 변수

    public clinicAPIEntity( int index) {
        this.clinicsList = new ArrayList<SelectedClinic>();
        this.index = index;
    }

    public ArrayList<SelectedClinic> clinicAPI(int index) throws IOException, ParserConfigurationException, SAXException {
        ArrayList<SelectedClinic> clinicsList= new ArrayList<SelectedClinic>();

        String parsingUrl="";

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551182/pubReliefHospService/getpubReliefHospList"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=0eMMHHcbnpAK1eXmexxzB4pMr9lfDCq4Tl6P4wh2DrYWPkvQfiB0u9Vr5mMh39H6x63xk%2FesCnLgUfMbHBQV8g%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + URLEncoder.encode("-", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(Integer.toString(index), "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("50", "UTF-8")); /*한 페이지 결과 수*/ /*총 데이터 수*/
        URL url = new URL(urlBuilder.toString());

        parsingUrl=url.toString();

        DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
        Document doc=dBuilder.parse(parsingUrl);

        doc.getDocumentElement().normalize();

        NodeList nList=doc.getElementsByTagName("item");

        for(int i=0; i<nList.getLength(); i++) {
            Node nNode=nList.item(i);
            if(nNode.getNodeType()==Node.ELEMENT_NODE) {
                Element eElement=(Element) nNode;
                SelectedClinic temp = new SelectedClinic(getTagValue("yadmNm",eElement),
                        null,getTagValue("spclAdmTyCd",eElement),getTagValue("telno",eElement));
                temp.setPlace(temp.calXY(temp.getName()));
                clinicsList.add(temp);
            }
        }
        return clinicsList;
    } // 선별진료소 api와 통신 및 xml파싱
    private static String getTagValue(String tag, Element eElement) {
        NodeList nlList=eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue=(Node)nlList.item(0);
        if(nValue==null) return null;
        return nValue.getNodeValue();
    } // 태그 값을 읽어올 함수

    public static ArrayList<SelectedClinic> getClinicsList() {
        return clinicsList;
    }
    @Override
    public void run() {
        ArrayList<SelectedClinic> tempClinicsList=new ArrayList<SelectedClinic>();
        try {
            tempClinicsList = this.clinicAPI(this.index);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        clinicsList.addAll(tempClinicsList);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
