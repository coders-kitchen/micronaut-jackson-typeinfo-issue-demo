package com.demo

import io.micronaut.runtime.Micronaut.*
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("com.demo")
        .start()
}


@Controller("/suppliers")
class SupplierController() {

    @Post("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun addSupplier(supplier: StandardSupplier) = supplier

    @Put("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun addSupplier2(@Body supplier: WorkingSupplier) = supplier
}

@JsonTypeInfo(
    property = "type",
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = StandardSupplier::class,
)
@JsonSubTypes(value = [
    JsonSubTypes.Type(value = StandardSupplier::class, name = "standard"),
    JsonSubTypes.Type(value = BusinessSupplier::class, name = "business"),
])
@Introspected
open class SupplierI (
    open val id: Int?,
    open val type: String
)

@Introspected
data class WorkingSupplier  (
    val id: Int? = null,
    val type: String = "standard",
    val name: String,
    val website: String,
    val comment: String = "",
)


data class StandardSupplier  (
    override val id: Int? = null,
    override val type: String = "standard",
    val name: String,
    val website: String,
    val comment: String = "",
): SupplierI(id, type)


data class BusinessSupplier(
    override val id: Int? = null,
    override val type: String = "business",
    val discount: Double = 0.01,
    val name: String,
    val website: String,
    val comment: String = ""
) : SupplierI(id, type)

fun main() {
    ObjectMapper().findAndRegisterModules().readValue<SupplierI>("""
        {
          "name": "Peter",
          "type": "business",
          "website": "test.de",
          "comment": "jo"
        }
    """.trimIndent(), SupplierI::class.java).also { println(it::class) }
}