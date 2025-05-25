package controlador;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import modelo.*;
import vista.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class logica_ventana implements ActionListener, ListSelectionListener, ItemListener {
    private static final Logger logger = LoggerFactory.getLogger(logica_ventana.class);
    private final ventana vista;
    private final List<Persona> contactos;
    private int indiceSeleccionado = -1;
    private static final Set<String> CATEGORIAS_VALIDAS = Set.of("Amigos", "Familia", "Trabajo");
    private ResourceBundle bundle;

    public logica_ventana(ventana v) {
        this.vista = v;
        this.contactos = new ArrayList<>();
        this.bundle = ResourceBundle.getBundle("i18n.Messages", Locale.getDefault());
        init();
    }

    private void init() {
        cargarContactosIniciales();
        configurarListeners();
        configurarCategorias();
        actualizarVista();
    }

    private void configurarCategorias() {
        vista.getCmbCategoria().removeAllItems();
        vista.getCmbCategoria().addItem(bundle.getString("categoria.amigos"));
        vista.getCmbCategoria().addItem(bundle.getString("categoria.familia"));
        vista.getCmbCategoria().addItem(bundle.getString("categoria.trabajo"));
    }

    private void cargarContactosIniciales() {
        try {
            List<Persona> contactosCargados = JsonManager.cargarContactos();
            if (contactosCargados != null) {
                contactos.addAll(contactosCargados.stream()
                    .filter(p -> CATEGORIAS_VALIDAS.contains(p.getCategoria()))
                    .collect(Collectors.toList()));
            }
            logger.info("Contactos cargados: {}", contactos.size());
        } catch (IOException ex) {
            logger.error("Error al cargar contactos", ex);
            mostrarError("error.cargar.contactos", ex);
        }
    }

    private void configurarListeners() {
        vista.getBtnAdd().addActionListener(this);
        vista.getBtnModificar().addActionListener(this);
        vista.getBtnEliminar().addActionListener(this);
        vista.getBtnBuscar().addActionListener(this);
        vista.getBtnExportar().addActionListener(this);
        vista.getLstContactos().addListSelectionListener(this);
        vista.getCmbIdioma().addItemListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnAdd()) {
            agregarContacto();
        } else if (e.getSource() == vista.getBtnModificar()) {
            modificarContacto();
        } else if (e.getSource() == vista.getBtnEliminar()) {
            eliminarContacto();
        } else if (e.getSource() == vista.getBtnBuscar()) {
            buscarContacto();
        } else if (e.getSource() == vista.getBtnExportar()) {
            exportarContactos();
        }
    }

    private void agregarContacto() {
        Optional<Persona> personaOpt = obtenerDatosFormulario();
        if (personaOpt.isEmpty()) {
            mostrarMensaje("error.campos.incompletos");
            return;
        }

        Persona nuevaPersona = personaOpt.get();
        if (esDuplicado(nuevaPersona)) {
            mostrarMensaje("error.contacto.duplicado");
            return;
        }

        contactos.add(nuevaPersona);
        guardarYActualizar();
        mostrarMensaje("exito.contacto.agregado");
    }

    private boolean esDuplicado(Persona persona) {
        return contactos.stream().anyMatch(c ->
            c.getNombre().equalsIgnoreCase(persona.getNombre()) ||
            c.getTelefono().equals(persona.getTelefono()) ||
            c.getEmail().equalsIgnoreCase(persona.getEmail()));
    }

    private Optional<Persona> obtenerDatosFormulario() {
        String nombre = vista.getTxtNombres().getText().trim();
        String telefono = vista.getTxtTelefono().getText().trim();
        String email = vista.getTxtEmail().getText().trim();
        boolean favorito = vista.getChbFavorito().isSelected();
        String categoriaTraducida = (String) vista.getCmbCategoria().getSelectedItem();
        
        if (nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || categoriaTraducida == null) {
            return Optional.empty();
        }

        String categoria = obtenerCategoriaOriginal(categoriaTraducida);
        if (categoria == null || !CATEGORIAS_VALIDAS.contains(categoria)) {
            return Optional.empty();
        }

        return Optional.of(new Persona(nombre, telefono, email, favorito, categoria));
    }

    private String obtenerCategoriaOriginal(String categoriaTraducida) {
        if (categoriaTraducida.equals(bundle.getString("categoria.amigos"))) {
            return "Amigos";
        } else if (categoriaTraducida.equals(bundle.getString("categoria.familia"))) {
            return "Familia";
        } else if (categoriaTraducida.equals(bundle.getString("categoria.trabajo"))) {
            return "Trabajo";
        }
        return null;
    }

    private void modificarContacto() {
        if (indiceSeleccionado < 0 || indiceSeleccionado >= contactos.size()) {
            mostrarMensaje("error.seleccion.contacto");
            return;
        }

        Optional<Persona> personaOpt = obtenerDatosFormulario();
        if (personaOpt.isEmpty()) {
            mostrarMensaje("error.campos.incompletos");
            return;
        }

        contactos.set(indiceSeleccionado, personaOpt.get());
        guardarYActualizar();
        mostrarMensaje("exito.contacto.modificado");
    }

    private void eliminarContacto() {
        if (indiceSeleccionado < 0 || indiceSeleccionado >= contactos.size()) {
            mostrarMensaje("error.seleccion.contacto");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            vista, 
            bundle.getString("confirmacion.eliminar"),
            bundle.getString("titulo.confirmacion"),
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            contactos.remove(indiceSeleccionado);
            guardarYActualizar();
            mostrarMensaje("exito.contacto.eliminado");
        }
    }

    private void buscarContacto() {
        String buscar = vista.getTxtBuscar().getText().trim().toLowerCase();
        DefaultListModel<String> modelo = new DefaultListModel<>();
        
        contactos.stream()
            .filter(p -> p.getNombre().toLowerCase().contains(buscar) ||
                         p.getTelefono().toLowerCase().contains(buscar) ||
                         p.getEmail().toLowerCase().contains(buscar))
            .forEach(p -> modelo.addElement(p.formatoLista()));
            
        vista.getLstContactos().setModel(modelo);
    }

    private void exportarContactos() {
        try {
            JsonManager.guardarContactos(contactos);
            mostrarMensaje("exito.exportacion.completa");
        } catch (IOException ex) {
            logger.error("Error al exportar contactos", ex);
            mostrarError("error.exportacion", ex);
        }
    }

    private void guardarYActualizar() {
        try {
            JsonManager.guardarContactos(contactos);
            actualizarVista();
            limpiarFormulario();
        } catch (IOException ex) {
            logger.error("Error al guardar contactos", ex);
            mostrarError("error.guardar.contactos", ex);
        }
    }

    private void actualizarVista() {
        DefaultListModel<String> modelo = new DefaultListModel<>();
        
        contactos.stream()
            .filter(p -> CATEGORIAS_VALIDAS.contains(p.getCategoria()))
            .forEach(p -> modelo.addElement(p.formatoLista()));
            
        vista.getLstContactos().setModel(modelo);
    }

    private void limpiarFormulario() {
        vista.getTxtNombres().setText("");
        vista.getTxtTelefono().setText("");
        vista.getTxtEmail().setText("");
        vista.getTxtBuscar().setText("");
        vista.getChbFavorito().setSelected(false);
        vista.getCmbCategoria().setSelectedIndex(0);
        vista.getLstContactos().clearSelection();
        indiceSeleccionado = -1;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            indiceSeleccionado = vista.getLstContactos().getSelectedIndex();
            if (indiceSeleccionado >= 0 && indiceSeleccionado < contactos.size()) {
                Persona p = contactos.get(indiceSeleccionado);
                vista.getTxtNombres().setText(p.getNombre());
                vista.getTxtTelefono().setText(p.getTelefono());
                vista.getTxtEmail().setText(p.getEmail());
                vista.getChbFavorito().setSelected(p.isFavorito());
                
                String categoriaKey = "categoria." + p.getCategoria().toLowerCase();
                String categoriaTraducida = bundle.getString(categoriaKey);
                vista.getCmbCategoria().setSelectedItem(categoriaTraducida);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == vista.getCmbIdioma() && e.getStateChange() == ItemEvent.SELECTED) {
            ventana.IdiomaItem seleccionado = (ventana.IdiomaItem) vista.getCmbIdioma().getSelectedItem();
            Locale nuevo = switch (seleccionado.getCodigo()) {
                case "en" -> new Locale("en", "US");
                case "fr" -> new Locale("fr", "FR");
                default -> new Locale("es", "ES");
            };
            
            Locale.setDefault(nuevo);
            this.bundle = ResourceBundle.getBundle("i18n.Messages", nuevo);
            
            SwingUtilities.invokeLater(() -> {
                vista.dispose();
                new ventana(seleccionado).setVisible(true);
            });
        }
    }

    private void mostrarMensaje(String claveMensaje) {
        JOptionPane.showMessageDialog(
            vista, 
            bundle.getString(claveMensaje),
            bundle.getString("titulo.mensaje"), 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String claveMensaje, Exception ex) {
        String mensaje = bundle.getString(claveMensaje);
        String detalle = String.format("%s%nCausa: %s", mensaje, ex.getMessage());
        
        JOptionPane.showMessageDialog(
            vista, 
            detalle,
            bundle.getString("titulo.error"), 
            JOptionPane.ERROR_MESSAGE);
    }
}