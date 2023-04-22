package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.domain.room.RoomType;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.QAdminRoomResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static challenge.nDaysChallenge.domain.member.QMember.*;
import static challenge.nDaysChallenge.domain.room.QGroupRoom.*;
import static challenge.nDaysChallenge.domain.room.QRoomMember.*;
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
    public List<AdminRoomResponseDto> findRoomAdmin(RoomSearch roomSearch, Pageable pageable) {

        List<AdminRoomResponseDto> singleResults = queryFactory
                .select(new QAdminRoomResponseDto(
                        singleRoom.number.as("roomNumber"),
                        singleRoom.name,
                        singleRoom.type.stringValue().as("type"),
                        singleRoom.category.stringValue().as("category"),
                        singleRoom.status.stringValue().as("status"),
                        singleRoom.period.startDate,
                        singleRoom.period.endDate,
                        member.id.as("memberId")))
                .from(singleRoom)
                .join(singleRoom.member, member)
                .where(singleRoomStatusEq(roomSearch.getStatus()),
                        singleRoomMemberIdEq(roomSearch.getId()))
                .fetch();
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();

        List<AdminRoomResponseDto> groupResult = queryFactory
                .select(new QAdminRoomResponseDto(
                        groupRoom.number.as("roomNumber"),
                        groupRoom.name,
                        groupRoom.type.stringValue().as("type"),
                        groupRoom.category.stringValue().as("category"),
                        groupRoom.status.stringValue().as("status"),
                        groupRoom.period.startDate,
                        groupRoom.period.endDate,
                        member.id.as("memberId")))
                .from(groupRoom)
                .leftJoin(roomMember)
                .on(groupRoom.number.eq(roomMember.room.number))
                .leftJoin(member)
                .on(member.number.eq(roomMember.member.number))
                .where(groupRoomStatusEq(roomSearch.getStatus()),
                        groupRoomMemberIdEq(roomSearch.getId()))
                .fetch();
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();

//        List<AdminRoomResponseDto> content = Stream.concat(
//                singleResults.getResults().stream(), groupResults.getResults().stream()
//                ).collect(Collectors.toList());
//        long total = singleResults.getTotal() + groupResults.getTotal();
//
//        return new PageImpl<>(content, pageable, total);
        return Stream.concat(singleResults.stream(), groupResult.stream()).collect(Collectors.toList());
    }

    private static BooleanExpression singleRoomStatusEq(String status) {
        return hasText(status) ? singleRoom.status.eq(RoomStatus.valueOf(status)) : null;
    }

    private static BooleanExpression singleRoomMemberIdEq(String id) {
        return hasText(id) ? member.id.eq(id) : null;
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
