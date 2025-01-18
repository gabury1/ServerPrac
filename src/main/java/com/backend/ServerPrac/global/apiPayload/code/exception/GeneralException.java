package com.backend.ServerPrac.global.apiPayload.code.exception;

import com.backend.ServerPrac.global.apiPayload.code.BaseErrorCode;
import com.backend.ServerPrac.global.apiPayload.code.ErrorReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    // RuntimeException 을 상속했기 때문에, throw로 예외를 발생시킬 수 있다.
    // 직접 정의한 예외를 throw 할 수 있게 해줌.

    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}