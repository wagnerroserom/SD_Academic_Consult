package SD_Academic_Consult;

import java.io.*;
import java.net.*;

/**
 *
 * @author WAGNER ROSERO
 */
public class Academic_Client {
    private static final String IP_SERVIDOR = "localhost";
    private static final int PUERTO = 4500;
    
    public static void main(String[] args) {
        System.out.println("*** Iniciando el Cliente Académico ***");
        try (
             Socket socket = new Socket(IP_SERVIDOR, PUERTO);
             BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter salidaServidor = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))
         ) {
            // Inicia el servidor y muestra el mensaje de bienvenida
            System.out.println("Servidor: " + entradaServidor.readLine());
            
            String comandoUsuario;
            while (true) {
                System.out.print("\nIngrese consulta (horario, aula, docente, materia, fecha, hora, ayuda, salir): ");
                comandoUsuario = teclado.readLine();
                
                if (comandoUsuario == null) break;
                
                // A través del socket se envía la consulta al servidor
                salidaServidor.println(comandoUsuario);
                
                // Lee la respuesta devuelta por el servidor
                String respuestaServidor = entradaServidor.readLine();
                System.out.println("Respuesta: " + respuestaServidor);
                
                if (comandoUsuario.trim().equalsIgnoreCase("salir")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
        System.out.println("*** Cliente Finalizado ***");
    }
}
