package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final EntityManager em;
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
    }
}
