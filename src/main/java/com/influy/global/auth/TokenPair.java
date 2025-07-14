package com.influy.global.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public record TokenPair(String accessToken, String refreshToken) {}
