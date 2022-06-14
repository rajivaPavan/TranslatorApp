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
import com.example.translatorapp.states.Keyboard
import com.example.translatorapp.states.keyboardAsState
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

    private var _currentTranslationState : MutableLiveData<TranslationState> = MutableLiveData()
    val currentTranslationState: LiveData<TranslationState> = _currentTranslationState

    private var _prevTranslationState : MutableLiveData<TranslationState>
        = MutableLiveData(TranslationState.NotStarted)
    val prevTranslationState: LiveData<TranslationState> = _prevTranslationState

    private var _focusRequester : MutableLiveData<FocusRequester> = MutableLiveData()
    val focusRequester: LiveData<FocusRequester> = _focusRequester

    fun setTextToTranslate(text:String){
        _textToTranslate.value = text
    }

    fun updateTranslationState(state: TranslationState) {
        _prevTranslationState.value = _currentTranslationState.value
        _currentTranslationState.value = state
    }

    fun checkCurrentTranslationState(state: TranslationState) : Boolean {
        return  _currentTranslationState.value == state
    }
    fun checkPrevTranslationState(state: TranslationState) : Boolean {
        return  _prevTranslationState.value == state
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
        screenViewModel.currentTranslationState.observeAsState(TranslationState.NotStarted)
    val focusRequester by screenViewModel.focusRequester.observeAsState(FocusRequester())

    TranslationScreenContent(
        onBackClick = onBackClick,
        targetLanguage = targetLanguage,
        textToTranslate = textToTranslate,
        translationState = translationState,
        focusRequester = focusRequester,
        setTextToTranslate = { screenViewModel.setTextToTranslate(it) },
        setTranslationState = { screenViewModel.updateTranslationState(it)},
        checkTranslationState = { screenViewModel.checkCurrentTranslationState(it) }
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
            LaunchedEffect(Unit) {
                //open keyboard when screen loads
                focusRequester.requestFocus()
            }
            val keyboardState by keyboardAsState()

            //when keyboard is closed
            if(keyboardState == Keyboard.Closed){
                //
                if(checkTranslationState(TranslationState.Ongoing)){
                    //if text is empty when user has closed the keyboard
                    if(textToTranslate.isEmpty()){
                        setTranslationState(TranslationState.Cancelled)
                    }
                    else{
                        // if text is not empty when user has closed keyboard
                        setTranslationState(TranslationState.Complete)
                    }
                }
            }
            else{ //when keyboard is opened

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
                        && checkTranslationState(TranslationState.Ongoing) },
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
                            modifier = languageTextModifier,
                            style = languageTextStyle,
                            textToTranslate = textToTranslate,
                            targetLanguage = targetLanguage
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
    textToTranslate: String,
    targetLanguage: TargetLanguage
) {
    var translatedText by remember { mutableStateOf(textToTranslate) }
    translatedText = Translator.translate(
        textToTranslate = textToTranslate,
        targetLanguage = targetLanguage
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