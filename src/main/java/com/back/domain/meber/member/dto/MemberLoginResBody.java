package com.back.domain.meber.member.dto;

public record MemberLoginResBody(
        MemberDto item,
        String apiKey,
        String accessToken
) {
}
