package controlador;

import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modelo.Persona;
import modelo.PersonaDAO;
import vista.ventana;

public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {

    public ventana delegado;
    public ArrayList<Persona> contactos = new ArrayList<>();
    public int indiceSeleccionado = -1;
    public PersonaDAO dao;

    public logica_ventana(ventana v) {
        this.delegado = v;

        // Cargar contactos
        dao = new PersonaDAO(new Persona());
        try {
            contactos.addAll(dao.leerArchivo());
        } catch (IOException e) {
            e.printStackTrace();
        }

        actualizarLista();

        // Asignar listeners
        delegado.btn_add.addActionListener(this);
        delegado.btn_modificar.addActionListener(this);
        delegado.btn_eliminar.addActionListener(this);
        delegado.lst_contactos.addListSelectionListener(this);
        delegado.cmb_categoria.addItemListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == delegado.btn_add) {
            agregarContacto();
        } else if (e.getSource() == delegado.btn_modificar) {
            modificarContacto();
        } else if (e.getSource() == delegado.btn_eliminar) {
            eliminarContacto();
        }
    }

    private void agregarContacto() {
        Persona p = obtenerDatosFormulario();
        if (p != null) {
            contactos.add(p);
            guardarCambios();
            actualizarLista();
            limpiarFormulario();
        }
    }

    private void modificarContacto() {
        if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
            Persona p = obtenerDatosFormulario();
            if (p != null) {
                contactos.set(indiceSeleccionado, p);
                guardarCambios();
                actualizarLista();
                limpiarFormulario();
            }
        }
    }

    private void eliminarContacto() {
        if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
            contactos.remove(indiceSeleccionado);
            guardarCambios();
            actualizarLista();
            limpiarFormulario();
        }
    }

    private void guardarCambios() {
        try {
            dao.actualizarContactos(contactos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Persona obtenerDatosFormulario() {
        String nombre = delegado.txt_nombres.getText().trim();
        String telefono = delegado.txt_telefono.getText().trim();
        String email = delegado.txt_email.getText().trim();
        boolean favorito = delegado.chb_favorito.isSelected();
        String categoria = delegado.cmb_categoria.getSelectedItem().toString();

        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || categoria.equals("Elija una Categoria")) {
            return null;
        }

        return new Persona(nombre, telefono, email, favorito, categoria);
    }

    private void actualizarLista() {
        DefaultListModel modelo = new DefaultListModel();
        for (Persona p : contactos) {
            String info = p.getNombre() + " - " + p.getTelefono() +
                          (p.isFavorito() ? " ⭐" : "") + " [" + p.getCategoria() + "]";
            modelo.addElement(info);
        }
        delegado.lst_contactos.setModel(modelo);
    }

    private void limpiarFormulario() {
        delegado.txt_nombres.setText("");
        delegado.txt_telefono.setText("");
        delegado.txt_email.setText("");
        delegado.chb_favorito.setSelected(false);
        delegado.cmb_categoria.setSelectedIndex(0);
        delegado.lst_contactos.clearSelection();
        indiceSeleccionado = -1;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            indiceSeleccionado = delegado.lst_contactos.getSelectedIndex();
            if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
                cargarContacto(indiceSeleccionado);
            }
        }
    }

    private void cargarContacto(int index) {
        Persona p = contactos.get(index);
        delegado.txt_nombres.setText(p.getNombre());
        delegado.txt_telefono.setText(p.getTelefono());
        delegado.txt_email.setText(p.getEmail());
        delegado.chb_favorito.setSelected(p.isFavorito());
        delegado.cmb_categoria.setSelectedItem(p.getCategoria());
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Esto es opcional
        if (e.getSource() == delegado.cmb_categoria && e.getStateChange() == ItemEvent.SELECTED) {
            String categoria = delegado.cmb_categoria.getSelectedItem().toString();
        }
    }
}
