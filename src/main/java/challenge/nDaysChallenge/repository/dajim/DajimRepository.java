package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
<<<<<<< HEAD
import challenge.nDaysChallenge.domain.dajim.Open;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Repository
@Transactional
public interface DajimRepository extends JpaRepository<Dajim, Long> {

    //다짐리스트 조회
    Optional<Dajim> findAllByNumber();

    //선택한 다짐 조회 (상세)
    Optional<Dajim> findByNumber();

    //다짐 저장
    @Override
    <S extends Dajim> S save(S entity); //save() : insert & update 모두 수행
=======
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DajimRepository extends JpaRepository<Dajim, Long> {

    //다짐 등록
    @Override
    <S extends Dajim> S save(S entity);

    //룸멤버 (1~4명) 다짐 상세 조회
    @Query("SELECT d FROM Dajim d WHERE d.room.number=:roomNumber")
    List<Dajim> findAllByRoomNumber(Long roomNumber);

    //룸 넘버로 룸 객체 찾기
    //@Query("SELECT r FROM Room r WHERE r.number=:roomNumber")
    Room findByNumber(Long roomNumber);

>>>>>>> refs/remotes/origin/develop
}
