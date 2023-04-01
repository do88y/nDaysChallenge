# NDayschallenge

# 프로젝트 소개
- 팀소개
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
- 챌린지 기능 (서슬기)
  - 매일 챌린지에 스탬프를 찍으며 사용자가 목표한 바를 이룰 수 있도록 도움
  - 성공하지 못했을 경우 pass 기능
  - 최대 4명의 사용자가 함께 할 수 있는 그룹 챌린지 기능
- 다짐 기능 (오희경)
  - 챌린지 룸에서 오늘의 다짐 작성/수정 (삭제 불가)
  - 다짐 작성 시 공개/비공개 선택 가능
  - 공개 설정된 다짐은 다짐 피드에서 공유됨 (로그인하지 않은 사용자도 조회 가능)
- 이모션 기능 (오희경)
  - 다짐 피드에서 각 다짐에 이모션 스티커 1개 등록/변경/삭제
- 친구 기능 (최예은)
  - 회원A가 회원B에게 친구 요청 시 회원B는 수락/거절 수행
  - 요청 거절 시 요청리스트에서 사라짐
  - 요청 수락 시 요청리스트 -> 수락리스트(최종 친구 리스트)로 이동
- 로그인 기능 (오희경)
  - JWT 로그인

---

# ERD [링크](https://www.erdcloud.com/d/7omJZMM7j3oEdt2qE)
![N-days-challenge (1)](https://user-images.githubusercontent.com/104435274/224782738-91929c56-b9eb-49c0-ac1c-eef3edd52595.png)

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




## 3. DTO-엔티티 변환

### - 고민 1 : 컨트롤러, 서비스 계층 중 DTO와 엔티티를 변환할 계층 선택

1. 컨트롤러 계층에서 변환
    - 장점
    
    컨트롤러 유연하게 활용 가능 (여러 컨트롤러에서 같은 서비스 사용 가능)
    
    서비스에서 특정 DTO에 의존하지 않고 엔티티만 사용 가능
    
    - 단점
    
    여러 엔티티를 조회하면서 하나의 컨트롤러가 의존하는 서비스 많아질 수 있음
    
    뷰에 필요없는 데이터까지 컨트롤러 단으로 넘어옴
    
2. 서비스 계층에서 변환
    - 장점
        
        엔티티가 컨트롤러까지 접근하지 않아 보호 가능
        
        컨트롤러는 DTO 교환, 서비스는 비즈니스 로직이라는 명확한 역할을 맡음
        
    - 단점
        
        컨트롤러-서비스 간 결합도가 높아짐
        

### -  선택 : 서비스 계층에서의 DTO-엔티티 변환

컨트롤러는 DTO 교환, 서비스는 비즈니스 로직에 집중하게 하는 구조가 깔끔하다고 판단함

그리고 엔챌 프로젝트는 컨트롤러, 서비스가 1:1로 사용되고 있기 때문에 컨트롤러의 유연성이 필요하지 않음


### - 고민 2 
서비스 메소드에서 엔티티-DTO 변환, 비즈니스 로직을 모두 작성해 코드 가독성, 관심사 분리 수준이 낮음

### - 선택

요청DTO 내부에 엔티티로 변환하는 toEntity 메소드, 응답DTO 내부에는 엔티티에서 DTO로 변환하는 of 메소드 작성


## 4. @AuthenticationPrincipal

### - 상황 1 : @AuthenticationPrincial으로 Member 객체 반환하고자 함

서비스 코드에서 Member 엔티티를 자주 사용하므로 @AuthenticationPrincipal을 통해 Member 객체를 불러와 서비스에 바로 전달하고자 함

해당 어노테이션은 UserDetailsService 구현 클래스의 loadUserByUsername 메소드에서 반환한 객체를 가져오므로, Member 객체를 반환하도록 하려면 Member 엔티티가 UserDetails을 구현하거나 User을 상속받아야 함

하지만 도메인 객체는 다른 기술에 종속되지 않는 것이 권장됨

### - 해결 : 어댑터 패턴을 적용해 User을 상속하는 MemberAdapter 클래스 생성

CustomUserDetailsService의 메소드에서 MemberAdapter 객체를 리턴받도록 코드 작성, 
컨트롤러에서 @AuthenticationPrincipal을 통해 MemberAdapter 객체를 불러와 Member 객체 사용

ex. MemberAdapter

```java
public class MemberAdapter extends User {

    private Member member;

    public MemberAdapter(Member member){
        super(member.getId(), member.getPw(), Collections.singleton(member.getAuthority()));
        this.member=member;
    }

    public Member getMember(){
        return member;
    }

  }
```

ex. CustomUserDetailsService

```java
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id + " -> db에서 찾을 수 없습니다."));

        log.info("db에서 Member 객체를 가져왔습니다.");

        return new MemberAdapter(member);
    }
```


### - 상황 2 : 불러온 Member 객체의 준영속화  

스프링 프레임워크는 클라이언트 -> 필터 -> 디스패처 서블릿 -> 인터셉터 -> 컨트롤러 순서로 동작

Member 객체는 필터(Security Filter - UserDetailsService)를 거쳐 컨트롤러로 불려옴

기존 디폴트값으로 설정된 OpenEntityManagerInterceptor은 영속성 컨텍스트를 프레젠테이션 계층에서도 열어두는 기능이지만 인터셉터이기 때문에 필터 이후에 적용됨

즉 사용자 객체는 시큐리티 필터에서 준영속화된 채 OpenEntityManagerInterceptor을 거쳐 컨트롤러에 불려옴

### - 해결 : OpenEntityManagerInterceptor을 필터로 교체해 앞단에 추가
DelegatingFilterProxy 필터에서 시큐리티 필터 체인이 동작함 (이때 UserDetailsService에서 사용자 객체 반환)
OpenEntityManagerInterceptor을 DelegatingFilterProxy 필터 앞에 추가하면 프레젠테이션 계층에 영속성 컨텍스트를 유지하도록 설정한 이후 Security Filter을 거침

=> UserDetailsService에서 영속화된 사용자 객체 반환받을 수 있음


### - 고민

영속성 컨텍스트를 유지해가며 DB 커넥션 연장으로 인한 장애 발생 가능성을 만드는 것보다는 Principal의 name(아이디)만 서비스로 전달하고, 필요한 메소드에서 레포지토리를 통해 Member 객체를 불러오는 것이 효율적이고 안전할 것 같아 고민 후 수정할 예정
