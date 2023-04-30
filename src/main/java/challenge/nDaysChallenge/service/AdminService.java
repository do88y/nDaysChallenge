package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.response.AdminLoginDto;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.report.ReportRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AdminLoginDto login(LoginRequestDto loginRequestDto) {
        //id, pw 검증
        Member member = memberRepository.findById(loginRequestDto.getId())
                .orElseThrow(()->new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPw(), member.getPw())){
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        return AdminLoginDto.builder()
                .id(member.getId())
                .auth(member.getAuthority())
                .build();
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
