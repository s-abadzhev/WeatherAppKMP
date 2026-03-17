package ru.sergeyabadzhev.weatherappkmp.features.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.sergeyabadzhev.weatherappkmp.R
import ru.sergeyabadzhev.weatherappkmp.domain.model.City


@Composable
fun SearchScreen(
    onCitySelected: (City) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SearchViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(Color(0xFF1a6bcc), Color(0xFF0d3b8c))
                )
            )
    ) {
        Column(
            modifier = Modifier.statusBarsPadding()
        ) {
            SearchBarView(
                query = state.query,
                onQueryChanged = { viewModel.onQueryChanged(it) },
                onCancel = onBackClick
            )

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 40.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
                state.results.isNotEmpty() -> {
                    CityListView(
                        cities = state.results,
                        onSelect = { city ->
                            viewModel.saveCity(city)
                            onCitySelected(city)
                        }
                    )
                }
                state.query.length >= 2 -> {
                    EmptyStateView(message = stringResource(R.string.search_not_found), icon = "🔍")
                }
                state.savedCities.isNotEmpty() -> {
                    SavedCitiesView(
                        cities = state.savedCities,
                        onSelect = onCitySelected,
                        onDelete = { viewModel.removeCity(it) }
                    )
                }
                else -> {
                    EmptyStateView(message = stringResource(R.string.search_start_typing), icon = "📍")
                }
            }
        }
    }
}

@Composable
private fun SearchBarView(
    query: String,
    onQueryChanged: (String) -> Unit,
    onCancel: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(stringResource(R.string.search_placeholder), color = Color.White.copy(alpha = 0.4f))
            },
            leadingIcon = {
                Text("🔍", fontSize = 18.sp, modifier = Modifier.padding(start = 4.dp))
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    TextButton(onClick = { onQueryChanged("") }) {
                        Text("✕", color = Color.White.copy(alpha = 0.5f))
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedBorderColor = Color.White.copy(alpha = 0.4f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.2f),
                cursorColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        TextButton(onClick = onCancel) {
            Text(stringResource(R.string.search_cancel), color = Color.White)
        }
    }
}

@Composable
private fun CityListView(cities: List<City>, onSelect: (City) -> Unit) {
    LazyColumn {
        items(cities) { city ->
            CityRowView(city = city, onClick = { onSelect(city) })
        }
    }
}

@Composable
private fun SavedCitiesView(
    cities: List<City>,
    onSelect: (City) -> Unit,
    onDelete: (City) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.search_saved_cities).uppercase(),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn {
            items(cities) { city ->
                CityRowView(
                    city = city,
                    onClick = { onSelect(city) },
                    onDelete = { onDelete(city) }
                )
            }
        }
    }
}

@Composable
private fun CityRowView(
    city: City,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("📍", fontSize = 22.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(city.name, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                Text(city.country, fontSize = 13.sp, color = Color.White.copy(alpha = 0.5f))
            }
            if (onDelete != null) {
                TextButton(onClick = onDelete) {
                    Text("✕", color = Color.White.copy(alpha = 0.4f))
                }
            }
        }
        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
    }
}

@Composable
private fun EmptyStateView(message: String, icon: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(icon, fontSize = 36.sp)
        Text(message, fontSize = 16.sp, color = Color.White.copy(alpha = 0.5f))
    }
}