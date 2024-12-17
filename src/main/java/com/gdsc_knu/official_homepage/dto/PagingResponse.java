package com.gdsc_knu.official_homepage.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.function.Function;

@Getter
@Builder
@AllArgsConstructor
public class PagingResponse<T> {
    private final List<T> data;
    private final boolean hasNext;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer page;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Integer totalPage;



    public static <U,T> PagingResponse<T> from(Page<U> data, Function<U,T> converter) {
        return PagingResponse.<T>builder()
                .data(data.getContent().stream().map(converter).toList())
                .page(data.getNumber())
                .hasNext(data.hasNext())
                .totalPage(data.getTotalPages())
                .build();
    }

    public static <U,T> PagingResponse<T> withoutCountFrom(List<U> data, int size, Function<U,T> converter) {
        boolean hasNext = data.size() == size;

        return PagingResponse.<T>builder()
                .data(data.stream().map(converter).toList())
                .hasNext(hasNext)
                .build();
    }
}
