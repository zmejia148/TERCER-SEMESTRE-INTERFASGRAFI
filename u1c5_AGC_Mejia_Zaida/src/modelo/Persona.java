package modelo;

public class Persona {

    private String nombre;
    private String telefono;
    private String email; 
    private String categoria;
    private boolean favorito;
    
    public Persona() {
        this("", "", "", false, "");
    }
    

    // Constructor con todos los parámetros
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

    // Formato en columnas (puede usarse para consola o debug)
    public String formatoLista() {
        return String.format("%-30s %-20s %-30s %-15s %-10s", nombre, telefono, email, categoria, favorito ? "★" : " ");
    }

    // Devuelve un array con los datos (útil para JTable)
    public Object[] toObjectArray() {
        return new Object[] { nombre, telefono, email, categoria, favorito };
    }
}








