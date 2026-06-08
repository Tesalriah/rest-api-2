package com.back.domain.meber.member.dto;

import com.back.domain.meber.member.entity.Member;

import java.time.LocalDateTime;

public record MemberWithUsernameDto(
        long id,
        LocalDateTime createDate,
        LocalDateTime modifyDate,
        String nickname,
        String username
)  {
    public MemberWithUsernameDto(Member member) {
        this(
                member.getId(),
                member.getCreateDate(),
                member.getModifyDate(),
                member.getNickname(),
                member.getUsername()
        );
    }
}