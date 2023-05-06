package challenge.nDaysChallenge.repository.relationship;

import challenge.nDaysChallenge.domain.member.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static challenge.nDaysChallenge.domain.member.QMember.member;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class FriendSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Member findByIdOrNickname(String id, String nickname){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (hasText(id)){
            booleanBuilder.and(member.id.eq(id));
        }

        if (hasText(nickname)){
            booleanBuilder.and(member.nickname.eq(nickname));
        }

        return jpaQueryFactory.selectFrom(member)
                .where(booleanBuilder)
                .fetchOne();
    }

}
