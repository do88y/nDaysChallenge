package challenge.nDaysChallenge.relationship;


import challenge.nDaysChallenge.controller.RelationshipController;
import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.Relationship;
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


import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class RelationshipDeleteTest {
    @Autowired
    RelationshipRepository relationshipRepository;

    @Autowired
    RelationshipService relationshipService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RelationshipController relationshipController;


    @Autowired
    PasswordEncoder passwordEncoder;


    @DisplayName("관계 삭제, db에서 삭제")
    @Test
    @Transactional
    @Rollback(value = false)
    void deleteRelationDB (){
        //given
        //일단 회원가입//
        Member user = new Member("abc@naver.com","123","나",1,2, Authority.ROLE_USER);
        Member friend = new Member("dbf@naver.com","123","친구1",3,2, Authority.ROLE_USER);
        Member friend2 = new Member("ery@naver.com","123","친구2",2,2, Authority.ROLE_USER);

        memberRepository.save(user);         //나
        memberRepository.save(friend);      //친구1
        memberRepository.save(friend2);    //친구2

        //relationship 생성//
        relationshipService.saveRelationship(new MemberAdapter(user), friend);
        relationshipService.saveRelationship(new MemberAdapter(user),friend2);

        
        //when
            //거절 누를시 리포지토리에서 삭제//
            //나한테 요청한 사람의 relationship 객체를 찾은것//
        Relationship findUserRelation = relationshipRepository.findByUserAndFriend(user, friend);
        Relationship findFriendRelation = relationshipRepository.findByUserAndFriend(friend, user);

        //리포지토리에서 delete //
        relationshipRepository.delete(findUserRelation);
        relationshipRepository.delete(findFriendRelation);

        //then
        Relationship afterDeleteRelation1 = relationshipRepository.findByUserAndFriend(user, friend);
        Relationship afterDeleteRelation2 = relationshipRepository.findByUserAndFriend(friend, user);
        assertThat(afterDeleteRelation1).isNull();
        assertThat(afterDeleteRelation2).isNull();
    }
}
