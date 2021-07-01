package com.phone91.sdk.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.phone91.sdk.model.ChatObject

class JsonUtil {

    companion object {
//        private val mapper = ObjectMapper().registerModule(KotlinModule())
        private val mapper = ObjectMapper().registerKotlinModule()

        @Throws(Exception::class)
        fun <T> fromJson(value: ByteArray?, clazz: Class<T>?): T {
            return mapper.readValue(value, clazz)
        }

        @Throws(Exception::class)
        fun <T> fromJson(value: String?, clazz: Class<T>?): T {
            return mapper.readValue(value, clazz)
        }

        @Throws(Exception::class)
        fun asJson(value: Any?): String? {
            return mapper.writeValueAsString(value)
        }

        @Throws(Exception::class)
        fun <T> convert(value: JsonNode?, clazz: Class<T>?): T {
            return mapper.treeToValue(value, clazz)
        }

//        @Throws(Exception::class)
//        fun <T> convert(value: JsonNode?, clazz: Class<T>?): T {
//            var rd=mapper.readerFor(clazz)
//            return rd.readValue(value)
//        }

        @Throws(Exception::class)
        fun <T> convert(
            value: Map<String?, Any?>?,
            clazz: Class<T>?
        ): T {
            return mapper.convertValue(value, clazz)
        }
    }
}