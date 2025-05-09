package com.gdsc_knu.official_homepage.dto.admin.application;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.application.ApplicationAnswer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class AdminApplicationResponse {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Statistics {
        private Integer total;
        private Integer openCount;
        private Integer notOpenCount;
        private Integer approvedCount;
        private Integer rejectedCount;
        private Integer documentPassedCount;

        public static Statistics from(ApplicationStatisticType applicationStatisticType) {
            return Statistics.builder()
                    .total(applicationStatisticType.getTotal())
                    .openCount(applicationStatisticType.getOpenCount())
                    .notOpenCount(applicationStatisticType.getTotal() - applicationStatisticType.getOpenCount())
                    .approvedCount(applicationStatisticType.getApprovedCount())
                    .rejectedCount(applicationStatisticType.getRejectedCount())
                    .documentPassedCount(applicationStatisticType.getDocumentPassedCount())
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Overview {
        private Long id;
        private String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime submittedAt;
        private String studentNumber;
        private String major;
        private String track;
        private boolean isOpen;
        private boolean isMarked;

        public static Overview from(Application application){
            return Overview.builder()
                    .id(application.getId())
                    .name(application.getName())
                    .submittedAt(application.getSubmittedAt())
                    .studentNumber(application.getStudentNumber())
                    .major(application.getMajor())
                    .track(application.getTrack().name())
                    .isOpen(application.isOpened())
                    .isMarked(application.isMarked())
                    .build();
        }
    }


    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Detail {
        private Long id; // application id
        private Integer version;
        private String name;
        private String studentNumber;
        private String major;
        private String phoneNumber;
        private String email;
        private String track;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime submittedAt;
        private String techStack;
        private String link;
        private boolean isMarked;
        private String note;
        private List<Answer> answers;

        public static Detail from(Application application){
            return Detail.builder()
                    .id(application.getId())
                    .version(application.getVersion())
                    .name(application.getName())
                    .studentNumber(application.getStudentNumber())
                    .major(application.getMajor())
                    .phoneNumber(application.getPhoneNumber())
                    .email(application.getEmail())
                    .track(application.getTrack().name())
                    .submittedAt(application.getSubmittedAt())
                    .techStack(application.getTechStack())
                    .link(application.getLinks())
                    .isMarked(application.isMarked())
                    .note(application.getNote())
                    .answers(application.getAnswers().stream()
                            .map(Answer::from)
                            .toList())
                    .build();
        }

        @Getter
        @Builder
        @AllArgsConstructor
        private static class Answer {
            private int questionNumber;
            private String answer;
            public static Answer from(ApplicationAnswer applicationAnswer){
                return Answer.builder()
                        .questionNumber(applicationAnswer.getQuestionNumber())
                        .answer(applicationAnswer.getAnswer())
                        .build();
            }

        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Result {
        private int code;
        private String message;
        private HttpStatus data;

        public static Result from(HttpStatus status, String message) {
            return Result.builder()
                    .code(status.value())
                    .message(message)
                    .data(status)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClassYearResponse {
        private Long id;
        private String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime applyStartDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime applyEndDateTime;

        public static AdminApplicationResponse.ClassYearResponse from(ClassYear classYear) {
            return AdminApplicationResponse.ClassYearResponse.builder()
                    .id(classYear.getId())
                    .name(classYear.getName())
                    .applyStartDateTime(classYear.getApplicationStartDateTime())
                    .applyEndDateTime(classYear.getApplicationEndDateTime())
                    .build();
        }
    }
}
