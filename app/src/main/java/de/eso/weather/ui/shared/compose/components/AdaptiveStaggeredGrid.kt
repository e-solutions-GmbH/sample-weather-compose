package de.eso.weather.ui.shared.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.eso.weather.ui.shared.compose.WeatherTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdaptiveStaggeredGrid(
    modifier: Modifier = Modifier,
    isLargeScreen: Boolean = WeatherTheme.isLargeScreen(),
    content: LazyStaggeredGridScope.() -> Unit
) {
    val cells = StaggeredGridCells.Fixed(count = if (isLargeScreen) 3 else 2)
    val spacing = WeatherTheme.dimensions.containerPadding
    val arrangement = Arrangement.spacedBy(space = WeatherTheme.dimensions.containerPadding)

    when {
        isLargeScreen -> {
            LazyHorizontalStaggeredGrid(
                modifier = modifier,
                rows = cells,
                content = content,
                horizontalItemSpacing = spacing,
                verticalArrangement = arrangement
            )
        }
        else -> {
            LazyVerticalStaggeredGrid(
                modifier = modifier,
                columns = cells,
                content = content,
                verticalItemSpacing = spacing,
                horizontalArrangement = arrangement
            )
        }
    }
}
