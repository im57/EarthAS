# EarthAS
분리수거 자동개폐 시스템


###### EarthAS로 간단해진 분리수거

- 버튼만 누르면 수거함 자동 open
- 물품 검색 후 클릭하면 해당 분리수거함 자동 open

-------

### For

- 간단하고 올바른 분리수거

- 관리자가 별도로 검사를 할 필요가 없음


----

### About APP

<br>

#### 요구사항 정의
S/W

기능 | 설명
-- | --
모르는 품목 확인 | DB에 있는 모르는 품목 리스트를 확인할 수 있다.
모르는 품목 검색 | DB에 있는 모르는 품목 정보를 검색할 수 있다.
열려있는 수거함 확인 | 열려있는 수거함을 확인할 수 있다.
앱 설명서 확인 | 앱을 사용하는 방법을 알 수 있다.
수거 품목별 정보 확인 | 수거 품목별 주의사항과 버릴 수 있는 물건을 알 수 있다.

<br>
H/W

기능 | 설명
-- | --
수거함 열기 | 모터를 사용하여 수거함을 열 수 있다.
수거함 닫기 | 모터를 사용하여 수거함을 닫을 수 있다.
해당 품목 수거함 자동 닫기 | 초음파 센서를 통해 일정 시간동안 근처에 접근하지 않으면 자동으로 수거함을 닫을 수 있다.
블루투스 켜기 | 스마트폰의 블루투스를 켤 수 있다.
블루투스 끄기 | 스마트폰의 블루투스를 끌 수 있다.
블루투스 검색 | 스마트폰과 연결가능한 블루투스 기기들을 검색할 수 있다.
블루투스 연결 | 수거함의 블루투스와 스마트폰의 블루투스를 연결하여 통신할 수 있다.


<br><br>

#### 유스케이스
![image](https://user-images.githubusercontent.com/57435148/107976299-ad028f80-6ffc-11eb-948e-5410d9166f5f.png)


<br><br>

#### 테이블 정의서
Confusion

이름 | 타입
-- | --
name | String
type | String

<br>

Collection_box

이름 | 타입
-- | --
region | String
garbage | Bollean
can | Bollean
plastic | Bollean
glass | Bollean
paper | Bollean
vinyl | Bollean

<br><br>

#### 서비스 구성도
![image](https://user-images.githubusercontent.com/57435148/107976399-d4f1f300-6ffc-11eb-891c-affaffd7da55.png)

<br>

![image](https://user-images.githubusercontent.com/57435148/107976428-e4713c00-6ffc-11eb-9dcc-4e77ea245a3c.png)

<br><br>

#### 하드웨어 구성도
![image](https://user-images.githubusercontent.com/57435148/107976472-f5ba4880-6ffc-11eb-983f-aad6a015e7a9.png)

<br><br>

#### 메뉴 구성도
![image](https://user-images.githubusercontent.com/57435148/107976517-0a96dc00-6ffd-11eb-8f3e-4020f53371c2.png)

<br><br>

#### 화면 구성도
![image](https://user-images.githubusercontent.com/57435148/107976570-1f736f80-6ffd-11eb-8d4c-4536e2ca548c.png)

<br><br>

#### 기능 흐름도
open
<br>
![image](https://user-images.githubusercontent.com/57435148/107976609-31eda900-6ffd-11eb-9927-85c33590209f.png)

<br><br>
close
<br>
![image](https://user-images.githubusercontent.com/57435148/107976661-45007900-6ffd-11eb-8fb1-75d49c649548.png)
