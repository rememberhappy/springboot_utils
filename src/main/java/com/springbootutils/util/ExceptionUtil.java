package com.springbootutils.util;

/**
 * 获取异常堆栈信息
 *
 * @Author zhangdj
 * @Date 2021/6/9:16:37
 */
public class ExceptionUtil {
    /**
     * @param e          异常信息
     * @param levelCount 级别，异常信息的前几行
     * @return java.lang.String
     * @Throws
     * @Author zhangdj
     * @date 2021/6/9 16:38
     */
    public static String getFullStackTrace(Throwable e, Integer levelCount) {
        if (e == null) {
            return " ";
        } else {
            StackTraceElement[] stackTrace = e.getStackTrace();
            StringBuilder builder = new StringBuilder();
            builder.append(" ").append(e.toString()).append("; ");
            int count = 1;
            StackTraceElement[] var5 = stackTrace;
            int var6 = stackTrace.length;
            for (int var7 = 0; var7 < var6; ++var7) {
                StackTraceElement item = var5[var7];
                ++count;
                if (count > levelCount) {
                    break;
                }
                builder.append("at ").append(item.toString()).append("; ");
            }
            return builder.toString();
        }
    }
}