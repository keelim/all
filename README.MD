## 2019  09 14 project start

## project name: NANDA diagnosis project
## with nursing major junior JH, C

> 2019 09 15

    - firebase performance -> install complete
    - ux design -> 0.5 h
    - admob setting -> later

> 2019 09 16

    - 아이콘 만들기
    - 대략적인 내용있는 프레그 먼트 만들 것
        - 요구사항대로 
    - 다크 모드 지원을 할 것
    - 내용 구성을 어떻게 할지 알아보기
    
> 2019 09 17     
    
    - 아이콘 만들기 -> complete
        - 힘들면 보색으로 아이콘 구성
    
    - 프로그램 구조 정하기
        - activity 구조 -> complete
        - fragment 구조 -> complete
    
    - viewpager 사용하기 -> complete
        
    - 데이터베이스 구성
        - 스키마 작업 
        - server DB
        - 안드로이드 내부 DB > Jetpack library (Room)
        - SQLite
        
> 2019 09 18
    
    - 데이터베이스 구성
        - 데이터베이스 파일을 만들 것
        - 첫 시작시 데이터를 받아올 수 있도록 할 것
            - 와이파이 연결시에만 받아올 것
         
    - 도움말 액티비티/프레그먼트 짤 것 -> 리스트뷰
        - 도움말
        - 문의하기
        - 오픈소스 라이선스
        
> 2019 09 19

    - 데이터베이스 구축
        - 생각보다 간단하다.
        - 규칙은 필요할 듯 (띄어쓰기를 하지 말아야 하나?)
        - 엔티티 -> 위험 요인(reason), 진단 요인(diagnosis)
        - 엑셀 -> db 만들기
    
    - 도움말 문의 액티비티 구현      
    
    - 검색 기능 -> 위험 요인을 검색을 하면 진단 요인이 나오는 것


> 2019 09 20

    - 테스트 코드 배워서 적용하기

    - 도움말 문의 액티비티 만들기

    - 검색 가능 구현 (categroy frgamnet search 환경으로 변경)ㄴ
        - 첫 시작시 데이터베이스 받아오는 코드 작성
        - searchView -> database 의 연결을 하기 -> SQLite
        
> 2019 09 21

    - 데이터베이스 관련
        - sql ->*.db, sqlite로 받아오는 방법 complete
        - 안드로이드 db -> Room 연결
        - searchview -> Room 연결
        - 표현 방법 
  
    - 디자인 수정
        - 아이콘 디자인 수정
        - 도움말/문의 액티비티 수정
        
    - 바텀 시트 다이알로그 수정
    
> 2019 09 22
    
    - 데이터베이스 관련
        - 안드로이드에서 데이터베이스 사용하기
        - 검색 창에서 검색을 할 수 있는 방법
        
    - 디자인 수정
        - domain 디자인 수정
        - 도움말/문의 액티비티 수정
        
> 2019 09 23

    - 디자인 수정 
        - 도움말 문의 액티비티
        
    - 기능
        - 검색 결과 제대로 표시를 할 것
        - 초기 전부 검색되는 것은 배제 시킬 것
        
     - 데이터베이스 룸으로 연결을 할 것
        - sql 추상화
        
    - 프로세스
        디자인 수정 -> 코드 수정
        
> 2019 09 24

    - 룸으로 연결
    
    - 디자인 수정
    
> 2019 09 27

    - Domain -> webview
    - webview (github.io) -> dev.blog + comments // like web services
    - any other problem
    
> 2019 09 29

    - 데이터베이스 작성 완료
    - Searchfragment, MainActivity
    - Image asset 만들기 
    
> 2019 10 26

    - project modify start
    - github todo, doing, done list making
    
> 2019 10 27

    - mvp pattern 적용하기 
    - github todo list 조정
    
    
> 2019 10 28

    - 리싸이클러 뷰 어떻게 활용을 해야 하는 것인가?  --> IOT 저굥 방법이 있는가?
    
> 2019 10 29

    - 리싸이클러 뷰 사용법
    
> 2019 10 30

    - 리싸이클러 뷰 사용법 강화 -> activity 나 fragment 를 사용을 하는 방법
    
> 2019 10 31

    - 도움말 구현을 할 것
    
> 2019 11 01

    - 우선은 액티비티로 만들고 나중에 프레그 먼트로 바꾸는 작업을 하는 것이 좋을 것 같다.
    - 도움말은 그냥 FAQ 처럼 만드러양 하는지 고민?
    - 오픈소스 라이선스는 어떻게 만들어야 하나?
    - 문의사항은 어떻게 만들어야 하나?
     
> 2019 11 04

    - 일단 웹페이지 만드는 것에 중점을 하는 것이 좋을 듯 하다.?
    - 애널리틱스, 애드센스
    - 커스텀 도메인
    - 파이어 베이스는 생각을 해봐야 할 듯