import android.media.AudioTrack
import android.media.AudioFormat
import android.media.AudioManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.userapp.User

@Composable
fun SoundTestScreen() {
    val context = LocalContext.current
    var volumeLevel by remember { mutableStateOf(0.5f) }
    var frequency by remember { mutableStateOf(1000) } // Frecuencia en Hz (1000 Hz es común para pruebas)
    var isPlaying by remember { mutableStateOf(false) }

    val gradientBackground = Brush.linearGradient(
        colors = listOf(Color(0xFFD0021B), Color(0xFF731F73))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Prueba de Audición",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Slider para ajustar el volumen
            Text("Nivel de Volumen: ${(volumeLevel * 100).toInt()}%", color = Color.White)
            Slider(
                value = volumeLevel,
                onValueChange = { volumeLevel = it },
                valueRange = 0f..1f,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Slider para ajustar la frecuencia
            Text("Frecuencia: $frequency Hz", color = Color.White)
            Slider(
                value = frequency.toFloat(),
                onValueChange = { frequency = it.toInt() },
                valueRange = 20f..20000f,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Botón para iniciar/detener el sonido
            Button(
                onClick = {
                    if (!isPlaying) {
                        playTone(frequency, 3000, volumeLevel)  // Reproducir tono por 3 segundos
                    }
                    isPlaying = !isPlaying
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(brush = gradientBackground)
            ) {
                Text(
                    text = if (isPlaying) "Detener Sonido" else "Iniciar Sonido",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

// Función para generar el tono de sonido
fun playTone(frequency: Int, durationMs: Int, volume: Float) {
    val sampleRate = 44100
    val numSamples = durationMs * sampleRate / 1000
    val samples = ShortArray(numSamples)

    // Crear onda senoidal
    for (i in samples.indices) {
        val angle = 2.0 * Math.PI * i.toDouble() / (sampleRate / frequency)
        samples[i] = (Math.sin(angle) * Short.MAX_VALUE * volume).toInt().toShort()
    }

    // Configurar el track de audio
    val audioTrack = AudioTrack(
        AudioManager.STREAM_MUSIC,
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        samples.size * 2,
        AudioTrack.MODE_STATIC
    )

    audioTrack.write(samples, 0, samples.size)
    audioTrack.play()
}

@Preview(showBackground = true)
@Composable
fun PreviewSoundTestScreen() {
    SoundTestScreen()
}

