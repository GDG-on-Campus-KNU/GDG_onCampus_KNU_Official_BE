package com.gdsc_knu.official_homepage.dto;

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
    private final int page;
    private final boolean hasNext;
    private final int totalPage;



    public static <U,T> PagingResponse<T> from(Page<U> data, Function<U,T> converter) {
        return PagingResponse.<T>builder()
                .data(data.getContent().stream().map(converter).toList())
                .page(data.getNumber())
                .hasNext(data.hasNext())
                .totalPage(data.getTotalPages())
                .build();
    }
}
