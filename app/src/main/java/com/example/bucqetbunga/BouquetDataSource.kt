package com.example.bucqetbunga.data

import com.example.bucqetbunga.R
import com.example.bucqetbunga.models.Bouquet
import com.example.bucqetbunga.BouquetCategory
object BouquetDataSource {
    fun getBouquets(): List<Bouquet> = listOf(

        Bouquet(1, "Leony", "Bunga Mawar, Gompie, Filler", 75000.0, BouquetCategory.ARTIFICIAL, 10, R.drawable.ic_launcher_foreground),
        Bouquet(2, "Denish", "Full Of Snack", 85000.0, BouquetCategory.SNACK, 15, R.drawable.ic_launcher_foreground),
        Bouquet(3, "Caleo", "Red Rose, Baby Breath, Caspea", 125000.0, BouquetCategory.FRESH, 8, R.drawable.ic_launcher_foreground),
        Bouquet(4, "Lisa", "Lily, Garbera, Pink Rose, Aster, Ruscus, Pikok, Pompom", 185000.0, BouquetCategory.FRESH, 5, R.drawable.ic_launcher_foreground),
        Bouquet(5, "Edelweis", "Red Rose, Caspea, Edelweis", 200000.0, BouquetCategory.DRY, 7, R.drawable.ic_launcher_foreground),
        Bouquet(6, "Luxury", "Satin (By Request), Glitter", 300000.0, BouquetCategory.SATIN, 3, R.drawable.ic_launcher_foreground),
        Bouquet(7, "Abigail", "Rose, Garbera, Lily, Filler, Daisy", 285000.0, BouquetCategory.ARTIFICIAL, 12, R.drawable.ic_launcher_foreground),
        Bouquet(8, "Alea", "Pikok, Rose", 150000.0, BouquetCategory.WEDDING, 6, R.drawable.ic_launcher_foreground),
        Bouquet(9, "Moonblue", "Orchid, Aster, Garbera, Rose, Gompie, LL", 500000.0, BouquetCategory.ORCHID_VASE, 2, R.drawable.ic_launcher_foreground),
        Bouquet(10, "Cheera", "Baby Rose, Rose, Garbera, Gompie, Aster", 225000.0, BouquetCategory.FLOWER_VASE, 9, R.drawable.ic_launcher_foreground),
        Bouquet(11, "Ring Box", "Rose, Baby Breath", 200000.0, BouquetCategory.GIFT, 4, R.drawable.ic_launcher_foreground),
        Bouquet(12, "Snowie", "Rokok, Boneka, Flower Mix", 200000.0, BouquetCategory.ROKOK, 7, R.drawable.ic_launcher_foreground),
        Bouquet(13, "Cloudy", "Boneka, Baloon, Full Snack", 225000.0, BouquetCategory.SNACK, 10, R.drawable.ic_launcher_foreground),
        Bouquet(14, "Lely", "Lily, Sedap Malam", 300000.0, BouquetCategory.FLOWER_VASE, 5, R.drawable.ic_launcher_foreground),
        Bouquet(15, "Mimi", "Orchid, Krisan, Pikok", 700000.0, BouquetCategory.ORCHID_VASE, 1, R.drawable.ic_launcher_foreground),
        Bouquet(16, "Caleo Fresh", "Red Rose, Aster, Krisan", 120000.0, BouquetCategory.FRESH, 11, R.drawable.ic_launcher_foreground),
        Bouquet(17, "Money", "Money Buket by Request", 200000.0, BouquetCategory.MONEY_BQ, 8, R.drawable.ic_launcher_foreground),
        Bouquet(18, "Kiara", "Boneka and Mix Flower", 300000.0, BouquetCategory.GRADUATION, 6, R.drawable.ic_launcher_foreground),
        Bouquet(19, "Kiano", "Boneka and Dry Flower", 250000.0, BouquetCategory.GRADUATION, 7, R.drawable.ic_launcher_foreground),
        Bouquet(20, "Katty", "Full Of Red Rose", 800000.0, BouquetCategory.FRESH_FLOWER, 3, R.drawable.ic_launcher_foreground),
        Bouquet(21, "Mika", "Mix Flower", 170000.0, BouquetCategory.FRESH_FLOWER, 14, R.drawable.ic_launcher_foreground),
        Bouquet(22, "Papan Banner", "Artificial Flower", 500000.0, BouquetCategory.STANDING, 5, R.drawable.ic_launcher_foreground),
        Bouquet(23, "Papan Akrilik", "Artificial Flower", 300000.0, BouquetCategory.STANDING, 4, R.drawable.ic_launcher_foreground),
        Bouquet(24, "Jean", "Mix Aster", 90000.0, BouquetCategory.FRESH_FLOWER, 18, R.drawable.ic_launcher_foreground),
        Bouquet(25, "Money 2", "Money and Flower", 200000.0, BouquetCategory.MONEY_BQ, 6, R.drawable.ic_launcher_foreground),
        Bouquet(26, "Money 3", "Money and Flower", 150000.0, BouquetCategory.MONEY_BQ, 9, R.drawable.ic_launcher_foreground),
        Bouquet(27, "Lifie", "Red Rose and Filler", 70000.0, BouquetCategory.ARTIFICIAL, 20, R.drawable.ic_launcher_foreground),
        Bouquet(28, "Baylage", "Full of mawar and garbera", 600000.0, BouquetCategory.ARTIFICIAL, 3, R.drawable.ic_launcher_foreground),
        Bouquet(29, "Milea", "Full of mawar", 200000.0, BouquetCategory.FRESH, 10, R.drawable.ic_launcher_foreground),
        Bouquet(30, "Farasya", "Full of mawar", 150000.0, BouquetCategory.ARTIFICIAL, 15, R.drawable.ic_launcher_foreground)
    )
}