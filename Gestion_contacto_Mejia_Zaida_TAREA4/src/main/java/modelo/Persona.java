package modelo;

import java.io.Serializable;
import java.util.ResourceBundle;

public class Persona implements Serializable {
    private String nombre;
    private String telefono;
    private String email;
    private String categoria;
    private boolean favorito;

    // Constructor agregado
    public Persona(String nombre, String telefono, String email, boolean favorito, String categoria) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.favorito = favorito;
        this.categoria = categoria;
    }

    // Getters (puedes agregar setters si los necesitas)
    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isFavorito() {
        return favorito;
    }

    // Método para mostrar en lista
    public String formatoLista() {
        ResourceBundle mensajes = ResourceBundle.getBundle("i18n.Messages");
        String categoriaTraducida = mensajes.getString("categoria." + categoria.toLowerCase());

        return String.format("%s - %s - %s - %s %s",
            nombre, telefono, email, categoriaTraducida, favorito ? "★" : "");
    }
}
