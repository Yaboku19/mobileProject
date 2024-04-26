import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.traveldiary.data.database.Marker

@Composable
fun HomeMarkDetailScreen(navController: NavHostController, marker: Marker) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()  // Assicura che la Column occupi tutto lo spazio orizzontale del Box
            ){
                Text(
                    text = "Name:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth()  // Assicura che il Text occupi tutto lo spazio orizzontale della Column
                        .wrapContentWidth(Alignment.CenterHorizontally)  // Centra il Text all'interno della sua area
                )
                Text(
                    text = marker.name,
                    fontSize = 18.sp
                    // Questo Text è allineato a sinistra per default
                )
            }
        }
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()  // Assicura che la Column occupi tutto lo spazio orizzontale del Box
            ){
                Text(
                    text = "Descrizione:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier
                        .fillMaxWidth()  // Assicura che il Text occupi tutto lo spazio orizzontale della Column
                        .wrapContentWidth(Alignment.CenterHorizontally)  // Centra il Text all'interno della sua area
                )
                Text(
                    text = marker.description,
                    fontSize = 18.sp
                    // Questo Text è allineato a sinistra per default
                )
            }
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Guarda posizione")
        }
    }
}
