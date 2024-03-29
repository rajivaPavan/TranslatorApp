package com.example.translatorapp

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.translatorapp.states.Keyboard
import com.example.translatorapp.states.keyboardAsState
import com.example.translatorapp.ui.theme.BackButton
import com.example.translatorapp.ui.theme.ClearButton
import com.example.translatorapp.ui.theme.CopyButton
import org.w3c.dom.Text


enum class TranslationState{
    NotStarted,
    Ongoing,
    Complete,
    Cancelled
}

class TranslationScreenViewModel() : ViewModel(){
    private var _textToTranslate : MutableLiveData<String> = MutableLiveData()
    val textToTranslate: LiveData<String> = _textToTranslate

    private var _translatedText : MutableLiveData<String> = MutableLiveData()
    val translatedText: LiveData<String> = _translatedText

    private var _currentTranslationState : MutableLiveData<TranslationState>
        = MutableLiveData(TranslationState.NotStarted)

    private var _prevTranslationState : MutableLiveData<TranslationState>
            = MutableLiveData(TranslationState.NotStarted)

    private var _focusRequester : MutableLiveData<FocusRequester> = MutableLiveData()
    val focusRequester: LiveData<FocusRequester> = _focusRequester

    private var _fontSize : MutableLiveData<TextUnit> = MutableLiveData(34.sp)
    val fontSize: LiveData<TextUnit> = _fontSize

    private var _textToTranslateLength = 0;
    private var _numberOfLines = 1;

    private val size1 = 34.sp
    private val size2 = 30.sp
    private val size3 = 26.sp

    fun setFontSize(numberOfLines: Int) {
        val size = when (numberOfLines) {
            1 -> size1
            2 -> size2
            else -> size3
        }
        val isTextLengthIncreasing = _textToTranslate.value!!.length > _textToTranslateLength
        val isTextLengthDecreasing = _textToTranslate.value!!.length < _textToTranslateLength
        val sizeIncreasing = size > _fontSize.value!!
        val sizeDecreasing = size < _fontSize.value!!
        val isUpdate = (isTextLengthIncreasing && sizeDecreasing
                || isTextLengthDecreasing && sizeIncreasing)
        if(isUpdate)
        {
            _numberOfLines = numberOfLines
            _fontSize.value = size
        }
        _textToTranslateLength = _textToTranslate.value!!.length
    }

    fun setTextToTranslate(text:String){
        _textToTranslate.value = text
    }

    fun updateTranslationState(state: TranslationState) {
        if(state != _currentTranslationState.value){
            if(state == TranslationState.Complete || state == TranslationState.Cancelled){
                _prevTranslationState.value = state
                _currentTranslationState.value = TranslationState.NotStarted
            }
            else{
                _prevTranslationState.value = _currentTranslationState.value
                _currentTranslationState.value = state
            }
        }
    }
    fun checkCurrentTranslationState(state: TranslationState) : Boolean {
        return  _currentTranslationState.value == state
    }
    fun checkPrevTranslationState(state: TranslationState) : Boolean {
        return  _prevTranslationState.value == state
    }

    fun translation(textToTranslate: String,targetLanguage: TargetLanguage){
        _translatedText.value = Translator.translate(textToTranslate,targetLanguage)
    }
}


@Composable
fun TranslationScreen(
    onBackClick: () -> Unit,
    targetLanguage: TargetLanguage,
    screenViewModel: TranslationScreenViewModel = viewModel()
){
    val textToTranslate by screenViewModel.textToTranslate.observeAsState("")
    val focusRequester by screenViewModel.focusRequester.observeAsState(FocusRequester())
    val translatedText by screenViewModel.translatedText.observeAsState("")
    val fontSize by screenViewModel.fontSize.observeAsState(34.sp)

    //run this process to get the translation real time
    screenViewModel.translation(textToTranslate,targetLanguage)

    TranslationScreenContent(
        onBackClick = onBackClick,
        targetLanguage = targetLanguage,
        textToTranslate = textToTranslate,
        translatedText = translatedText,
        focusRequester = focusRequester,
        setTextToTranslate = { screenViewModel.setTextToTranslate(it) },
        setTranslationState = { screenViewModel.updateTranslationState(it)},
        checkTranslationState = { screenViewModel.checkCurrentTranslationState(it) },
        checkPrevTranslationState = { screenViewModel.checkPrevTranslationState(it) },
        translationTextFontSize = fontSize,
        setFontSize = { screenViewModel.setFontSize(it) }
    )
}

