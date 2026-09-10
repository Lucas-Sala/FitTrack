package com.lucas.fittrack.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.lucas.fittrack.model.NutritionGoals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferencesRepository(
    private val context: Context
) {

    private object Keys {
        val CALORIES_GOAL = doublePreferencesKey("calories_goal")
        val PROTEIN_GOAL = doublePreferencesKey("protein_goal")
        val CARBS_GOAL = doublePreferencesKey("carbs_goal")
        val FAT_GOAL = doublePreferencesKey("fat_goal")
        val FIBER_GOAL = doublePreferencesKey("fiber_goal")
    }

    val nutritionGoals: Flow<NutritionGoals> =
        context.dataStore.data.map { preferences ->

            NutritionGoals(
                calories = preferences[Keys.CALORIES_GOAL] ?: 2750.0,
                protein = preferences[Keys.PROTEIN_GOAL] ?: 180.0,
                carbs = preferences[Keys.CARBS_GOAL] ?: 320.0,
                fat = preferences[Keys.FAT_GOAL] ?: 80.0,
                fiber = preferences[Keys.FIBER_GOAL] ?: 100.0
            )
        }

    suspend fun saveNutritionGoals(
        goals: NutritionGoals
    ) {
        context.dataStore.edit { preferences ->

            preferences[Keys.CALORIES_GOAL] = goals.calories
            preferences[Keys.PROTEIN_GOAL] = goals.protein
            preferences[Keys.CARBS_GOAL] = goals.carbs
            preferences[Keys.FAT_GOAL] = goals.fat
            preferences[Keys.FIBER_GOAL] = goals.fiber
        }
    }
}