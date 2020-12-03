# TheWisePreventiveLife

사용자 기능 및 UI 

1. 인트로
<div>
<img width="189" alt="KakaoTalk_20201203_202717232_01" src="https://user-images.githubusercontent.com/57628980/101015840-54018e80-35ab-11eb-9968-8458b856d3c6.png">
</div>
2. 내 주변 확진자 검색
<div>
<img width="189" alt="KakaoTalk_20201203_210203575" src="https://user-images.githubusercontent.com/57628980/101016223-cffbd680-35ab-11eb-9fb3-597da9c737f0.png">
</div>
3. 경로 위험도 측정 기능
<div>
<img width="189" alt="KakaoTalk_20201203_202717232_07" src="https://user-images.githubusercontent.com/57628980/101016317-f3268600-35ab-11eb-9141-696762c70550.png">
</div>
4. 자가진단 기능
<div>
<img width="189" alt="KakaoTalk_20201203_202717232_03" src="https://user-images.githubusercontent.com/57628980/101015960-7d221f00-35ab-11eb-8f8a-29a19ba806fd.png">
<img width="189" alt="KakaoTalk_20201203_202717232_04" src="https://user-images.githubusercontent.com/57628980/101015962-7dbab580-35ab-11eb-8f9d-6f28cdbab3f4.png">
</div>
5. 내 주변 제일 가까운 5개의 선별진료소 제공

6. 전국별 지역별 통계 기능 제공
<div>
<img width="189" alt="KakaoTalk_20201203_202717232_05" src="https://user-images.githubusercontent.com/57628980/101016037-9925c080-35ab-11eb-9c13-2fbd8490fa93.png">
</div>


관리자 기능
통계와 별개로 확진자 동선은 api로 직접 제공 되지 않음 
따라서 관리자가 직접 관리해야하는 부분
확진자 관리에 편의를 주기 위하여 기능을 추가
1. 확진자 추가 및 확진자 동선 관리

사용 api
1. 건강보험심사평가원_코로나19병원정보(국민안심병원 외)서비스: https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15043078

2. 보건복지부_코로나19 시·도발생_현황: https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15043378

3. 보건복지부_코로나19 감염_현황: https://www.data.go.kr/tcs/dss/selectApiDataDetailView.do?publicDataPk=15043376

4. 대중 교통 길찾기 API: https://lab.odsay.com/

5. 장소 검색 api: https://www.vworld.kr/dev/v4dv_search2_s001.do

6. 구글 map api:https://cloud.google.com/maps-platform/?hl=ko&utm_source=google&utm_medium=cpc&utm_campaign=FY18-Q2-global-demandgen-paidsearchonnetworkhouseads-cs-maps_contactsal_saf&utm_content=text-ad-none-none-DEV_c-CRE_467208338789-ADGP_Hybrid%20%7C%20AW%20SEM%20%7C%20BKWS%20~%20Brand%20%7C%20EXA%20%7C%20Google%20Maps%20API-KWID_43700057416637750-kwd-356751068841-userloc_1009877&utm_term=KW_%EA%B5%AC%EA%B8%80%20%EC%A7%80%EB%8F%84%20api-ST_%EA%B5%AC%EA%B8%80%20%EC%A7%80%EB%8F%84%20API&gclid=Cj0KCQiAtqL-BRC0ARIsAF4K3WFCuXCiZIXMdVYfFXDATz7p0KLv-is3yL0GIPmEuXSCKDJBGliA92gaAir8EALw_wcB

