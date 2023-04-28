package challenge.nDaysChallenge.admin;

import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.report.ReportRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final ReportRepository reportRepository;

    public Member loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("id를 찾을 수 없습니다."));
        return member;
    }

    //챌린지 조회(멤버 id, 챌린지 상태)
    public Page<AdminRoomResponseDto> findRooms(RoomSearch roomSearch, Pageable pageable) {
        if ("SINGLE".equals(roomSearch.getType())) {
            return roomRepository.findSingleRoomAdmin(roomSearch, pageable);
        }
        if ("GROUP".equals(roomSearch.getType())) {
            return roomRepository.findGroupRoomAdmin(roomSearch, pageable);
        }
        return new PageImpl<>(new ArrayList<>(), pageable, 0l);
    }

    //여러 챌린지 삭제
    @Transactional
    public void deleteRoom(List<Long> numbers) {
        for (Long number : numbers) {
            Room room = roomRepository.findByNumber(number).orElseThrow(
                    () -> new NoSuchElementException("챌린지가 존재하지 않습니다."));
            roomRepository.delete(room);
        }
    }

    @Transactional
    public void deleteReport(List<Long> numbers) {
        for (Long number : numbers) {
            Report report = reportRepository.findByNumber(number).orElseThrow(
                    () -> new NoSuchElementException("신고가 존재하지 않습니다."));
            reportRepository.delete(report);
        }
    }
}
