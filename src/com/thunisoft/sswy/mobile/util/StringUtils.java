package com.thunisoft.sswy.mobile.util;

public class StringUtils {
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0)
            return true;
        for (int i = 0; i < strLen; i++)
            if (!Character.isWhitespace(cs.charAt(i)))
                return false;

        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String trim(String str) {
        return str != null ? str.trim() : null;
    }

    public static String trimToNull(String str) {
        String ts = trim(str);
        return isEmpty(ts) ? null : ts;
    }

    public static String trimToEmpty(String str) {
        return str != null ? str.trim() : "";
    }

    /**
     * 将Url的前半部分和后半部分连接成一个Url。
     * 主要处理preUrl的结尾、suffUrl的开头是否包含字符"/"。
     * @param preUrl
     * @param suffUrl
     * @return
     */
    public static String concatUrl(String preUrl, String suffUrl) {
        String url = null;
        if (isBlank(preUrl)) {
            url = suffUrl;
        } else if (isBlank(suffUrl)) {
            url = preUrl;
        } else if (preUrl.endsWith("/") && suffUrl.startsWith("/")) {
            url = preUrl + suffUrl.substring(1);
        } else if (!preUrl.endsWith("/") && !suffUrl.startsWith("/")) {
            url = preUrl + "/" + suffUrl;
        } else {
            url = preUrl + suffUrl;
        }
        return url;
    }
}
