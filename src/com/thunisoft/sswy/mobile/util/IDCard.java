/**
 *
 */
package com.thunisoft.sswy.mobile.util;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 中国公民身份证处理工具类
 *
 * @author gewx
 */
public class IDCard {
    /**
     * 新身份证号码位数
     */
    static final int NEW_CARD_LENGTH = 18;
    /**
     * 旧身份证号码位数
     */
    static final int OLD_CARD_LENGTH = 15;
    /**
     * card
     */
    private Card card;

    /**
     * 身份证号码
     */
    private String cardNo;

    /**
     * 18位身份证号码
     */
    private volatile String newCardNo;

    /**
     * 构造方法。
     * @param cardNo 身份证号码
     * @throws IllegalArgumentException 身份证号码不合法
     */
    public IDCard(String cardNo) throws IllegalArgumentException {
        if (cardNo == null)
            throwIllegalArgumentException(null);
        cardNo = cardNo.trim();
        int len = cardNo.length();
        if (len != NEW_CARD_LENGTH && len != OLD_CARD_LENGTH)
            throwIllegalArgumentException(cardNo);

        byte i;
        char ch;
        for (i = 0; i < cardNo.length() - 1; i++) {
            ch = cardNo.charAt(i);
            if (ch > '9' || ch < '0')
                throwIllegalArgumentException(cardNo);
        }
        ch = cardNo.charAt(i);
        if (ch == 'x' || ch == 'X' || (ch <= '9' && ch >= '0'))
            card = new Card(cardNo);
        else
            throwIllegalArgumentException(cardNo);

        if (!card.isValid()) {
            throwIllegalArgumentException(cardNo);
        }
        this.cardNo = cardNo;
    }

    private void throwIllegalArgumentException(String cardNo) {
        throw new IllegalArgumentException("不合法的身份证号码：" + cardNo);
    }

    /**
     * 判断身份证号码是否合法
     * @param cardNo 身份证号码
     * @return 合法返回true，否则返回false
     */
    public static boolean isValid(String cardNo) {
        try {
            return new IDCard(cardNo).card.isValid();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 获取出生日期
     * @return yyyy-MM-dd HH:mm:ss.SSS
     */
    @SuppressLint("SimpleDateFormat")
	public static String getBirthDay(String cardNo) {
    	String birthdayStr = "";
		try {
			Date birthday = new IDCard(cardNo).card.getBirthday();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			birthdayStr = sdf.format(birthday);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return birthdayStr;
    }
    
    /**
     * Card 身份证
     * @author anyx
     * @version 1.0
     */
    private static class Card {
        /**
         * 身份证号码中的出生日期的格式
         */
        static final String BIRTHDAY_FORMAT = "yyyyMMdd";
        /**
         * 身份证的最小出生日期,1900年1月1日
         */
        static final Date MINIMAL_BIRTHDAY = new Date(-2209017600000L);
        /**
         * 18位身份证中，各个数字的生成校验和时的权值
         */
        static final byte[] WEIGHT =
            { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        /**
         * 18位身份证中最后一位校验码
         */
        static final char[] CHECK_CODE =
            { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
        /**
         * 号码
         */
        String cardNo;
        /**
         * 是否18位新身份证号码
         */
        boolean isNewCard;
        /**
         * 是否合法
         */
        boolean valid;

        /**
         * 经过长度验证的号码(15或18位)
         * @param cardNo 身份证号码
         */
        Card(String cardNo) {
            this.cardNo = cardNo;
            isNewCard = cardNo.length() == NEW_CARD_LENGTH;

            valid = isAreaValid()
                    && isBirthdayValid()
                    && isCheckCodeValid();
        }

        /**
         * 判断身份证合法性
         * @return 合法返回true，否则返回false;
         */
        boolean isValid() {
            return valid;
        }

        /**
         * @return 符合校验码返回true，否则返回false
         */
        private boolean isCheckCodeValid() {
            //15位身份证号码直接返回true
            if (!isNewCard)
                return true;

            char expected = calculateCheckCode(cardNo);
            char actual = cardNo.charAt(NEW_CARD_LENGTH - 1);
            if (expected == 'X') {
                return actual == 'X' || actual == 'x';
            } else {
                return expected == actual;
            }
        }

        /**
         * 计算身份证号码的校验码
         * <li>校验码（第十八位数）：<br/>
         * <ul>
         * <li>十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0...16 ，先对前17位数字的权求和；<br>
         *      Ai:表示第i位置上的身份证号码数字值 <br>
         *      Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2；</li>
         * <li>计算模 Y = mod(S, 11)</li>
         * <li>通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2</li>
         * </ul>
         *
         * @param cardNo 身份证号码，至少保证{@link NEW_CARD_LENGTH} - 1 位长度
         * @return 校验码
         */
        private static final char calculateCheckCode(String cardNo) {
            int checkSum = 0;
            char ch;
            for (int i = 0; i < NEW_CARD_LENGTH - 1; i++) {
                ch = cardNo.charAt(i);
                checkSum += (ch - '0') * WEIGHT[i];
            }
            return CHECK_CODE[checkSum % 11];
        }

        /**
         * @return 出生日期合法返回true，否则返回false
         */
        private boolean isBirthdayValid() {
            Date actual;
            try {
                actual = getBirthday();
            } catch (ParseException e) {
                return false;
            }
            return !actual.before(MINIMAL_BIRTHDAY)
                    && !actual.after(new Date());
        }

        /**
         * @return 区域合法返回true，否则返回false
         */
        private boolean isAreaValid() {
            // FIXME 行政区域暂未做校验
            return true;
        }

        /**
         * 获取出生日期
         * @return 出生日期
         * @throws ParseException
         */
        Date getBirthday() throws ParseException {
            String birthdayStr;
            if (isNewCard)
                birthdayStr = cardNo.substring(6, 14);
            else
                birthdayStr = "19" + cardNo.substring(6, 12);
            DateFormat format = new SimpleDateFormat(BIRTHDAY_FORMAT);
            format.setLenient(false);
            return format.parse(birthdayStr);
        }

        /**
         * 判断性别
         * @return 0：女；1：男
         */
        int getGender() {
            int genderBit;
            if (isNewCard)
                genderBit = cardNo.charAt(NEW_CARD_LENGTH - 2) - '0';
            else
                genderBit = cardNo.charAt(OLD_CARD_LENGTH - 1) - '0';
            return genderBit % 2;
        }
    }

    /**
     * 判断性别
     * @return 0：女，1：男
     */
    public int getGender() {
        return card.getGender();
    }

    /**
     * 获取18位身份证号码
     * @return 18位身份证号码
     */
    public String getNewCardNo() {
        if (card.isNewCard)
            return cardNo;

        if (newCardNo == null) {
            synchronized (this) {
                if (newCardNo == null) {
                    newCardNo = cardNo.substring(0, 6) + "19" + cardNo.substring(6);
                    newCardNo += Card.calculateCheckCode(newCardNo);
                }
            }
        }
        return newCardNo;
    }

    /**
     * 获取性别，字符串表示
     * @return "女" 或 "男"
     */
    public String getGenderString() {
        return getGender() == 0 ? "女" : "男";
    }

    /**
     * 获取出生日期
     * @return 出生日期
     */
    public Date getBrithday() {
        try {
            return card.getBirthday();
        } catch (ParseException e) {
            //此时一定是合法身份证号码了，不应再抛出异常
            //如果抛出一定是 运行时 出问题了
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the cardNo
     */
    public String getCardNo() {
        return cardNo;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getCardNo();
    }

    /**
     * 获取身份证号码位数
     */
    public int getLength() {
        return cardNo.length();
    }

}
