package com.task.data.dto.login

/**
 * 登录数据实体
 * @property email 邮箱
 * @property password 密码
 */
data class LoginRequest(val email: String, val password: String)
