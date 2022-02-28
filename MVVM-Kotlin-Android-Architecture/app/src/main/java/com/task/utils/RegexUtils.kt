package com.task.utils

import java.util.regex.Pattern

/**
 *正则表达式工具
 */
object RegexUtils {

    /**
     * 是否是邮箱的正则
     */
    private val EMAIL_ADDRESS: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    /**
     * 是否是邮箱
     * @param email 邮箱
     * @return
     */
    fun isValidEmail(email: String): Boolean {
        return EMAIL_ADDRESS.matcher(email).matches()
    }
}
