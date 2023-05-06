package challenge.nDaysChallenge.repository.report;

import challenge.nDaysChallenge.dto.response.ReportResponseDto;
import challenge.nDaysChallenge.dto.response.QReportResponseDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static challenge.nDaysChallenge.domain.QReport.*;
import static java.util.Objects.*;

public class ReportRepositoryImpl implements ReportRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ReportRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ReportResponseDto> findReports(ReportSearch reportSearch) {

        return queryFactory
                .select(new QReportResponseDto(
                            report.number.as("report"),
                            report.cause,
                            report.content,
                            report.dajim.number.as("dajim")))
                .from(report)
                .where(dajimNumberEq(reportSearch.getDajimNumber()))
                .fetch();
    }

    private static BooleanExpression dajimNumberEq(Long dajim) {
        return isNull(dajim) ? null : report.dajim.number.eq(dajim);
    }
}
