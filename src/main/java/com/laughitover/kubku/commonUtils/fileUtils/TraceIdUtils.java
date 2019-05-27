package com.laughitover.kubku.commonUtils.fileUtils;

public class TraceIdUtils {

    private static final ThreadLocal<String> traceIdCache = new ThreadLocal<String>();

    public static String getTraceId() {
        return traceIdCache.get();
    }

    public static void setTraceId(String traceId) {
        traceIdCache.set(traceId);
    }

    public static void clear() {
        traceIdCache.remove();
    }

}