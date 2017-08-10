
/**
 * @author WSJohnCai
 * 
 *  Description: This is a trial version of a notepad application
 */

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

public class NotePad implements ActionListener {

	// ���ý�����
			{
				try {
					javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
	public static void main(String[] args) {
		/**
		 * ����һ���߳�
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NotePad notepad = new NotePad();
				// ���ô�������ʾ���ڷ�������ʾ����
				notepad.createAndshowUI();
			}
		});
	}

	private boolean haschanged = false;
	private File file = null;
	private UndoManager undo = new UndoManager();

	// ��������ʾ����
	private JFrame npFrame = null;
	private JScrollPane textScroll = null;
	private JTextArea textarea = null;
	private JPopupMenu popup = null;
	private JMenuBar menuBar = null;
	// ��ѡ��
	private JMenu menu_file, menu_edit, menu_option, menu_view, menu_help;
	// �ļ��˵�
	private JMenuItem menu_file_new, menu_file_open, menu_file_save, menu_file_saveas, menu_file_page, menu_file_print,
			menu_file_quit;
	// �༭�˵�
	private JMenuItem menu_edit_selectall, menu_edit_copy, menu_edit_cut, menu_edit_paste, menu_edit_undo,
			menu_edit_redo;
	// �����˵�
	private JMenuItem menu_option_font, menu_view_statusbar, menu_help_help, menu_help_about;
	// �����˵�ѡ��
	private JMenuItem pop_copy, pop_selectall, pop_cut, pop_paste, pop_undo, pop_redo, pop_font;
	private JRadioButtonMenuItem menu_option_wordwarp;

	public void createAndshowUI() {
		// �������
		ImageIcon icon = new ImageIcon("images/fairytail.png");
		npFrame = new JFrame();
		npFrame.setTitle("noname - ���±�");
		npFrame.setIconImage(icon.getImage());
		npFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// �����˵�
		file = new File("noname.txt");
		menuBar = new JMenuBar();
		npFrame.setJMenuBar(menuBar);
		menu_file = new JMenu("File");
		menu_file.setMnemonic('F');
		menu_edit = new JMenu("Edit");
		menu_edit.setMnemonic('E');
		menu_option = new JMenu("Option");
		menu_option.setMnemonic('O');
		menu_view = new JMenu("View");
		menu_view.setMnemonic('V');
		menu_help = new JMenu("Help");
		menu_help.setMnemonic('H');
		menuBar.add(menu_file);
		menuBar.add(menu_edit);
		menuBar.add(menu_option);
		menuBar.add(menu_view);
		menuBar.add(menu_help);
		menuBar.setFocusable(false);

		// Fileѡ��
		menu_file_new = new JMenuItem("New", 'N');
		menu_file_new.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		menu_file_new.addActionListener(this);
		menu_file_new.setActionCommand("new");
		menu_file.add(menu_file_new);
		menu_file_open = new JMenuItem("Open", 'O');
		menu_file_open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		menu_file_open.addActionListener(this);
		menu_file_open.setActionCommand("open");
		menu_file.add(menu_file_open);
		menu_file.addSeparator();
		menu_file_save = new JMenuItem("Save", 'S');
		menu_file_save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menu_file_save.addActionListener(this);
		menu_file_save.setActionCommand("save");
		menu_file.add(menu_file_save);
		menu_file_saveas = new JMenuItem("Save as...", 'A');
		menu_file_saveas.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		menu_file_saveas.addActionListener(this);
		menu_file_saveas.setActionCommand("saveas");
		menu_file.add(menu_file_saveas);
		menu_file.addSeparator();
		menu_file_page = new JMenuItem("Page Settings", 'F');
		menu_file_page.addActionListener(this);
		menu_file_page.setActionCommand("page");
		menu_file_page.setEnabled(false);
		menu_file_print = new JMenuItem("Print");
		menu_file_print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
		menu_file_print.addActionListener(this);
		menu_file_print.setActionCommand("print");
		menu_file_quit = new JMenuItem("Quit", 'X');
		menu_file_quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menu_file_quit.addActionListener(this);
		menu_file_quit.setActionCommand("quit");
		menu_file.add(menu_file_page);
		menu_file.add(menu_file_print);
		menu_file.addSeparator();
		menu_file.add(menu_file_quit);

		// Editѡ��
		menu_edit_selectall = new JMenuItem("Select All", KeyEvent.VK_A);
		menu_edit_selectall.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		menu_edit_selectall.addActionListener(this);
		menu_edit_selectall.setActionCommand("selectall");
		menu_edit_copy = new JMenuItem("Copy");
		menu_edit_copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		menu_edit_copy.addActionListener(this);
		menu_edit_copy.setActionCommand("copy");
		menu_edit_copy.setEnabled(false);
		menu_edit_cut = new JMenuItem("Cut");
		menu_edit_cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		menu_edit_cut.addActionListener(this);
		menu_edit_cut.setActionCommand("cut");
		menu_edit_cut.setEnabled(false);
		menu_edit_paste = new JMenuItem("Paste");
		menu_edit_paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		menu_edit_paste.addActionListener(this);
		menu_edit_paste.setActionCommand("paste");
		menu_edit_undo = new JMenuItem("Undo");
		menu_edit_undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
		menu_edit_undo.addActionListener(this);
		menu_edit_undo.setEnabled(false);
		menu_edit_undo.setActionCommand("undo");
		menu_edit_redo = new JMenuItem("Redo");
		menu_edit_redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
		menu_edit_redo.addActionListener(this);
		menu_edit_redo.setActionCommand("redo");
		menu_edit_redo.setEnabled(false);
		menu_edit.add(menu_edit_undo);
		menu_edit.add(menu_edit_redo);
		menu_edit.addSeparator();
		menu_edit.add(menu_edit_selectall);
		menu_edit.add(menu_edit_copy);
		menu_edit.add(menu_edit_cut);
		menu_edit.add(menu_edit_paste);

		// Optionѡ��
		menu_option_font = new JMenuItem("Fonts and Colors");
		menu_option_font.addActionListener(this);
		menu_option_font.setActionCommand("font");
		menu_option_wordwarp = new JRadioButtonMenuItem("Toogle Word Warp", false);
		menu_option.add(menu_option_wordwarp);
		menu_option.add(menu_option_font);
		menu_option_wordwarp.addActionListener(this);
		menu_option_wordwarp.setActionCommand("warp");

		// Viewѡ��
		menu_view_statusbar = new JMenuItem("Status Bar");
		menu_view.add(menu_view_statusbar);
		menu_view_statusbar.setEnabled(false);

		// Helpѡ��
		menu_help_help = new JMenuItem("Help");
		menu_help_help.setEnabled(false);
		menu_help_about = new JMenuItem("About");
		menu_help_about.addActionListener(this);
		menu_help_about.setActionCommand("about");
		menu_help.add(menu_help_help);
		menu_help.addSeparator();
		menu_help.add(menu_help_about);

		// �����˵�ѡ��
		popup = new JPopupMenu();
		pop_undo = new JMenuItem("Undo");
		pop_undo.addActionListener(this);
		pop_undo.setEnabled(false);
		pop_undo.setActionCommand("undo");
		pop_redo = new JMenuItem("Redo");
		pop_redo.addActionListener(this);
		pop_redo.setActionCommand("redo");
		pop_redo.setEnabled(false);
		pop_selectall = new JMenuItem("Select All (A)");
		pop_selectall.addActionListener(this);
		pop_selectall.setActionCommand("selectall");
		pop_copy = new JMenuItem("Copy (C)");
		pop_copy.addActionListener(this);
		pop_copy.setActionCommand("copy");
		pop_copy.setEnabled(false);
		pop_cut = new JMenuItem("Cut (X)");
		pop_cut.addActionListener(this);
		pop_cut.setActionCommand("cut");
		pop_cut.setEnabled(false);
		pop_paste = new JMenuItem("Paste (V)");
		pop_paste.addActionListener(this);
		pop_paste.setActionCommand("paste");
		pop_font = new JMenuItem("Fonts And Colors");
		pop_font.addActionListener(this);
		pop_font.setActionCommand("font");
		popup.add(pop_undo);
		popup.add(pop_redo);
		popup.addSeparator();
		popup.add(pop_selectall);
		popup.add(pop_copy);
		popup.add(pop_cut);
		popup.add(pop_paste);
		popup.addSeparator();
		popup.add(pop_font);

		// �ı�����
		textarea = new JTextArea();
		textScroll = new JScrollPane(textarea);
		textarea.setFont(new Font("΢���ź�", Font.PLAIN, 14));
		textarea.setDragEnabled(true);
		textarea.setComponentPopupMenu(popup);// ��textarea�뵯���˵�����
		textarea.addMouseListener(new MouseAdapter() {// ���������
			public void mouseClicked(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {
				if (textarea.getSelectedText() != null) {
					menu_edit_copy.setEnabled(true);
					menu_edit_cut.setEnabled(true);
					pop_copy.setEnabled(true);
					pop_cut.setEnabled(true);
				} else {
					menu_edit_copy.setEnabled(false);
					menu_edit_cut.setEnabled(false);
					pop_copy.setEnabled(false);
					pop_cut.setEnabled(false);
				}
			}
		});
		textarea.setEditable(true);
		// ����ı��䶯���
		textarea.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				// System.out.println("insert");
				haschanged = true;
				npFrame.setTitle(file.getName() + "* - ���±�");
				menu_edit_copy.setEnabled(false);
				menu_edit_cut.setEnabled(false);
				pop_copy.setEnabled(false);
				pop_cut.setEnabled(false);
				if (undo.canRedo()) {
					menu_edit_redo.setEnabled(true);
					pop_redo.setEnabled(true);
				} else {
					menu_edit_redo.setEnabled(false);
					pop_redo.setEnabled(false);
				}
			}

			public void removeUpdate(DocumentEvent e) {
				// System.out.println("remove");
				haschanged = true;
				npFrame.setTitle(file.getName() + "* - ���±�");
				menu_edit_copy.setEnabled(false);
				menu_edit_cut.setEnabled(false);
				pop_copy.setEnabled(false);
				pop_cut.setEnabled(false);
				if (undo.canRedo()) {
					menu_edit_redo.setEnabled(true);
					pop_redo.setEnabled(true);
				} else {
					menu_edit_redo.setEnabled(false);
					pop_redo.setEnabled(false);
				}
			}

			public void changedUpdate(DocumentEvent e) {
				// System.out.println("changed");
				haschanged = true;
				npFrame.setTitle(file.getName() + "* - ���±�");
			}

		});
		// ��ӳ���������
		textarea.getDocument().addUndoableEditListener(new UndoableEditListener() {

			public void undoableEditHappened(UndoableEditEvent e) {
				undo.addEdit(e.getEdit());
				menu_edit_undo.setEnabled(true);
				pop_undo.setEnabled(true);
			}

		});
		textScroll.setPreferredSize(new Dimension(480, 600));
		npFrame.add(textScroll);
		npFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				quit();
			}
		});

		// ��ʾ����
		npFrame.setLocation(100, 50);
		npFrame.setVisible(true);
		npFrame.pack();
		textarea.requestFocus();
	}

	private JFileChooser choose = null;

	// �򿪲�д���ļ�
	public void fileIn() {
		FileInputStream fistream = null;
		textarea.setText("");
		/*
		 * ���ֽ�����ʽ����
		 */
		try {
			fistream = new FileInputStream(file);
			byte[] bytein = new byte[1024];
			int n;
			while ((n = fistream.read(bytein)) != -1) {
				String s = new String(bytein, 0, n);
				textarea.append(s);
			}
			haschanged = false;
			npFrame.setTitle(file.getName() + " - ���±�");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fistream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * ���ַ�����ʽ����
		 */
//		 BufferedReader bf = null;
//		 FileReader fr = null;
//		 try{
//		 fr = new FileReader(file);
//		 bf = new BufferedReader(fr);
//		 String s = "";
//		 while((s = bf.readLine()) != null) {
//		 textarea.append(s+"\r\n");
//		 haschanged = false;
//		 }
//		 } catch(IOException e) {
//		 e.printStackTrace();
//		 }finally {
//		 try {
//		 bf.close();
//		 fr.close();
//		 } catch (IOException e) {
//		 e.printStackTrace();
//		 }
//		 }

	}

	// �����ļ������ļ���д
	public int save() {
		/**
		 * �����ַ����ķ�ʽд��
		 */
		FileWriter fw = null;
		// ����ļ����ڻ���������ֱ�ӱ������д���ļ�
		if(haschanged)
		try {
			fw = new FileWriter(file);
			String str = "";
			for (int i = 0, j, k; i < textarea.getLineCount(); i++) {
				j = textarea.getLineStartOffset(i);
				k = textarea.getLineEndOffset(i) - textarea.getLineStartOffset(i);
				str = textarea.getText(j, k);
				fw.write(str);
			}
			// ��д�ļ�״̬Ϊδ�ı�
			haschanged = false;
			npFrame.setTitle(file.getName() + " - ���±�");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// ����1�������ļ��Ѿ���ȷ����
		return 1;
	}

	public int saveAs() {
		// ����ļ�ָ��Ϊ�ջ�����������Ϊ�����Ϊ��ʱ������Դ���������б���
		choose = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT  (*.txt)", "txt");
		choose.setFileFilter(filter);
		if (file.exists())
			choose.setCurrentDirectory(file);
		// Ĭ���������ļ�
		choose.setSelectedFile(file);

		// �򿪴洢�ļ�����
		int rtnVal = 0;
		rtnVal = choose.showSaveDialog(npFrame);
		// �û��������
		if (rtnVal == JFileChooser.APPROVE_OPTION) {
			// ��fileָ��ָ���Ķ���
			file = new File(choose.getSelectedFile().getAbsolutePath());

			// ���ѡ����ļ��Ѿ����ڣ���ʾ������
			if (file.exists()) {
				int op = showSaveDialog("saveFile");
				// ���ѡ�����Ϊ�� ���ļ����Ϊ
				if (op == 0)
					return (saveAs());
				// �û�ѡ�񸲸�ԭ���ļ�
				else if (op == 1)
					return (save());
				return 0;
			} else
				// ����û�ѡ����ļ�1�ļ������ڣ��������ļ��������ж�
				try {
				file.createNewFile();
				return (save());
				} catch (IOException e) {
				e.printStackTrace();
				}
		} else if (rtnVal == JFileChooser.CANCEL_OPTION) {
			// �û�ȡ�����Ϊ����������0����ʾ�ļ�û�д洢
			return 0;
		}
		return 0;
	}

	// ���ļ�����
	private boolean ignorechanged = false;

	public void open() {
		if (!haschanged || ignorechanged) { //�ļ��Ѿ����棬�����û����Ը���
			choose = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT  (*.txt)", "txt");
			choose.setFileFilter(filter);
			if (file.exists())
				choose.setCurrentDirectory(file);
			int rtnVal = 0;
			rtnVal = choose.showOpenDialog(npFrame);
			if (rtnVal == JFileChooser.APPROVE_OPTION) {
				file = new File(choose.getSelectedFile().getAbsolutePath());
				fileIn();
			}
		} else { //�ļ�δ���棬��ʾ����
			int op = JOptionPane.showConfirmDialog(npFrame,
					"File hasn't been saved since last edit, \n" + "save the file?", "���±�", JOptionPane.YES_NO_OPTION);
			if (op == JOptionPane.YES_OPTION) {
				if (file.exists())
					save();
				else
					saveAs();
				haschanged = false;
			} else
				ignorechanged = true;
			//�������ļ�
			open();
			ignorechanged = false;
		}
	}

	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	// ���Ʒ���
	public void copy() {
		String copytext = textarea.getSelectedText();
		StringSelection ssel = new StringSelection(copytext);
		clipboard.setContents(ssel, null);
	}

	// ���з���
	public void cut() {
		String cuttext = textarea.getSelectedText();
		StringSelection ssel = new StringSelection(cuttext);
		clipboard.setContents(ssel, null);
		textarea.replaceRange("", textarea.getSelectionStart(), textarea.getSelectionEnd());
	}

	// ճ������
	public void paste() {
		String paste = null;
		// Transferable�ӿڣ��Ѽ����������ת��������
		Transferable trans = clipboard.getContents(this);
		// DataFalvor���ж��Ƿ��ܰѼ����������ת����������������
		DataFlavor flavor = DataFlavor.stringFlavor;
		if (trans.isDataFlavorSupported(flavor)) {
			try {
				paste = (String) trans.getTransferData(flavor);
				if (textarea.getSelectedText() != null) {
					textarea.replaceSelection(paste);
				} else {
					textarea.insert(paste, textarea.getCaretPosition());
				}
			} catch (UnsupportedFlavorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// ��ӡ���������ô�ӡ��
	public void print() {

		FileInputStream fis = null;
		DocAttributeSet das = null;
		Doc doc = null;

		// ������ӡ�������Լ�
		HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		// ���ô�ӡ��ʽ����Ϊδȷ�����ͣ�����ѡ��autosense
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		// �������еĿ��õĴ�ӡ����
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		// ��λĬ�ϵĴ�ӡ����
		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
		// ��ʾ��ӡ�Ի���
		if (printService.length > 0) {
			PrintService service = ServiceUI.printDialog(null, npFrame.getX() + 13, npFrame.getY() + 160, printService,
					defaultService, flavor, pras);
			if (service != null) {
				try {
					DocPrintJob job = service.createPrintJob(); // ������ӡ��ҵ
					fis = new FileInputStream(file); // �������ӡ���ļ���
					das = new HashDocAttributeSet();
					doc = new SimpleDoc(fis, flavor, das);
					job.print(doc, pras);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						fis.close();
					} catch (Exception e1) {

					}
				}
			}
		}
	}

	// �����浵��ʾ�򣬸��ݲ�ͬ������ʾ��ͬ��ʾ,�����û���ѡ�񷵻���Ӧ��ʾֵ
	public int showSaveDialog(String action) {
		Object[] options = null;
		String s = null;

		if (file.exists()) {
			// �������������saveFile����
			if (action.equals("saveFile")) {
				options = new Object[] { "Save as", "Yes", "Cancel" };
				s = "File has already existed, override it?";
			} else {
				options = new Object[] { "Save", "No", "Cancel" };
				s = "File hasn't been saved since last edited, \nsave file?";
			}
		} else {
			// �����������������
			s = "File hasn't been saved since last edited, \nsave file?";
			options = new Object[] { "Save as", "Don't Save", "Cancel" };
		}

		int op = JOptionPane.showOptionDialog(npFrame, s, "���±�", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
		return op;
	}

	// ����������ʾ��
	public void showWarningDialog(String warning) {
		JOptionPane.showMessageDialog(npFrame, warning, "���±�", JOptionPane.WARNING_MESSAGE);
	}

	public void about() {
		JDialog about_dialog = new JDialog(npFrame, "About",true);
		JLabel verText = new JLabel("Version 1.0");
		JLabel authText = new JLabel("Author: WSJohnCai");
		JLabel emailText = new JLabel("Connect me at 1092647174@qq.com");
		JPanel aboutPan = new JPanel(null);
		about_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		verText.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		verText.setBounds(110, 40, 100, 20);
		authText.setFont(new Font("΢���ź�", Font.PLAIN, 12));
		authText.setBounds(90, 60, 150, 20);
		emailText.setFont(new Font("΢���ź�", Font.PLAIN, 10));
		emailText.setBounds(70, 80, 200, 20);
		aboutPan.add(verText);
		aboutPan.add(authText);
		aboutPan.add(emailText);
		about_dialog.add(aboutPan);
		
		about_dialog.setResizable(false);
		about_dialog.setSize(300, 186);
		about_dialog.setLocation(npFrame.getX() + 100, npFrame.getY() + 200);
		about_dialog.setVisible(true);
	}

	public void quit() {
		int filesaved = 0;
		if (haschanged) {
			// ����ļ��иĶ�����ʾ��Ϣ
			int op = showSaveDialog("quit");
			if (op == 0) {
				// ���Ϊ�����˳�
				if (file.exists())
					filesaved = save();
				else
					filesaved = saveAs();

				if (filesaved == 1)
					System.exit(0);
				else
					showWarningDialog("Failed to save file!\nTry again!");
			} else if (op == 1) {
				// �����ļ����棬�˳���δʵ�֣�
				System.exit(0);
			}
		} else
			System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ���ݵ���İ�ťѡ����Ӧ����
		String ace = e.getActionCommand();
		switch (ace) {
		case "open":
			open();
			break;
		case "new":
			npFrame.setTitle("noname - ���±�");
			file = new File("noname.txt");
			textarea.setText("");
			haschanged = false;
			break;
		case "save":
			if (file.exists())
				save();
			else
				saveAs();
			break;
		case "saveas":
			saveAs();
			break;
		case "font":
			JFontChooser jfc = new JFontChooser(textarea.getFont(), textarea.getForeground(), textarea.getBackground());
			jfc.showDialog(npFrame, npFrame.getX() + 45, npFrame.getY() + 165);
			textarea.setFont(jfc.getSelectedfont());
			textarea.setBackground(jfc.getBgselcolor());
			textarea.setForeground(jfc.getFselcolor());
			break;
		case "print":
			print();
			break;
		case "selectall":
			textarea.selectAll();
			if (textarea.getSelectedText() != null) {
				menu_edit_copy.setEnabled(true);
				menu_edit_cut.setEnabled(true);
				pop_copy.setEnabled(true);
				pop_cut.setEnabled(true);
			}
			break;
		case "copy":
			copy();
			break;
		case "cut":
			cut();
			break;
		case "paste":
			paste();
			break;
		case "undo":
			if (undo.canUndo())
				undo.undo();
			break;
		case "redo":
			if (undo.canRedo())
				undo.redo();
			break;
		case "warp":
			if (menu_option_wordwarp.isSelected()) {
				textarea.setLineWrap(true);
				// menu_option_wordwarp.setArmed(true);
				// menu_option_wordwarp.setSelectedIcon(new
				// ImageIcon("images/fairytail.png"));
				// menu_option_wordwarp.setPressedIcon(new
				// ImageIcon("images/fairytail.png"));
			} else {
				textarea.setLineWrap(false);
			}
			break;
		case "about":
			about();
			break;
		case "quit":
			quit();
		}
	}

}
