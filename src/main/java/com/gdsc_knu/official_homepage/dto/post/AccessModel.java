package com.gdsc_knu.official_homepage.dto.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AccessModel {
    @JsonProperty(value = "canDelete")
    private boolean delete;
    private boolean modify;

    public boolean canDelete() {
        return delete;
    }

    public boolean canModify() {
        return modify;
    }

    public static AccessModel of(boolean canDelete, boolean canModify) {
        return AccessModel.builder()
                .delete(canDelete)
                .modify(canModify)
                .build();
    }
}
