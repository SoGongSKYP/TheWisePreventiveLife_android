package com.example.project;

import android.content.Context;

import com.odsay.odsayandroidsdk.ODsayService;
import java.util.ArrayList;

public class CalRoute {
    private Place startPlace;
    private Place endPlace;
    private ArrayList<SearchPath> searchResultPath;
    private Context thisContext;
    private ResultCallbackListener resultCallbackListener;
    public CalRoute(Context context, final Place startPlace, final Place endPlace) throws InterruptedException {
        this.startPlace = startPlace;
        this.endPlace = endPlace;
        this.searchResultPath = new ArrayList<SearchPath>();
        this.thisContext=context;
        this.resultCallbackListener  = new ResultCallbackListener();
        if(startPlace != null && endPlace!=null){
            Thread thread =new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("스타트");
                    ODsayService oDsayService=ODsayService.init(thisContext,"cDSdUY9qLmrLpcqsJL3zPvgpx3IgkOf4sLsbkzSOZ2Y");
                    // 서버 연결 제한 시간(단위(초), default : 5초)
                    oDsayService.setReadTimeout(5000);
                    // 데이터 획득 제한 시간(단위(초), default : 5초)
                    oDsayService.setConnectionTimeout(5000);
                    oDsayService.requestSearchPubTransPath(Double.toString(startPlace.get_placeY()),Double.toString(startPlace.get_placeX()),Double.toString(endPlace.get_placeY())
                            ,Double.toString(endPlace.get_placeX()),"0","0","0",resultCallbackListener);
                }
            });
            thread.start();
        }
    }
    public ResultCallbackListener calRoute1() throws InterruptedException {
        return resultCallbackListener;
    }

    public Place getEndPlace() {
        return endPlace;
    }
    public Place getStartPlace() {
        return startPlace;
    }
    public ArrayList<SearchPath> getSearchResultPath() {
        return searchResultPath;
    }
}
