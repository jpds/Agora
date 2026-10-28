package com.newoether.agora.ui.chat.message

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import com.newoether.agora.R
import com.newoether.agora.model.ChatMessage
import com.newoether.agora.model.Participant
import com.newoether.agora.model.TokenUsage
import com.newoether.agora.ui.motion.AgoraMotionPolicy
import com.newoether.agora.ui.motion.LocalAgoraMotionPolicy
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], application = Application::class, qualifiers = "en-w400dp-h900dp-mdpi")
class AssistantGenerationSpeedTest {
    @get:Rule val compose = createComposeRule()

    private val resources get() = ApplicationProvider.getApplicationContext<Application>().resources

    /** 40 output tokens over 2 s of observed generation. */
    private fun answer() = ChatMessage(
        id = "answer",
        text = "Settled answer",
        participant = Participant.MODEL,
        tokenUsage = TokenUsage(
            totalTokenCount = 140,
            outputTokenCount = 40,
            generationDurationMs = 2_000,
        ),
    )

    private fun render(showGenerationSpeed: Boolean) {
        val message = answer()
        compose.setContent {
            CompositionLocalProvider(
                LocalAgoraMotionPolicy provides AgoraMotionPolicy(reduceMotion = true),
            ) {
                MaterialTheme {
                    Box(Modifier.fillMaxSize()) {
                        MessageItem(
                            message = message,
                            onEdit = { _, _ -> },
                            segmentAppearanceRegistry = remember { SegmentAppearanceRegistry() },
                            userBubbleSizeAnimationReady = false,
                            showGenerationSpeed = showGenerationSpeed,
                        )
                    }
                }
            }
        }
    }

    private val inlineLabel: String
        get() = resources.getString(R.string.token_speed_value, "20.0")

    @Test
    fun `enabled row prints the turn rate beside the actions`() {
        render(showGenerationSpeed = true)
        compose.onNodeWithText(inlineLabel).assertExists()
    }

    @Test
    fun `disabled row keeps the action strip free of the rate`() {
        render(showGenerationSpeed = false)
        compose.onAllNodesWithText(inlineLabel).fetchSemanticsNodes().let {
            assert(it.isEmpty()) { "generation speed must stay hidden while the option is off" }
        }
        compose.onNodeWithText("Settled answer").assertExists()
    }
}
