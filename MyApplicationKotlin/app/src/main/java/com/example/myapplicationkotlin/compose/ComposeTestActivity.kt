package com.example.myapplicationkotlin.compose

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplicationkotlin.R

/**
 * Author: Wanshenpeng
 * Date: 2023/5/29 16:04
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * Wanshenpeng 2023/5/29 1.0 首次创建
 */
class ComposeTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
//                MessageCard(Message("a: ", "12345"))
                Conversation(message = conversationSample)
            }
        }
    }

    @Composable
    fun MessageCard(msg: Message) {
        Row(modifier = Modifier.padding(all = 8.dp)) {
            Image(
                painter = painterResource(id = R.drawable.test),
                contentDescription = "Contact profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            var isExpanded by remember { mutableStateOf(false) }
            Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp) {
                    Text(
                        text = msg.body,
                        modifier = Modifier.padding(all = 4.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = if (isExpanded) Int.MAX_VALUE else 1
                    )
                }
            }
        }
    }

    data class Message(val author: String, val body: String)

    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun PreviewMessageCard() {
        Surface {
            MessageCard(msg = Message("a", "123"))
        }
    }

    @Composable
    fun Conversation(message: List<Message>) {
        LazyColumn {
            items(message) {
                MessageCard(msg = it)
            }
        }
    }

    @Preview
    @Composable
    fun PreviewConversation() {
        Conversation(message = conversationSample)
    }


    private val conversationSample = listOf(
        ComposeTestActivity.Message(
            "Colleague",
            "Test...Test...Test..."
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "List of Android versions:\n" +
                    "Android KitKat (API 19)\n" +
                    "Android Lollipop (API 21)\n" +
                    "Android Marshmallow (API 23)\n" +
                    "Android Nougat (API 24)\n" +
                    "Android Oreo (API 26)\n" +
                    "Android Pie (API 28)\n" +
                    "Android 10 (API 29)\n" +
                    "Android 11 (API 30)\n" +
                    "Android 12 (API 31)\n"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "I think Kotlin is my favorite programming language.\n" +
                    "It's so much fun!"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Searching for alternatives to XML layouts..."
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Hey, take a look at Jetpack Compose, it's great!\n" +
                    "It's the Android's modern toolkit for building native UI." +
                    "It simplifies and accelerates UI development on Android." +
                    "Less code, powerful tools, and intuitive Kotlin APIs :)"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "It's available from API 21+ :)"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Writing Kotlin for UI seems so natural, Compose where have you been all my life?"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Android Studio next version's name is Arctic Fox"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Android Studio Arctic Fox tooling for Compose is top notch ^_^"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "I didn't know you can now run the emulator directly from Android Studio"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Compose Previews are great to check quickly how a composable layout looks like"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Previews are also interactive after enabling the experimental setting"
        ),
        ComposeTestActivity.Message(
            "Colleague",
            "Have you tried writing build.gradle with KTS?"
        ),
    )
}



