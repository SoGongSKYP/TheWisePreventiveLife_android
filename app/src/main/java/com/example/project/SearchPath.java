package com.example.project;

import java.util.ArrayList;
/*
1-9 확장 노드 결과 리스트
 */
public class SearchPath {

    SearchPath(int pathType, ExtendNode info, ArrayList<SubPath> subPaths){
        this.pathType=pathType;
        this.info=info;
        this.subPaths=subPaths;
    }
    private int pathType; //결과 종류 1-지하철, 2-버스, 3-버스+지하철 1개
    private ExtendNode info; // 요약 정보 확장 노드 1개
    private ArrayList<SubPath> subPaths; //1-9-3 이동 교통 수단 정보 확장 노드

    public ArrayList<SubPath> getSubPaths() {
        return subPaths;
    }
    public ExtendNode getInfo() {
        return info;
    }
    public int getPathType() {
        return pathType;
    }

    public void setInfo(ExtendNode info) {
        this.info = info;
    }
    public void setPathType(int pathType) {
        this.pathType = pathType;
    }
    public void setSubPaths(ArrayList<SubPath> subPaths) {
        this.subPaths = subPaths;
    }
}
