import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rickyandmorty.model.Character
import com.example.rickyandmorty.model.Episode
import com.example.rickyandmorty.service.EpisodeService
import com.example.rickyandmorty.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun <NavHostController> CharacterDetail(controleDeNavegacao: NavHostController, i: Int) {

    val id = remember { mutableStateOf(i) }
    var character = remember { mutableStateOf(Character()) }
    var episodes = remember { mutableStateOf(listOf<Episode>()) }

    val callCharacter = RetrofitFactory()
        .getCharactersService().getCharacterById(id.value)

    val episodeService: EpisodeService = RetrofitFactory().getEpisodeService()

    callCharacter.enqueue(object : Callback<Character> {
        override fun onResponse(call: Call<Character>, response: Response<Character>) {
            character.value = response.body() ?: Character()

            val episodeUrls = character.value.episode
            episodeUrls.forEach { episodeUrl ->
                val callEpisode = episodeService.getEpisodeByUrl(episodeUrl)
                callEpisode.enqueue(object : Callback<Episode> {
                    override fun onResponse(p0: Call<Episode>, p1: Response<Episode>) {
                        p1.body()?.let { episode ->
                            episodes.value = episodes.value + episode
                        }
                    }

                    override fun onFailure(p0: Call<Episode>, p1: Throwable) {

                    }
                })
            }
        }

        override fun onFailure(call: Call<Character>, t: Throwable) {

        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Rick and Morty Character Details",
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Color(0xFF00FF00),
            modifier = Modifier.padding(8.dp)
        )

        AsyncImage(
            model = character.value.image,
            contentDescription = "",
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
                .background(Color(0xFF009688), RoundedCornerShape(24.dp))
        )

        Text(
            text = character.value.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFEB3B),
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        )

        Text(
            text = "Origin: ${character.value.origin?.name ?: "Unknown"}",
            fontSize = 18.sp,
            color = Color(0xFFBB86FC),
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Species: ${character.value.species}",
            fontSize = 18.sp,
            color = Color(0xFFFF5722),
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Status: ${character.value.status}",
            fontSize = 18.sp,
            color = Color(0xFF03DAC5),
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = "Gender: ${character.value.gender}",
            fontSize = 18.sp,
            color = Color(0xFF8BC34A),
            modifier = Modifier.padding(8.dp)
        )


        Text(
            text = "Episódios que o personagem aparece:",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )


        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(episodes.value) { episode ->
                EpisodeCard(episode = episode)
            }
        }
    }
}

@Composable
fun EpisodeCard(episode: Episode) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF212121)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = episode.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF00)
            )
            Text(
                text = "Air Date: ${episode.air_date}",
                fontSize = 16.sp,
                color = Color(0xFFFFEB3B),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
