package com.johncai;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class JFontChooser extends JPanel {

	// ���ý�����
	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// [start] �������
	private String current_fontName = "����";// ��ǰ����������,Ĭ������.
	private String showStr = "����ʾ���� AaBb,CcDd.";// չʾ������
	private int current_fontStyle = Font.PLAIN;// ��ǰ������,Ĭ�ϳ���.
	private int current_fontSize = 9;// ��ǰ�����С,Ĭ��9��.
	private Color current_fcolor = Color.BLACK, current_bgcolor = Color.WHITE;// ��ǰ��ɫ,Ĭ�Ϻ�ɫ.
	private JDialog dialog; // ������ʾģ̬�Ĵ���
	private JLabel fontLbl; // ѡ�������LBL
	private JLabel styleLbl; // ѡ�����͵�LBL
	private JLabel sizeLbl; // ѡ���ִ�С��LBL
	private JLabel bgcolorLbl,fcolorLbl; // ѡ��Color��label
	private JLabel bgotherColor,fotherColor; // ������ɫ
	private JTextField txtFont; // ��ʾѡ�������TEXT
	private JTextField txtStyle; // ��ʾѡ�����͵�TEXT
	private JTextField txtSize; // ��ʾѡ���ִ�С��TEXT
	private JTextField showTF; // չʾ�������
	private JList<?> fontList; // ѡ��������б�.
	private JList<?> styleList; // ѡ�����͵��б�.
	private JList<?> sizeList; // ѡ�������С���б�.
	private JComboBox<?> bgcbColor,fcbColor; // ѡ��Color��������.
	private JButton ok, cancel; // "ȷ��","ȡ��"��ť.
	private JScrollPane fontSP;
	private JScrollPane sizeSP;
	private JPanel showPan; // ��ʾ��.
	private Map<String, Integer> sizeMap; // �ֺ�ӳ���.
	private Map<String, Color> colorMap; // ����ɫӳ���.
	private Font selectedfont; // �û�ѡ�������
	private Color bgselcolor,fselcolor; // �û�ѡ�����ɫ
	// [end]

	// �޲γ�ʼ��
	public JFontChooser() {
		this.selectedfont = null;
		this.bgselcolor = null;
		this.fselcolor = null;
		/* ��ʼ������ */
		init(null, null,null);
	}

	// ���ع��죬�вεĳ�ʼ�� ���ڳ�ʼ���������
	public JFontChooser(Font font, Color fcolor,Color bgcolor) {
		if (font != null) {
			this.selectedfont = font;
			this.bgselcolor = bgcolor;
			this.fselcolor = fcolor;
			this.current_fontName = font.getName();
			this.current_fontSize = font.getSize();
			this.current_fontStyle = font.getStyle();
			this.current_bgcolor = bgcolor;
			this.current_fcolor = fcolor;
			/* ��ʼ������ */
			init(font, fcolor,bgcolor);
		} else {
			JOptionPane.showMessageDialog(this, "û�б�ѡ��Ŀؼ�", "����", JOptionPane.ERROR_MESSAGE);
		}
	}

	// �ɹ��ⲿ���õķ���
	public Font getSelectedfont() {
		return selectedfont;
	}

	public void setSelectedfont(Font selectedfont) {
		this.selectedfont = selectedfont;
	}

	public Color getBgselcolor() {
		return bgselcolor;
	}

	public void setBgselcolor(Color bgselcolor) {
		this.bgselcolor = bgselcolor;
	}

	public Color getFselcolor() {
		return fselcolor;
	}

	public void setFselcolor(Color fselcolor) {
		this.fselcolor = fselcolor;
	}

	/* ��ʼ������ */
	// private void init(Font txt_font) {
	private void init(Font font, Color fcolor, Color bgcolor) {
		// ʵ��������
		fontLbl = new JLabel("����:");
		styleLbl = new JLabel("����:");
		sizeLbl = new JLabel("��С:");
		bgcolorLbl = new JLabel("������ɫ:");
		fcolorLbl = new JLabel("������ɫ:");
		bgotherColor = new JLabel("<html><U>������ɫ</U></html>");
		fotherColor = new JLabel("<html><U>������ɫ</U></html>");
		txtFont = new JTextField("����");
		txtStyle = new JTextField("����");
		txtSize = new JTextField("9");

		// ȡ�õ�ǰ������������.
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String[] fontNames = ge.getAvailableFontFamilyNames();

		fontList = new JList<Object>(fontNames);

		// ����.
		styleList = new JList<Object>(new String[] { "����", "����", "б��", "��б��" });

		// �ֺ�.
		String[] sizeStr = new String[] { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28",
				"36", "48", "72", "����", "С��", "һ��", "Сһ", "����", "С��", "����", "С��", "�ĺ�", "С��", "���", "С��", "����", "С��",
				"�ߺ�", "�˺�" };
		int sizeVal[] = { 8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72, 42, 36, 26, 24, 22, 18, 16, 15,
				14, 12, 11, 9, 8, 7, 6, 5 };
		sizeMap = new HashMap<String, Integer>();
		for (int i = 0; i < sizeStr.length; ++i) {
			sizeMap.put(sizeStr[i], sizeVal[i]);
		}
		sizeList = new JList<Object>(sizeStr);
		fontSP = new JScrollPane(fontList);
		sizeSP = new JScrollPane(sizeList);

		// ��ɫ
		String[] colorStr = new String[] { "��ɫ", "��ɫ", "��ɫ", "���", "��ɫ", "��ɫ", "ǳ��", "���", "�ۻ�", "�ۺ�", "��ɫ", "��ɫ",
				"��ɫ" };
		Color[] colorVal = new Color[] { Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN,
				Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.WHITE, Color.YELLOW };
		colorMap = new HashMap<String, Color>();
		for (int i = 0; i < colorStr.length; i++) {
			colorMap.put(colorStr[i], colorVal[i]);
		}
		bgcbColor = new JComboBox<Object>(colorStr);
		fcbColor = new JComboBox<Object>(colorStr);
		showPan = new JPanel();
		ok = new JButton("ȷ��");
		cancel = new JButton("ȡ��");

		// ���ֿؼ�
		// �����
		this.setLayout(null); // ���ò��ֹ�����
		add(fontLbl);
		fontLbl.setBounds(12, 10, 30, 20);
		txtFont.setEditable(false);
		add(txtFont);
		txtFont.setBounds(10, 30, 155, 20);
		txtFont.setText("����");
		fontList.setSelectedValue("����", true);
		if (font != null) {
			txtFont.setText(font.getName());
			fontList.setSelectedValue(font.getName(), true);
		}

		add(fontSP);
		fontSP.setBounds(10, 50, 155, 100);

		// ��ʽ
		add(styleLbl);
		styleLbl.setBounds(175, 10, 30, 20);
		txtStyle.setEditable(false);
		add(txtStyle);
		txtStyle.setBounds(175, 30, 130, 20);
		styleList.setBorder(javax.swing.BorderFactory.createLineBorder(Color.gray));
		add(styleList);
		styleList.setBounds(175, 50, 130, 100);
		txtStyle.setText("����"); // ��ʼ��ΪĬ�ϵ���ʽ
		styleList.setSelectedValue("����", true); // ��ʼ��ΪĬ�ϵ���ʽ
		if (font != null) {
			styleList.setSelectedIndex(font.getStyle()); // ��ʼ����ʽlist
			if (font.getStyle() == 0) {
				txtStyle.setText("����");
			} else if (font.getStyle() == 1) {
				txtStyle.setText("����");
			} else if (font.getStyle() == 2) {
				txtStyle.setText("б��");
			} else if (font.getStyle() == 3) {
				txtStyle.setText("��б��");
			}
		}

		// ��С
		add(sizeLbl);
		sizeLbl.setBounds(320, 10, 30, 20);
		txtSize.setEditable(false);
		add(txtSize);
		txtSize.setBounds(320, 30, 60, 20);
		add(sizeSP);
		sizeSP.setBounds(320, 50, 60, 100);
		sizeList.setSelectedValue("9", false);
		txtSize.setText("9");
		if (font != null) {
			sizeList.setSelectedValue(Integer.toString(font.getSize()), false);
			txtSize.setText(Integer.toString(font.getSize()));
		}

		// ��ɫ
		add(bgcolorLbl);
		bgcolorLbl.setBounds(18, 220, 70, 20);
		bgcbColor.setBounds(18, 245, 100, 22);
		bgcbColor.setMaximumRowCount(5);
		bgcbColor.setSelectedItem("��ɫ");
		add(bgcbColor);
		bgotherColor.setForeground(Color.blue);
		bgotherColor.setBounds(130, 245, 60, 22);
		bgotherColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(bgotherColor);
		fcolorLbl.setBounds(200, 220, 70, 20);
		add(fcolorLbl);
		fcbColor.setBounds(200, 245, 100, 22);
		fcbColor.setMaximumRowCount(5);
		add(fcbColor);
		fotherColor.setForeground(Color.RED);
		fotherColor.setBounds(310, 245, 60, 22);
		fotherColor.setCursor(new Cursor(Cursor.HAND_CURSOR));
		add(fotherColor);

		// չʾ��
		showTF = new JTextField();
		showTF.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
		showTF.setBounds(10, 10, 300, 50);
		showTF.setHorizontalAlignment(JTextField.CENTER);
		showTF.setText(showStr);
		showTF.setBackground(Color.white);
		showTF.setEditable(false);
		showPan.setBorder(javax.swing.BorderFactory.createTitledBorder("ʾ��"));
		add(showPan);
		showPan.setBounds(13, 150, 370, 80);
		showPan.setLayout(new BorderLayout());
		showPan.add(showTF);
		if (font != null) {
			showTF.setFont(font); // ����ʾ���е����ָ�ʽ
		}
		if (font != null) {
			showTF.setForeground(fcolor);
			showTF.setBackground(bgcolor);
		}

		// ȷ����ȡ����ť
		add(ok);
		ok.setBounds(230, 275, 60, 20);
		add(cancel);
		cancel.setBounds(300, 275, 60, 20);
		// ���ֿؼ�_����

		// listener.....
		/* �û�ѡ������ */
		fontList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				current_fontName = (String) fontList.getSelectedValue();
				txtFont.setText(current_fontName);
				showTF.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			}
		});

		/* �û�ѡ������ */
		styleList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String value = (String) ((JList<?>) e.getSource()).getSelectedValue();
				if (value.equals("����")) {
					current_fontStyle = Font.PLAIN;
				}
				if (value.equals("б��")) {
					current_fontStyle = Font.ITALIC;
				}
				if (value.equals("����")) {
					current_fontStyle = Font.BOLD;
				}
				if (value.equals("��б��")) {
					current_fontStyle = Font.BOLD | Font.ITALIC;
				}
				txtStyle.setText(value);
				showTF.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			}
		});

		/* �û�ѡ�������С */
		sizeList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				current_fontSize = (Integer) sizeMap.get(sizeList.getSelectedValue());
				txtSize.setText(String.valueOf(current_fontSize));
				showTF.setFont(new Font(current_fontName, current_fontStyle, current_fontSize));
			}
		});

		/* �û�ѡ�񱳾���������ɫ */
		bgcbColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				current_bgcolor = (Color) colorMap.get(bgcbColor.getSelectedItem());
				showTF.setBackground(current_bgcolor);
			}
		});
		fcbColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				current_fcolor = (Color) colorMap.get(fcbColor.getSelectedItem());
				showTF.setForeground(current_fcolor);
			}
		});
		/* ������ɫ */
		bgotherColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new JColorChooser();
				Color col_temp = JColorChooser.showDialog(null, null, Color.WHITE);
				if (col_temp != null) {
					current_bgcolor = col_temp;
					showTF.setBackground(current_bgcolor);
					super.mouseClicked(e);
				}
			}
		});
		fotherColor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new JColorChooser();
				Color col_temp = JColorChooser.showDialog(null, null, Color.BLACK);
				if (col_temp != null) {
					current_fcolor = col_temp;
					showTF.setForeground(current_fcolor);
					super.mouseClicked(e);
				}
			}
		});
		/* �û�ȷ�� */
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* �û��û�ѡ����������� */
				setSelectedfont(new Font(current_fontName, current_fontStyle, current_fontSize));
				/* �û��û�ѡ�����ɫ���� */
				setBgselcolor(current_bgcolor);
				setFselcolor(current_fcolor);
				dialog.dispose();
				dialog = null;
			}
		});

		/* �û�ȡ�� */
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
				dialog = null;
			}
		});
	}

	/* ��ʾ����ѡ�����Ի���(x,y��ʾ���������λ��) */
	public void showDialog(Frame parent, int x, int y) {
		String title = "����";
		dialog = new JDialog(parent, title, true);
		dialog.add(this);
		dialog.setResizable(false);
		dialog.setSize(400, 335);
		// ���ýӽ��������λ��
		dialog.setLocation(x, y);
		dialog.addWindowListener(new WindowAdapter() {

			/* ����ر�ʱ���� */
			public void windowClosing(WindowEvent e) {
				dialog.removeAll();
				dialog.dispose();
				dialog = null;
			}
		});
		dialog.setVisible(true);
	}

	/* ����ʹ�� */
	public static void main(String[] args) {
		JFontChooser one = new JFontChooser(new Font("��������", Font.BOLD, 18), Color.BLACK, Color.WHITE);
		// JFontChooser one = new JFontChooser(); //�޲�
		one.showDialog(null, 500, 200);
		// ��ȡѡ�������
		Font font = one.getSelectedfont();
		// ��ȡѡ�����ɫ
		Color bgcolor = one.getBgselcolor();
		Color fcolor = one.getFselcolor();
		if (font != null && bgcolor != null &&fcolor!=null) {
			// ��ӡ�û�ѡ����������ɫ
			System.out.println(font);
			System.out.println(fcolor+","+bgcolor);
		}
	}
}

