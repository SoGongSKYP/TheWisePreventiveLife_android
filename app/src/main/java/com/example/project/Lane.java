package com.example.project;

public class Lane {

    Lane(String name, int subwayCodeORtype, int subwayCityCodeORBusId){
        this.name =name;
        this.subwayCodeORtype = subwayCodeORtype;
        this.subwayCityCodeORBusId =subwayCityCodeORBusId;
    }

    private String name; //지하철 노선명 (지하철인 경우에만 필수)
    private int subwayCodeORtype; //지하철 노선 번호 (지하철인 경우에만 필수)
    private int subwayCityCodeORBusId; //지하철 도시코드 (지하철인 경우에만 필수)

    public int getSubwayCityCodeORBusId() {
        return subwayCityCodeORBusId;
    }

    public int getSubwayCodeORtype() {
        return subwayCodeORtype;
    }

    public void setSubwayCodeORtype(int subwayCodeORtype) {
        this.subwayCodeORtype = subwayCodeORtype;
    }

    public void setSubwayCityCodeORBusId(int subwayCityCodeORBusId) {
        this.subwayCityCodeORBusId = subwayCityCodeORBusId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
