package com.example.springbootinit.exception;

import com.example.springbootinit.common.ErrorCode;

/**
 * ���쳣������
 */
public class ThrowUtils {

    /**
     * �������������쳣
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * �������������쳣
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }
}
