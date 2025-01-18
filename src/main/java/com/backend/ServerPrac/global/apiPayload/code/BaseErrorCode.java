package com.backend.ServerPrac.global.apiPayload.code;

public interface BaseErrorCode {
    ErrorReasonDto getReason();
    ErrorReasonDto getReasonHttpStatus();
}
