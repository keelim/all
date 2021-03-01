
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keelim.nandadiagnosis.ui.theme.PrimaryColor

@Preview
@Composable
fun DefaultView(){
    Surface(color = PrimaryColor) {
        Column(modifier = Modifier.fillMaxHeight()){
            Text(
                text = "NandaDiagnosis",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.h1
            )
        }
    }
}

