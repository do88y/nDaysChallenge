package challenge.nDaysChallenge.repository.report;

import challenge.nDaysChallenge.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryCustom {

    public Optional<Report> findByNumber(Long number);
}
