package com.springbootutils.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 利用Random方法生产随机密码。
 * 将字母，数字，符号拆开成可取用的数组，按照密码长度做生成策略， 分别为小写字母占密码长度的1/2，大写字母和数字各占1/4，符号占剩余无法除尽的（最多两个符号）。
 * 然后循环取值，每一次从数组中随机拿取字符，并把这个字符随机放入生产的密码字符串中的某个位置。
 */
public class PasswordUtils {

    private static final String ALL_CHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public final static String[] LOWER_CASES = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    public final static String[] UPPER_CASES = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public final static String[] NUMS_LIST = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    // public final static String[] SYMBOLS_ARRAY = {"2", "8", "a", "s", "x"};

    /**
     * 生成随机密码
     *
     * @param pwd_len 密码长度
     * @return 密码的字符串
     */
    public static String genRandomPwd(int pwd_len) {
        if (pwd_len < 6 || pwd_len > 20) {
            return "";
        }
        int lower = pwd_len / 2;
        int upper = (pwd_len - lower) / 2;
        int num = pwd_len - lower - upper;
        StringBuffer pwd = new StringBuffer();
        Random random = new Random();
        while ((lower + upper + num) > 0) {
            if (lower > 0) {
                pwd.append(LOWER_CASES[random.nextInt(LOWER_CASES.length)]);
                lower--;
            }
            if (upper > 0) {
                pwd.append(UPPER_CASES[random.nextInt(UPPER_CASES.length)]);
                upper--;
            }
            if (num > 0) {
                pwd.append(NUMS_LIST[random.nextInt(NUMS_LIST.length)]);
                num--;
            }
        }
        return pwd.toString();
    }

    public static String getPassword() {
        int length = 6 + Math.abs(new Random().nextInt(12));
        char[] chars =
                ALL_CHAR.toCharArray();
        List<String> res = new ArrayList<>();
        for (char c : chars) {
            res.add(c + "");
        }
        Collections.shuffle(res);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(res.get(i));
        }
        return buffer.toString();
    }
}
