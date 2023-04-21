package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.QAdminRoomResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static challenge.nDaysChallenge.domain.member.QMember.*;
import static challenge.nDaysChallenge.domain.room.QGroupRoom.*;
import static challenge.nDaysChallenge.domain.room.QRoomMember.*;
import static challenge.nDaysChallenge.domain.room.QSingleRoom.*;
import static com.querydsl.core.types.dsl.Expressions.*;
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
    public Page<AdminRoomResponseDto> findSingleRoomAdmin(RoomSearch roomSearch, Pageable pageable) {

        String stringType = singleRoom.type.toString();
        String stringCategory = singleRoom.category.toString();
        String stringStatus = singleRoom.status.toString();

        QueryResults<AdminRoomResponseDto> results = queryFactory
                .select(new QAdminRoomResponseDto(
                        singleRoom.number.as("roomNumber"),
                        singleRoom.name,
                        as(constant(stringType), "type"),
                        as(constant(stringCategory), "category"),
                        as(constant(stringStatus), "status"),
                        singleRoom.period.startDate,
                        singleRoom.period.endDate,
                        member.id.as("memberId")))
                .from(singleRoom)
                .join(singleRoom.member, member)
                .where(
                        singleRoomStatusEq(roomSearch.getStatus()),
                        singleRoomMemberIdEq(roomSearch.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminRoomResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private static BooleanExpression singleRoomStatusEq(String status) {
        return hasText(status) ? singleRoom.status.eq(RoomStatus.valueOf(status)) : null;
    }

    private static BooleanExpression singleRoomMemberIdEq(String id) {
        return hasText(id) ? member.id.eq(id) : null;
    }

    @Override
    public Page<AdminRoomResponseDto> findGroupRoomAdmin(RoomSearch roomSearch, Pageable pageable) {

        String stringType = groupRoom.type.toString();
        String stringCategory = groupRoom.category.toString();
        String stringStatus = groupRoom.status.toString();

        QueryResults<AdminRoomResponseDto> results = queryFactory
                .select(new QAdminRoomResponseDto(
                        groupRoom.number.as("roomNumber"),
                        groupRoom.name,
                        as(constant(stringType), "type"),
                        as(constant(stringCategory), "category"),
                        as(constant(stringStatus), "status"),
                        groupRoom.period.startDate,
                        groupRoom.period.endDate,
                        roomMember.member.id.as("memberId")))
                .distinct()
                .from(groupRoom)
                .join(roomMember)
                .on(roomMember.room.number.eq(groupRoom.number))
                .where(
                        groupRoomStatusEq(roomSearch.getStatus()),
                        groupRoomMemberIdEq(roomSearch.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminRoomResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private static BooleanExpression groupRoomStatusEq(String status) {
        return hasText(status) ? groupRoom.status.eq(RoomStatus.valueOf(status)) : null;
    }

    private static BooleanExpression groupRoomMemberIdEq(String id) {
        return hasText(id) ? roomMember.member.id.eq(id) : null;
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
