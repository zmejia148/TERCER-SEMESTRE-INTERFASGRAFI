package vista;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import controlador.logica_ventana;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JProgressBar;

public class ventana extends JFrame {

	public JPanel contentPane;
	public JTextField txt_nombres;
	public JTextField txt_telefono;
	public JTextField txt_email;
	public JTextField txt_buscar;
	public JCheckBox chb_favorito;
	public JComboBox cmb_categoria;
	public JButton btn_add;
	public JButton btn_modificar;
	public JButton btn_eliminar;
	public JButton btn_buscar;
	public JButton btn_exportar;
	public JList lst_contactos;
	public JScrollPane scrLista;
	public JProgressBar barra_progreso;

	public static void main(String[] args) {
	    EventQueue.invokeLater(new Runnable() {
	        public void run() {
	            try {
	                ventana frame = new ventana();
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	public ventana() {
		setTitle("GESTION DE CONTACTOS");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 1026, 780);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lbl_etiqueta1 = new JLabel("NOMBRES:");
		lbl_etiqueta1.setBounds(25, 41, 89, 13);
		lbl_etiqueta1.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lbl_etiqueta1);

		JLabel lbl_etiqueta2 = new JLabel("TELEFONO:");
		lbl_etiqueta2.setBounds(25, 80, 89, 13);
		lbl_etiqueta2.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lbl_etiqueta2);

		JLabel lbl_etiqueta3 = new JLabel("EMAIL:");
		lbl_etiqueta3.setBounds(25, 122, 89, 13);
		lbl_etiqueta3.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPane.add(lbl_etiqueta3);

		JLabel lbl_etiqueta4 = new JLabel("BUSCAR POR NOMBRE:");
		lbl_etiqueta4.setFont(new Font("Tahoma", Font.BOLD, 15));
		lbl_etiqueta4.setBounds(25, 661, 192, 13);
		contentPane.add(lbl_etiqueta4);

		txt_nombres = new JTextField();
		txt_nombres.setBounds(124, 28, 427, 31);
		txt_nombres.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_nombres.setColumns(10);
		contentPane.add(txt_nombres);

		txt_telefono = new JTextField();
		txt_telefono.setBounds(124, 69, 427, 31);
		txt_telefono.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_telefono.setColumns(10);
		contentPane.add(txt_telefono);

		txt_email = new JTextField();
		txt_email.setBounds(124, 110, 427, 31);
		txt_email.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_email.setColumns(10);
		contentPane.add(txt_email);

		txt_buscar = new JTextField();
		txt_buscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txt_buscar.setColumns(10);
		txt_buscar.setBounds(212, 650, 784, 31);
		contentPane.add(txt_buscar);

		chb_favorito = new JCheckBox("CONTACTO FAVORITO");
		chb_favorito.setBounds(24, 170, 193, 21);
		chb_favorito.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(chb_favorito);

		cmb_categoria = new JComboBox();
		cmb_categoria.setBounds(300, 167, 251, 31);
		contentPane.add(cmb_categoria);

		String[] categorias = {"Elija una Categoria", "Familia", "Amigos", "Trabajo"};
		for (String categoria : categorias) {
		    cmb_categoria.addItem(categoria);
		}

		btn_add = new JButton("AGREGAR");
		btn_add.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_add.setBounds(601, 70, 125, 65);
		contentPane.add(btn_add);

		btn_modificar = new JButton("MODIFICAR");
		btn_modificar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_modificar.setBounds(736, 70, 125, 65);
		contentPane.add(btn_modificar);

		btn_eliminar = new JButton("ELIMINAR");
		btn_eliminar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_eliminar.setBounds(871, 69, 125, 65);
		contentPane.add(btn_eliminar);

		btn_buscar = new JButton("BUSCAR");
		btn_buscar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_buscar.setBounds(850, 690, 150, 30);
		contentPane.add(btn_buscar);

		btn_exportar = new JButton("EXPORTAR CSV");
		btn_exportar.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btn_exportar.setBounds(25, 690, 180, 30);
		contentPane.add(btn_exportar);

		barra_progreso = new JProgressBar();
		barra_progreso.setBounds(220, 690, 600, 30);
		barra_progreso.setStringPainted(true);
		barra_progreso.setVisible(false);
		contentPane.add(barra_progreso);

		lst_contactos = new JList();
		lst_contactos.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lst_contactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrLista = new JScrollPane(lst_contactos);
		scrLista.setBounds(25, 242, 971, 398);
		contentPane.add(scrLista);

		// Instanciar el controlador
		logica_ventana lv = new logica_ventana(this);

		// Asignar listeners
		btn_add.addActionListener(lv);
		btn_modificar.addActionListener(lv);
		btn_eliminar.addActionListener(lv);
		btn_buscar.addActionListener(lv);
		btn_exportar.addActionListener(lv);
	}
}
