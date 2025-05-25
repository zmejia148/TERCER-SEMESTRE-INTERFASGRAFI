package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controlador.logica_ventana;

import java.util.Locale;
import java.util.ResourceBundle;

public class ventana extends JFrame {
    // Componentes de la interfaz
    public JPanel contentPane;
    public JTextField txt_nombres, txt_telefono, txt_email, txt_buscar;
    public JCheckBox chb_favorito;
    public JComboBox<String> cmb_categoria;
    public JButton btn_add, btn_modificar, btn_eliminar, btn_buscar, btn_exportar;
    public JList<String> lst_contactos;
    public JProgressBar barra_progreso;
    public JComboBox<IdiomaItem> cmb_idioma;

    // Getters
    public JButton getBtnAdd() { return btn_add; }
    public JButton getBtnModificar() { return btn_modificar; }
    public JButton getBtnEliminar() { return btn_eliminar; }
    public JButton getBtnBuscar() { return btn_buscar; }
    public JButton getBtnExportar() { return btn_exportar; }
    public JTextField getTxtNombres() { return txt_nombres; }
    public JTextField getTxtTelefono() { return txt_telefono; }
    public JTextField getTxtEmail() { return txt_email; }
    public JTextField getTxtBuscar() { return txt_buscar; }
    public JCheckBox getChbFavorito() { return chb_favorito; }
    public JComboBox<String> getCmbCategoria() { return cmb_categoria; }
    public JComboBox<IdiomaItem> getCmbIdioma() { return cmb_idioma; }
    public JList<String> getLstContactos() { return lst_contactos; }
    public JProgressBar getBarraProgreso() { return barra_progreso; }

    public ventana() {
        this(null);
    }

    public ventana(IdiomaItem idiomaPreseleccionado) {
        configurarVentanaPrincipal();
        inicializarComponentesUI();
        configurarInternacionalizacion(idiomaPreseleccionado);
        posicionarComponentes();
        
        // Conectar la lógica con la vista
        new logica_ventana(this);
    }

    private void configurarVentanaPrincipal() {
        setTitle("Contact Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setResizable(false);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);
    }

    private void inicializarComponentesUI() {
        txt_nombres = new JTextField();
        txt_telefono = new JTextField();
        txt_email = new JTextField();
        txt_buscar = new JTextField();
        chb_favorito = new JCheckBox();
        cmb_categoria = new JComboBox<>();
        btn_add = new JButton();
        btn_modificar = new JButton();
        btn_eliminar = new JButton();
        btn_buscar = new JButton();
        btn_exportar = new JButton();
        lst_contactos = new JList<>();
        barra_progreso = new JProgressBar();
        cmb_idioma = new JComboBox<>();
    }

    private void configurarInternacionalizacion(IdiomaItem idiomaPreseleccionado) {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Messages", Locale.getDefault());
        
        cmb_idioma.addItem(new IdiomaItem("es", bundle.getString("idioma.es")));
        cmb_idioma.addItem(new IdiomaItem("en", bundle.getString("idioma.en")));
        cmb_idioma.addItem(new IdiomaItem("fr", bundle.getString("idioma.fr")));
        
        if (idiomaPreseleccionado != null) {
            cmb_idioma.setSelectedItem(idiomaPreseleccionado);
        } else {
            setDefaultLanguageSelection();
        }
    }

    private void setDefaultLanguageSelection() {
        String currentLang = Locale.getDefault().getLanguage();
        for (int i = 0; i < cmb_idioma.getItemCount(); i++) {
            if (cmb_idioma.getItemAt(i).getCodigo().equals(currentLang)) {
                cmb_idioma.setSelectedIndex(i);
                break;
            }
        }
    }

    private void posicionarComponentes() {
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Messages", Locale.getDefault());

        // Combo idioma
        JLabel lbl_idioma = new JLabel(bundle.getString("etiqueta.idioma"));
        lbl_idioma.setBounds(10, 10, 70, 25);
        contentPane.add(lbl_idioma);

        cmb_idioma.setBounds(80, 10, 150, 25);
        contentPane.add(cmb_idioma);

        // Campos de texto
        JLabel lbl_nombre = new JLabel(bundle.getString("etiqueta.nombre"));
        lbl_nombre.setBounds(10, 50, 100, 25);
        contentPane.add(lbl_nombre);

        txt_nombres.setBounds(120, 50, 200, 25);
        contentPane.add(txt_nombres);

        JLabel lbl_telefono = new JLabel(bundle.getString("etiqueta.telefono"));
        lbl_telefono.setBounds(10, 90, 100, 25);
        contentPane.add(lbl_telefono);

        txt_telefono.setBounds(120, 90, 200, 25);
        contentPane.add(txt_telefono);

        JLabel lbl_email = new JLabel(bundle.getString("etiqueta.email"));
        lbl_email.setBounds(10, 130, 100, 25);
        contentPane.add(lbl_email);

        txt_email.setBounds(120, 130, 200, 25);
        contentPane.add(txt_email);

        // Checkbox y combo categoría
        chb_favorito.setText(bundle.getString("etiqueta.favorito"));
        chb_favorito.setBounds(10, 170, 200, 25);
        contentPane.add(chb_favorito);

        cmb_categoria.setBounds(220, 170, 200, 25);
        contentPane.add(cmb_categoria);

        // Botones
        btn_add.setText(bundle.getString("boton.agregar"));
        btn_add.setBounds(350, 50, 120, 30);
        contentPane.add(btn_add);

        btn_modificar.setText(bundle.getString("boton.modificar"));
        btn_modificar.setBounds(480, 50, 120, 30);
        contentPane.add(btn_modificar);

        btn_eliminar.setText(bundle.getString("boton.eliminar"));
        btn_eliminar.setBounds(610, 50, 120, 30);
        contentPane.add(btn_eliminar);

        // Lista de contactos
        JScrollPane scroll = new JScrollPane(lst_contactos);
        scroll.setBounds(10, 210, 860, 200);
        contentPane.add(scroll);

        // Búsqueda
        JLabel lbl_buscar = new JLabel(bundle.getString("etiqueta.buscar"));
        lbl_buscar.setBounds(10, 430, 140, 25);
        contentPane.add(lbl_buscar);

        txt_buscar.setBounds(160, 430, 300, 25);
        contentPane.add(txt_buscar);

        btn_buscar.setText(bundle.getString("boton.buscar"));
        btn_buscar.setBounds(470, 430, 100, 25);
        contentPane.add(btn_buscar);

        btn_exportar.setText(bundle.getString("boton.exportar"));
        btn_exportar.setBounds(580, 430, 120, 25);
        contentPane.add(btn_exportar);

        // Barra de progreso
        barra_progreso.setBounds(10, 470, 860, 20);
        barra_progreso.setVisible(false);
        contentPane.add(barra_progreso);
    }

    public static class IdiomaItem {
        private final String codigo;
        private final String nombre;

        public IdiomaItem(String codigo, String nombre) {
            this.codigo = codigo;
            this.nombre = nombre;
        }

        public String getCodigo() {
            return codigo;
        }

        @Override
        public String toString() {
            return nombre;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            IdiomaItem that = (IdiomaItem) obj;
            return codigo.equals(that.codigo);
        }

        @Override
        public int hashCode() {
            return codigo.hashCode();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception ex) {
                System.err.println("Error al cargar FlatLaf");
            }
            new ventana().setVisible(true);
        });
    }
}