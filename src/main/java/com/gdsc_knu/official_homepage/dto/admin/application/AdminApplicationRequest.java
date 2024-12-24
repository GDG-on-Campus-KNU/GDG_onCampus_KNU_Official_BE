package com.gdsc_knu.official_homepage.dto.admin.application;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gdsc_knu.official_homepage.entity.ClassYear;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AdminApplicationRequest {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Append {
        private String note;
        private Integer version;
    }
  
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ClassYearRequest {
        private String name;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime applyStartDateTime;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime applyEndDateTime;

        public ClassYear toEntity() {
            return ClassYear.builder()
                    .name(name)
                    .applicationStartDateTime(applyStartDateTime)
                    .applicationEndDateTime(applyEndDateTime)
                    .build();
        }
    }
}