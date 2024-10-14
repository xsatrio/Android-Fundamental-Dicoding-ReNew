package com.dicoding.dicodingevent.data

enum class ResponseCode(val value: Int) {
    RC_UNAUTHORIZED(401),
    RC_FORBIDDEN(403),
    RC_NOT_FOUND(404)
}
