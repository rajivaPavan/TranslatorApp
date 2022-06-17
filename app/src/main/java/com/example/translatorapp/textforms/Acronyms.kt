package com.example.translatorapp.textforms

import java.io.File

private fun getAcronymsFromFile(): Map<String, String> {
    val acronyms = mutableMapOf<String,String>()
    acronymsText.split("\n").forEach { line ->
        val acronymInfo = line.trim(' ').split(" - ")
        val acronym = acronymInfo[0]
        val phrase = acronymInfo[1]
        acronyms[acronym.lowercase()] = phrase.trimEnd('\n').lowercase()
    }
    return acronyms
}

private val acronyms:Map<String,String> = getAcronymsFromFile()

fun replaceWordIfAcronym(word:String):String{
    val phrase = acronyms.get(word.lowercase())
    return if(!phrase.isNullOrEmpty()){
        val words = phrase.split(" ")
        word +" "+ words.subList(1, words.size).joinToString(" ")
    }else{
        word
    }
}

const val acronymsText = """ASAP - As Soon As Possible
    BAE - Before Anyone Else
    BOLO - Be On the LookOut
    FISH - First In, Still Here
    FOMO - Fear Of Missing Out
    GIF - Graphics Interchange Format
    LOL - Laughing Out Loud
    YOLO - You Only Live Once
    AFK - Away From Keyboard
    BBL - Be Back Later
    BBS - Be Back Soon
    BEG - Big Evil Grin
    BRB - Be Right Back
    BTW - By The Way
    EG - Evil Grin
    IDK - I Don't Know
    IMO - In My Opinion
    IRL - In Real Life
    LMK - Let Me Know
    NOYB - None of Your Business
    OMG - Oh My God
    POS - Parents Over Shoulder
    ROFL - Rolling On the Floor Laughing
    SMH - Shaking My Head
    TTYL - Talk To You Later
    WTH - What The Hell
    CIA - Central Intelligence Agency
    CPS - Child Protective Services
    CSI - Crime Scene Investigation
    DMV - Division of Motor Vehicles
    DNC - Democratic National Committee
    DOD - Department of Defense
    DON - Department of the Navy
    DZ - Drop Zone
    FBI - Federal Bureau of Investigation
    GIB - GI Bill
    MIA - Missing In Action
    POW - Prisoner Of War
    RNC - Republican National Committee
    UN - United Nations
    USAF - United States Air Force
    ABS - Anti-lock Braking System
    ADD - Attention Deficit Disorder
    ADHD - Attention Deficit Hyperactivity Disorder
    AMA - Against Medical Advice
    CDC - Centers for Disease Control and Prevention
    DOA - Dead On Arrival
    DOB - Date Of Birth
    DIY - Do It Yourself
    ESL - English As A Second Language
    FAQ - Frequently Asked Questions
    HIV - Human Immunodeficiency Virus
    IQ - Intelligence Quotient
    MD - Medical Doctor
    OTC - Over The Counter
    PPV - Pay Per View
    PS - Post Script
    SUV - Sports Utility Vehicle
    UFO - Unidentified Flying Object
    ADP - Automated Data Processing
    AKA - Also Known As
    CDT - Central Daylight Time
    CST - Central Standard Time
    DBA - Doing Business As
    DND - Do Not Disturb
    EDS - Electronic Data Systems
    EOD - End of Day
    EOW - End of Week
    EDT - Eastern Daylight Time
    EST - Eastern Standard Time
    ETA - Estimated Time of Arrival
    FYI - For Your Information
    MBA - Masters of Business Administration
    MDT - Mountain Daylight Time
    MST - Mountain Standard Time
    POS - Point Of Service
    PDT - Pacific Daylight Time
    PST - Pacific Standard Time
    TBA - To Be Announced
    TBD - To Be Determined
    FML - Fuck My Life
    G2G - Got to Go"""

