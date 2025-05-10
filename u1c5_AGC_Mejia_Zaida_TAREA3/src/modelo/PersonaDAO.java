package modelo;

import java.io.*;
import java.util.*;

public class PersonaDAO {

    private static final String NOMBRE_ARCHIVO = "datosContactos.csv";
    private static final String ENCABEZADO = "NOMBRE;TELEFONO;EMAIL;CATEGORIA;FAVORITO";
    private static final File archivo = new File("c:/gestionContactos", NOMBRE_ARCHIVO);

    public PersonaDAO(Persona persona) {
        prepararArchivo();
    }

    private void prepararArchivo() {
        File carpeta = new File("c:/gestionContactos");
        if (!carpeta.exists()) carpeta.mkdir();

        if (!archivo.exists()) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
                bw.write(ENCABEZADO);
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean escribirContacto(Persona persona) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
            bw.write(persona.toCSV());
            bw.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized List<Persona> leerArchivo() throws IOException {
        List<Persona> personas = new ArrayList<>();
        if (!archivo.exists()) return personas;

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
                        campos[0],
                        campos[1],
                        campos[2],
                        Boolean.parseBoolean(campos[4]),
                        campos[3]
                );
                personas.add(p);
            }
        }
        return personas;
    }

    public synchronized void actualizarContactos(List<Persona> personas) throws IOException {
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
