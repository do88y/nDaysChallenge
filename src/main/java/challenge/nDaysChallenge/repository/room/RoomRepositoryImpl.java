package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomStatus;
import lombok.RequiredArgsConstructor;
import reactor.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final EntityManager em;
    @Override
    public List<Room> findSingleRoomAdmin(RoomSearch roomSearch) {

        String jpql = "select s from SingleRoom s join s.member m";
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
        TypedQuery<Room> query = em.createQuery(jpql, Room.class)
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
