# NDayschallenge

# 프로젝트 소개
- 팀소개
  - [노션 링크](https://www.notion.so/N-3875b06df3254acbb0edc1194cacba20)
  - [Figma 링크](https://www.figma.com/file/uRxyBkYgUa0FFCY39vvrcz/NDaysChallange?node-id=0%3A1&t=rArekkfnpvnnXEvK-0)
  - 백엔드: 서슬기, 오희경, 최예은
  - 프론트엔드: 원소연

# 개발환경
- 백엔드 기술 스택
  - JAVA 11
  - SpringBoot 2.7.5
  - SpringSecurity
  - Gradle 7.5.1
  - JPA
  - MySQL 8.0.31
- 협업툴
  Github
- 아키텍쳐
  - AWS EC2


# 메인기능
- 챌린지 기능
  - 매일 챌린지에 스탬프를 찍으며 사용자가 목표한 바를 이룰 수 있도록 도움
  - 성공하지 못했을 경우 pass 기능
  - 최대 4명의 사용자가 함께 할 수 있는 그룹 챌린지 기능
- 다짐 공유 기능
- 친구 추가 기능
- 로그인 기능

# ERD
!(https://user-images.githubusercontent.com/102232306/212079242-d90ea343-42f5-443f-930e-194ebe7065d4.png)

# 기술적 고민들
## 1.챌린지 상속을 통한 개인 챌린지와 그룹 챌린지 관리
- 상황
  개인과 그룹챌린지 모두 Room 테이블에서 생성해서 관리하도록 설계 함. 멤버의 개인 챌린지와 그룹 챌린지 리스트를 가져오는 상황에서 문제 발생. 그룹의 경우 챌린지와 멤버 사이의 RoomMember 테이블에서 따로 검색 가능하지만, 개인 챌린지의 경우 따로 만든 room_type 컬럼으로 조건 걸고 검색해야 하는 것이 객체지향스럽지 못하다고 판단함.
  
- 해결방법
  좀 더 객체지향적인 설계를 위해 공통 속성은 Room에 두고 GroupRoom과 SingleRoom이 Room을 상속해서 개인과 그룹챌린지를 생성할 때 각각 생성 되도록 함. InheritanceType을 SINGLE_TABLE 전략으로  설정해서 결국 처음과 같이 테이블은 하나로 생성되지만, 각각의 repository에서 관리 가능하게 되어 가독성과 관리가 쉬워짐.
  
## 2.JpaRepository 상속 클래스애서 동적 쿼리 생성
- 상황
  관리자 페이지에서 챌린지 상태와 회원 ID로 검색 할 수 있도록 하는 쿼리를 만들어야 하는데 기존에 있던 JpaRepository를 상속받는 repository에서는 구현 메서드를 만들 수 없었음.
  
- 원인
  인터페이스에서는 구현 메서드를 만들 수 없음.

- 해결방법
  RoomRepositoryCustom 인터페이스를 새로 만들어서 findSingleRoomAdmin 메서드를 만들고 RoomRepositoryImpl 클래스에서 findSingleRoomAdmin 메서드를 동적쿼리로 구현함. 기존 RoomRepository에서 RoomRepositoryCustom 인터페이스를 상속받아서 RoomRepository에서 바로 findSingleRoomAdmin에 접근 할 수 있도록 함.
  

# 기술적 고민들
## 1.챌린지 상속을 통한 개인 챌린지와 그룹 챌린지 관리
- 상황
  개인과 그룹챌린지 모두 Room 테이블에서 생성해서 관리하도록 설계 함. 멤버의 개인 챌린지와 그룹 챌린지 리스트를 가져오는 상황에서 문제 발생. 그룹의 경우 챌린지와 멤버 사이의 RoomMember 테이블에서 따로 검색 가능하지만, 개인 챌린지의 경우 따로 만든 room_type 컬럼으로 조건 걸고 검색해야 하는 것이 객체지향스럽지 못하다고 판단함.
  
- 해결방법
  좀 더 객체지향적인 설계를 위해 공통 속성은 Room에 두고 GroupRoom과 SingleRoom이 Room을 상속해서 개인과 그룹챌린지를 생성할 때 각각 생성 되도록 함. InheritanceType을 SINGLE_TABLE 전략으로  설정해서 결국 처음과 같이 테이블은 하나로 생성되지만, 각각의 repository에서 관리 가능하게 되어 가독성과 관리가 쉬워짐.
  
## 2.JpaRepository 상속 클래스애서 동적 쿼리 생성
- 상황
  관리자 페이지에서 챌린지 상태와 회원 ID로 검색 할 수 있도록 하는 쿼리를 만들어야 하는데 기존에 있던 JpaRepository를 상속받는 repository에서는 구현 메서드를 만들 수 없었음.
  
- 원인
  인터페이스에서는 구현 메서드를 만들 수 없음.

- 해결방법
  RoomRepositoryCustom 인터페이스를 새로 만들어서 findSingleRoomAdmin 메서드를 만들고 RoomRepositoryImpl 클래스에서 findSingleRoomAdmin 메서드를 동적쿼리로 구현함. 기존 RoomRepository에서 RoomRepositoryCustom 인터페이스를 상속받아서 RoomRepository에서 바로 findSingleRoomAdmin에 접근 할 수 있도록 함.
  
