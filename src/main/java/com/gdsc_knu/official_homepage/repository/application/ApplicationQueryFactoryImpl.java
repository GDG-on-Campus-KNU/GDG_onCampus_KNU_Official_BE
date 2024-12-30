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

import static com.gdsc_knu.official_homepage.entity.application.QApplication.application;
import static com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus.*;


@Repository
@RequiredArgsConstructor
public class ApplicationQueryFactoryImpl implements ApplicationQueryFactory{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Application> findAllApplicationsByOption(Pageable pageable, Track track, Boolean isMarked, Long classYearId) {
        List<Application> applications = jpaQueryFactory
                .selectFrom(application)
                .where(application.applicationStatus.ne(TEMPORAL)
                        .and(eqTrack(track))
                        .and(eqIsMarked(isMarked))
                        .and(eqClassYear(classYearId)))
                .orderBy(application.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(application.count())
                .from(application)
                .where(application.applicationStatus.ne(TEMPORAL)
                        .and(eqTrack(track))
                        .and(eqIsMarked(isMarked))
                        .and(eqClassYear(classYearId)))
                .fetchFirst();
        return new PageImpl<>(applications, pageable, total == null ? 0 : total);
    }

    // 리팩토링 대상
    @Override
    public ApplicationStatisticType getStatistics(Long classYearId) {
        return jpaQueryFactory
            .select(Projections.fields(ApplicationStatisticType.class,
                new CaseBuilder().when(application.applicationStatus.ne(TEMPORAL))
                        .then(1).otherwise(0).sum().as("total"),
                new CaseBuilder().when(application.applicationStatus.ne(TEMPORAL)
                        .and(application.isOpened.eq(true)))
                        .then(1).otherwise(0).sum().as("openCount"),
                new CaseBuilder().when(application.applicationStatus.eq(APPROVED))
                        .then(1).otherwise(0).sum().as("approvedCount"),
                new CaseBuilder().when(application.applicationStatus.eq(REJECTED))
                        .then(1).otherwise(0).sum().as("rejectedCount"),
                new CaseBuilder().when(application.isMarked.eq(true))
                        .then(1).otherwise(0).sum().as("documentPassedCount")
            ))
            .from(application)
            .where(eqClassYear(classYearId))
            .fetchOne();
    }

    @Override
    public List<ApplicationTrackType> getGroupByTrack(Long classYearId) {
        return jpaQueryFactory
                .select(Projections.fields(ApplicationTrackType.class,
                        application.track.as("track"),
                        application.count().as("count")
                ))
                .from(application)
                .where(application.applicationStatus.ne(TEMPORAL)
                        .and(eqClassYear(classYearId)))
                .groupBy(application.track)
                .fetch();
    }


    private BooleanExpression eqTrack(Track track) {
        return track == null ? null : application.track.eq(track);
    }

    private BooleanExpression eqIsMarked(Boolean isMarked) {
        return (isMarked == null || !isMarked) ? null : application.isMarked.eq(true);
    }

    private BooleanExpression eqClassYear(Long classYearId) {
        return classYearId == null ? null : application.classYear.id.eq(classYearId);
    }
}
