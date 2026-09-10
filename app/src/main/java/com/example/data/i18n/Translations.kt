package com.example.data.i18n

data class LanguageInfo(
    val code: String,
    val nativeName: String,
    val englishName: String
)

object Translations {
    val languages = listOf(
        LanguageInfo("en", "English", "English"),
        LanguageInfo("as", "অসমীয়া", "Assamese"),
        LanguageInfo("bn", "বাংলা", "Bengali"),
        LanguageInfo("mni", "মৈতৈলোন্", "Manipuri"),
        LanguageInfo("kh", "Khasi", "Khasi"),
        LanguageInfo("miz", "Mizo ṭawng", "Mizo"),
        LanguageInfo("bodo", "बर’", "Bodo")
    )

    fun get(key: String, lang: String = "en"): String {
        val dict = when (lang.lowercase()) {
            "as" -> assamese
            "bn" -> bengali
            "mni" -> manipuri
            "kh" -> khasi
            "miz" -> mizo
            "bodo" -> bodo
            else -> english
        }
        return dict[key] ?: english[key] ?: key
    }

    private val english = mapOf(
        "app_title" to "SmritiNER",
        "app_subtitle" to "Cognitive Care & Dementia Support for NER India",
        "greeting_morning" to "Good Morning",
        "greeting_afternoon" to "Good Afternoon",
        "greeting_evening" to "Good Evening",
        "today_activities" to "Today's Gentle Activities",
        "play" to "PLAY",
        "done" to "DONE ✓",
        "snooze" to "SNOOZE",
        "hear_instructions" to "Hear Instructions",
        "start_activity" to "START ACTIVITY",
        "well_done" to "Wonderful effort! You did great!",
        "try_together" to "Let's try together!",
        "take_rest" to "Take a peaceful rest",
        "memory_match" to "Memory Match",
        "water_reminder" to "Drink Fresh Water",
        "med_reminder" to "Morning Medicine",
        "walk_reminder" to "Gentle Garden Walk",
        "streak_text" to "4-Day Mindful Streak!",
        "smriti_ai" to "Smriti AI Voice Companion",
        "doctor_consult" to "Doctor Consult",
        "caregiver_portal" to "Caregiver Portal",
        "patient_portal" to "Patient Portal",
        "doctor_portal" to "Doctor Portal"
    )

    private val assamese = mapOf(
        "app_title" to "স্মৃতি-এন.ই.আৰ",
        "app_subtitle" to "উত্তৰ-পূব ভাৰতৰ বাবে স্মৃতি আৰু চিন্তা বিকাশ",
        "greeting_morning" to "শুভ প্ৰভাত",
        "greeting_afternoon" to "শুভ অপৰাহ্ন",
        "greeting_evening" to "শুভ সন্ধিয়া",
        "today_activities" to "আজিৰ সহজ কাৰ্যসূচী",
        "play" to "খেলক",
        "done" to "হ’ল ✓",
        "snooze" to "১০ মিনিট পিছত",
        "hear_instructions" to "নিৰ্দেশনা শুনক",
        "start_activity" to "আৰম্ভ কৰক",
        "well_done" to "বৰ সুন্দৰ হৈছে! বহুত ভাল কৰিলে!",
        "try_together" to "আকৌ এবাৰ যত্ন কৰোঁ আহক!",
        "take_rest" to "অলপ জিৰণি লওঁ আহক",
        "memory_match" to "স্মৃতি মিলাওক",
        "water_reminder" to "বিশুদ্ধ পানী খাওক",
        "med_reminder" to "নিয়মিত ঔষধ",
        "walk_reminder" to "পদচালনা বা খোজ কঢ়া",
        "streak_text" to "৪ দিনৰ শান্ত ধাৰাবাহিকতা!",
        "smriti_ai" to "স্মৃতি এআই সংগী",
        "doctor_consult" to "চিকিৎসকৰ সৈতে কথা",
        "caregiver_portal" to "যত্ন লোৱাৰ পৃষ্ঠা",
        "patient_portal" to "ৰোগীৰ পৃষ্ঠা",
        "doctor_portal" to "চিকিৎসকৰ পৃষ্ঠা"
    )

