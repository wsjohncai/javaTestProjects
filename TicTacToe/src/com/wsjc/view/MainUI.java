package com.wsjc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.wsjc.tool.AIPutChess;

public class MainUI implements ActionListener {

	public int HM_Chess, AI_Chess;

	private JFrame frame;
	private JPanel leftPane;
	private ChessPanel chessPanel;
	private JRadioButton ai_first, hm_first;
	private ButtonGroup group;
	private JButton restart, backward;
	private JLabel searchHint;
	private JTextField searchDepth;
	private int width = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
	private int height = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
	private boolean isGameOn = false;
	private boolean ishumanTurn = false;
	private int depth = 4;
	private AIPutChess AI;

	private void init() {
		HM_Chess = ChessPanel.FIRSTHAND;
		AI_Chess = ChessPanel.SECONDHAND;
		AI = new AIPutChess(this, depth);
		frame = new JFrame("������");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);

		// �����岼��
		leftPane = new JPanel();
		leftPane.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30));
		leftPane.setPreferredSize(new Dimension(width / 3, height));
		ai_first = new JRadioButton("AI����");
		ai_first.setActionCommand("ai_first");
		hm_first = new JRadioButton("��������", true);
		hm_first.setActionCommand("hm_first");
		restart = new JButton("��ʼ�Ծ�");
		restart.setActionCommand("restart");
		backward = new JButton("����");
		backward.setActionCommand("backward");
		searchHint = new JLabel("�����������");
		searchDepth = new JTextField(10);
		searchDepth.setText("4");
		group = new ButtonGroup();
		ai_first.setFont(new Font("����", Font.BOLD, 20));
		hm_first.setFont(new Font("����", Font.BOLD, 20));
		searchHint.setFont(new Font("����", Font.BOLD, 16));
		searchDepth.setFont(new Font("����", Font.BOLD, 20));
		restart.setFont(new Font("����", Font.BOLD, 25));
		backward.setFont(new Font("����", Font.BOLD, 20));
		group.add(ai_first);
		group.add(hm_first);
		ai_first.addActionListener(this);
		hm_first.addActionListener(this);
		restart.addActionListener(this);
		backward.addActionListener(this);
		leftPane.add(ai_first);
		leftPane.add(hm_first);
		leftPane.add(searchHint);
		leftPane.add(searchDepth);
		leftPane.add(restart);
		leftPane.add(backward);
		frame.add(leftPane, BorderLayout.WEST);

		chessPanel = new ChessPanel(width / 3 * 2, height);
		chessPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!isGameOn) {
						JOptionPane.showConfirmDialog(frame, "�뿪ʼ��Ϸ��", "��Ϸδ��ʼ", JOptionPane.CLOSED_OPTION);
						return;
					}
					if (!ishumanTurn) {
						JOptionPane.showConfirmDialog(frame, "������Ļغϣ�", "��ȴ�AI����", JOptionPane.CLOSED_OPTION);
						return;
					}
					int x = e.getX();
					int y = e.getY();
					int poi = y / (chessPanel.getHeight() / 3) * 3 + x / (chessPanel.getWidth() / 3);
					if (chessPanel.putChess(poi, HM_Chess) != ChessPanel.ALREADY_EXIT) {
						backward.setEnabled(true);
						chessPanel.setEnabled(false);
						if (!hasWinner())
							AIStep();
					}
				}
			}
		});
		frame.add(chessPanel, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * �ж��Ƿ������ʤ��
	 * 
	 * @return
	 */
	private boolean hasWinner() {
		int winner = ChessPanel.checkResult(chessPanel.getMap());
		if (winner == 0)
			return false;

		finish();
		int op;
		if (winner == AI_Chess)
			op = JOptionPane.showConfirmDialog(frame, "��ѽ����������~����һ�ְɣ�", "��Ϸ����", JOptionPane.YES_NO_OPTION);
		else if (winner == ChessPanel.NO_SPACE)
			op = JOptionPane.showConfirmDialog(frame, "�����������͵���ƽ���ˣ�����һ�֣�", "��Ϸ����", JOptionPane.YES_NO_OPTION);
		else
			op = JOptionPane.showConfirmDialog(frame, "��ϲ����ʤ���ˣ��Ƿ�����һ�֣�", "��Ϸ����", JOptionPane.YES_NO_OPTION);

		if (op == JOptionPane.YES_OPTION)
			newGame();
		return true;
	}

	private void finish() {
		isGameOn = false;
		restart.setText("��ʼ��Ϸ");
		hm_first.setEnabled(true);
		ai_first.setEnabled(true);
		searchDepth.setEnabled(true);
	}

	/**
	 * ��ʼ�¶Ծ�
	 */
	private void newGame() {
		isGameOn = !isGameOn;
		chessPanel.init();
		if (isGameOn) {
			depth = Integer.parseInt(searchDepth.getText());
			if (depth < 1 || depth > 9) {
				JOptionPane.showMessageDialog(frame, "��������" + "������ΧӦΪ1~9�����������������룡", "��������",
						JOptionPane.ERROR_MESSAGE);
			}
			if (HM_Chess == ChessPanel.FIRSTHAND)
				ishumanTurn = true;
			else {
				ishumanTurn = false;
				AIStep();
			}
			restart.setText("���¿�ʼ");
			hm_first.setEnabled(false);
			ai_first.setEnabled(false);
			searchDepth.setEnabled(false);
		} else {
			finish();
		}
	}

	/**
	 * ����������
	 * 
	 * @param depth
	 *            �����������
	 */
	private void AIStep() {
		AI.setSearchDepth(depth);
		int poi = AI.getNextStep(chessPanel.getMap());
		if (poi == AIPutChess.NO_AVAILABLE_STEP) {
			finish();
			return;
		}
		chessPanel.putChess(poi, AI_Chess);
		ishumanTurn = true;
		hasWinner();
	}

	public static void main(String[] args) {
		MainUI ui = new MainUI();
		ui.init();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "hm_first":
			HM_Chess = ChessPanel.FIRSTHAND;
			AI_Chess = ChessPanel.SECONDHAND;
			break;
		case "ai_first":
			HM_Chess = ChessPanel.SECONDHAND;
			AI_Chess = ChessPanel.FIRSTHAND;
			break;
		case "restart":
			newGame();
			break;
		case "backward":
			int preStep = chessPanel.regret();
			if (preStep == ChessPanel.LASTCHESS) {
				if (AI_Chess == ChessPanel.FIRSTHAND)
					AIStep();
				else {
					backward.setEnabled(false);
					chessPanel.setEnabled(true);
				}
			}
			if (preStep == AI_Chess) {
				int temp = chessPanel.regret();
				if (temp == ChessPanel.LASTCHESS)
					backward.setEnabled(false);
			}
			isGameOn = true;
			break;
		default:
			break;
		}

	}

}
