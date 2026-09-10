package com.lucas.fittrack.data.local.importer

import android.content.Context
import android.util.Log
import com.lucas.fittrack.data.local.dao.FoodDao
import com.lucas.fittrack.data.local.entity.FoodEntity
import org.apache.commons.csv.CSVFormat
import java.io.InputStreamReader

class TacoImporter(
    private val context: Context,
    private val foodDao: FoodDao
) {

    suspend fun importIfNeeded() {

        if (foodDao.countFoods() > 0) {
            return
        }

        val foods = readFoodsFromCsv()

        foodDao.insertFoods(foods)

        Log.d(
            "TacoImporter",
            "Alimentos importados: ${foodDao.countFoods()}"
        )
    }

    private fun parseDouble(value: String): Double {
        return value
            .trim()
            .toDoubleOrNull()
            ?: 0.0
    }
    private fun readFoodsFromCsv(): List<FoodEntity> {

        context.assets
            .open("taco_foods.csv")
            .use { inputStream ->

                InputStreamReader(
                    inputStream,
                    Charsets.UTF_8
                ).use { reader ->

                    val csvFormat =
                        CSVFormat.DEFAULT
                            .builder()
                            .setHeader()
                            .setSkipHeaderRecord(true)
                            .get()

                    return csvFormat
                        .parse(reader)
                        .map { record ->

                            FoodEntity(
                                id = record["id"].toLong(),
                                name = record["nome"],
                                category = record["categoria"],

                                caloriesPer100g =
                                    parseDouble(record["energia_kcal"]),

                                proteinPer100g =
                                    parseDouble(record["proteina_g"]),

                                carbsPer100g =
                                    parseDouble(record["carboidrato_g"]),

                                fatPer100g =
                                    parseDouble(record["lipideos_g"]),

                                fiberPer100g =
                                    parseDouble(record["fibra_g"]),

                                cholesterolPer100g =
                                    parseDouble(record["colesterol_mg"])
                            )
                        }
                }
            }
    }

}