package modelo;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

public class Persona implements Serializable {

    private String nombre;
    private String telefono;
    private String email;
    private String categoria;
    private boolean favorito;

    public Persona() {
        this("", "", "", false, "");
    }

    public Persona(String nombre, String telefono, String email, boolean favorito, String categoria) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.favorito = favorito;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public String toCSV() {
        return String.format("%s;%s;%s;%s;%s", nombre, telefono, email, categoria, favorito);
    }

    public String formatoLista() {
    ResourceBundle mensajes = ResourceBundle.getBundle("i18n.Messages", Locale.getDefault());

    String categoriaTraducida = switch (categoria.toLowerCase()) {
        case "familia", "family", "famille" -> mensajes.getString("categoria.familia");
        case "amigos", "friends", "amis" -> mensajes.getString("categoria.amigos");
        case "trabajo", "work", "travail" -> mensajes.getString("categoria.trabajo");
        default -> categoria;
    };

    return String.format("%-30s %-20s %-30s %-15s %-10s",
            nombre, telefono, email, categoriaTraducida, favorito ? "★" : " ");
}

    public Object[] toObjectArray() {
        return new Object[] { nombre, telefono, email, categoria, favorito };
    }
} 