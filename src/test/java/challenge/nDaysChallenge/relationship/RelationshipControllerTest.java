//package challenge.nDaysChallenge.relationship;
//
//import challenge.nDaysChallenge.domain.Relationship;
//import challenge.nDaysChallenge.domain.member.Authority;
//import challenge.nDaysChallenge.domain.member.Member;
//import challenge.nDaysChallenge.repository.RelationshipRepository;
//import challenge.nDaysChallenge.repository.member.MemberRepository;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.util.List;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Transactional
//@Rollback(value = false)
//public class RelationshipControllerTest {
//
//    @Autowired
//    RelationshipRepository relationshipRepository;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//        @Test
//        public void 친구수락쿼리테스트() throws Exception{
//              //given
//            Member user = new Member("abc@naver.com","123","나",1,Authority.ROLE_USER);
//            memberRepository.save(user);
//            Member friend = new Member("dbf@naver.com","123","친구1",3,Authority.ROLE_USER);
//            memberRepository.save(friend);
//
//            List<Relationship> relationshipByUserAndStatus = relationshipRepository.findRelationshipByUserAndStatus(user);
//
//            for (Relationship userAndStatus : relationshipByUserAndStatus) {
//
//                System.out.println("userAndStatus = " + userAndStatus.getFriend().getId());
//            }
//
//
//              //when
//
//              //then
//            }
//
//
//}
