package com.example.translatorapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.translatorapp.custom.Keyboard
import com.example.translatorapp.custom.keyboardAsState
import com.example.translatorapp.ui.theme.BackButton
import com.example.translatorapp.ui.theme.ClearButton


enum class TranslationState{
    NotStarted,
    Ongoing,
    Complete,
    Cancelled
}

class TranslationScreenViewModel() : ViewModel(){
    private var _textToTranslate : MutableLiveData<String> = MutableLiveData()
    val textToTranslate: LiveData<String> = _textToTranslate

    private var _translationState : MutableLiveData<TranslationState> = MutableLiveData()
    val translationState: LiveData<TranslationState> = _translationState

    private var _focusRequester : MutableLiveData<FocusRequester> = MutableLiveData()
    val focusRequester: LiveData<FocusRequester> = _focusRequester

    fun setTextToTranslate(text:String){
        _textToTranslate.value = text
    }

    fun setTranslationState(state: TranslationState) {
        _translationState.value = state
    }

    fun checkTranslationState(state: TranslationState) : Boolean {
        return  _translationState.value == state
    }
}

@Composable
fun TranslationScreen(
    onBackClick: () -> Unit,
    targetLanguage: TargetLanguage,
    screenViewModel: TranslationScreenViewModel = viewModel()
){

    val textToTranslate by screenViewModel.textToTranslate.observeAsState("")
    val translationState by
        screenViewModel.translationState.observeAsState(TranslationState.NotStarted)
    val focusRequester by screenViewModel.focusRequester.observeAsState(FocusRequester())

    TranslationScreenContent(
        onBackClick = onBackClick,
        targetLanguage = targetLanguage,
        textToTranslate = textToTranslate,
        translationState = translationState,
        focusRequester = focusRequester,
        setTextToTranslate = { screenViewModel.setTextToTranslate(it) },
        setTranslationState = { screenViewModel.setTranslationState(it)},
        checkTranslationState = { screenViewModel.checkTranslationState(it) }
    )
}

@Composable
fun TranslationScreenContent(
    onBackClick: () -> Unit,
    targetLanguage: TargetLanguage,
    textToTranslate: String,
    translationState: TranslationState,
    focusRequester: FocusRequester,
    setTextToTranslate: (String) -> Unit,
    setTranslationState: (TranslationState) -> Unit,
    checkTranslationState: (TranslationState) -> Boolean
){
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.onSecondary) {
        Column(modifier = Modifier
            .background(color = MaterialTheme.colors.secondary)
            .fillMaxSize()
        ){
            val keyboardState by keyboardAsState()
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            if(keyboardState == Keyboard.Closed){
                if(checkTranslationState(TranslationState.Ongoing)
                    && textToTranslate.isEmpty()
                ){
                    setTranslationState(TranslationState.Cancelled)
                }
                else if(checkTranslationState(TranslationState.Ongoing)
                    && textToTranslate.isNotEmpty()){

                    setTranslationState(TranslationState.Complete)
                }
            }else{
                if(!checkTranslationState(TranslationState.Complete)){
                    setTranslationState(TranslationState.Ongoing)
                }
            }
            if(checkTranslationState(TranslationState.Cancelled)){
                setTranslationState(TranslationState.NotStarted)
                onBackClick()
            }

            TranslationHeader(
                onBackClick = onBackClick,
                showClearButton = { textToTranslate.isNotEmpty()
                        && translationState == TranslationState.Ongoing },
                onClearClick = { setTextToTranslate("") }
            )
            val languageTextModifier: Modifier = Modifier.padding(8.dp)
            val languageTextStyle = MaterialTheme.typography.h4

            Row{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    if(checkTranslationState(TranslationState.Complete)){
                        Row{
                            Text(
                                text = "English",
                                modifier = Modifier.padding(4.dp),
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                    Row{
                        val focusManager = LocalFocusManager.current

                        TextField(
                            value = textToTranslate,
                            onValueChange = {
                                setTranslationState(TranslationState.Ongoing)
                                setTextToTranslate(it)
                            },
                            placeholder = {
                                Text(
                                    text = "Enter text",
                                    style = MaterialTheme.typography.h4
                                )
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .focusRequester(focusRequester)
                                .onFocusEvent {
                                    if(checkTranslationState(TranslationState.Complete)){
                                        focusManager.clearFocus()
                                    }
                                }

                            ,
                            textStyle = MaterialTheme.typography.h4,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.onSecondary,
                                backgroundColor = MaterialTheme.colors.secondary,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                            keyboardActions = KeyboardActions(
                                onGo = {
                                    if(textToTranslate.isNotEmpty()){
                                        setTranslationState(TranslationState.Complete)
                                        focusManager.clearFocus()
                                    }
                                }
                            )
                        )
                    }
                    if(checkTranslationState(TranslationState.Complete)){
                        Row{
                            Text(
                                text = targetLanguage.name,
                                modifier = Modifier.padding(4.dp),
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                    Row{
                        TranslatedText(
                            textToTranslate = textToTranslate,
                            modifier = languageTextModifier,
                            style = languageTextStyle
                        )
                    }

                }
            }

        }
    }

}
@Composable
fun TranslatedText(
    modifier: Modifier,
    style: TextStyle,
    textToTranslate: String
) {
    var translatedText by remember { mutableStateOf(textToTranslate) }
    translatedText = Translator.translate(textToTranslate = textToTranslate,
                            targetLanguage = TargetLanguage(0, "reversed")
   )
    Text(
        text = translatedText,
        modifier = modifier,
        style = style
    )
}

@Composable
fun TranslationHeader(
    onBackClick: () -> Unit,
    showClearButton: ()-> Boolean,
    onClearClick: () -> Unit
){
    Row(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        BackButton(onClick = onBackClick)
        if(showClearButton()) {
            ClearButton(onClick = onClearClick)
        }
    }
}