package com.norihiro.walkinbingo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged

data class BingoLabel(
    val index: Int,
    val label: String,
    val imageUri: MutableLiveData<String> = MutableLiveData(null),
    val isHit: MutableLiveData<Boolean> = MutableLiveData(false)
)

class MainViewModel : ViewModel() {
    val words = listOf("りんご", "バナナ", "ぶどう", "メロン", "スイカ", "さくらんぼ", "みかん", "もも", "パイナップル", "キウイ", "イチゴ")
    val reallabels = listOf("Team", "Bonfire", "Comics", "Himalayan", "Iceberg", "Bento", "Sink", "Toy", "Statue", "Cheeseburger", "Tractor", "Sled", "Aquarium", "Circus", "Sitting", "Beard", "Bridge", "Tights", "Bird", "Rafting", "Park", "Factory", "Graduation", "Porcelain", "Twig", "Petal", "Cushion", "Sunglasses", "Infrastructure", "Ferris wheel", "Pomacentridae", "Wetsuit", "Shetland sheepdog", "Brig", "Watercolor paint", "Competition", "Cliff", "Badminton", "Safari", "Bicycle", "Stadium", "Boat", "Smile", "Surfboard", "Fast food", "Sunset", "Hot dog", "Shorts", "Bus", "Bullfighting", "Sky", "Gerbil", "Rock", "Interaction", "Dress", "Toe", "Bear", "Eating", "Tower", "Brick", "Junk", "Person", "Windsurfing", "Swimwear", "Roller", "Camping", "Playground", "Bathroom", "Laugh", "Balloon", "Concert", "Prom", "Construction", "Product", "Reef", "Picnic", "Wreath", "Wheelbarrow", "Boxer", "Necklace", "Bracelet", "Casino", "Windshield", "Stairs", "Computer", "Cookware and bakeware", "Monochrome", "Chair", "Poster", "Bar", "Shipwreck", "Pier", "Community", "Caving", "Cave", "Tie", "Cabinetry", "Underwater", "Clown", "Nightclub", "Cycling", "Comet", "Mortarboard", "Track", "Christmas", "Church", "Clock", "Dude", "Cattle", "Jungle", "Desk", "Curling", "Cuisine", "Cat", "Juice", "Couscous", "Screenshot", "Crew", "Skyline", "Stuffed toy", "Cookie", "Tile", "Hanukkah", "Crochet", "Skateboarder", "Clipper", "Nail", "Cola", "Cutlery", "Menu", "Sari", "Plush", "Pocket", "Neon", "Icicle", "Pasteles", "Chain", "Dance", "Dune", "Santa claus", "Thanksgiving", "Tuxedo", "Mouth", "Desert", "Dinosaur", "Mufti", "Fire", "Bedroom", "Goggles", "Dragon", "Couch", "Sledding", "Cap", "Whiteboard", "Hat", "Gelato", "Cavalier", "Beanie", "Jersey", "Scarf", "Vacation", "Pitch", "Blackboard", "Deejay", "Monument", "Bumper", "Longboard", "Waterfowl", "Flesh", "Net", "Icing", "Dalmatian", "Speedboat", "Trunk", "Coffee", "Soccer", "Ragdoll", "Food", "Standing", "Fiction", "Fruit", "Pho", "Sparkler", "Presentation", "Swing", "Cairn terrier", "Forest", "Flag", "Frigate", "Foot", "Jacket", "Pillow", "Bathing", "Glacier", "Gymnastics", "Ear", "Flora", "Shell", "Grandparent", "Ruins", "Eyelash", "Bunk bed", "Balance", "Backpacking", "Horse", "Glitter", "Saucer", "Hair", "Miniature", "Crowd", "Curtain", "Icon", "Pixie-bob", "Herd", "Insect", "Ice", "Bangle", "Flap", "Jewellery", "Knitting", "Centrepiece", "Outerwear", "Love", "Muscle", "Motorcycle", "Money", "Mosque", "Tableware", "Ballroom", "Kayak", "Leisure", "Receipt", "Lake", "Lighthouse", "Bridle", "Leather", "Horn", "Strap", "Lego", "Scuba diving", "Leggings", "Pool", "Musical instrument", "Musical", "Metal", "Moon", "Blazer", "Marriage", "Mobile phone", "Militia", "Tablecloth", "Party", "Nebula", "News", "Newspaper", "Piano", "Plant", "Passport", "Penguin", "Shikoku", "Palace", "Doily", "Polo", "Paper", "Pop music", "Skiff", "Pizza", "Pet", "Quilting", "Cage", "Skateboard", "Surfing", "Rugby", "Lipstick", "River", "Race", "Rowing", "Road", "Running", "Room", "Roof", "Star", "Sports", "Shoe", "Tubing", "Space", "Sleep", "Skin", "Swimming", "School", "Sushi", "Loveseat", "Superman", "Cool", "Skiing", "Submarine", "Song", "Class", "Skyscraper", "Volcano", "Television", "Rein", "Tattoo", "Train", "Handrail", "Cup", "Vehicle", "Handbag", "Lampshade", "Event", "Wine", "Wing", "Wheel", "Wakeboarding", "Web page", "Ranch", "Fishing", "Heart", "Cotton", "Cappuccino", "Bread", "Sand", "Museum", "Helicopter", "Mountain", "Duck", "Soil", "Turtle", "Crocodile", "Musician", "Sneakers", "Wool", "Ring", "Singer", "Carnival", "Snowboarding", "Waterskiing", "Wall", "Rocket", "Countertop", "Beach", "Rainbow", "Branch", "Moustache", "Garden", "Gown", "Field", "Dog", "Superhero", "Flower", "Placemat", "Subwoofer", "Cathedral", "Building", "Airplane", "Fur", "Bull", "Bench", "Temple", "Butterfly", "Model", "Marathon", "Needlework", "Kitchen", "Castle", "Aurora", "Larva", "Racing", "Airliner", "Dam", "Textile", "Groom", "Fun", "Steaming", "Vegetable", "Unicycle", "Jeans", "Flowerpot", "Drawer", "Cake", "Armrest", "Aviation", "Fog", "Fireworks", "Farm", "Seal", "Shelf", "Bangs", "Lightning", "Van", "Sphynx", "Tire", "Denim", "Prairie", "Snorkeling", "Umbrella", "Asphalt", "Sailboat", "Basset hound", "Pattern", "Supper", "Veil", "Waterfall", "Lunch", "Odometer", "Baby", "Glasses", "Car", "Aircraft", "Hand", "Rodeo", "Canyon", "Meal", "Softball", "Alcohol", "Bride", "Swamp", "Pie", "Bag", "Joker", "Supervillain", "Army", "Canoe", "Selfie", "Rickshaw", "Barn", "Archery", "Aerospace engineering", "Storm", "Helmet")
    val labels = listOf("Bonfire", "Comics", "Bento", "Toy", "Statue", "Tractor", "Aquarium", "Sitting", "Beard", "Bridge", "Bird", "Park", "Factory", "Graduation", "Petal", "Cushion", "Sunglasses", "Bicycle", "Stadium", "Boat", "Smile", "Fast food", "Sunset", "Hot dog", "Shorts", "Bus", "Bullfighting", "Sky", "Rock", "Dress", "Toe", "Bear","Brick","Necklace", "Bracelet","Stairs", "Computer","Clock", "Cat")
    val easyLabels = listOf("Smile", "Bird", "Cat", "Dog", "Eating", "Food", "Mobile phone", "Toy", "Ice")
    val themeList = listOf("円形◯", "三角形△", "四角形□", "赤色", "青色", "緑色", "黄色", "黒色", "白色" )

