package com.newoether.agora.ui.chat.message

import com.newoether.agora.model.TokenUsage
import java.util.Locale

internal data class TokenUsagePresentation(
    val input: Int?,
    val cachedInput: Int?,
    val output: Int?,
    val generationTokensPerSecond: Double? = null,
    val timeToFirstTokenMs: Long? = null,
)

/** Sub-second waits read in milliseconds; anything longer reads in seconds. */
internal fun formatLatency(millis: Long, locale: Locale = Locale.getDefault()): String =
    if (millis < 1_000L) "$millis ms"
    else String.format(locale, "%.1f s", millis / 1_000.0)

internal fun tokenUsagePresentation(
    usage: TokenUsage?,
): TokenUsagePresentation {
    if (usage == null) return TokenUsagePresentation(null, null, null)
    val input = usage.inputTokenCount
        ?: if (
            usage.cachedInputTokenCount != null &&
            usage.uncachedInputTokenCount != null
        ) {
            TokenUsage.addCounts(
                usage.cachedInputTokenCount,
                usage.uncachedInputTokenCount,
            )
        } else {
            usage.outputTokenCount
                ?.let { output -> (usage.totalTokenCount - output).takeIf { it >= 0 } }
        }
    val output = usage.outputTokenCount
        ?: input?.let { inputCount ->
            (usage.totalTokenCount - inputCount).takeIf { it >= 0 }
        }
    return TokenUsagePresentation(
        input = input,
        cachedInput = usage.cachedInputTokenCount,
        output = output,
        generationTokensPerSecond = usage.generationDurationMs?.takeIf { it > 0 }
            ?.let { duration -> output?.takeIf { it > 0 }?.toDouble()?.times(1000.0)?.div(duration) },
        timeToFirstTokenMs = usage.timeToFirstTokenMs?.takeIf { it > 0 },
    )
}
