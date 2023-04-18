package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.QAdminRoomResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static challenge.nDaysChallenge.domain.member.QMember.*;
import static challenge.nDaysChallenge.domain.room.QSingleRoom.*;
import static org.springframework.util.StringUtils.*;

@Repository
@Slf4j
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public RoomRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AdminRoomResponseDto> findSingleRoomAdmin(RoomSearch roomSearch) {

        String stringType = singleRoom.type.toString();
        String stringCategory = singleRoom.category.toString();
        String stringStatus = singleRoom.status.toString();

        return queryFactory
                .select(new QAdminRoomResponseDto(
                        singleRoom.number.as("roomNumber"),
                        singleRoom.name,
                        Expressions.as(Expressions.constant(stringType), "type"),
                        Expressions.as(Expressions.constant(stringCategory), "category"),
                        Expressions.as(Expressions.constant(stringStatus), "status"),
                        singleRoom.period.startDate,
                        singleRoom.period.endDate,
                        member.id.as("memberId")))
                .from(singleRoom)
                .join(singleRoom.member, member)
                .where(
                        statusEq(roomSearch.getStatus()),
                        memberIdEq(roomSearch.getId()))
                .fetch();
    }

    private static BooleanExpression statusEq(String status) {
        return hasText(status) ? singleRoom.status.eq(RoomStatus.valueOf(status)) : null;
    }

    private static BooleanExpression memberIdEq(String id) {
        return hasText(id) ? member.id.eq(id) : null;
    }

    @Override
    public List<AdminRoomResponseDto> findGroupRoomAdmin(RoomSearch roomSearch) {
        return null;
    }



    /*    //개인 챌린지 검색
    @Override
    public List<Tuple> findSingleRoomAdmin(RoomSearch roomSearch) {

        String jpql = "select s, m.id from SingleRoom s join s.member m";
        boolean isFirstCondition = true;

        //챌린지 상태 검색
        if (StringUtils.hasText(roomSearch.getStatus())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " s.status = :status";
        }

        //회원 ID 검색
        if (StringUtils.hasText(roomSearch.getId())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.id like :id";
        }
        TypedQuery<Tuple> query = em.createQuery(jpql, Tuple.class)
                .setMaxResults(1000);  //최대 1000건

        //파라미터 바인딩
        if (StringUtils.hasText(roomSearch.getStatus())) {
            query.setParameter("status", RoomStatus.valueOf(roomSearch.getStatus()));
        }
        if (StringUtils.hasText(roomSearch.getId())) {
            query.setParameter("id", roomSearch.getId());
        }

        log.info(query.getResultList().toString());

        return query.getResultList();
    }

    //그룹 챌린지 검색
    @Override
    public List<Tuple> findGroupRoomAdmin(RoomSearch roomSearch) {

        String jpql = "select distinct g, g.member.id from GroupRoom g" +
                " join RoomMember rm on g.number = rm.room.number";
        boolean isFirstCondition = true;

        //챌린지 상태 검색
        if (StringUtils.hasText(roomSearch.getStatus())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " g.status = :status";
        }

        //회원 ID 검색
        if (StringUtils.hasText(roomSearch.getId())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " rm.member.id like :id";
        }
        TypedQuery<Tuple> query = em.createQuery(jpql, Tuple.class)
                .setMaxResults(1000);  //최대 1000건

        //파라미터 바인딩
        if (StringUtils.hasText(roomSearch.getStatus())) {
            query.setParameter("status", RoomStatus.valueOf(roomSearch.getStatus()));
        }
        if (StringUtils.hasText(roomSearch.getId())) {
            query.setParameter("id", roomSearch.getId());
        }

        return query.getResultList();
    }*/
}
