package com.back.domain.meber.member.dto;

import com.back.domain.meber.member.entity.Member;

import java.time.LocalDateTime;

public record MemberDto(
        long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String nickname
) {
    public MemberDto(Member member) {
        this(
                member.getId(),
                member.getCreateDate(),
                member.getModifyDate(),
                member.getNickname()
        );
    }
}