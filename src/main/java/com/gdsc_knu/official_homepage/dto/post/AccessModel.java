package com.gdsc_knu.official_homepage.dto.post;

import com.gdsc_knu.official_homepage.entity.Member;
import com.gdsc_knu.official_homepage.entity.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class AccessModel {
    private boolean delete;
    private boolean modify;

    public boolean canDelete() {
        return delete;
    }

    public boolean canModify() {
        return modify;
    }

    private static AccessModel of(boolean canDelete, boolean canModify) {
        return AccessModel.builder()
                .delete(canDelete)
                .modify(canModify)
                .build();
    }


    public static AccessModel calcCommentAccess(Long memberId, Long postAuthorId, Long commentAuthorId) {
        boolean canDelete = memberId.equals(postAuthorId) || memberId.equals(commentAuthorId);
        boolean canModify = memberId.equals(commentAuthorId);
        return AccessModel.of(canDelete, canModify);
    }

    public static AccessModel calcPostAccess(Long memberId, Member postAuthor) {
        boolean canModify = memberId.equals(postAuthor.getId());
        boolean canDelete = memberId.equals(postAuthor.getId())
                || postAuthor.getRole().equals(Role.ROLE_CORE);
        return AccessModel.of(canDelete, canModify);
    }
}
