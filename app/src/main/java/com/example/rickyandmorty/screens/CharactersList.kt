import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rickyandmorty.model.Character
import com.example.rickyandmorty.model.Result
import com.example.rickyandmorty.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun CharacterList(controleDeNavegacao: NavHostController?) {
    var characterList by remember { mutableStateOf(listOf<Character>()) }

    // Chamada para a API
    val callCharacters = RetrofitFactory().getCharactersService().getAllCharacters()
    callCharacters.enqueue(object : Callback<Result> {
        override fun onResponse(p0: Call<Result>, p1: Response<Result>) {
            characterList = p1.body()!!.results
        }

        override fun onFailure(p0: Call<Result>, p1: Throwable) {}
    })

    // Layout da lista de personagens
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)), // Fundo escuro
        color = Color(0xFF121212) // Fundo secundário mais escuro
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Rick and Morty Characters",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF00), // Verde brilhante
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp) // Espaçamento entre os cards
            ) {
                items(characterList) { char ->
                    CharacterCard(character = char, controleDeNavegacao)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(character: Character?, controleDeNavegacao: NavHostController?) {
    Card(
        modifier = Modifier

            .fillMaxWidth()
            .clickable(
                onClick = { controleDeNavegacao?.navigate("detalhePersonagem/${character?.id}") }
            )
            .height(100.dp),
        colors = CardDefaults
            .cardColors(
                containerColor = Color(0xFF333333)
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier.size(100.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF444444))
            ) {
                AsyncImage(
                    model = character?.image,
                    contentDescription = "Character Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = character?.name ?: "",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFEB3B)
                )
                Text(
                    text = character?.species ?: "",
                    fontSize = 16.sp,
                    color = Color(0xFF00BCD4)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CharacterCardPreview() {
    CharacterCard(character = Character(), controleDeNavegacao = null)
}

@Preview(showSystemUi = true)
@Composable
private fun CharacterListPreview() {
    CharacterList(controleDeNavegacao = null)
}
