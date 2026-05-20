package com.example.playimdb.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.example.playimdb.Constants
import com.example.playimdb.ui.components.AppButton
import com.example.playimdb.ui.components.AppOutlinedButton
import com.example.playimdb.ui.theme.ColorBackground
import com.example.playimdb.ui.theme.ColorOverlay
import com.example.playimdb.ui.theme.ColorPrimary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun DetailScreen(
    movieId: Int,
    onPlayClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: DetailViewModel = viewModel(factory = DetailViewModelFactory(movieId))
) {
    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorBackground)
    ) {
        state.movie?.let { movie ->
            AsyncImage(
                model = if (movie.backdropPath != null)
                    "${Constants.TMDB_IMAGE_BASE_URL}${Constants.TMDB_BACKDROP_SIZE}${movie.backdropPath}"
                else null,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorOverlay)
            )

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 64.dp, vertical = 48.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                AsyncImage(
                    model = if (movie.posterPath != null)
                        "${Constants.TMDB_IMAGE_BASE_URL}${Constants.TMDB_POSTER_SIZE}${movie.posterPath}"
                    else null,
                    contentDescription = movie.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(220.dp)
                        .height(330.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "★ ${"%.1f".format(movie.voteAverage)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = ColorPrimary
                        )
                        movie.releaseDate?.take(4)?.let { year ->
                            Text(
                                text = year,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        movie.runtime?.takeIf { it > 0 }?.let { runtime ->
                            Text(
                                text = "${runtime}m",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    if (movie.genres.isNotEmpty()) {
                        Text(
                            text = movie.genres.joinToString(" · ") { it.name },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    if (movie.overview.isNotBlank()) {
                        Text(
                            text = movie.overview,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AppButton(
                            onClick = { movie.imdbId?.let { onPlayClick(it) } },
                            enabled = movie.imdbId != null
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (movie.imdbId != null) "Watch Now" else "Not Available",
                                color = Color.Black
                            )
                        }

                        AppOutlinedButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Back", color = Color.White)
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                color = ColorPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        state.error?.let { error ->
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Failed to load movie",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                AppOutlinedButton(onClick = onBackClick) {
                    Text("Go Back", color = Color.White)
                }
            }
        }
    }
}
