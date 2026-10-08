package SD_Academic_Consult;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author WAGNER ROSERO
 */
public class Academic_Server {
  private static final int PUERTO = 4500;
  private static final Map<String, String> infoAcademic = new HashMap<>();
  
  static {
      infoAcademic.put("horario", "Lunes a Viernes de 08:00 a 12:00.");
      infoAcademic.put("aula", "Laboratorio de Computación, Aula #306.");
      infoAcademic.put("docente", "Ing. Miguel Rodríguez Véliz, Ph.D.");
      infoAcademic.put("materia", "Sistemas Distribuidos (Octavo Semestre).");
      infoAcademic.put("ayuda", "Comandos disponibles: horario, aula, docente, materia, fecha, hora, ayuda, salir.");
  }
  
  public static void main(String[] args) {
      System.out.print("*** Servidor Académico Concurrente Iniciado ***");
      try (ServerSocket servidorSocket = new ServerSocket(PUERTO)) {
          while (true) {
              Socket clienteSocket = servidorSocket.accept();
              System.out.println("Un nuevo cliente se ha conectado desde: " + clienteSocket.getRemoteSocketAddress());
              
              // Crea la atención concurrente multi-hilo para cada clente
              new HiloCliente(clienteSocket).start();
          }
      } catch (IOException e) {
          System.err.println("Atencion: hay un error en el servidor: " + e.getMessage());
      }
  }
  
  private static class HiloCliente extends Thread {
      private final Socket socket;
      
      public HiloCliente(Socket socket) {
          this.socket = socket;
      }
      
      @Override
      public void run() {
          try (
              BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
              PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
          ) {
              salida.println("Su conexión se ha establecido con éxito. Por favor ingrese una consulta o 'ayuda'.");
              
              String peticion;
              // Conexión persistente abierta durante la sesión de consultas
              while ((peticion = entrada.readLine()) != null) {
                  peticion = peticion.trim().toLowerCase();
                  System.out.println("Se ha recibido la consulta: [" + peticion + "]");
                  
                  if (peticion.equals("salir")) {
                      salida.println("Adios. Procediendo a cerrar su conexión.");
                      break;
                  }
                  
                  // Obtención dinámica de datos del sistema
                  if (peticion.equals("fecha")) {
                      salida.println("fecha actual: " + java.time.LocalDate.now());                 
                  } else if (peticion.equals("hora")) {
                      salida.println("Hora actual: " + java.time.LocalTime.now().withNano(0));
                  }
                  // Búsqueda en la estructura de datos interna
                  else if (infoAcademic.containsKey(peticion)) {
                      salida.println(infoAcademic.get(peticion));
                  } else {
                      salida.println("El comando que ha escrito no es reconocido. Por favor escriba 'ayuda' para ver las opciones.");
                  }
              }
          } catch (IOException e) {
              System.err.println("Se ha detectado un error al atender al cliente: " + e.getMessage());
          } finally {
              try {
                  socket.close();
                  System.out.println("Se ha cerrado la conexión únicmanete para este cliente. ");                  
              } catch (IOException e) {
                  System.err.println("Atención: no se pudo cerrar el socket: " + e.getMessage());
              }
          }     
      }
  }
}
