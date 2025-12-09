package com.example.bucqetbunga.data

import com.example.bucqetbunga.R
import com.example.bucqetbunga.models.Bouquet
import com.example.bucqetbunga.BouquetCategory

object BouquetDataSource {
    fun getBouquets(): List<Bouquet> = listOf(
        Bouquet(1, "leony", "Bunga Mawar, Gompie, Filler", 75000.0, BouquetCategory.ARTIFICIAL, R.drawable.leony),
        Bouquet(2, "denish", "Full Of Snack", 85000.0, BouquetCategory.SNACK, R.drawable.denish),
        Bouquet(3, "caleo", "Red Rose, Baby Breath, Caspea", 125000.0, BouquetCategory.FRESH, R.drawable.caleo),
        Bouquet(4, "lisa", "Lily, Garbera, Pink Rose, Aster, Ruscus, Pikok, Pompom", 185000.0, BouquetCategory.FRESH, R.drawable.lisa),
        Bouquet(5, "edelweis", "Red Rose, Caspea, Edelweis", 200000.0, BouquetCategory.DRY, R.drawable.edelweis),
        Bouquet(6, "luxury", "Satin (By Request), Glitter", 300000.0, BouquetCategory.SATIN, R.drawable.luxury),
        Bouquet(7, "abigail", "Rose, Garbera, Lily, Filler, Daisy", 285000.0, BouquetCategory.ARTIFICIAL, R.drawable.abigail),
        Bouquet(8, "alea", "Pikok, Rose", 150000.0, BouquetCategory.WEDDING, R.drawable.alea),
        Bouquet(9, "moonblue", "Orchid, Aster, Garbera, Rose, Gompie, LL", 500000.0, BouquetCategory.ORCHID_VASE, R.drawable.moonblue),
        Bouquet(10, "cheera", "Baby Rose, Rose, Garbera, Gompie, Aster", 225000.0, BouquetCategory.FLOWER_VASE, R.drawable.cheera),
        Bouquet(11, "ring Box", "Rose, Baby Breath", 200000.0, BouquetCategory.GIFT, R.drawable.ringbox),
        Bouquet(12, "snowie", "Rokok, Boneka, Flower Mix", 200000.0, BouquetCategory.ROKOK, R.drawable.snowie),
        Bouquet(13, "cloudy", "Boneka, Baloon, Full Snack", 225000.0, BouquetCategory.SNACK, R.drawable.cloudy),
        Bouquet(14, "lely", "Lily, Sedap Malam", 300000.0, BouquetCategory.FLOWER_VASE, R.drawable.lely),
        Bouquet(15, "mimi", "Orchid, Krisan, Pikok", 700000.0, BouquetCategory.ORCHID_VASE, R.drawable.mimi),
        Bouquet(16, "caleofresh", "Red Rose, Aster, Krisan", 120000.0, BouquetCategory.FRESH, R.drawable.caleofresh),
        Bouquet(17, "money", "Money Buket by Request", 200000.0, BouquetCategory.MONEY_BQ, R.drawable.money),
        Bouquet(18, "kiara", "Boneka and Mix Flower", 300000.0, BouquetCategory.GRADUATION, R.drawable.kiara),
        Bouquet(19, "kiano", "Boneka and Dry Flower", 250000.0, BouquetCategory.GRADUATION, R.drawable.kiano),
        Bouquet(20, "katty", "Full Of Red Rose", 800000.0, BouquetCategory.FRESH_FLOWER, R.drawable.katty),
        Bouquet(21, "mika", "Mix Flower", 170000.0, BouquetCategory.FRESH_FLOWER, R.drawable.mika),
        Bouquet(22, "papan Banner", "Artificial Flower", 500000.0, BouquetCategory.STANDING, R.drawable.papanbanner),
        Bouquet(23, "papan Akrilik", "Artificial Flower", 300000.0, BouquetCategory.STANDING, R.drawable.papanakrilik),
        Bouquet(24, "jean", "Mix Aster", 90000.0, BouquetCategory.FRESH_FLOWER, R.drawable.jean),
        Bouquet(25, "money2", "Money and Flower", 200000.0, BouquetCategory.MONEY_BQ, R.drawable.money2),
        Bouquet(26, "money3", "Money and Flower", 150000.0, BouquetCategory.MONEY_BQ, R.drawable.money3),
        Bouquet(27, "lifie", "Red Rose and Filler", 70000.0, BouquetCategory.ARTIFICIAL, R.drawable.lifie),
        Bouquet(28, "baylage", "Full of mawar and garbera", 600000.0, BouquetCategory.ARTIFICIAL, R.drawable.baylage),
        Bouquet(29, "milea", "Full of mawar", 200000.0, BouquetCategory.FRESH, R.drawable.milea),
        Bouquet(30, "farasya", "Full of mawar", 150000.0, BouquetCategory.ARTIFICIAL, R.drawable.farasya)
    )
}