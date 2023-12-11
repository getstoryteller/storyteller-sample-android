package com.getstoryteller.storytellersampleapp.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.getstoryteller.storytellersampleapp.R
import com.getstoryteller.storytellersampleapp.features.main.MainViewModel
import com.getstoryteller.storytellersampleapp.ui.LocalStorytellerColorsPalette

@Composable
fun LoginDialog(
  viewModel: MainViewModel
) {
  Dialog(
    onDismissRequest = {},
    properties = DialogProperties(
      dismissOnBackPress = false,
      dismissOnClickOutside = false,
      usePlatformDefaultWidth = false
    )
  ) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(start = 10.dp, end = 10.dp),
      shape = RoundedCornerShape(8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        var text by rememberSaveable { mutableStateOf("") }
        Image(
          modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
          painter = painterResource(id = if (isDarkTheme) R.drawable.ic_logo_dark else R.drawable.ic_logo),
          contentDescription = "Logo of Storyteller"
        )
        Text(
          modifier = Modifier
            .padding(top = 16.dp)
            .align(alignment = Alignment.CenterHorizontally),
          text = stringResource(id = R.string.label_login_description)
        )
        val loginError = viewModel.loginError.collectAsState()
        val loginProgress = viewModel.loginProgress.collectAsState()

        OutlinedTextField(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
          value = text,
          onValueChange = { text = it },
          isError = loginError.value != null,
          placeholder = { Text(text = stringResource(id = R.string.label_login_enter_code)) },
          colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = LocalStorytellerColorsPalette.current.background),
          leadingIcon = {
            Image(
              painter = painterResource(id = R.drawable.ic_key),
              contentDescription = ""
            )
          }
        )

        loginError.value?.let {
          val annotatedText = buildAnnotatedString {
            appendInlineContent("leadingIcon", "[icon]")
            append(" $it")
          }
          val inlineContent = mapOf(
            Pair(
              "leadingIcon",
              InlineTextContent(
                Placeholder(
                  width = 12.sp,
                  height = 12.sp,
                  placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                )
              ) {
                Icon(painterResource(id = R.drawable.ic_error), "", tint = MaterialTheme.colors.error)
              }
            )
          )
          Text(
            text = annotatedText,
            color = MaterialTheme.colors.error,
            modifier = Modifier.padding(top = 8.dp),
            inlineContent = inlineContent,
          )
        }

        Button(modifier = Modifier
          .height(48.dp)
          .padding(top = 8.dp)
          .fillMaxWidth(),
          enabled = !loginProgress.value,
          onClick = {
            viewModel.verifyCode(text)
          }) {
          if (loginProgress.value) {
            CircularProgressIndicator(
              modifier = Modifier
                .height(24.dp)
                .width(24.dp)
            )
          } else {
            Text(text = stringResource(id = R.string.action_login_verify))
          }
        }

      }
    }
  }
}
