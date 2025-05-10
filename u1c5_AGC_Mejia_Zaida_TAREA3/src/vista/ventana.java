package vista;

import controlador.logica_ventana;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.Locale;
import java.util.ResourceBundle;

public class ventana extends JFrame {

    public JPanel contentPane;
    public JTextField txt_nombres, txt_telefono, txt_email, txt_buscar;
    public JCheckBox chb_favorito;
    public JComboBox<String> cmb_categoria;
    public JButton btn_add, btn_modificar, btn_eliminar, btn_buscar, btn_exportar;
    public JList<String> lst_contactos;
    public JProgressBar barra_progreso;
    public JComboBox<IdiomaItem> cmb_idioma;

    public ventana() {
        this(null); 
    }

    public ventana(IdiomaItem idiomaPreseleccionado) {
        setTitle("Contact Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        ResourceBundle bundle = ResourceBundle.getBundle("i18n.Messages", Locale.getDefault());

        // Configuración del combo de idiomas
        cmb_idioma = new JComboBox<>();
        cmb_idioma.setBounds(80, 10, 150, 25);
        cmb_idioma.addItem(new IdiomaItem("es", "Español"));
        cmb_idioma.addItem(new IdiomaItem("en", "English"));
        cmb_idioma.addItem(new IdiomaItem("fr", "Français"));
        
        if (idiomaPreseleccionado != null) {
            cmb_idioma.setSelectedItem(idiomaPreseleccionado);
        } else {
            String currentLang = Locale.getDefault().getLanguage();
            for (int i = 0; i < cmb_idioma.getItemCount(); i++) {
                if (cmb_idioma.getItemAt(i).getCodigo().equals(currentLang)) {
                    cmb_idioma.setSelectedIndex(i);
                    break;
                }
            }
        }
        contentPane.add(cmb_idioma);

        JLabel lbl_idioma = new JLabel(bundle.getString("etiqueta.idioma"));
        lbl_idioma.setBounds(10, 10, 70, 25);
        contentPane.add(lbl_idioma);

        JLabel lbl_nombre = new JLabel(bundle.getString("etiqueta.nombre"));
        lbl_nombre.setBounds(10, 50, 100, 25);
        contentPane.add(lbl_nombre);

        txt_nombres = new JTextField();
        txt_nombres.setBounds(120, 50, 200, 25);
        contentPane.add(txt_nombres);

        JLabel lbl_telefono = new JLabel(bundle.getString("etiqueta.telefono"));
        lbl_telefono.setBounds(10, 90, 100, 25);
        contentPane.add(lbl_telefono);

        txt_telefono = new JTextField();
        txt_telefono.setBounds(120, 90, 200, 25);
        contentPane.add(txt_telefono);

        JLabel lbl_email = new JLabel(bundle.getString("etiqueta.email"));
        lbl_email.setBounds(10, 130, 100, 25);
        contentPane.add(lbl_email);

        txt_email = new JTextField();
        txt_email.setBounds(120, 130, 200, 25);
        contentPane.add(txt_email);

        chb_favorito = new JCheckBox(bundle.getString("etiqueta.favorito"));
        chb_favorito.setBounds(10, 170, 200, 25);
        contentPane.add(chb_favorito);

        cmb_categoria = new JComboBox<>();
        cmb_categoria.setBounds(220, 170, 200, 25);
        cmb_categoria.addItem(bundle.getString("categoria.elegir"));
        cmb_categoria.addItem(bundle.getString("categoria.amigos"));
        cmb_categoria.addItem(bundle.getString("categoria.familia"));
        cmb_categoria.addItem(bundle.getString("categoria.trabajo"));
        contentPane.add(cmb_categoria);

        btn_add = new JButton(bundle.getString("boton.agregar"));
        btn_add.setBounds(350, 50, 120, 30);
        contentPane.add(btn_add);

        btn_modificar = new JButton(bundle.getString("boton.modificar"));
        btn_modificar.setBounds(480, 50, 120, 30);
        contentPane.add(btn_modificar);

        btn_eliminar = new JButton(bundle.getString("boton.eliminar"));
        btn_eliminar.setBounds(610, 50, 120, 30);
        contentPane.add(btn_eliminar);

        lst_contactos = new JList<>();
        JScrollPane scroll = new JScrollPane(lst_contactos);
        scroll.setBounds(10, 210, 860, 200);
        contentPane.add(scroll);

        JLabel lbl_buscar = new JLabel(bundle.getString("etiqueta.buscar"));
        lbl_buscar.setBounds(10, 430, 140, 25);
        contentPane.add(lbl_buscar);

        txt_buscar = new JTextField();
        txt_buscar.setBounds(160, 430, 300, 25);
        contentPane.add(txt_buscar);

        btn_buscar = new JButton(bundle.getString("boton.buscar"));
        btn_buscar.setBounds(470, 430, 100, 25);
        contentPane.add(btn_buscar);

        btn_exportar = new JButton(bundle.getString("boton.exportar"));
        btn_exportar.setBounds(580, 430, 120, 25);
        contentPane.add(btn_exportar);

        barra_progreso = new JProgressBar();
        barra_progreso.setBounds(10, 470, 860, 20);
        barra_progreso.setVisible(false);
        contentPane.add(barra_progreso);

        // Iniciar controlador
        new logica_ventana(this);
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
        SwingUtilities.invokeLater(() -> new ventana().setVisible(true));
    }
}