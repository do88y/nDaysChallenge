package challenge.nDaysChallenge.relationship;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.relationship.FriendSearchRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FriendSearchRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendSearchRepository friendSearchRepository;

    Member member;

    @Before
    public void 회원_세팅(){
        member = memberRepository.save(Member.builder()
                .id("abcd12345@gmail.com")
                .nickname("새닉네임")
                .authority(Authority.ROLE_USER)
                .image(1)
                .build());
    }

    @After
    public void 회원_삭제(){
        memberRepository.delete(member);
    }

    @Test
    public void 닉네임_빈칸_검색() {
        Member foundMember = friendSearchRepository.findByIdOrNickname(member.getId(), "");

        assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    public void 닉네임_null_검색() {
        Member foundMember = friendSearchRepository.findByIdOrNickname(member.getId(), null);

        assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    public void 아이디_빈칸_검색() {
        Member foundMember = friendSearchRepository.findByIdOrNickname("", member.getNickname());

        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    public void 아이디_null_검색() {
        Member foundMember = friendSearchRepository.findByIdOrNickname(null, member.getNickname());

        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

}