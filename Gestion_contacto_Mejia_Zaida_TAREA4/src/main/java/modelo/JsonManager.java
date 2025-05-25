package modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonManager {
    private static final String ARCHIVO_JSON = "contactos.json";
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void guardarContactos(List<Persona> contactos) throws IOException {
        // Crear directorio si no existe
        File directorio = new File("data");
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        
        try (Writer writer = new FileWriter("data/" + ARCHIVO_JSON)) {
            gson.toJson(contactos, writer);
            writer.flush(); // Forzar escritura inmediata
        }
    }

    public static List<Persona> cargarContactos() throws IOException {
        File archivo = new File("data/" + ARCHIVO_JSON);
        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(archivo)) {
            Type tipoLista = new TypeToken<List<Persona>>(){}.getType();
            List<Persona> contactos = gson.fromJson(reader, tipoLista);
            return contactos != null ? contactos : new ArrayList<>();
        }
    }
}