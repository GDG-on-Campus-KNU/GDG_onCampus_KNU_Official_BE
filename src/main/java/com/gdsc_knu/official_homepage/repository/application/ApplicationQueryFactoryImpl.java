package com.gdsc_knu.official_homepage.repository.application;

import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationStatisticType;
import com.gdsc_knu.official_homepage.dto.admin.application.ApplicationTrackType;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.application.QApplication;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import static com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus.*;


@Repository
@RequiredArgsConstructor
public class ApplicationQueryFactoryImpl implements ApplicationQueryFactory{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Application> findAllApplicationsByOption(Pageable pageable, Track track, Boolean isMarked, Long classYearId) {
        List<Application> applications = jpaQueryFactory
                .selectFrom(QApplication.application)
                .where(QApplication.application.applicationStatus.ne(TEMPORAL)
                        .and(eqTrack(track))
                        .and(eqIsMarked(isMarked))
                        .and(eqClassYear(classYearId)))
                .orderBy(QApplication.application.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(QApplication.application.count())
                .from(QApplication.application)
                .where(QApplication.application.applicationStatus.ne(TEMPORAL)
                        .and(eqTrack(track))
                        .and(eqIsMarked(isMarked))
                        .and(eqClassYear(classYearId)))
                .fetchFirst();
        return new PageImpl<>(applications, pageable, total == null ? 0 : total);
    }

    @Override
    public ApplicationStatisticType getStatistics(Long classYearId) {
        return jpaQueryFactory
            .select(Projections.fields(ApplicationStatisticType.class,
                new CaseBuilder().when(QApplication.application.applicationStatus.ne(TEMPORAL))
                        .then(1).otherwise(0).sum().as("total"),
                new CaseBuilder().when(QApplication.application.applicationStatus.ne(TEMPORAL)
                        .and(QApplication.application.isOpened.eq(true)))
                        .then(1).otherwise(0).sum().as("openCount"),
                new CaseBuilder().when(QApplication.application.applicationStatus.eq(APPROVED))
                        .then(1).otherwise(0).sum().as("approvedCount"),
                new CaseBuilder().when(QApplication.application.applicationStatus.eq(REJECTED))
                        .then(1).otherwise(0).sum().as("rejectedCount")
            ))
            .from(QApplication.application)
            .where(eqClassYear(classYearId))
            .fetchOne();
    }

    @Override
    public List<ApplicationTrackType> getGroupByTrack(Long classYearId) {
        return jpaQueryFactory
                .select(Projections.fields(ApplicationTrackType.class,
                        QApplication.application.track.as("track"),
                        QApplication.application.count().as("count")
                ))
                .from(QApplication.application)
                .where(QApplication.application.applicationStatus.ne(TEMPORAL)
                        .and(eqClassYear(classYearId)))
                .groupBy(QApplication.application.track)
                .fetch();
    }


    private BooleanExpression eqTrack(Track track) {
        return track == null ? null : QApplication.application.track.eq(track);
    }

    private BooleanExpression eqIsMarked(Boolean isMarked) {
        return (isMarked == null || !isMarked) ? null : QApplication.application.isMarked.eq(true);
    }

    private BooleanExpression eqClassYear(Long classYearId) {
        return classYearId == null ? null : QApplication.application.classYear.id.eq(classYearId);
    }
}
