# NDayschallenge

# 프로젝트 소개
- 팀소개
  - [노션 링크](https://www.notion.so/N-3875b06df3254acbb0edc1194cacba20)
  - [Figma 링크](https://www.figma.com/file/uRxyBkYgUa0FFCY39vvrcz/NDaysChallange?node-id=0%3A1&t=rArekkfnpvnnXEvK-0)
  - 백엔드: 서슬기, 오희경, 최예은 (3명)
  - 프론트엔드: 원소연 (1명)

---

# 개발환경
- 백엔드 기술 스택
  - JAVA 11
  - SpringBoot 2.7.5
  - SpringSecurity
  - Gradle 7.5.1
  - JPA
  - MySQL 8.0.31
- 프론트엔드 기술 스택
  - React
  - Redux Toolkit
  - Axios
  - Styled Componenets
- 인프라
  - AWS Elastic Beanstalk
  - AWS EC2
  - AWS RDS
  - Github Actions
- 협업툴
  - Github
  - Slack

---

# 아키텍처
![AWS-architecture](https://user-images.githubusercontent.com/104435274/216767253-ee4e6b41-a5dd-4f23-bbd4-0c4f58c53992.png)

---

# 메인기능
- 챌린지 기능
  - 매일 챌린지에 스탬프를 찍으며 사용자가 목표한 바를 이룰 수 있도록 도움
  - 성공하지 못했을 경우 pass 기능
  - 최대 4명의 사용자가 함께 할 수 있는 그룹 챌린지 기능
- 다짐 기능
  - 챌린지 룸에서 오늘의 다짐 작성/수정 (삭제 불가)
  - 다짐 작성 시 공개/비공개 선택 가능
  - 공개 설정된 다짐은 다짐 피드에서 공유됨 (로그인하지 않은 사용자도 조회 가능)
- 이모션 기능
  - 다짐 피드에서 각 다짐에 감정 스티커 1개 등록/변경/삭제
- 친구 추가 기능
- 로그인 기능
  - JWT 로그인

---

# ERD [링크](https://www.erdcloud.com/d/7omJZMM7j3oEdt2qE)
![N-days-challenge](https://user-images.githubusercontent.com/102232306/212079242-d90ea343-42f5-443f-930e-194ebe7065d4.png)

---

# 기술적 고민들


## 1.챌린지 상속을 통한 개인 챌린지와 그룹 챌린지 관리


### - 상황
  개인과 그룹챌린지 모두 Room 테이블에서 생성해서 관리하도록 설계 함. 멤버의 개인 챌린지와 그룹 챌린지 리스트를 가져오는 상황에서 문제 발생. 그룹의 경우 챌린지와 멤버 사이의 RoomMember 테이블에서 따로 검색 가능하지만, 개인 챌린지의 경우 따로 만든 room_type 컬럼으로 조건 걸고 검색해야 하는 것이 객체지향스럽지 못하다고 판단함.
   
### - 해결방법
  좀 더 객체지향적인 설계를 위해 공통 속성은 Room에 두고 GroupRoom과 SingleRoom이 Room을 상속해서 개인과 그룹챌린지를 생성할 때 각각 생성 되도록 함. InheritanceType을 SINGLE_TABLE 전략으로  설정해서 결국 처음과 같이 테이블은 하나로 생성되지만, 각각의 repository에서 관리 가능하게 되어 가독성과 관리가 쉬워짐.




## 2.JpaRepository 상속 클래스애서 동적 쿼리 생성

### - 상황
  관리자 페이지에서 챌린지 상태와 회원 ID로 검색 할 수 있도록 하는 쿼리를 만들어야 하는데 기존에 있던 JpaRepository를 상속받는 repository에서는 구현 메서드를 만들 수 없었음.
  
### - 원인
  인터페이스에서는 구현 메서드를 만들 수 없음.

### - 해결방법
  인터페이스를 새로 만들어서 그 안에 추상 메서드를 만들고 구현 클래스에서 검색을 동적쿼리로 구현함. 기존 RoomRepository가 새로 만든 인터페이스를 상속받아서 RoomRepository에서 바로 챌린지 상태와 ID로 검색하는 메서드에 접근 할 수 있도록 함.




## 3. DTO-엔티티 변환 계층


### - 고민
컨트롤러/서비스 계층 중 DTO-엔티티 변환할 계층 선택  

(서비스의 비즈니스 로직에서 엔티티를 사용해야 하므로 레포지토리 계층에서 변환하는 것은 제외했으나 쿼리dsl 사용 시 레포지토리 계층에서 변환하는 것도 가능하다고 함)  


  - 컨트롤러 계층
    - 장점: 


        컨트롤러 유연하게 활용 가능 (여러 컨트롤러에서 같은 서비스 사용 가능)
    
    
        서비스에서 특정 DTO에 의존하지 않고 엔티티만 사용 가능

    - 단점:
    
    
        여러 엔티티를 조회하면서 하나의 컨트롤러가 의존하는 서비스 많아짐
    
    
        뷰에 필요없는 데이터까지 컨트롤러에 넘어옴

  - 서비스 계층
    - 장점:


      엔티티가 컨트롤러까지 접근하지 않아 보호 가능
      
      
      컨트롤러는 DTO 교환, 서비스는 비즈니스 로직이라는 명확한 역할을 맡음

    - 단점:


      컨트롤러-서비스 간 결합도가 높아짐




### - 선택
  - 서비스 계층에서의 DTO-엔티티 변환
  컨트롤러는 DTO 교환, 서비스는 비즈니스 로직에 집중하게 하는 구조가 깔끔하다고 판단함  
  
  그리고 앤챌 프로젝트는 도메인 별로 컨트롤러, 서비스가 각각 사용되고 있기 때문에 컨트롤러의 유연성이 필요하지 않음  
  
  
**+**
서비스 내에서 엔티티-DTO 변환과 비즈니스 로직을 모두 작성하니 코드가 지나치게 길어짐  

=> 요청DTO 내부에 엔티티로 변환하는 toEntity 메소드, 엔티티 내부에는 DTO로 변환하는 of 메소드를 작성해 서비스 내 코드 감축




## 4. Optional

### - 사용에 대한 고민
반환 타입으로 사용되는 Optional은 값의 유무 확인하는 데 적합함  

일일히 서비스에서 Null 확인 위해 try-catch문이나 if문을 작성하는 것보다 가독성도 좋고 Null 처리에 적합하다고 생각해 사용 선택함  

레포지토리에서 findByXXX 메소드에서 객체 불러올 때 리턴 타입으로 사용함  

Null, 즉 Optional이 empty일 때 예외 던지거나 빈 컬렉션 반환하도록 설정

ex.
    
    //불러올 다짐이 없을 때 예외 발생
    Dajim dajim = dajimRepository.findByNumber(emotionRequestDto.getDajimNumber())
            .orElseThrow(()->new RuntimeException("감정 스티커를 등록할 다짐을 찾을 수 없습니다."));
    //불러올 다짐이 없을 때 빈 리스트 리턴
    List<Dajim> dajims = dajimRepository.findAllByMemberNickname(nickname).orElseGet(ArrayList::new);




## 5. AuthenticationPrincipal - 어댑터 디자인 패턴 채택

### - 고민 
서비스 코드에서 Member 엔티티를 자주 사용하므로 @AuthenticationPrincipal을 통해 Member 객체를 가져와 서비스에 전달하고자 함  

어노테이션을 통해 Member을 불러오려면 UserDetails 구현하거나 User 상속받아야 하지만 엔티티는 다른 클래스에 의존하지 않는 것이 권장됨

※
@AuthenticationPrincipal은 UserDetailsService를 구현한 클래스의 loadUserByUsername 메소드에서 반환한 객체를 가져오는 어노테이션  

이때 반환할 객체 타입은 UserDetails 또는 User이어야 함

### - 선택 
어댑터 패턴을 적용한 MemberAdapter를 생성해 User을 상속하기로 함  

@AuthenticationPrincipal에서 MemberAdapter 객체를 받은 후, getMember 메소드로 멤버 객체 불러옴


### - 또다른 고민
계속 @AuthenticationPrincipal 통해 Member 객체를 가져오는 것보다 Principal의 name(아이디)을 서비스로 전달해 필요한 메소드 내에서만 레포지토리에서 Member 객체를 불러오는 기본적인 방식이 더 효율적일 것 같음  

다시 한번 고민해보고 수정해나갈 예정




## 6. 등록/수정을 하나의 메소드에서 구현

### - 고민
처음에는 REST API 관례에 맞춰 POST-등록, PATCH-부분 수정, DELETE-삭제 세 가지로 컨트롤러 메소드 분리  

그런데 다짐, 이모션 도메인 뷰에는 등록-수정 버튼이 따로 없음 (다짐은 등록 버튼만 / 이모션은 스티커들 나열) & 한 멤버는 한 룸에 하나의 다짐만 등록 가능하고, 한 다짐에 한 이모션만 등록 가능함  

두가지 조건 하에서 등록/수정을 하나의 PUT 메소드에 진행하는 것이 적합하지 않을까 고민함  


ex.

    //해당 멤버, 룸에 다짐 존재하지 않으면 새로 등록, 존재하면 수정
    Dajim dajim = dajimRepository.findByMemberAndRoom(memberNumber, roomNumber)
      .orElse(new Dajim(member, room, content, open));


하지만 이러한 방식으로는 하나의 메소드에서 다짐은 등록/수정, 이모션은 등록/수정/삭제를 구현하게 된다. 이는 추후 유지보수, 테스트 코드 작성을 어렵게 만들 것이라 예상

### - 해결
유지보수, 테스트 코드 작성을 고려해 기존대로 기능별 메소드 분리 방식을 선택함  

그리고 REST 방식은 클라이언트와 명확히 소통하기 위해 사용되는 것이기 때문에, 등록/수정/삭제 여부는 매번 확실히 알리는 게 좋다고 판단함
