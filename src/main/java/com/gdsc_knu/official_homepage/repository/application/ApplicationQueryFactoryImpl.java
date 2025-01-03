package com.gdsc_knu.official_homepage.repository.application;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.application.QApplication;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ApplicationQueryFactoryImpl implements ApplicationQueryFactory{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Application> findAllApplicationsByOption(Pageable pageable, Track track, Boolean isMarked, Long classYearId) {
        List<Application> applications = jpaQueryFactory
                .selectFrom(QApplication.application)
                .where(QApplication.application.applicationStatus.ne(ApplicationStatus.TEMPORAL)
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
                .where(QApplication.application.applicationStatus.ne(ApplicationStatus.TEMPORAL)
                        .and(eqTrack(track))
                        .and(eqIsMarked(isMarked)))
                .fetchFirst();
        return new PageImpl<>(applications, pageable, total == null ? 0 : total);
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