    private val bengali = mapOf(
        "app_title" to "স্মৃতি-এন.ই.আর",
        "app_subtitle" to "উত্তর-পূর্ব ভারতের জন্য স্মৃতি ও যত্ন সহায়তা",
        "greeting_morning" to "সুপ্রভাত",
        "greeting_afternoon" to "শুভ অপরাহ্ন",
        "greeting_evening" to "শুভ সন্ধ্যা",
        "today_activities" to "আজকের সহজ কর্মকাণ্ড",
        "play" to "খেলুন",
        "done" to "হয়েছে ✓",
        "snooze" to "১০ মিনিট পর",
        "hear_instructions" to "নির্দেশ শুনুন",
        "start_activity" to "শুরু করুন",
        "well_done" to "খুব ভালো হয়েছে! দারুণ চেষ্টা!",
        "try_together" to "চলুন আবার চেষ্টা করি!",
        "take_rest" to "একটু বিশ্রাম নিন",
        "memory_match" to "ছবি মিলান",
        "water_reminder" to "জল পান করুন",
        "med_reminder" to "ওষুধের সময়",
        "walk_reminder" to "হাঁটাহাটি",
        "streak_text" to "৪ দিনের ধারাবাহিকতা!",
        "smriti_ai" to "স্মৃতি এআই ভয়েস",
        "doctor_consult" to "ডাক্তারের সাথে বার্তা",
        "caregiver_portal" to "যত্নকারীর পোর্টাল",
        "patient_portal" to "রোগীর পোর্টাল",
        "doctor_portal" to "ডাক্তারের পোর্টাল"
    )

    private val manipuri = mapOf(
        "app_title" to "SmritiNER",
        "app_subtitle" to "মণিপুর অমসুং অৱাং-নোংপোক লমদমগী ৱাখল কাউদনবা",
        "greeting_morning" to "অয়ুক্কী য়াইফ-পাউজেল",
        "greeting_afternoon" to "নুমিৎথাংবা য়াইফ-পাউজেল",
        "greeting_evening" to "নুমিদাংগী য়াইফ-পাউজেল",
        "today_activities" to "ঙসিগী তোইনা পাংথোক্কদবা",
        "play" to "শান্নসি",
        "done" to "লোইরে ✓",
        "snooze" to "১০ মিনিত লেপহৌ",
        "hear_instructions" to "ৱাহৈ তাবিয়ু",
        "start_activity" to "হৌদোকসি",
        "well_done" to "য়াম্না ফরে! ঙসি ফনা পাংথোক্লে!",
        "try_together" to "অমুক্কা হোৎনসি!",
        "take_rest" to "হৌজিক পোথারসি",
        "memory_match" to "ময়েক মান্নবা",
        "water_reminder" to "ঈশিং থকপা",
        "med_reminder" to "হিদাক চাবা",
        "walk_reminder" to "খঙহৌদনা চৎপা",
        "streak_text" to "৪-সুবা নুমিৎকী ফিভম!",
        "smriti_ai" to "স্মৃতি এআই",
        "doctor_consult" to "দাক্তরগা শম্নবা",
        "caregiver_portal" to "নায়গিরিগী পোর্টাল",
        "patient_portal" to "অনাবাগী পোর্টাল",
        "doctor_portal" to "দাক্তরগী পোর্টাল"
    )

