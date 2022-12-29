import mu.KotlinLogging

fun main(args: Array<String>) {

    val logger = KotlinLogging.logger {}

    logger.info { "Starting up Adjustments" }

    println("Program arguments: ${args.joinToString()}")
}