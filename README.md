# NandaDiagnosis

## 1

병원 erp 를 통해서만 어떠한 정보를 접속을 할 수 있어서 학생들이 공부하기 위한 리소스가 부족했다.

그런 부족을 해소하기 위하여 대상들이 사용하는 정보를 데이터베이스화 하여 클라이언트를 보내주어야 했다.

## 2

대상 개체는 모바일 디바이스였습니다. 그래서 이 프로젝트를 구성을 할 때 안드로이드, IOS, 서버 등을 팀으로 나누었습니다.
특이 사항 1 초기 안드로이드 클라이언트만 지원을 했기 때문에 DB 를 migration 할 필요가 있었다.
특이 사항 2 클라이언트에서는 DB 를 수정을 할 필요가 없음으로 HTTP GET 으로만 사용한다.

## 3

1. 13가지 카테고리르 중심으로 진단, 진단원인, 영역 구분 1, 영역 구분 2로 DB 구성
2. 서버에서 HTTP/HTTPS 를 지원하는 안드로이드 시스템서버 중 하나인 DownloadManager 를 사용하여 데이터베이스 다운로드
3. 인터넷 연결 환경인 경우 REST API 를 통하여 통신 //비연결 환경일 경우는 로컬에 있는 DB 를 활용하여 조회

## 4

1. 사용성에서는 호평을 받았으나 UI/UX 요소를 전무로 서비스 차원에서는 보완이 필요

## picture

![](https://github.com/keelim/nandaDiagnosis/blob/gh-pages/assets/pic4.png?raw=true)
![](https://github.com/keelim/nandaDiagnosis/blob/gh-pages/assets/pic5.png?raw=true)

![](https://github.com/keelim/nandaDiagnosis/blob/gh-pages/assets/pic3.png?raw=true)
