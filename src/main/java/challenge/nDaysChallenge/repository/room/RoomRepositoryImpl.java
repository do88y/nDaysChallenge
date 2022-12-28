package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import lombok.RequiredArgsConstructor;
import reactor.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepositoryCustom {

    private final EntityManager em;
    @Override
    public List<SingleRoom> findSingleRoomAdmin(Member member, Room room) {

        String jpql = "select s from SingleRoom s join s.member m";
        boolean isFirstCondition = true;

        //챌린지 상태 검색
        if (room.getStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " s.status = :status";
        }

        //회원 ID 검색
        if (StringUtils.hasText(member.getId())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.id like :id";
        }
        TypedQuery<SingleRoom> query = em.createQuery(jpql, SingleRoom.class)
                .setMaxResults(1000);  //최대 1000건


        return query.getResultList();
    }
}
