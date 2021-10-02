package com.example.loginkitsample

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.example.loginkitsample.ui.theme.LoginKitSampleTheme
import com.example.loginkitsample.ui.theme.SnapbtnColor
import com.snapchat.kit.sdk.SnapLogin
import com.snapchat.kit.sdk.core.controller.LoginStateController.OnLoginStateChangedListener
import com.snapchat.kit.sdk.login.models.UserDataResponse
import com.snapchat.kit.sdk.login.networking.FetchUserDataCallback


val username = mutableStateOf("")
val bitmoji = mutableStateOf("")
val is_logged_in = mutableStateOf(false)

class MainActivity : ComponentActivity() {
    private lateinit var mLoginStateChangedListener: OnLoginStateChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginKitSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen()
                }
            }
        }
        mLoginStateChangedListener = object: OnLoginStateChangedListener{
            override fun onLoginSucceeded() {
                getUserDetails(this@MainActivity)
                is_logged_in.value = SnapLogin.isUserLoggedIn(this@MainActivity)
            }

            override fun onLoginFailed() {
                Toast.makeText(this@MainActivity, "Login Failed", Toast.LENGTH_LONG).show()

            }

            override fun onLogout() {
                Toast.makeText(this@MainActivity, "Logged out!", Toast.LENGTH_LONG).show()
                is_logged_in.value = SnapLogin.isUserLoggedIn(this@MainActivity)
            }

        }
        SnapLogin.getLoginStateController(this).addOnLoginStateChangedListener(mLoginStateChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        SnapLogin.getAuthTokenManager(this).clearToken()
    }
}



@Composable
fun MainScreen() {
    val context = LocalContext.current
    is_logged_in.value = SnapLogin.isUserLoggedIn(context)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(
                if (is_logged_in.value) bitmoji.value else "",
                builder = {
                    transformations(CircleCropTransformation())
                }
            ),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = MaterialTheme.colors.secondary, CircleShape)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = if (is_logged_in.value) username.value else "",
            onValueChange = { username.value = it },
            placeholder = { Text("Display Name", color = Color.LightGray) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if(is_logged_in.value){
                    SnapLogin.getAuthTokenManager(context).clearToken()
                }else {
                    SnapLogin.getAuthTokenManager(context).startTokenGrant()
                }
            },
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(backgroundColor = SnapbtnColor),
            enabled = true

        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_snapchat),
                contentDescription = "snapchat icon",
                modifier = Modifier.size(ButtonDefaults.IconSize),
                tint = Color.White
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            if (is_logged_in.value){
                Text("Log out")
            }else {
                Text("Login with Snapchat")
            }

        }
    }
}


fun getUserDetails(context: Context){
    val query = "{me{bitmoji{avatar},displayName}}"
    val is_logged_in = SnapLogin.isUserLoggedIn(context)
    Log.e("Is user logged in", "$is_logged_in")
    SnapLogin.fetchUserData(context, query, null, object : FetchUserDataCallback{
        override fun onSuccess(userDataResponse: UserDataResponse?) {
            if (userDataResponse != null) {
                val data = userDataResponse.data.me
                if (data != null) {
                    username.value = data.displayName
                    bitmoji.value  = data.bitmojiData.avatar
                    Log.e("data", "Name: ${data.displayName}, avatar: ${data.bitmojiData.avatar}")
                }
            }
        }

        override fun onFailure(isNetworkError: Boolean, statusCode: Int) {
            Log.e("Snapkit Failure", "network error: $isNetworkError, status code: $statusCode")
        }


    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LoginKitSampleTheme {
        MainScreen()
    }
}
