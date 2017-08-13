import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class HelperUI implements ActionListener {
	private static final int SCR_WID = Toolkit.getDefaultToolkit().getScreenSize().width;
	private static final int SCR_HGT = Toolkit.getDefaultToolkit().getScreenSize().height;

	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Long startTime, endTime;
	private JFrame frame;
	private JPanel pl_title, pl_image, pl_sort, pl_btn, pl_bottom;
	private JLabel lb_image, lb_comfirm, lb_path;
	private JTextField lb_count;
	private JButton btn_add, btn_sel, btn_pre, btn_next, btn_undo, btn_skip;
	private JTextField in_folder;
	private File workSpace, curImgFile;
	private FileHandler fileHandler;
	private HashMap<Integer, JButton> buttons;
	private int totalImg = 0; // 需要处理的文件总数量
	private int curImg = 1; // 当前页面图片的编号
	private HashMap<Integer, String> undoStack;
	private HashMap<JButton, Integer> clickedCount;
	private int stack = 0;
	private int sortedImg = 0;

	private void createUI() {
		// 设置主窗体
		frame = new JFrame("图片分类助手");
		frame.setSize(SCR_WID / 2, SCR_HGT);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		myMouseAdapter mML = new myMouseAdapter();
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (fileHandler != null) {
					fileHandler.endWork();
					String sorted = "";
					for (JButton b : clickedCount.keySet()) {
						String f = b.getText();
						int i = clickedCount.get(b);
						if (!f.equals("跳过") && i > 0) {
							sorted += f + ": " + i + " 张\n";
						}
					}
					endTime = java.util.Calendar.getInstance().getTimeInMillis();
					long time = endTime - startTime;
					String msg = "本次对" + sortedImg + "张图片进行了分类\n" + "总用时" + time / 1000 / 60 + "分钟\n"
							+ "其中各文件夹的分类详情如下：\n" + sorted;
					int r = JOptionPane.showConfirmDialog(frame, msg, "关闭程序", JOptionPane.OK_CANCEL_OPTION);
					if (r == JOptionPane.OK_OPTION)
						System.exit(0);
					else
						return;
				}
				System.exit(0);
			}
		});
		frame.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					btn_pre.doClick();
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					btn_next.doClick();
				}
			}
		});
		frame.addMouseListener(mML);

		// 用于显示图片的面板
		pl_image = new JPanel(null);
		pl_title = new JPanel(null);
		lb_path = new JLabel("尚未设置工作目录(顶层目录)");
		btn_sel = new JButton("选择目录");
		lb_count = new JTextField();
		btn_undo = new JButton("撤销");
		btn_skip = new JButton("跳过");
		lb_image = new JLabel();
		lb_count.setFont(new Font("Consolas", Font.PLAIN, 16));
		lb_path.setFont(new Font("宋体", Font.BOLD, 16));
		btn_sel.addActionListener(this);
		btn_undo.addActionListener(this);
		btn_undo.setEnabled(false);
		btn_skip.addActionListener(this);
		btn_skip.setFont(new Font("微软雅黑", Font.BOLD, 20));
		btn_skip.setEnabled(false);
		lb_image.addMouseListener(mML);
		lb_image.addMouseMotionListener(mML);
		pl_image.addMouseWheelListener(mML);
		lb_count.addMouseListener(mML);
		lb_count.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				String p = lb_count.getText();
				int page = Integer.parseInt(p);
				lb_count.setEditable(false);
				if (page > 0 && page <= totalImg) {
					curImg = page;
					setImage(page);
				}
				frame.requestFocus();
			}

		});

		pl_title.setBackground(Color.YELLOW);
		pl_image.setBackground(Color.BLACK);
		pl_title.setPreferredSize(new Dimension(SCR_WID / 2, 30));
		pl_image.setPreferredSize(new Dimension(SCR_WID / 2, SCR_HGT / 4 * 3 - 5));

		pl_title.add(lb_path);
		lb_path.setBounds(10, 2, SCR_WID / 4, 25);
		pl_title.add(btn_sel);
		btn_sel.setBounds(SCR_WID / 4 + 20, 4, 100, 25);
		pl_title.add(lb_count);
		lb_count.setBounds(SCR_WID / 4 + 130, 4, 200, 25);
		pl_title.add(btn_undo);
		btn_undo.setBounds(SCR_WID / 4 + 340, 4, 65, 25);

		initBtnMap();
		// pl_title.add(btn_skip);
		// btn_skip.setBounds(SCR_WID / 4 + 410, 4, 65, 25);
		pl_image.add(lb_image);
		frame.add(pl_title, BorderLayout.NORTH);
		frame.add(pl_image, BorderLayout.CENTER);

		/*
		 * 用于显示分类相关按钮界面。初始化为按钮空白， 在用户输入文件夹信息并确认后添加按钮。 在文件归档后，在lb_comfirm显示相关信息
		 */
		pl_sort = new JPanel(new BorderLayout());
		pl_btn = new JPanel();
		pl_bottom = new JPanel(null);
		lb_comfirm = new JLabel();
		in_folder = new JTextField(10);
		btn_add = new JButton("添加");
		btn_pre = new JButton("Pre");
		btn_next = new JButton("Next");

		pl_sort.setPreferredSize(new Dimension(SCR_WID / 2, SCR_HGT / 4 - 100));
		pl_btn.setBackground(Color.WHITE);
		pl_btn.setPreferredSize(new Dimension(SCR_WID / 4, SCR_HGT / 4 - 180));
		pl_bottom.setBackground(Color.YELLOW);
		pl_bottom.setPreferredSize(new Dimension(SCR_WID / 2, 40));
		lb_comfirm.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		in_folder.setFont(new Font("微软雅黑", Font.PLAIN, 16));
		in_folder.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addBtn();
				}
			}

		});
		pl_bottom.add(lb_comfirm);
		lb_comfirm.setBounds(0, 3, SCR_WID / 4, 30);
		pl_bottom.add(in_folder);
		in_folder.setBounds(SCR_WID / 4 + 10, 4, 200, 30);
		// 为按钮添加监听，点击后创建相关按钮
		btn_add.addActionListener(this);
		pl_bottom.add(btn_add);
		btn_add.setBounds(SCR_WID / 4 + 220, 4, 80, 30);
		btn_pre.addActionListener(this);
		btn_pre.setEnabled(false);
		pl_bottom.add(btn_pre);
		btn_pre.setBounds(SCR_WID / 4 + 320, 4, 60, 30);
		btn_next.addActionListener(this);
		btn_next.setEnabled(false);
		pl_bottom.add(btn_next);
		btn_next.setBounds(SCR_WID / 4 + 385, 4, 70, 30);
		pl_sort.add(pl_btn, BorderLayout.CENTER);
		pl_sort.add(pl_bottom, BorderLayout.SOUTH);
		frame.add(pl_sort, BorderLayout.SOUTH);

		updateBtn(null);
		frame.setVisible(true);
		frame.pack();
	}

	// 用于让其他类调用以设置图片
	private void setImage(int id) {
		curImgFile = fileHandler.getFile(id);
		if (curImgFile != null) {
			ImageTool imgTool = new ImageTool(curImgFile.getAbsolutePath(), pl_image.getWidth(), pl_image.getHeight());
			lb_image.setIcon(imgTool.getImageIcon());

			// 设置图片的位置居中
			int width = lb_image.getIcon().getIconWidth();
			int height = lb_image.getIcon().getIconHeight();
			int poiX = (pl_image.getWidth() - width) / 2;
			int poiY = (pl_image.getHeight() - height) / 2;
			lb_image.setBounds(poiX, poiY, width, height);
			pl_image.repaint();
			pl_image.revalidate();

			lb_count.setText("Progress: " + curImg + "/" + totalImg);
			lb_path.setText((fileHandler.isSorted(curImg) ? "(已分类)" : "(未分类)") + curImgFile.getAbsolutePath());
			lb_path.setToolTipText(lb_path.getText());
			lb_comfirm.setToolTipText(lb_comfirm.getText());
			if (totalImg > 1) {
				if (curImg < totalImg) {
					btn_next.setEnabled(true);
					btn_skip.setEnabled(true);
				} else {
					btn_next.setEnabled(false);
					btn_skip.setEnabled(false);
				}
				if (curImg > 1)
					btn_pre.setEnabled(true);
				else
					btn_pre.setEnabled(false);
			}
		}
	}

	private File selectPath() {
		JFileChooser choose = new JFileChooser();
		choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		choose.setDialogTitle("选择工作目录");
		if (workSpace != null)
			choose.setCurrentDirectory(workSpace);
		int result = choose.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			return choose.getSelectedFile();
		}
		return null;
	}

	// 初始化与按钮相关的表
	private void initBtnMap() {
		buttons = new HashMap<Integer, JButton>();
		clickedCount = new HashMap<JButton, Integer>();
		buttons.put(buttons.size() + 1, btn_skip);
		clickedCount.put(btn_skip, 0);
	}

	// 对按钮进行点击统计和排序
	private void sortButton() {
		ArrayList<Integer> list = new ArrayList<Integer>(); // 临时表，用于存放点击次数并排序
		ArrayList<JButton> btntmp = new ArrayList<JButton>(); // 临时表，用于存储已经添加到buttons的按钮，检测重复
		list.addAll(clickedCount.values());
		Collections.sort(list, Collections.reverseOrder());
		int i = 1;
		for (int c : list) {
			for (JButton button : clickedCount.keySet()) {
				if (clickedCount.get(button) == c) {
					if (btntmp.contains(button))
						continue;
					// print(button.getText());
					btntmp.add(button);
					buttons.put(i++, button);
					break;
				}
			}
		}
	}

	private JButton getBtn(String btn) {
		for (int i = 1; i <= buttons.size(); i++) {
			if (buttons.get(i).getText().equals(btn))
				return buttons.get(i);
		}
		return null;
	}

	private void removeBtn(JButton btn) {
		int size = buttons.size();
		for (int i = 1; i <= size; i++) {
			buttons.remove(i);
		}
		clickedCount.remove(btn);
		sortButton();
	}

	// 用于刷新文件夹的按钮。如果输入的文件夹名不为空，则添加按钮，否则刷新面板
	private void updateBtn(JButton button) {
		if (button != null) { // 输入不为null，则添加按钮
			// 设置按钮及其相应事件
			button.setFont(new Font("微软雅黑", Font.BOLD, 20));
			button.addActionListener(this);
			button.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						String path = workSpace + "\\" + button.getText();
						int r = JOptionPane.showConfirmDialog(pl_btn, "同时删除文件夹？", "警告", JOptionPane.YES_NO_OPTION);
						if (r == JOptionPane.YES_OPTION) { // 尝试删除文件夹
							File pathF = new File(path);
							if (pathF.exists() && !pathF.delete())
								JOptionPane.showMessageDialog(pl_btn, "文件夹不为空，无法删除！");
							else {
								button.removeMouseListener(this);
								removeBtn(button);
								updateBtn(null);
							}
							fileHandler.removeDir(path);
						} else { // 直接删除按钮，不对文件夹处理
							button.removeMouseListener(this);
							removeBtn(button);
							updateBtn(null);
							fileHandler.removeDir(path);
						}
					}
				}

			});
			buttons.put(buttons.size() + 1, button);
			clickedCount.put(button, 0);
		}
		sortButton();
		pl_btn.removeAll();
		for (int i = 1; i <= buttons.size(); i++) {
			pl_btn.add(buttons.get(i));
		}
		pl_btn.repaint();
		pl_btn.revalidate();
	}

	private boolean containsBtn(String name) {
		for (JButton btn : clickedCount.keySet()) {
			if (name.equals(btn.getText()))
				return true;
		}
		return false;
	}

	// 添加按钮的功能实现
	private void addBtn() {
		String folder = in_folder.getText();
		// 如果输入的文件夹名为空，则提示
		if (folder.matches("\\s+.*") || folder.equals("")) {
			JOptionPane.showMessageDialog(pl_btn, "文件名不正确", "Warning", JOptionPane.ERROR_MESSAGE);
			in_folder.setText("");
		} else if (!containsBtn(folder)) { // 如果按钮不存在，则进行添加,并创建一个文件夹

			String name = workSpace + "\\" + folder;
			JButton btn = new JButton(folder);
			File temp = new File(name);
			boolean r = false;
			if (!temp.exists()) {
				r = fileHandler.createDir(new File(name));
				if (r) {
					updateBtn(btn);
					fileHandler.addDir(name);
					in_folder.setText("");
				} else
					lb_comfirm.setText("创建目录失败，尝试重新创建或者手动创建！");
			} else {
				updateBtn(btn);
				fileHandler.addDir(name);
				in_folder.setText("");
			}
			in_folder.requestFocus();
		} else
			in_folder.setText("");
	}

	// 对按钮进行排序

	// 撤销操作
	private void undo() {
		String[] text = undoStack.get(stack).split(";;;");
		String curPath = workSpace.getAbsolutePath() + "\\";
		// print("stack Lefted: "+stack);
		// print("this undo text: "+text[0]+": "+text[1]+": "+text[2]);
		if (text[2].equals("skipped")) {
			lb_comfirm.setText("撤销：" + text[1]);
			undoStack.remove(stack--);
			if (stack == 0)
				btn_undo.setEnabled(false);
			curImg = Integer.parseInt(text[0]);
			fileHandler.removeMark(curImg, text[1]);
			sortedImg--;
			setImage(curImg);
			JButton b = getBtn(text[1].substring(curPath.length(), text[1].lastIndexOf("\\")));
			// print(text[1].substring(curPath.length(),
			// text[1].lastIndexOf("\\")));
			if (b != null)
				clickedCount.put(b, clickedCount.get(b) - 1);
			return;
		}
		String src = text[2];
		String dst = text[1];
		File r = fileHandler.moveFile(new File(src), new File(dst));
		if (r != null) {
			lb_comfirm.setText("撤销：" + src + "→" + dst);
			undoStack.remove(stack--);
			if (stack == 0)
				btn_undo.setEnabled(false);
			curImg = Integer.parseInt(text[0]);
			fileHandler.removeMark(curImg, dst);
			sortedImg--;
			setImage(curImg);
			JButton b = getBtn(dst.substring(curPath.length(), dst.lastIndexOf("\\")));
			// print(dst.substring(curPath.length(), dst.lastIndexOf("\\")));
			if (b != null)
				clickedCount.put(b, clickedCount.get(b) - 1);
		}
	}

	// 加载内容，初始化
	public void loadWork(int cur, int workLoad) {
		totalImg = workLoad;
		curImg = cur;
		setImage(curImg);
		lb_count.setText("Progress: " + curImg + "/" + totalImg);
		lb_path.setText(fileHandler.isSorted(curImg) ? "(已分类)" : "(未分类)" + curImgFile.getAbsolutePath());
		lb_path.setToolTipText(lb_path.getText());
		lb_comfirm.setText("系统就绪");
		initBtnMap();
		ArrayList<String> list = fileHandler.getDir();
		if (list != null && list.size() > 0)
			for (String name : list) {
				String n = name.substring(name.lastIndexOf("\\") + 1, name.length());
				JButton btn = new JButton(n);
				updateBtn(btn);
			}
	}

	// 设置工作目录方法
	private void setWorkSpace() {
		int cur = -1;
		File workPath;
		workPath = selectPath();
		/*
		 * 如果当前已经打开了一个文件夹，判断是否为当前工作目录的子目录， 如果是，那么跳转到相应的序号， 否则创建新的实例
		 */
		if (fileHandler != null) {
			cur = fileHandler.changeWork(workPath.getAbsolutePath());
			// print("setWorkSpace: return cur = "+cur);
			if (cur > 0) {
				curImg = cur;
				setImage(curImg);
			} else if (cur == -1)
				fileHandler.endWork();
			else if (cur == -2)
				JOptionPane.showMessageDialog(frame, "该文件夹下不存在图片文件，请重新选择");
		}
		if (workPath != null && cur == -1) {
			// print("setWorkSpace: "+workPath.getAbsolutePath());
			workSpace = workPath;
			fileHandler = new FileHandler(this, workSpace);
			Thread t = new Thread(fileHandler);
			t.start();
			lb_path.setText(workSpace.getAbsolutePath());
			undoStack = new HashMap<Integer, String>();
			initBtnMap();
		}
	}

	// 打印
	void print(String x) {
		System.out.println(x);
	}

	// 运行
	public static void main(String[] args) {
		HelperUI ui = new HelperUI();
		ui.createUI();
		ui.startTime = java.util.Calendar.getInstance().getTimeInMillis();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btn_add) {
			addBtn();
		} else if (e.getSource() == btn_skip) {
			if (!fileHandler.isSorted(curImg)) {
				String curPath = workSpace.getAbsolutePath() + "\\";
				fileHandler.markSorted(curImg, curImgFile.getAbsolutePath());
				undoStack.put(++stack, curImg + ";;;" + curImgFile.getAbsolutePath() + ";;;" + "skipped");
				lb_comfirm.setText("跳过: " + curImgFile.getAbsolutePath());
				btn_undo.setEnabled(true);
				sortedImg++;
				String name = curImgFile.getAbsolutePath();
				JButton b = getBtn(name.substring(curPath.length(), name.lastIndexOf("\\")));
				// print("act: "+name.substring(curPath.length(),
				// name.lastIndexOf("\\")));
				if (b != null)
					clickedCount.put(b, clickedCount.get(b) + 1);
			}
			setImage(++curImg);
		} else if (e.getSource() == btn_sel) {
			setWorkSpace();
		} else if (e.getSource() == btn_pre) {
			if (curImg > 1) {
				setImage(--curImg);
			}
		} else if (e.getSource() == btn_next) {
			if (curImg < totalImg) {
				setImage(++curImg);
			}
		} else if (e.getSource() == btn_undo) {
			undo();
		} else if (buttons != null) {
			for (int i = 1; i <= buttons.size(); i++) {
				JButton btn = buttons.get(i);
				if (e.getSource() == btn) {
					String dstName = workSpace.getAbsolutePath() + "\\" + btn.getText() + "\\" + curImgFile.getName();
					if (!dstName.equals(curImgFile.getAbsolutePath())) {
						File result = fileHandler.moveFile(curImgFile, new File(dstName));
						if (result != null) {
							undoStack.put(++stack,
									curImg + ";;;" + curImgFile.getAbsolutePath() + ";;;" + result.getAbsolutePath());
							btn_undo.setEnabled(true);
							fileHandler.markSorted(curImg, dstName);
							sortedImg++;
							lb_comfirm.setText("move to: " + result.getAbsolutePath());
							if (curImg < totalImg)
								setImage(++curImg);
							clickedCount.put(btn, clickedCount.get(btn) + 1);
						} else
							lb_comfirm.setText("System ERROR!");
						updateBtn(null);
						break;
					} else {
						btn_skip.doClick();
					}
				}
			}
		}
		frame.requestFocus();
	}

	class myMouseAdapter extends MouseAdapter {

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int width = lb_image.getIcon().getIconWidth() - 100 * e.getWheelRotation();
			int height = lb_image.getIcon().getIconHeight() - 100 * e.getWheelRotation();
			int poiX = (pl_image.getWidth() - width) / 2;
			int poiY = (pl_image.getHeight() - height) / 2;
			ImageTool imgTool = new ImageTool(curImgFile.getAbsolutePath(), width, height);
			lb_image.setIcon(imgTool.getScaledIcon());
			pl_image.add(lb_image);
			lb_image.setBounds(poiX, poiY, width, height);
			pl_image.repaint();
			pl_image.revalidate();
		}

		int lbX, lbY;

		@Override
		public void mousePressed(MouseEvent e) {
			lbX = e.getX();
			lbY = e.getY();
			if (e.getSource() == frame) {
				frame.requestFocus();
			}
			if(e.getSource() == lb_count) {
				lb_count.setEditable(true);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int x = e.getX() - lbX;
			int y = e.getY() - lbY;
			lb_image.setBounds(lb_image.getX() + x, lb_image.getY() + y, lb_image.getIcon().getIconWidth(),
					lb_image.getIcon().getIconHeight());
			// print("lb:" + lbX + ":lbY:" + lbY + ": x: " + x + ": y: " + y);
			// print(lb_image.getX() + ":" + lb_image.getY());
			pl_image.repaint();
			pl_image.revalidate();
		}
	}
}
