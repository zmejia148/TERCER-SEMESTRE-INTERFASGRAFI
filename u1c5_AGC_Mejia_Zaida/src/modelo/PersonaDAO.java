package modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO {

    private File archivo;
    private Persona persona;
    private static final String NOMBRE_ARCHIVO = "datosContactos.csv";
    private static final String ENCABEZADO = "NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO";

    public PersonaDAO(Persona persona) {
        this.persona = persona;
        archivo = new File("c:/gestionContactos");
        prepararArchivo();
    }

    private void prepararArchivo() {
        if (!archivo.exists()) {
            archivo.mkdir();
        }

        archivo = new File(archivo.getAbsolutePath(), NOMBRE_ARCHIVO);

        if (!archivo.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                bw.write(ENCABEZADO);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void escribir(String texto) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(texto);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean escribirArchivo() {
        if (persona == null) return false;

        // Verificación de campos vacíos
        if (persona.getNombre().isEmpty() ||
            persona.getTelefono().isEmpty() ||
            persona.getEmail().isEmpty() ||
            persona.getCategoria().isEmpty()) {
            System.out.println("❌ Datos incompletos. No se guardó.");
            return false;
        }

        escribir(persona.toCSV());
        return true;
    }

    public List<Persona> leerArchivo() throws IOException {
        List<Persona> personas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                if (linea.trim().isEmpty()) continue;

                String[] campos = linea.split(";");
                if (campos.length < 5) continue;

                Persona p = new Persona(
                    campos[0],                      // nombre
                    campos[1],                      // telefono
                    campos[2],                      // email
                    Boolean.parseBoolean(campos[4]),// favorito
                    campos[3]                       // categoria
                );

                personas.add(p);
            }
        }

        return personas;
    }

    public void actualizarContactos(List<Persona> personas) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
            bw.write(ENCABEZADO);
            bw.newLine();

            for (Persona p : personas) {
                bw.write(p.toCSV());
                bw.newLine();
            }
        }
    }
}