    private val khasi = mapOf(
        "app_title" to "SmritiNER",
        "app_subtitle" to "Ka jingkyrshan kynmaw na ka bynta ka Ri-lum Khasi & NER",
        "greeting_morning" to "Khublei Step",
        "greeting_afternoon" to "Khublei Kmie/Kpa",
        "greeting_evening" to "Khublei Janmiet",
        "today_activities" to "Ki Kam ba suk mynta ka sngi",
        "play" to "ÏALEHKAI",
        "done" to "LA DEP ✓",
        "snooze" to "SA 10 MINIT",
        "hear_instructions" to "Sngap Jingbatai",
        "start_activity" to "SDANG NOH",
        "well_done" to "Bha shibun! Phi la leh bha!",
        "try_together" to "Ngin ïaleh biang!",
        "take_rest" to "Shongthait noh khyndiat",
        "memory_match" to "Pynïasnoh Dur",
        "water_reminder" to "Dih Um",
        "med_reminder" to "Dih Dawai",
        "walk_reminder" to "Shang Pyrthei",
        "streak_text" to "4 Sngi Jingkiew!",
        "smriti_ai" to "Smriti AI Sur",
        "doctor_consult" to "Ïakren bad u Doktor",
        "caregiver_portal" to "Portal Nongri",
        "patient_portal" to "Portal Nongpang",
        "doctor_portal" to "Portal Doktor"
    )

    private val mizo = mapOf(
        "app_title" to "SmritiNER",
        "app_subtitle" to "Hriatreuna tichakna platform Mizoram & NER",
        "greeting_morning" to "Chibai Zinglam",
        "greeting_afternoon" to "Chibai Chhunchawl",
        "greeting_evening" to "Chibai Tlai/Zan",
        "today_activities" to "Vawiina thiltih turte",
        "play" to "KHEWL RAWH",
        "done" to "TIHTAWH ✓",
        "snooze" to "MINIT 10 HNUAH",
        "hear_instructions" to "Hrilhfiahna ngaithla rawh",
        "start_activity" to "TAN RAWH",
        "well_done" to "I ti tha hle mai!",
        "try_together" to "Ti leh chhin ang aw!",
        "take_rest" to "Chawl hahdam lawk ang",
        "memory_match" to "Hriat kawp rem",
        "water_reminder" to "Tui in rawh",
        "med_reminder" to "Damdawi ei hun",
        "walk_reminder" to "Vakul hlek rawh",
        "streak_text" to "Ni 4 Hmasawnna!",
        "smriti_ai" to "Smriti AI Aw",
        "doctor_consult" to "Doctor biakna",
        "caregiver_portal" to "Enkawltu Portal",
        "patient_portal" to "Damlotu Portal",
        "doctor_portal" to "Doctor Portal"
    )

    private val bodo = mapOf(
        "app_title" to "SmritiNER",
        "app_subtitle" to "गोसो खांफिननाय आरो बाहागो लानाय",
        "greeting_morning" to "मोजां फुं",
        "greeting_afternoon" to "मोजां सानजौफु",
        "greeting_evening" to "मोजां बेलासे",
        "today_activities" to "दिनैनि गोरलै खामानि",
        "play" to "गेले",
        "done" to "जाबाय ✓",
        "snooze" to "१० मिनिट उनाव",
        "hear_instructions" to "बिथोन खोनासं",
        "start_activity" to "जागाय",
        "well_done" to "जोबोर मोजां! दिनै नों मोजां मावदों!",
        "try_together" to "आरोबाव नाजा दिनि!",
        "take_rest" to "दानिया जिरायथ’नि",
        "memory_match" to "गोसो खांनाय मिलिहोनाय",
        "water_reminder" to "दै लों",
        "med_reminder" to "मुली जानाय सम",
        "walk_reminder" to "दावबायनाय",
        "streak_text" to "४-साननि मोजां फारि!",
        "smriti_ai" to "स्मृति एआई",
        "doctor_consult" to "डाक्टरजों रायज्लायनाय",
        "caregiver_portal" to "नायगिरिनि पोर्टल",
        "patient_portal" to "साग्लोबनि पोर्टल",
        "doctor_portal" to "डाक्टरनि पोर्टल"
    )
}
