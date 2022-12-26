package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
