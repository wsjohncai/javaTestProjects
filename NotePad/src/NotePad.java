
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

	// 设置界面风格
			{
				try {
					javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
	public static void main(String[] args) {
		/**
		 * 创建一个线程
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				NotePad notepad = new NotePad();
				// 调用创建和显示窗口方法，显示界面
				notepad.createAndshowUI();
			}
		});
	}

	private boolean haschanged = false;
	private File file = null;
	private UndoManager undo = new UndoManager();

	// 创建和显示界面
	private JFrame npFrame = null;
	private JScrollPane textScroll = null;
	private JTextArea textarea = null;
	private JPopupMenu popup = null;
	private JMenuBar menuBar = null;
	// 主选单
	private JMenu menu_file, menu_edit, menu_option, menu_view, menu_help;
	// 文件菜单
	private JMenuItem menu_file_new, menu_file_open, menu_file_save, menu_file_saveas, menu_file_page, menu_file_print,
			menu_file_quit;
	// 编辑菜单
	private JMenuItem menu_edit_selectall, menu_edit_copy, menu_edit_cut, menu_edit_paste, menu_edit_undo,
			menu_edit_redo;
	// 其他菜单
	private JMenuItem menu_option_font, menu_view_statusbar, menu_help_help, menu_help_about;
	// 弹出菜单选单
	private JMenuItem pop_copy, pop_selectall, pop_cut, pop_paste, pop_undo, pop_redo, pop_font;
	private JRadioButtonMenuItem menu_option_wordwarp;

	public void createAndshowUI() {
		// 创建框架
		ImageIcon icon = new ImageIcon("images/fairytail.png");
		npFrame = new JFrame();
		npFrame.setTitle("noname - 记事本");
		npFrame.setIconImage(icon.getImage());
		npFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// 创建菜单
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

		// File选单
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

		// Edit选单
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

		// Option选单
		menu_option_font = new JMenuItem("Fonts and Colors");
		menu_option_font.addActionListener(this);
		menu_option_font.setActionCommand("font");
		menu_option_wordwarp = new JRadioButtonMenuItem("Toogle Word Warp", false);
		menu_option.add(menu_option_wordwarp);
		menu_option.add(menu_option_font);
		menu_option_wordwarp.addActionListener(this);
		menu_option_wordwarp.setActionCommand("warp");

		// View选单
		menu_view_statusbar = new JMenuItem("Status Bar");
		menu_view.add(menu_view_statusbar);
		menu_view_statusbar.setEnabled(false);

		// Help选单
		menu_help_help = new JMenuItem("Help");
		menu_help_help.setEnabled(false);
		menu_help_about = new JMenuItem("About");
		menu_help_about.addActionListener(this);
		menu_help_about.setActionCommand("about");
		menu_help.add(menu_help_help);
		menu_help.addSeparator();
		menu_help.add(menu_help_about);

		// 弹出菜单选单
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

		// 文本区域
		textarea = new JTextArea();
		textScroll = new JScrollPane(textarea);
		textarea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		textarea.setDragEnabled(true);
		textarea.setComponentPopupMenu(popup);// 将textarea与弹出菜单关联
		textarea.addMouseListener(new MouseAdapter() {// 添加鼠标监听
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
		// 添加文本变动检测
		textarea.getDocument().addDocumentListener(new DocumentListener() {

			public void insertUpdate(DocumentEvent e) {
				// System.out.println("insert");
				haschanged = true;
				npFrame.setTitle(file.getName() + "* - 记事本");
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
				npFrame.setTitle(file.getName() + "* - 记事本");
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
				npFrame.setTitle(file.getName() + "* - 记事本");
			}

		});
		// 添加撤销监视器
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

		// 显示窗体
		npFrame.setLocation(100, 50);
		npFrame.setVisible(true);
		npFrame.pack();
		textarea.requestFocus();
	}

	private JFileChooser choose = null;

	// 打开并写入文件
	public void fileIn() {
		FileInputStream fistream = null;
		textarea.setText("");
		/*
		 * 以字节流方式读入
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
			npFrame.setTitle(file.getName() + " - 记事本");
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
		 * 以字符流方式输入
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

	// 保存文件，将文件回写
	public int save() {
		/**
		 * 采用字符流的方式写入
		 */
		FileWriter fw = null;
		// 如果文件存在或者输入了直接保存命令，写入文件
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
			// 改写文件状态为未改变
			haschanged = false;
			npFrame.setTitle(file.getName() + " - 记事本");
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

		// 返回1，代表文件已经正确保存
		return 1;
	}

	public int saveAs() {
		// 如果文件指针为空或者输入命令为“另存为”时，打开资源管理器进行保存
		choose = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT  (*.txt)", "txt");
		choose.setFileFilter(filter);
		if (file.exists())
			choose.setCurrentDirectory(file);
		// 默认名保存文件
		choose.setSelectedFile(file);

		// 打开存储文件弹框
		int rtnVal = 0;
		rtnVal = choose.showSaveDialog(npFrame);
		// 用户点击保存
		if (rtnVal == JFileChooser.APPROVE_OPTION) {
			// 将file指向指定的对象
			file = new File(choose.getSelectedFile().getAbsolutePath());

			// 如果选择的文件已经存在，提示并处理
			if (file.exists()) {
				int op = showSaveDialog("saveFile");
				// 如果选择另存为， 将文件另存为
				if (op == 0)
					return (saveAs());
				// 用户选择覆盖原本文件
				else if (op == 1)
					return (save());
				return 0;
			} else
				// 如果用户选择的文件1文件不存在，创建新文件，跳出判断
				try {
				file.createNewFile();
				return (save());
				} catch (IOException e) {
				e.printStackTrace();
				}
		} else if (rtnVal == JFileChooser.CANCEL_OPTION) {
			// 用户取消另存为操作，返回0，表示文件没有存储
			return 0;
		}
		return 0;
	}

	// 打开文件方法
	private boolean ignorechanged = false;

	public void open() {
		if (!haschanged || ignorechanged) { //文件已经保存，或者用户忽略更改
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
		} else { //文件未保存，提示保存
			int op = JOptionPane.showConfirmDialog(npFrame,
					"File hasn't been saved since last edit, \n" + "save the file?", "记事本", JOptionPane.YES_NO_OPTION);
			if (op == JOptionPane.YES_OPTION) {
				if (file.exists())
					save();
				else
					saveAs();
				haschanged = false;
			} else
				ignorechanged = true;
			//继续打开文件
			open();
			ignorechanged = false;
		}
	}

	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

	// 复制方法
	public void copy() {
		String copytext = textarea.getSelectedText();
		StringSelection ssel = new StringSelection(copytext);
		clipboard.setContents(ssel, null);
	}

	// 剪切方法
	public void cut() {
		String cuttext = textarea.getSelectedText();
		StringSelection ssel = new StringSelection(cuttext);
		clipboard.setContents(ssel, null);
		textarea.replaceRange("", textarea.getSelectionStart(), textarea.getSelectionEnd());
	}

	// 粘贴方法
	public void paste() {
		String paste = null;
		// Transferable接口，把剪贴板的内容转换成数据
		Transferable trans = clipboard.getContents(this);
		// DataFalvor类判断是否能把剪贴板的内容转换成所需数据类型
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

	// 打印方法，调用打印机
	public void print() {

		FileInputStream fis = null;
		DocAttributeSet das = null;
		Doc doc = null;

		// 构建打印请求属性集
		HashPrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		// 设置打印格式，因为未确定类型，所以选择autosense
		DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
		// 查找所有的可用的打印服务
		PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
		// 定位默认的打印服务
		PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
		// 显示打印对话框
		if (printService.length > 0) {
			PrintService service = ServiceUI.printDialog(null, npFrame.getX() + 13, npFrame.getY() + 160, printService,
					defaultService, flavor, pras);
			if (service != null) {
				try {
					DocPrintJob job = service.createPrintJob(); // 创建打印作业
					fis = new FileInputStream(file); // 构造待打印的文件流
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

	// 创建存档提示框，根据不同输入显示不同提示,根据用户的选择返回相应提示值
	public int showSaveDialog(String action) {
		Object[] options = null;
		String s = null;

		if (file.exists()) {
			// 如果动作来自于saveFile方法
			if (action.equals("saveFile")) {
				options = new Object[] { "Save as", "Yes", "Cancel" };
				s = "File has already existed, override it?";
			} else {
				options = new Object[] { "Save", "No", "Cancel" };
				s = "File hasn't been saved since last edited, \nsave file?";
			}
		} else {
			// 如果来自于其他方法
			s = "File hasn't been saved since last edited, \nsave file?";
			options = new Object[] { "Save as", "Don't Save", "Cancel" };
		}

		int op = JOptionPane.showOptionDialog(npFrame, s, "记事本", JOptionPane.YES_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[0]);
		return op;
	}

	// 创建警告提示框
	public void showWarningDialog(String warning) {
		JOptionPane.showMessageDialog(npFrame, warning, "记事本", JOptionPane.WARNING_MESSAGE);
	}

	public void about() {
		JDialog about_dialog = new JDialog(npFrame, "About",true);
		JLabel verText = new JLabel("Version 1.0");
		JLabel authText = new JLabel("Author: WSJohnCai");
		JLabel emailText = new JLabel("Connect me at 1092647174@qq.com");
		JPanel aboutPan = new JPanel(null);
		about_dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		verText.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		verText.setBounds(110, 40, 100, 20);
		authText.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		authText.setBounds(90, 60, 150, 20);
		emailText.setFont(new Font("微软雅黑", Font.PLAIN, 10));
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
			// 如果文件有改动，提示信息
			int op = showSaveDialog("quit");
			if (op == 0) {
				// 另存为，后退出
				if (file.exists())
					filesaved = save();
				else
					filesaved = saveAs();

				if (filesaved == 1)
					System.exit(0);
				else
					showWarningDialog("Failed to save file!\nTry again!");
			} else if (op == 1) {
				// 备份文件保存，退出（未实现）
				System.exit(0);
			}
		} else
			System.exit(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 根据点击的按钮选择相应方法
		String ace = e.getActionCommand();
		switch (ace) {
		case "open":
			open();
			break;
		case "new":
			npFrame.setTitle("noname - 记事本");
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
