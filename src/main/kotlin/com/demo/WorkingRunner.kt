package com.demo

import com.fasterxml.jackson.databind.ObjectMapper

fun main() {
    ObjectMapper().findAndRegisterModules().readValue("""
        {
          "name": "Peter",
          "type": "business",
          "website": "test.de",
          "comment": "jo"
        }
    """.trimIndent(), SupplierI::class.java).also { println(it::class) }
}