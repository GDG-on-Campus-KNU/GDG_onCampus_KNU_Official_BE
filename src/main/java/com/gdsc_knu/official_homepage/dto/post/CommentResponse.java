package com.gdsc_knu.official_homepage.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.gdsc_knu.official_homepage.entity.post.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createAt;

    private String name;
    private String profileUrl;

    @JsonProperty(value = "isChild")
    private boolean isChild;
    private boolean canDelete;
    private boolean canModify;

    public static CommentResponse from(Comment comment, AccessModel access){
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createAt(comment.getCreateAt())
                .name(comment.getAuthorName())
                .profileUrl(comment.getAuthorProfile())
                .isChild(comment.getParent() != null)
                .canDelete(access.canDelete())
                .canModify(access.canModify())
                .build();
    }


}
