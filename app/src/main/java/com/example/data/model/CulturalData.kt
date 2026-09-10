package com.example.data.model

data class CulturalItem(
    val id: String,
    val label: String,
    val symbol: String,
    val nerContext: String,
    val category: String
)

data class RoutineItem(
    val id: String,
    val order: Int,
    val label: String,
    val symbol: String,
    val time: String
)

data class QuestionOption(
    val text: String,
    val isCorrect: Boolean
)

data class StoryQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int
)

data class FolkStory(
    val id: String,
    val title: String,
    val text: String,
    val questions: List<StoryQuestion>
)

data class HelperPerson(
    val id: String,
    val name: String,
    val role: String,
    val avatar: String,
    val cue: String
)

object CulturalData {
    val items = listOf(
        CulturalItem("gamosa", "Gamosa (অসমীয়া গামোচা)", "🧣", "Assamese handwoven towel of respect", "attire"),
        CulturalItem("japi", "Jaapi (জাপি)", "👒", "Traditional conical woven sun hat of Assam", "household"),
        CulturalItem("dhol", "Bihu Dhol (ঢোল)", "🥁", "Folk drum played during Bihu celebrations", "music"),
        CulturalItem("pepa", "Pepa (পেঁপা)", "🎺", "Buffalo horn pipe used in festive music", "music"),
        CulturalItem("tea", "Assam Chai (চাহ)", "🍵", "Fragrant golden orthodox Assam tea", "food"),
        CulturalItem("bamboo_mug", "Bamboo Cup (বাঁহৰ বাতি)", "🎋", "Traditional artisan bamboo drinking cup", "household"),
        CulturalItem("hornbill", "Hornbill (ধনেশ চৰাই)", "🦅", "Venerated majestic bird celebrated in Nagaland", "nature"),
        CulturalItem("cheraw", "Bamboo Stems (Cheraw)", "🥢", "Smooth bamboo poles for Mizoram bamboo dance", "music"),
        CulturalItem("loktak", "Phumdi (ফুমদি)", "🏝️", "Floating biomass circles of Loktak Lake Manipur", "nature"),
        CulturalItem("pitha", "Pitha (ঘিলা পিঠা)", "🥞", "Rice cake delicacies prepared with jaggery", "food"),
        CulturalItem("eri_silk", "Eri Shawl (এৰী চাদৰ)", "🧶", "Peace silk shawl spun naturally in Meghalaya", "attire"),
        CulturalItem("kopou", "Kopou Phool (কপৌ ফুল)", "🌸", "Foxtail orchid adorning Bihu dancers", "nature"),
        CulturalItem("bell_metal", "Kahi (কাঁহৰ কাঁহী)", "🍽️", "Traditional bell-metal dining platter of Sarthebari", "household"),
        CulturalItem("bridge", "Living Root Bridge", "🌉", "Ficus elastica bio-engineered root bridge in Meghalaya", "landmarks")
    )

    val routineItems = listOf(
        RoutineItem("wake", 1, "Wake up (পুৱা শুই উঠা)", "🌅", "6:00 AM"),
        RoutineItem("water", 2, "Drink warm water (পানী খোৱা)", "💧", "6:30 AM"),
        RoutineItem("breakfast", 3, "Breakfast (ৰাতিপুৱাৰ জলপান)", "🥣", "8:00 AM"),
        RoutineItem("medicine", 4, "Morning Medicine (ঔষধ খোৱা)", "💊", "9:00 AM"),
        RoutineItem("bath", 5, "Morning Bath (গা ধোৱা)", "🚿", "10:30 AM"),
        RoutineItem("lunch", 6, "Lunch (দুপৰীয়াৰ ভাত)", "🍛", "1:00 PM"),
        RoutineItem("walk", 7, "Garden Walk (খোজ কঢ়া)", "🚶", "4:30 PM"),
        RoutineItem("dinner", 8, "Dinner & Sleep (ৰাতিৰ ভাত আৰু টোপনি)", "🌙", "8:30 PM")
    )

    val bihuStory = FolkStory(
        id = "story_bihu_grandpa",
        title = "Grandpa Rongmon & the Golden Gamosa",
        text = "Every spring during Rongali Bihu, Grandpa Rongmon walked to the tea garden with his wooden Dhol drum. His granddaughter Bobi gifted him a freshly woven red and white Gamosa with Kopou orchids. Grandpa wore it with pride and played joyous rhythms under the ancient banyan tree until the sunset.",
        questions = listOf(
            StoryQuestion(
                question = "What musical instrument did Grandpa Rongmon carry?",
                options = listOf("Wooden Dhol drum", "Brass Flute", "Silver Pepa", "Harmonium"),
                correctIndex = 0
            ),
            StoryQuestion(
                question = "What did his granddaughter Bobi gift to him?",
                options = listOf("Brass tea cup", "Red & white Gamosa", "Walking cane", "Sweet pitha"),
                correctIndex = 1
            ),
            StoryQuestion(
                question = "Where did Grandpa sit and play the festive rhythm?",
                options = listOf("Under the ancient banyan tree", "Near the river bank", "On the porch", "At the community hall"),
                correctIndex = 0
            )
        )
    )

    val helpers = listOf(
        HelperPerson("bina", "Nurse Bina Bora", "Friendly Community Nurse from Guwahati", "👩‍⚕️", "Checks your morning blood pressure with a warm smile"),
        HelperPerson("tsering", "Tsering Dorjee", "Kind Neighbor who shares organic apples", "👨‍🌾", "Wears a warm woolen cap and visits on Tuesdays"),
        HelperPerson("mary", "Sister Mary Nongrum", "Physical Therapist from Shillong", "🏃‍♀️", "Guides your gentle veranda walking exercises")
    )
}
