package com.lucas.fittrack.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lucas.fittrack.model.MealItem
import com.lucas.fittrack.ui.components.DateSelector
import com.lucas.fittrack.ui.components.EditMealItemDialog
import com.lucas.fittrack.ui.components.FoodDetails
import com.lucas.fittrack.ui.components.FoodList
import com.lucas.fittrack.ui.components.MealSummary
import com.lucas.fittrack.ui.components.MealTypeSelector
import com.lucas.fittrack.ui.components.SectionCard
import com.lucas.fittrack.ui.theme.Dimens
import com.lucas.fittrack.ui.viewmodel.HomeViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DietScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onSearchTextChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onEditingFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var mealTypeMenuExpanded by remember {
        mutableStateOf(false)
    }

    var editingItem by remember {
        mutableStateOf<MealItem?>(null)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(
            Dimens.spacingLarge
        )
    ) {

        Text(
            text = "Dieta",
            style = MaterialTheme.typography.headlineLarge
        )

        SectionCard {

            Text(
                text = "Selecionar alimento",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            FoodSearchSection(
                searchText = uiState.foodSearchText,
                categories = uiState.foodCategories,
                selectedCategory = uiState.selectedFoodCategory,
                onSearchTextChange = onSearchTextChange,
                onCategorySelected = onCategorySelected
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            FoodList(
                foods = uiState.foods,
                onFoodSelected = { food ->
                    viewModel.selectFood(food)
                }
            )
        }

        uiState.selectedFood?.let { food ->

            SectionCard {

                Text(
                    text = "Alimento selecionado",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier = Modifier.height(
                        Dimens.spacingMedium
                    )
                )

                FoodDetails(
                    food = food,
                    quantityText = uiState.quantityText,

                    onQuantityChange = { value ->
                        viewModel.updateQuantityText(value)
                    },

                    onAdd = {
                        viewModel.addFoodToCurrentMeal()
                    }
                )
            }
        }

        SectionCard {
            MealSummary(
                mealItems = uiState.mealItems,
                onRemoveItem = { item ->
                    viewModel.removeFoodFromCurrentMeal(item)
                },
                onEditItem = { item ->
                    editingItem = item
                }
            )
        }

        DateSelector(
            selectedDate = uiState.selectedDate,
            onDateChange = viewModel::selectDate
        )

        SectionCard {

            Text(
                text = "Tipo da refeição",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingMedium
                )
            )

            MealTypeSelector(
                selectedMealType = uiState.selectedMealType,
                expanded = mealTypeMenuExpanded,

                onExpandedChange = { expanded ->
                    mealTypeMenuExpanded = expanded
                },

                onMealTypeSelected = { mealType ->
                    viewModel.selectMealType(mealType)
                }
            )

            Spacer(
                modifier = Modifier.height(
                    Dimens.spacingLarge
                )
            )

            Button(
                onClick = {

                    val wasEditing =
                        uiState.editingMealId != null

                    viewModel.saveCurrentMeal()

                    if (wasEditing) {
                        onEditingFinished()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (uiState.editingMealId == null) {
                        "Salvar refeição"
                    } else {
                        "Salvar alterações"
                    }
                )
            }
            if (uiState.editingMealId != null) {

                OutlinedButton(
                    onClick = {
                        viewModel.cancelMealEditing()
                        onEditingFinished()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar edição")
                }
            }
        }

        editingItem?.let { item ->

            EditMealItemDialog(
                item = item,
                onDismiss = {
                    editingItem = null
                },
                onConfirm = { newQuantity ->

                    viewModel.updateMealItemQuantity(
                        item = item,
                        newQuantity = newQuantity
                    )

                    editingItem = null
                }
            )
        }
    }
}

@Composable
fun FoodSearchSection(
    searchText: String,
    categories: List<String>,
    selectedCategory: String?,
    onSearchTextChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(
            Dimens.spacingMedium
        )
    ) {

        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Buscar alimento")
            },
            singleLine = true
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedButton(
                onClick = {
                    expanded = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedCategory
                        ?: "Todas as categorias"
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                DropdownMenuItem(
                    modifier = Modifier.height(30.dp),
                    text = {
                        Text(
                            text = "Todas as categorias",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onCategorySelected(null)
                        expanded = false
                    }
                )

                categories.forEach { category ->
                    DropdownMenuItem(
                        modifier = Modifier.height(36.dp),
                        text = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}