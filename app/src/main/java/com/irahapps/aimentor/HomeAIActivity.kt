package com.irahapps.aimentor

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.irahapps.aimentor.ui.theme.IrahAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class HomeAIActivity : ComponentActivity() {

    private val uriState = MutableStateFlow("")

    private val imagePicker =
        registerForActivityResult<PickVisualMediaRequest, Uri>(
            ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                uriState.update { uri.toString() }
            }
        }
    @SuppressLint("Range", "UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IrahAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                ) {

                    Scaffold(
                        topBar = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFF4F4FDD),
                                                Color(0xFF29298D)
                                            )
                                        )
                                    )
                                    .height(100.dp)
                                    .padding(horizontal = 16.dp)
                            ) {

                                Image(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .fillMaxWidth()
                                        .padding(15.dp)
                                        .size(180.dp),
                                    painter = painterResource(id = R.drawable.icon_hori),
                                    contentDescription = "App Logo"
                                )
                            }
                        }

                    ) {
                        ChatScreen()
                    }

                }
            }
        }
    }




    @SuppressLint("Range")
    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun ChatScreen() {
        val chaViewModel = viewModel<GeminiChatViewModel>()
        val chatState = chaViewModel.chatState.collectAsState().value
        val bitmap = getBitmap()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 100.dp,
                    bottom = 0.dp
                ),
            verticalArrangement = Arrangement.Bottom
        ) {

            /*Image(
                modifier = Modifier
                    .layoutId("image_top")
                    .fillMaxWidth()
                    .alpha(0.8f)
                    //.blur(5.dp, BlurredEdgeTreatment.Unbounded)
                    .align(Alignment.CenterHorizontally)
                    .size(250.dp),
                painter = painterResource(id = R.drawable.ai_b),
                contentDescription = "AI image",
            )

            Text(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                text = "Hi, ".plus("\nHave a nice day..."),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )*/


            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                reverseLayout = true,
            ) {
                itemsIndexed(chatState.geminiChatList) { index, chat ->
                    if (chat.isFromUser) {
                        UserChatItem(prompt = chat.prompt, bitmap = chat.bitmap)
                    } else {
                        ModelChatItem(response = chat.prompt)
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clipToBounds()
                    .clip(RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp))
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(top = 16.dp, bottom = 16.dp, start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    bitmap?.let {
                        Image(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(bottom = 2.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentDescription = "picked image",
                            contentScale = ContentScale.Crop,
                            bitmap = it.asImageBitmap()
                        )
                    }

                    Icon(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(2.dp)
                            .clickable {
                                imagePicker.launch(
                                    PickVisualMediaRequest
                                        .Builder()
                                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        .build()
                                )
                            },
                        imageVector = Icons.Rounded.AttachFile,
                        contentDescription = "Add Photo",
                        tint = Color(0xFF4D88D1)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(22.dp)),
                    value = chatState.prompt,
                    onValueChange = {
                        chaViewModel.onEvent(GeminiChatUiEvent.UpdatePrompt(it))
                    },
                    placeholder = {
                        Text(text = "Enter a prompt here")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent, // Hide the line when focused
                        unfocusedIndicatorColor = Color.Transparent // Hide the line when unfocused
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(2.dp)
                        .clickable {
                            if (chatState.prompt.isNotEmpty()) {
                                chaViewModel.onEvent(
                                    GeminiChatUiEvent.SendPrompt(
                                        chatState.prompt,
                                        bitmap
                                    )
                                )
                                uriState.update { "" }
                            }
                        },
                    imageVector = Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Send prompt",
                    tint = Color(0xFF4D88D1)
                )


            }

        }

    }


    @Composable
    fun UserChatItem(prompt: String, bitmap: Bitmap?) {
        Column(
            modifier = Modifier
                .padding(start = 100.dp, bottom = 16.dp)
        ) {

            bitmap?.let {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    bitmap = it.asImageBitmap()
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF4F4FDD),
                                Color(0xFF7272C2)
                            )
                        )
                    )
                    .padding(16.dp),
                text = prompt,
                fontSize = 17.sp,
                color = Color(0xFFFFFFFF)
            )

        }
    }

    @Composable
    fun ModelChatItem(response: String) {
        Column(
            modifier = Modifier.padding(end = 100.dp, bottom = 16.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFEAEBF3),
                                Color(0xFFEDF0F5)
                            )
                        )
                    )
                    .padding(16.dp),
                text = response,
                fontSize = 17.sp,
                color = Color(0xFF000000)
            )

        }
    }

    @Composable
    private fun getBitmap(): Bitmap? {
        val uri = uriState.collectAsState().value
        val imageState: AsyncImagePainter.State = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .size(Size.ORIGINAL)
                .build()
        ).state
        if (imageState is AsyncImagePainter.State.Success) {
            return imageState.result.drawable.toBitmap()
        }
        return null
    }






}


