@Composable
fun TranslationScreenContent(
    onBackClick: () -> Unit,
    targetLanguage: TargetLanguage,
    textToTranslate: String,
    focusRequester: FocusRequester,
    setTextToTranslate: (String) -> Unit,
    setTranslationState: (TranslationState) -> Unit,
    checkTranslationState: (TranslationState) -> Boolean,
    checkPrevTranslationState: (TranslationState) -> Boolean,
    translatedText: String,
    translationTextFontSize: TextUnit,
    setFontSize: (Int)->Unit
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
            val focusManager = LocalFocusManager.current
            var isFocusedOnTextField by rememberSaveable { mutableStateOf(false)}
            var maxLines by remember { mutableStateOf(7) }

            if(keyboardState == Keyboard.Closed) {
                if (checkTranslationState(TranslationState.Ongoing)) {
                    //if text is empty when user has closed the keyboard
                    if (textToTranslate.isEmpty()) {
                        setTranslationState(TranslationState.Cancelled)
                    }
                    else if (checkPrevTranslationState(TranslationState.NotStarted)) {
                        // if text is not empty when user has closed keyboard
                        setTranslationState(TranslationState.Complete)
                        focusManager.clearFocus()
                    }
                }
            }
            else {
                // if text is not empty and translation has not started,
                // or if this is first time keyboard has open
                //      hence both translation states is NotStarted
                if(checkTranslationState(TranslationState.NotStarted)
                    && (textToTranslate.isNotEmpty()
                    || checkPrevTranslationState(TranslationState.NotStarted))
                ){
                    setTranslationState(TranslationState.Ongoing)
                }
            }

            if(checkPrevTranslationState(TranslationState.Cancelled)){
                onBackClick()
            }

            TranslationHeader(
                onBackClick = onBackClick,
                showClearButton = { textToTranslate.isNotEmpty()
                        && isFocusedOnTextField },
                onClearClick = { setTextToTranslate("") }
            )

            val translationTextModifier: Modifier = Modifier.padding(8.dp)
            val translationTextStyle = MaterialTheme.typography.h4

            Row{
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    val languageNameModifier = Modifier.padding(8.dp)
                    val languageNameTextStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                    if(checkPrevTranslationState(TranslationState.Complete)){
                        Row(modifier = languageNameModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = "English",
                                modifier = languageNameModifier,
                                style = languageNameTextStyle
                            )
                            val clipboardManager: ClipboardManager = LocalClipboardManager.current
                            CopyButton(onClick = {
                                clipboardManager.setText(AnnotatedString(textToTranslate))
                            })
                        }
                    }
                    val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
                    val textLayoutResult = textLayoutResultState.value

                    Row{

                        TextField(
                            value = textToTranslate,
                            onValueChange = {
                                setTextToTranslate(it)
                                setFontSize(textLayoutResult?.lineCount!!)
                            },
                            maxLines = maxLines,
                            placeholder = {
                                Text(
                                    text = "Enter text",
                                    style = MaterialTheme.typography.h4,
                                )
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    isFocusedOnTextField = it.isFocused
                                    maxLines = if (it.isFocused) 8 else Int.MAX_VALUE
                                },
                            textStyle = TextStyle(fontSize = translationTextFontSize),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colors.onSecondary,
                                backgroundColor = MaterialTheme.colors.secondary,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                imeAction = ImeAction.Go
                            ),
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
                    Row(modifier = Modifier.height(
                        if (translatedText.isNotEmpty() && isFocusedOnTextField)
                            20.dp
                        else
                            40.dp
                        ).animateContentSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        val lineColor = MaterialTheme.colors.primary
                        Column(modifier = Modifier.fillMaxWidth().height(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Canvas(modifier = Modifier.fillMaxHeight()
                                .fillMaxWidth(
                                    if(translatedText.isEmpty())
                                        0f
                                    else if (translatedText.isNotEmpty() && isFocusedOnTextField)
                                        0.2f
                                    else
                                        0.3f
                                ).animateContentSize()
                            ) {
                                drawRect(
                                    color = lineColor
                                )
                            }
                        }
                    }
                    if(checkPrevTranslationState(TranslationState.Complete)){
                        Row(modifier = languageNameModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = targetLanguage.name,
                                modifier = languageNameModifier,
                                style = languageNameTextStyle
                            )
                            val clipboardManager: ClipboardManager = LocalClipboardManager.current
                            CopyButton(onClick = {
                                clipboardManager.setText(AnnotatedString(translatedText))
                            })
                        }
                    }
                    Row{
                        TranslatedText(
                            modifier = translationTextModifier,
                            style = translationTextStyle,
                            translatedText = translatedText,
                            fontSize = translationTextFontSize,
                            setTextLayoutResult = { layoutResult ->
                                textLayoutResultState.value = layoutResult
                            }
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
    translatedText: String,
    fontSize: TextUnit,
    setTextLayoutResult: (TextLayoutResult)->Unit
) {

    SelectionContainer {
        Text(
            text = translatedText,
            modifier = modifier.padding(8.dp),
            style = style,
            fontSize = fontSize,
            onTextLayout = setTextLayoutResult
        )
    }
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