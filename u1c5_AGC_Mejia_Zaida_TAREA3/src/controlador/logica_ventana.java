package controlador;

import modelo.Persona;
import modelo.PersonaDAO;
import vista.ventana;
import vista.ventana.IdiomaItem;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {

    private final ventana delegado;
    private final ArrayList<Persona> contactos;
    private int indiceSeleccionado = -1;
    private final List<String> categoriasValidas = Arrays.asList("Amigos", "Familia", "Trabajo");

    public logica_ventana(ventana v) {
        this.delegado = v;
        this.contactos = new ArrayList<>();

        try {
            this.contactos.addAll(new PersonaDAO(null).leerArchivo());
            actualizarLista();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        delegado.btn_add.addActionListener(this);
        delegado.btn_modificar.addActionListener(this);
        delegado.btn_eliminar.addActionListener(this);
        delegado.btn_buscar.addActionListener(this);
        delegado.btn_exportar.addActionListener(this);
        delegado.lst_contactos.addListSelectionListener(this);
        delegado.cmb_idioma.addItemListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == delegado.btn_add) {
            new Thread(this::agregarContacto).start();
        } else if (e.getSource() == delegado.btn_modificar) {
            modificarContacto();
        } else if (e.getSource() == delegado.btn_eliminar) {
            eliminarContacto();
        } else if (e.getSource() == delegado.btn_buscar) {
            buscarContacto();
        } else if (e.getSource() == delegado.btn_exportar) {
            exportarContactos();
        }
    }

    private void agregarContacto() {
        Persona p = obtenerDatosFormulario();
        if (p == null) {
            SwingUtilities.invokeLater(() -> mostrarMensaje("Todos los campos deben estar completos y la categoría debe ser válida"));
            return;
        }

        synchronized (contactos) {
            boolean duplicado = contactos.stream().anyMatch(c ->
                    c.getNombre().equalsIgnoreCase(p.getNombre()) ||
                    c.getTelefono().equalsIgnoreCase(p.getTelefono()) ||
                    c.getEmail().equalsIgnoreCase(p.getEmail()));

            if (duplicado) {
                SwingUtilities.invokeLater(() -> mostrarMensaje("Contacto ya existe"));
                return;
            }

            contactos.add(p);
        }

        SwingUtilities.invokeLater(() -> {
            actualizarLista();
            limpiarFormulario();
            guardarContactosEnArchivo();
            mostrarMensaje("Contacto agregado con éxito");
        });
    }

    private void modificarContacto() {
        if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
            Persona p = obtenerDatosFormulario();
            if (p != null) {
                contactos.set(indiceSeleccionado, p);
                actualizarLista();
                limpiarFormulario();
                guardarContactosEnArchivo();
                mostrarMensaje("Contacto modificado");
            }
        }
    }

    private void eliminarContacto() {
        if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
            contactos.remove(indiceSeleccionado);
            actualizarLista();
            limpiarFormulario();
            guardarContactosEnArchivo();
            mostrarMensaje("Contacto eliminado");
        }
    }

    private void buscarContacto() {
        String buscar = delegado.txt_buscar.getText().trim().toLowerCase();
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Persona p : contactos) {
            if (p.getNombre().toLowerCase().contains(buscar)) {
                modelo.addElement(p.getNombre() + " - " + p.getTelefono());
            }
        }
        delegado.lst_contactos.setModel(modelo);
    }

    private void exportarContactos() {
        guardarContactosEnArchivo();
        mostrarMensaje("Exportación completada");
    }

    private Persona obtenerDatosFormulario() {
        String nombre = delegado.txt_nombres.getText().trim();
        String telefono = delegado.txt_telefono.getText().trim();
        String email = delegado.txt_email.getText().trim();
        boolean favorito = delegado.chb_favorito.isSelected();
        String categoria = (String) delegado.cmb_categoria.getSelectedItem();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || categoria == null || !categoriasValidas.contains(categoria)) {
            return null;
        }

        return new Persona(nombre, telefono, email, favorito, categoria);
    }

    private void actualizarLista() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Persona p : contactos) {
            if (!categoriasValidas.contains(p.getCategoria())) continue;

            String info = String.format("%s - %s - %s - %s %s",
                p.getNombre(),
                p.getTelefono(),
                p.getEmail(),
                p.getCategoria(),
                p.isFavorito() ? "★" : "");
            modelo.addElement(info);
        }
        delegado.lst_contactos.setModel(modelo);
    }

    private void limpiarFormulario() {
        delegado.txt_nombres.setText("");
        delegado.txt_telefono.setText("");
        delegado.txt_email.setText("");
        delegado.txt_buscar.setText("");
        delegado.chb_favorito.setSelected(false);
        delegado.cmb_categoria.setSelectedIndex(0);
        delegado.lst_contactos.clearSelection();
        indiceSeleccionado = -1;
    }

    private void guardarContactosEnArchivo() {
        try {
            new PersonaDAO(null).actualizarContactos(contactos);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(delegado, "Error al guardar contactos.");
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(delegado, mensaje);
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            indiceSeleccionado = delegado.lst_contactos.getSelectedIndex();
            if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
                Persona p = contactos.get(indiceSeleccionado);
                delegado.txt_nombres.setText(p.getNombre());
                delegado.txt_telefono.setText(p.getTelefono());
                delegado.txt_email.setText(p.getEmail());
                delegado.chb_favorito.setSelected(p.isFavorito());
                delegado.cmb_categoria.setSelectedItem(p.getCategoria());
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == delegado.cmb_idioma && e.getStateChange() == ItemEvent.SELECTED) {
            IdiomaItem seleccionado = (IdiomaItem) delegado.cmb_idioma.getSelectedItem();
            Locale nuevo = switch (seleccionado.getCodigo()) {
                case "en" -> new Locale("en", "US");
                case "fr" -> new Locale("fr", "FR");
                default -> new Locale("es", "ES");
            };
            Locale.setDefault(nuevo);
            SwingUtilities.invokeLater(() -> {
                delegado.dispose();
                new ventana().setVisible(true);
            });
        }
    }
}