    val theme: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    var bingoNum = 0
    private val bingoCardRaw = mutableListOf<BingoLabel>()
    private val _bingoCard = MutableLiveData<List<BingoLabel>>(emptyList())
    val bingoCard: LiveData<List<BingoLabel>> = _bingoCard.distinctUntilChanged()

    fun initBingo(gameMode: String) {
        bingoCardRaw.clear()
        if (gameMode == "easy") {
            easyLabels.shuffled().subList(0, 9).forEach {
                val bingoLabel = BingoLabel(labels.indexOf(it), it)
                bingoCardRaw.add(bingoLabel)
            }
        }else if (gameMode == "hard") {
            labels.shuffled().subList(0, 9).forEach {
                val bingoLabel = BingoLabel(labels.indexOf(it), it)
                bingoCardRaw.add(bingoLabel)
            }
        }
        _bingoCard.value = ArrayList(bingoCardRaw)
        bingoNum = 0
        theme.value = themeList.random()
    }

    fun setHit(labelName: String?, stringUri: String?) {
        bingoCardRaw.find { it.label == labelName }?.run {
            this.isHit.value = true
            this.imageUri.value = stringUri
        }
    }

    fun isBingo(): Boolean {
        return bingoCardRaw.chunked(3).any { it.all { it.isHit.value == true } } ||
                bingoCardRaw.filterIndexed { index, _ -> index % 3 == 0 }.all { it.isHit.value == true } ||
                bingoCardRaw.filterIndexed { index, _ -> index % 3 == 1 }.all { it.isHit.value == true } ||
                bingoCardRaw.filterIndexed { index, _ -> index % 3 == 2 }.all { it.isHit.value == true } ||
                bingoCardRaw.filterIndexed { index, _ -> index % 4 == 0 }.all { it.isHit.value == true } ||
                bingoCardRaw.filterIndexed { index, _ -> index == 2 || index == 4 || index == 6}.all { it.isHit.value == true }

    }

    fun countBingo(): Int {
        var count =  bingoCardRaw.chunked(3).count { it.all { it.isHit.value == true } }
        count += if (bingoCardRaw.filterIndexed { index, _ -> index % 3 == 0 }.all { it.isHit.value == true } ) 1 else 0
        count += if (bingoCardRaw.filterIndexed { index, _ -> index % 3 == 1 }.all { it.isHit.value == true } ) 1 else 0
        count += if (bingoCardRaw.filterIndexed { index, _ -> index % 3 == 2 }.all { it.isHit.value == true } ) 1 else 0
        count += if (bingoCardRaw.filterIndexed { index, _ -> index % 4 == 0 }.all { it.isHit.value == true } ) 1 else 0
        count += if (bingoCardRaw.filterIndexed { index, _ -> index == 2 || index == 4 || index == 6}.all { it.isHit.value == true } ) 1 else 0
        return count
    }

    val state = MutableLiveData<DialogState<CheckPhotoDialogFragment>>()
}