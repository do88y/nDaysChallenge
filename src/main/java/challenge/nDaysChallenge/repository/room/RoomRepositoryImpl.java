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
    public Page<AdminRoomResponseDto> findSingleRoomAdmin(RoomSearch roomSearch, Pageable pageable) {

        QueryResults<AdminRoomResponseDto> results = queryFactory
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
                .where(singleRoomTypeEq(roomSearch.getType()),
                        singleRoomStatusEq(roomSearch.getStatus()),
                        singleRoomMemberIdEq(roomSearch.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminRoomResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<AdminRoomResponseDto> findGroupRoomAdmin(RoomSearch roomSearch, Pageable pageable) {

        QueryResults<AdminRoomResponseDto> results = queryFactory
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
                .join(roomMember)
                .on(groupRoom.number.eq(roomMember.room.number))
                .join(member)
                .on(member.number.eq(roomMember.member.number))
                .where(groupRoomTypeEq(roomSearch.getType()),
                        groupRoomStatusEq(roomSearch.getStatus()),
                        groupRoomMemberIdEq(roomSearch.getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<AdminRoomResponseDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private static BooleanExpression singleRoomTypeEq(String type) {
        return hasText(type) ? singleRoom.type.stringValue().eq(type) : null;
    }

    private static BooleanExpression singleRoomStatusEq(String status) {
        return hasText(status) ? singleRoom.status.eq(RoomStatus.valueOf(status)) : null;
    }

    private static BooleanExpression singleRoomMemberIdEq(String id) {
        return hasText(id) ? member.id.eq(id) : null;
    }

    private static BooleanExpression groupRoomTypeEq(String type) {
        return hasText(type) ? groupRoom.type.stringValue().eq(type) : null;
    }

    private static BooleanExpression groupRoomStatusEq(String status) {
        return hasText(status) ? groupRoom.status.eq(RoomStatus.valueOf(status)) : null;
    }

    private static BooleanExpression groupRoomMemberIdEq(String id) {
        return hasText(id) ? roomMember.member.id.eq(id) : null;
    }
}
