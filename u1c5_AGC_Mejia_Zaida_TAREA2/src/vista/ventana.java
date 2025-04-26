package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;

import controlador.logica_ventana;

public class ventana extends JFrame {

    public JPanel contentPane;
    public JTextField txt_nombres, txt_telefono, txt_email, txt_buscar;
    public JCheckBox chb_favorito;
    public JComboBox<String> cmb_categoria;
    public JButton btn_add, btn_modificar, btn_eliminar, btn_buscar, btn_exportar;
    public JList<String> lst_contactos;
    public JScrollPane scrLista;
    public JProgressBar barra_progreso;
    public JComboBox<String> cmb_idioma;

    private ResourceBundle mensajes;

	public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new ventana().setVisible(true);
        });
    }
	
    public ventana() {
        setLocale(Locale.getDefault());
        mensajes = ResourceBundle.getBundle("i18n.Messages", getLocale());

        setTitle(mensajes.getString("titulo"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 1026, 780);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lbl_nombres = new JLabel(mensajes.getString("nombres"));
        lbl_nombres.setBounds(25, 41, 89, 13);
        lbl_nombres.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(lbl_nombres);

        JLabel lbl_telefono = new JLabel(mensajes.getString("telefono"));
        lbl_telefono.setBounds(25, 80, 89, 13);
        lbl_telefono.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(lbl_telefono);

        JLabel lbl_email = new JLabel(mensajes.getString("email"));
        lbl_email.setBounds(25, 122, 89, 13);
        lbl_email.setFont(new Font("Tahoma", Font.BOLD, 15));
        contentPane.add(lbl_email);

        JLabel lbl_buscar = new JLabel(mensajes.getString("buscar"));
        lbl_buscar.setFont(new Font("Tahoma", Font.BOLD, 15));
        lbl_buscar.setBounds(25, 661, 192, 13);
        contentPane.add(lbl_buscar);

        txt_nombres = new JTextField();
        txt_nombres.setBounds(124, 28, 427, 31);
        contentPane.add(txt_nombres);

        txt_telefono = new JTextField();
        txt_telefono.setBounds(124, 69, 427, 31);
        contentPane.add(txt_telefono);

        txt_email = new JTextField();
        txt_email.setBounds(124, 110, 427, 31);
        contentPane.add(txt_email);

        txt_buscar = new JTextField();
        txt_buscar.setBounds(212, 650, 784, 31);
        contentPane.add(txt_buscar);

        chb_favorito = new JCheckBox(mensajes.getString("favorito"));
        chb_favorito.setBounds(24, 170, 193, 21);
        contentPane.add(chb_favorito);

        cmb_categoria = new JComboBox<>();
        cmb_categoria.setBounds(300, 167, 251, 31);
        String[] categorias = {
                mensajes.getString("categoria.elija"),
                mensajes.getString("categoria.familia"),
                mensajes.getString("categoria.amigos"),
                mensajes.getString("categoria.trabajo")
        };
        for (String c : categorias) cmb_categoria.addItem(c);
        contentPane.add(cmb_categoria);

        btn_add = new JButton(mensajes.getString("agregar"));
        btn_add.setBounds(601, 70, 125, 65);
        contentPane.add(btn_add);

        btn_modificar = new JButton(mensajes.getString("modificar"));
        btn_modificar.setBounds(736, 70, 125, 65);
        contentPane.add(btn_modificar);

        btn_eliminar = new JButton(mensajes.getString("eliminar"));
        btn_eliminar.setBounds(871, 69, 125, 65);
        contentPane.add(btn_eliminar);

        btn_buscar = new JButton(mensajes.getString("buscar"));
        btn_buscar.setBounds(850, 690, 150, 30);
        contentPane.add(btn_buscar);

        btn_exportar = new JButton(mensajes.getString("exportar"));
        btn_exportar.setBounds(25, 690, 180, 30);
        contentPane.add(btn_exportar);

        barra_progreso = new JProgressBar();
        barra_progreso.setBounds(220, 690, 600, 30);
        barra_progreso.setStringPainted(true);
        barra_progreso.setVisible(false);
        contentPane.add(barra_progreso);

        lst_contactos = new JList<>();
        lst_contactos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lst_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrLista = new JScrollPane(lst_contactos);
        scrLista.setBounds(25, 242, 971, 398);
        contentPane.add(scrLista);

        JLabel lbl_idioma = new JLabel("Idioma:");
        lbl_idioma.setBounds(25, 10, 100, 13);
        contentPane.add(lbl_idioma);

        cmb_idioma = new JComboBox<>();
        cmb_idioma.setBounds(124, 5, 150, 20);
        cmb_idioma.addItem("Español");
        cmb_idioma.addItem("English");
        cmb_idioma.addItem("Français");
        contentPane.add(cmb_idioma);
		cmb_idioma.setSelectedItem(logica_ventana.idiomaSeleccionado);

        // Controlador
        new logica_ventana(this);
    }
}
