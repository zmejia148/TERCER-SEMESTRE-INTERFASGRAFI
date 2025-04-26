package controlador;

import modelo.Persona;
import modelo.PersonaDAO;
import vista.ventana;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {

    public static String idiomaSeleccionado = "Español"; // Guardar el idioma actual

    private ventana vista;
    private List<Persona> contactos;
    private int indiceSeleccionado = -1;

    public logica_ventana(ventana v) {
        this.vista = v;
        this.contactos = new ArrayList<>();

        // Cargar contactos desde archivo
        try {
            contactos = new PersonaDAO(new Persona()).leerArchivo();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar contactos: " + e.getMessage());
        }

        actualizarLista();

        vista.btn_add.addActionListener(this);
        vista.btn_modificar.addActionListener(this);
        vista.btn_eliminar.addActionListener(this);
        vista.btn_buscar.addActionListener(this);
        vista.btn_exportar.addActionListener(this);
        vista.cmb_categoria.addItemListener(this);
        vista.cmb_idioma.addItemListener(this);
        vista.lst_contactos.addListSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src == vista.btn_add) agregarContacto();
        else if (src == vista.btn_modificar) modificarContacto();
        else if (src == vista.btn_eliminar) eliminarContacto();
        else if (src == vista.btn_buscar) buscarContacto();
        else if (src == vista.btn_exportar) exportarContactos();
    }

    private void agregarContacto() {
        Persona p = obtenerFormulario();
        if (p != null) {
            contactos.add(p);
            guardarCambios();
            actualizarLista();
            limpiarFormulario();
        }
    }

    private void modificarContacto() {
        if (indiceSeleccionado >= 0) {
            Persona p = obtenerFormulario();
            if (p != null) {
                contactos.set(indiceSeleccionado, p);
                guardarCambios();
                actualizarLista();
                limpiarFormulario();
            }
        }
    }

    private void eliminarContacto() {
        if (indiceSeleccionado >= 0) {
            contactos.remove(indiceSeleccionado);
            guardarCambios();
            actualizarLista();
            limpiarFormulario();
        }
    }

    private void buscarContacto() {
        String filtro = vista.txt_buscar.getText().toLowerCase();
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Persona p : contactos) {
            if (p.getNombre().toLowerCase().contains(filtro)) {
                modelo.addElement(p.formatoLista());
            }
        }
        vista.lst_contactos.setModel(modelo);
    }

    private void exportarContactos() {
        new Thread(() -> {
            vista.barra_progreso.setVisible(true);
            vista.barra_progreso.setValue(0);
            for (int i = 0; i <= 100; i += 10) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}
                vista.barra_progreso.setValue(i);
            }
            try {
                new PersonaDAO(new Persona()).actualizarContactos(contactos);
                JOptionPane.showMessageDialog(vista, "Exportación completada.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(vista, "Error exportando: " + e.getMessage());
            }
            vista.barra_progreso.setVisible(false);
        }).start();
    }

    private void actualizarLista() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Persona p : contactos) {
            modelo.addElement(p.formatoLista());
        }
        vista.lst_contactos.setModel(modelo);
    }

    private void limpiarFormulario() {
        vista.txt_nombres.setText("");
        vista.txt_telefono.setText("");
        vista.txt_email.setText("");
        vista.txt_buscar.setText("");
        vista.chb_favorito.setSelected(false);
        vista.cmb_categoria.setSelectedIndex(0);
        vista.lst_contactos.clearSelection();
        indiceSeleccionado = -1;
    }

    private Persona obtenerFormulario() {
        String nombre = vista.txt_nombres.getText().trim();
        String telefono = vista.txt_telefono.getText().trim();
        String email = vista.txt_email.getText().trim();
        String categoria = Objects.toString(vista.cmb_categoria.getSelectedItem(), "");
        boolean favorito = vista.chb_favorito.isSelected();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || categoria.equals("Elija una Categoria")) {
            JOptionPane.showMessageDialog(vista, "Todos los campos deben estar completos.");
            return null;
        }

        return new Persona(nombre, telefono, email, favorito, categoria);
    }

    private void guardarCambios() {
        try {
            new PersonaDAO(new Persona()).actualizarContactos(contactos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            indiceSeleccionado = vista.lst_contactos.getSelectedIndex();
            if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
                Persona p = contactos.get(indiceSeleccionado);
                vista.txt_nombres.setText(p.getNombre());
                vista.txt_telefono.setText(p.getTelefono());
                vista.txt_email.setText(p.getEmail());
                vista.chb_favorito.setSelected(p.isFavorito());
                vista.cmb_categoria.setSelectedItem(p.getCategoria());
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == vista.cmb_idioma && e.getStateChange() == ItemEvent.SELECTED) {
            String idioma = Objects.toString(vista.cmb_idioma.getSelectedItem(), "").toLowerCase();
            idiomaSeleccionado = vista.cmb_idioma.getSelectedItem().toString(); // Guardar el idioma
            Locale nuevoLocale = switch (idioma) {
                case "english" -> new Locale("en", "US");
                case "français" -> new Locale("fr", "FR");
                default -> new Locale("es", "ES");
            };
            Locale.setDefault(nuevoLocale);
            vista.dispose();
            new ventana().setVisible(true);
        }
    }
}
