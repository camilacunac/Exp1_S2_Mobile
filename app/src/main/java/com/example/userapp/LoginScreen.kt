import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.userapp.R
import com.example.userapp.RecoverPasswordActivity
import com.example.userapp.RegisterActivity
import com.example.userapp.User
import com.example.userapp.WelcomeActivity
import com.google.firebase.database.FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    emailState: MutableState<String>,
    passwordState: MutableState<String>,
    onVoiceInputRequest: (requestCode: Int) -> Unit
) {
    val context = LocalContext.current
    val gradientBackground = Brush.linearGradient(
        colors = listOf(Color(0xFFD0021B), Color(0xFF731F73))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = gradientBackground)
    ) {
        Text(
            text = "Hola\nInicia Sesion!",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart)
        )

        Card(
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(top = 160.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Spacer(modifier = Modifier.height(32.dp))

                    // Campo de correo electrónico
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        label = { Text("Correo electronico", color = Color.Red) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_email),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mic),
                                contentDescription = "Voice Input",
                                modifier = Modifier.clickable {
                                    onVoiceInputRequest(1)  // Iniciar reconocimiento de voz para correo electrónico
                                },
                                tint = Color.Gray
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo de contraseña
                    OutlinedTextField(
                        value = passwordState.value,
                        onValueChange = { passwordState.value = it },
                        label = { Text("Contraseña", color = Color.Red) },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_lock),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_mic),
                                contentDescription = "Voice Input",
                                modifier = Modifier.clickable {
                                    onVoiceInputRequest(2)  // Iniciar reconocimiento de voz para contraseña
                                },
                                tint = Color.Gray
                            )
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Red,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Recuperar contraseña
                    Text(
                        text = "Olvidaste tu contraseña?",
                        color = Color.Gray,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable {
                                Toast.makeText(context, "Navegar a recuperación de contraseña", Toast.LENGTH_SHORT).show()
                                val intent = Intent(context, RecoverPasswordActivity::class.java)
                                context.startActivity(intent)
                            }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botón de inicio de sesión
                Button(
                    onClick = {
                        if (emailState.value.isEmpty() || passwordState.value.isEmpty()) {
                            Toast.makeText(context, "Por favor, ingrese su correo electrónico y contraseña.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val database = FirebaseDatabase.getInstance()
                        val userRef = database.getReference("users")

                        userRef.get().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val snapshot = task.result
                                var userFound: User? = null

                                if (snapshot != null && snapshot.exists()) {
                                    // Recorre todos los usuarios en la base de datos
                                    for (userSnapshot in snapshot.children) {
                                        try {
                                            // Intenta mapear el snapshot a la clase User
                                            val user = userSnapshot.getValue(User::class.java)

                                            if (user != null) {
                                                // Depuración para revisar los valores
                                                println("Email en Firebase: ${user.email}, Contraseña en Firebase: ${user.password}")
                                                // Agrega la lógica de comparación aquí si los datos se mapearon correctamente
                                                if (user.email == emailState.value.trim() && user.password == passwordState.value.trim()) {
                                                    userFound = user
                                                    break
                                                }
                                            } else {
                                                println("Error: el usuario es null")
                                            }
                                        } catch (e: Exception) {
                                            println("Error al obtener el usuario: ${e.message}")
                                        }
                                    }

                                    if (userFound != null) {
                                        Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(context, WelcomeActivity::class.java)
                                        intent.putExtra("userName", userFound.fullName)
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(context, "Correo electrónico o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, "No se encontraron usuarios en la base de datos", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show()
                            }
                        }
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
                        text = "INICIAR SESION",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navegar a la pantalla de registro
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "No tienes cuenta?", color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Registrate aquí",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            Toast.makeText(context, "Navegar a registro", Toast.LENGTH_SHORT).show()
                            val intent = Intent(context, RegisterActivity::class.java)
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(
        emailState = remember { mutableStateOf("") },
        passwordState = remember { mutableStateOf("") },
        onVoiceInputRequest = {}
    )
}
