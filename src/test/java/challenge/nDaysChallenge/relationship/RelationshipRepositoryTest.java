<<<<<<< HEAD
package challenge.nDaysChallenge.relationship;

import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.relationship.ApplyRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import challenge.nDaysChallenge.service.RelationshipService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class RelationshipRepositoryTest {
    @Autowired
    RelationshipRepository relationshipRepository;

    @Autowired
    RelationshipService relationshipService;

    @Autowired
    MemberRepository memberRepository;


    @Autowired
    PasswordEncoder passwordEncoder;


    @DisplayName("관계 저장, 친구 관계 저장")
    @Test
    @Transactional
    @Rollback(value = false)
    void makeRelationship (){
        //given
        Member user = new Member("abc@naver.com","123","나",1,2, Authority.ROLE_USER);
        Member friend = new Member("dbf@naver.com","123","친구1",3,2, Authority.ROLE_USER);
        Member friend2 = new Member("ery@naver.com","123","친구2",2,2, Authority.ROLE_USER);
        Member friend3 = new Member("tjd@naver.com","123","친구3",4,4, Authority.ROLE_USER);
        Member friend4 = new Member("rud@naver.com","123","친구4",5,3, Authority.ROLE_USER);
        Member friend5 = new Member("tmf@naver.com","123","친구5",6,2, Authority.ROLE_USER);
        Member friend6 = new Member("gml@naver.com","123","친구6",1,2, Authority.ROLE_USER);
        Member friend7 = new Member("dnjs@naver.com","123","친구7",2,2, Authority.ROLE_USER);
        Member friend8 = new Member("dh@naver.com","123","친구8",3,2, Authority.ROLE_USER);
        Member friend9 = new Member("rl@naver.com","123","친구9",4,2, Authority.ROLE_USER);
        Member friend10 = new Member("dP@naver.com","123","친구10",5,2, Authority.ROLE_USER);
        Member friend11 = new Member("dms@naver.com","123","친구11",6,2, Authority.ROLE_USER);
        Member friend12 = new Member("chl@naver.com","123","친구12",1,2, Authority.ROLE_USER);
        Member friend13 = new Member("dus@naver.com","123","친구13",2,2, Authority.ROLE_USER);
        Member friend14 = new Member("tj@naver.com","123","친구14",3,2, Authority.ROLE_USER);
        Member friend15 = new Member("gh@naver.com","123","친구15",4,2, Authority.ROLE_USER);

        List <String> friendList = new ArrayList<>();


        //중복이니까 나중에 수정해야해//
        ApplyRequestDTO dto1 =new ApplyRequestDTO("dbf@naver.com","친구1",3,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(user);         //나
        memberRepository.save(friend);      //친구1
        ApplyRequestDTO dto2 =new ApplyRequestDTO("ery@naver.com","친구2",2,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend2);    //친구2
        ApplyRequestDTO dto3 =new ApplyRequestDTO("tjd@naver.com","친구3",4,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend3);
        ApplyRequestDTO dto4 =new ApplyRequestDTO("rud@naver.com","친구4",5,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend4);
        ApplyRequestDTO dto5 =new ApplyRequestDTO("tmf@naver.com","친구5",6,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend5);
        ApplyRequestDTO dto6 =new ApplyRequestDTO("gml@naver.com","친구6",1,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend6);
        ApplyRequestDTO dto7 =new ApplyRequestDTO("dnjs@naver.com","친구7",2,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend7);
        ApplyRequestDTO dto8 =new ApplyRequestDTO("dh@naver.com","친구8",3,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend8);
        ApplyRequestDTO dto9 =new ApplyRequestDTO("rl@naver.com","친구9",4,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend9);
        ApplyRequestDTO dto10 =new ApplyRequestDTO("dP@naver.com","친구10",5,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend10);
        ApplyRequestDTO dto11 =new ApplyRequestDTO("dms@naver.com","친구11",6,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend11);
        ApplyRequestDTO dto12=new ApplyRequestDTO("chl@naver.com","친구12",1,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend12);
        ApplyRequestDTO dto13 =new ApplyRequestDTO("dus@naver.com","친구13",2,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend13);
        ApplyRequestDTO dto14=new ApplyRequestDTO("tj@naver.com","친구14",3,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend14);
        ApplyRequestDTO dto15=new ApplyRequestDTO("gh@naver.com","친구15",4,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
        memberRepository.save(friend15);



        relationshipService.saveRelationship(new MemberAdapter(user), friend);
        relationshipService.saveRelationship(new MemberAdapter(user),friend2);
        relationshipService.saveRelationship(new MemberAdapter(user),friend3);
        relationshipService.saveRelationship(new MemberAdapter(user),friend4);
        relationshipService.saveRelationship(new MemberAdapter(user),friend5);
        relationshipService.saveRelationship(new MemberAdapter(user),friend6);
        relationshipService.saveRelationship(new MemberAdapter(user),friend7);
        relationshipService.saveRelationship(new MemberAdapter(user),friend8);
        relationshipService.saveRelationship(new MemberAdapter(user),friend9);
        relationshipService.saveRelationship(new MemberAdapter(user),friend10);
        relationshipService.saveRelationship(new MemberAdapter(user),friend11);
        relationshipService.saveRelationship(new MemberAdapter(user),friend12);
        relationshipService.saveRelationship(new MemberAdapter(user),friend13);
        relationshipService.saveRelationship(new MemberAdapter(user),friend14);
        relationshipService.saveRelationship(new MemberAdapter(user),friend15);



        //when

        //친구 신청 수락 //담아==단축키(옵+커+v)//
        AcceptResponseDTO friendInfo = relationshipService.acceptRelationship (user, friend,dto1);
        AcceptResponseDTO friendInfo2= relationshipService.acceptRelationship(user, friend2,dto2);
        AcceptResponseDTO friendInfo3= relationshipService.acceptRelationship(user, friend3,dto3);
        AcceptResponseDTO friendInfo4= relationshipService.acceptRelationship(user, friend4,dto4);
        AcceptResponseDTO friendInfo5= relationshipService.acceptRelationship(user, friend5,dto5);
        AcceptResponseDTO friendInfo6= relationshipService.acceptRelationship(user, friend6,dto6);
        AcceptResponseDTO friendInfo7= relationshipService.acceptRelationship(user, friend7,dto7);
        AcceptResponseDTO friendInfo8= relationshipService.acceptRelationship(user, friend8,dto8);
        AcceptResponseDTO friendInfo9= relationshipService.acceptRelationship(user, friend9,dto9);
        AcceptResponseDTO friendInfo10= relationshipService.acceptRelationship(user, friend10,dto10);
        AcceptResponseDTO friendInfo11= relationshipService.acceptRelationship(user, friend11,dto11);
        AcceptResponseDTO friendInfo12= relationshipService.acceptRelationship(user, friend12,dto12);
        AcceptResponseDTO friendInfo13= relationshipService.acceptRelationship(user, friend13,dto13);
        AcceptResponseDTO friendInfo14= relationshipService.acceptRelationship(user, friend14,dto14);
        AcceptResponseDTO friendInfo15= relationshipService.acceptRelationship(user, friend15,dto15);




        //confirmFriendList 불러오기//
        List<Relationship> confirmedFriendsList = user.getConfirmedFriendsList();
        //향상된 for 문 each for 문//
        for (Relationship confirmList : confirmedFriendsList) {
            System.out.println("confirmedFriendsList. = " + confirmList.getUser().getNickname());
        }



        //then (넣고 들어온 정보가 같은지 확인을 해줘야해)
        assertThat(friendInfo.getId()).isEqualTo(dto1.getId());
        assertThat(friendInfo2.getId()).isEqualTo(dto2.getId());
        assertThat(friendInfo3.getId()).isEqualTo(dto3.getId());
        assertThat(friendInfo4.getId()).isEqualTo(dto4.getId());
        assertThat(friendInfo5.getId()).isEqualTo(dto5.getId());
        assertThat(friendInfo6.getId()).isEqualTo(dto6.getId());
        assertThat(friendInfo7.getId()).isEqualTo(dto7.getId());
        assertThat(friendInfo8.getId()).isEqualTo(dto8.getId());
        assertThat(friendInfo9.getId()).isEqualTo(dto9.getId());
        assertThat(friendInfo10.getId()).isEqualTo(dto10.getId());
        assertThat(friendInfo11.getId()).isEqualTo(dto11.getId());
        assertThat(friendInfo12.getId()).isEqualTo(dto12.getId());
        assertThat(friendInfo13.getId()).isEqualTo(dto13.getId());
        assertThat(friendInfo14.getId()).isEqualTo(dto14.getId());
        assertThat(friendInfo15.getId()).isEqualTo(dto15.getId());


        List<Relationship> relationshipByUserAndStatus = relationshipRepository.findRelationshipByUserAndStatus(user, RelationshipStatus.ACCEPT);
        assertThat(user.getConfirmedFriendsList().size()).isEqualTo(relationshipByUserAndStatus.size());

    }
}


=======
//package challenge.nDaysChallenge.relationship;
//
//import challenge.nDaysChallenge.domain.*;
//import challenge.nDaysChallenge.domain.member.Authority;
//import challenge.nDaysChallenge.domain.member.Member;
//import challenge.nDaysChallenge.dto.request.relationship.ApplyRequestDTO;
//import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
//import challenge.nDaysChallenge.dto.response.relationship.RequestResponseDTO;
//import challenge.nDaysChallenge.repository.MemberRepository;
//import challenge.nDaysChallenge.repository.RelationshipRepository;
//import challenge.nDaysChallenge.service.RelationshipService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//
//@SpringBootTest
//class RelationshipRepositoryTest {
//    @Autowired
//    RelationshipRepository relationshipRepository;
//
//    @Autowired
//    RelationshipService relationshipService;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//
//    @DisplayName("관계 저장, 친구 관계 저장")
//    @Test
//    @Transactional
//    @Rollback(value = false)
//    void makeRelationship (){
//        //given
//        Member user = new Member("abc@naver.com","123","나",1,2, Authority.ROLE_USER);
//        Member friend = new Member("dbf@naver.com","123","친구1",3,2, Authority.ROLE_USER);
//        Member friend2 = new Member("ery@naver.com","123","친구2",2,2, Authority.ROLE_USER);
//        Member friend3 = new Member("tjd@naver.com","123","친구3",4,4, Authority.ROLE_USER);
//        Member friend4 = new Member("rud@naver.com","123","친구4",5,3, Authority.ROLE_USER);
//        Member friend5 = new Member("tmf@naver.com","123","친구5",6,2, Authority.ROLE_USER);
//        Member friend6 = new Member("gml@naver.com","123","친구6",1,2, Authority.ROLE_USER);
//        Member friend7 = new Member("dnjs@naver.com","123","친구7",2,2, Authority.ROLE_USER);
//        Member friend8 = new Member("dh@naver.com","123","친구8",3,2, Authority.ROLE_USER);
//        Member friend9 = new Member("rl@naver.com","123","친구9",4,2, Authority.ROLE_USER);
//        Member friend10 = new Member("dP@naver.com","123","친구10",5,2, Authority.ROLE_USER);
//        Member friend11 = new Member("dms@naver.com","123","친구11",6,2, Authority.ROLE_USER);
//        Member friend12 = new Member("chl@naver.com","123","친구12",1,2, Authority.ROLE_USER);
//        Member friend13 = new Member("dus@naver.com","123","친구13",2,2, Authority.ROLE_USER);
//        Member friend14 = new Member("tj@naver.com","123","친구14",3,2, Authority.ROLE_USER);
//        Member friend15 = new Member("gh@naver.com","123","친구15",4,2, Authority.ROLE_USER);
//
//        List <String> friendList = new ArrayList<>();
//
//
//        //중복이니까 나중에 수정해야해//
//        ApplyRequestDTO dto1 =new ApplyRequestDTO("dbf@naver.com","친구1",3,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(user);         //나
//        memberRepository.save(friend);      //친구1
//        ApplyRequestDTO dto2 =new ApplyRequestDTO("ery@naver.com","친구2",2,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend2);    //친구2
//        ApplyRequestDTO dto3 =new ApplyRequestDTO("tjd@naver.com","친구3",4,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend3);
//        ApplyRequestDTO dto4 =new ApplyRequestDTO("rud@naver.com","친구4",5,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend4);
//        ApplyRequestDTO dto5 =new ApplyRequestDTO("tmf@naver.com","친구5",6,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend5);
//        ApplyRequestDTO dto6 =new ApplyRequestDTO("gml@naver.com","친구6",1,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend6);
//        ApplyRequestDTO dto7 =new ApplyRequestDTO("dnjs@naver.com","친구7",2,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend7);
//        ApplyRequestDTO dto8 =new ApplyRequestDTO("dh@naver.com","친구8",3,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend8);
//        ApplyRequestDTO dto9 =new ApplyRequestDTO("rl@naver.com","친구9",4,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend9);
//        ApplyRequestDTO dto10 =new ApplyRequestDTO("dP@naver.com","친구10",5,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend10);
//        ApplyRequestDTO dto11 =new ApplyRequestDTO("dms@naver.com","친구11",6,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend11);
//        ApplyRequestDTO dto12=new ApplyRequestDTO("chl@naver.com","친구12",1,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend12);
//        ApplyRequestDTO dto13 =new ApplyRequestDTO("dus@naver.com","친구13",2,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend13);
//        ApplyRequestDTO dto14=new ApplyRequestDTO("tj@naver.com","친구14",3,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend14);
//        ApplyRequestDTO dto15=new ApplyRequestDTO("gh@naver.com","친구15",4,LocalDateTime.now(),RelationshipStatus.REQUEST.name());
//        memberRepository.save(friend15);
//
//
//
//        relationshipService.saveRelationship(new MemberAdapter(user), friend);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend2);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend3);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend4);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend5);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend6);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend7);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend8);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend9);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend10);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend11);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend12);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend13);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend14);
//        relationshipService.saveRelationship(new MemberAdapter(user),friend15);
//
//
//
//        //when
//
//        //친구 신청 수락 //담아==단축키(옵+커+v)//
//        AcceptResponseDTO friendInfo = relationshipService.acceptRelationship (user, friend,dto1);
//        AcceptResponseDTO friendInfo2= relationshipService.acceptRelationship(user, friend2,dto2);
//        AcceptResponseDTO friendInfo3= relationshipService.acceptRelationship(user, friend3,dto3);
//        AcceptResponseDTO friendInfo4= relationshipService.acceptRelationship(user, friend4,dto4);
//        AcceptResponseDTO friendInfo5= relationshipService.acceptRelationship(user, friend5,dto5);
//        AcceptResponseDTO friendInfo6= relationshipService.acceptRelationship(user, friend6,dto6);
//        AcceptResponseDTO friendInfo7= relationshipService.acceptRelationship(user, friend7,dto7);
//        AcceptResponseDTO friendInfo8= relationshipService.acceptRelationship(user, friend8,dto8);
//        AcceptResponseDTO friendInfo9= relationshipService.acceptRelationship(user, friend9,dto9);
//        AcceptResponseDTO friendInfo10= relationshipService.acceptRelationship(user, friend10,dto10);
//        AcceptResponseDTO friendInfo11= relationshipService.acceptRelationship(user, friend11,dto11);
//        AcceptResponseDTO friendInfo12= relationshipService.acceptRelationship(user, friend12,dto12);
//        AcceptResponseDTO friendInfo13= relationshipService.acceptRelationship(user, friend13,dto13);
//        AcceptResponseDTO friendInfo14= relationshipService.acceptRelationship(user, friend14,dto14);
//        AcceptResponseDTO friendInfo15= relationshipService.acceptRelationship(user, friend15,dto15);
//
//
//
//
//        //confirmFriendList 불러오기//
//        List<Relationship> confirmedFriendsList = user.getConfirmedFriendsList();
//        //향상된 for 문 each for 문//
//        for (Relationship confirmList : confirmedFriendsList) {
//            System.out.println("confirmedFriendsList. = " + confirmList.getUser().getNickname());
//        }
//
//
//
//        //then (넣고 들어온 정보가 같은지 확인을 해줘야해)
//        assertThat(friendInfo.getId()).isEqualTo(dto1.getId());
//        assertThat(friendInfo2.getId()).isEqualTo(dto2.getId());
//        assertThat(friendInfo3.getId()).isEqualTo(dto3.getId());
//        assertThat(friendInfo4.getId()).isEqualTo(dto4.getId());
//        assertThat(friendInfo5.getId()).isEqualTo(dto5.getId());
//        assertThat(friendInfo6.getId()).isEqualTo(dto6.getId());
//        assertThat(friendInfo7.getId()).isEqualTo(dto7.getId());
//        assertThat(friendInfo8.getId()).isEqualTo(dto8.getId());
//        assertThat(friendInfo9.getId()).isEqualTo(dto9.getId());
//        assertThat(friendInfo10.getId()).isEqualTo(dto10.getId());
//        assertThat(friendInfo11.getId()).isEqualTo(dto11.getId());
//        assertThat(friendInfo12.getId()).isEqualTo(dto12.getId());
//        assertThat(friendInfo13.getId()).isEqualTo(dto13.getId());
//        assertThat(friendInfo14.getId()).isEqualTo(dto14.getId());
//        assertThat(friendInfo15.getId()).isEqualTo(dto15.getId());
//
//
//        List<Relationship> relationshipByUserAndStatus = relationshipRepository.findRelationshipByUserAndStatus(user, RelationshipStatus.ACCEPT);
//        assertThat(user.getConfirmedFriendsList().size()).isEqualTo(relationshipByUserAndStatus.size());
//
//    }
//}
//
//
>>>>>>> 7addbbfed2e85f17c84ba27c61adc10d6384d2bb
