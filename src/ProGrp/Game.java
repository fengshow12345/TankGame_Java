package ProGrp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;
import java.util.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Game extends JFrame implements ActionListener {

	MyPanel mp = null;
	GuankaPanel gkmb = null;

	JMenuBar cdan = null;
	JMenu cd1 = null;
	JMenuItem cdx1 = null;
	JMenuItem cdx2 = null;
	JMenuItem cdx3 = null;
	JMenuItem cdx4 = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Game tg = new Game();
	}

	//添加注释
	public Game() {
		cdan = new JMenuBar();
		cd1 = new JMenu("游戏(G)");
		cd1.setMnemonic('G');
		cdx1 = new JMenuItem("新游戏(N)");
		cdx2 = new JMenuItem("退出游戏(E)");
		cdx3 = new JMenuItem("存盘退出(C)");
		cdx4 = new JMenuItem("继续游戏(S)");

		cdx1.addActionListener(this);
		cdx1.setActionCommand("newGame");
		cdx2.addActionListener(this);
		cdx2.setActionCommand("exitGame");
		cdx3.addActionListener(this);
		cdx3.setActionCommand("saveGame");
		cdx4.addActionListener(this);
		cdx4.setActionCommand("goonGame");
		cd1.add(cdx1);
		cd1.add(cdx2);
		cd1.add(cdx3);
		cd1.add(cdx4);
		cdan.add(cd1);

		// mp = new MyPanel();
		// this.add(mp);
		// this.addKeyListener(mp);
		// // 创建线程，运行面板mp，启动线程
		// Thread t1 = new Thread(mp);
		// t1.start();

		gkmb = new GuankaPanel();
		Thread t6 = new Thread(gkmb);
		t6.start();
		this.setJMenuBar(cdan);
		this.add(gkmb);
		this.setTitle("坦克大战");

		// 设置
		this.setSize(600, 500);
		this.setLocation(300, 280);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals("newGame")) {
			mp = new MyPanel("newGame");
			this.remove(gkmb);
			this.add(mp);
			this.addKeyListener(mp);
			Thread t7 = new Thread(mp);
			t7.start();
			this.setVisible(true);
		} else if (e.getActionCommand().equals("exitGame")) {
			Jilu.bcjl();
			System.exit(0);
		} else if (e.getActionCommand().equals("saveGame")) {
			Jilu jl = new Jilu();
			jl.setDtk(mp.dtk);
			jl.cunpan();
			System.exit(0);
		} else if (e.getActionCommand().equals("goonGame")) {
			mp = new MyPanel("goonGame");
			Thread t8 = new Thread(mp);
			t8.start();
			this.remove(gkmb);
			this.add(mp);
			this.addKeyListener(mp);
			this.setVisible(true);
		}
	}

}

//
class GuankaPanel extends JPanel implements Runnable {

	int times = 0;

	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		if (times % 2 == 0) {
			g.setColor(Color.yellow);
			Font myFont = new Font("黑体", Font.BOLD, 38);
			g.setFont(myFont);
			g.drawString("第1关", 140, 140);
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Thread.sleep(600);
			} catch (Exception e) {
				// TODO: handle exception

			}
			times++;
			this.repaint();
		}
	}

}

class MyPanel extends JPanel implements KeyListener, Runnable {
	MyTank mt = null;
	Vector<DiTank> dtk = new Vector<DiTank>();
	Vector<Weizhi> wzjh =new Vector<Weizhi>();
	Vector<Baozha> bzjh = new Vector<Baozha>();
	int tksl = 3;

	// 初始化图片
	Image tp1 = null;

	public MyPanel(String ss) {
		
		Jilu.dqjl();

		mt = new MyTank(140, 232);// 创建坦克，并设置位置
		if (ss.equals("newGame")) {
			for (int i = 0; i < tksl; i++) {
				DiTank dt = new DiTank((i) * 181 + 5, 0);
				dt.setFangxiang(2);// 设置敌方坦克初始方向
				// 添加线程，并启动。使坦克随机运动
				Thread t2 = new Thread(dt);
				t2.start();
				// 创建子弹,设置发射起始点及方向，并加入到集合中
				Zidan zd = new Zidan(dt.x + 10, dt.y + 30, 2);
				dt.dzd.add(zd);
				// 创建敌方子弹线程，并启动
				Thread t3 = new Thread(zd);
				t3.start();

				dt.dtkxl(dtk);

				dtk.add(dt);
			}
		}
		else if(ss.equals("goonGame")){
			wzjh=Jilu.dupan();
			for(int i=0;i<wzjh.size();i++){
				Weizhi wz=wzjh.get(i);
				DiTank dt=new DiTank(wz.x, wz.y);
				dt.setFangxiang(wz.fangxiang);
				dt.dtkxl(dtk);
				Thread t9=new Thread(dt);
				t9.start();
				Zidan zd=new Zidan(dt.x+10, dt.y+30, 2);
				dt.dzd.add(zd);
				Thread t10=new Thread(zd);
				t10.start();
				dtk.add(dt);
						
			}
		}

		// 图片
		try {
			tp1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/on.jpg"));
			// 解决第一次不爆炸的方法
			bzjh.add(new Baozha(1, 1));
		} catch (Exception e) {
			// TODO: handle exception
		}
		Shengyin sy=new Shengyin("./tanksound.wav");
		sy.start();
	}

	public void tjsj(Graphics g) {
		this.drawTank(80, 330, g, 0, 0);
		g.setColor(Color.black);
		g.drawString(Jilu.getMtsl() + "", 116, 350);
		// 整型数据后面加上双引号，会把整型数据以字符串形式输出
		this.drawTank(150, 330, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Jilu.getDtsl() + "", 186, 350);
		this.drawTank(450, 86, g, 0, 1);
		g.setColor(Color.black);
		g.drawString(Jilu.getSdtj() + "", 486, 107);
		g.setColor(Color.black);
		Font f = new Font("黑体", Font.BOLD, 20);
		g.setFont(f);
		g.drawString("消灭的坦克数：", 410, 40);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 300);
		// 统计
		this.tjsj(g);

		// 绘制自己的坦克
		if (mt.shengming) {
			this.drawTank(mt.getX(), mt.getY(), g, mt.fangxiang, 0);
		}

		// 绘制DiTank
		for (int i = 0; i < dtk.size(); i++) {
			DiTank dt = dtk.get(i);
			if (dtk.get(i).shengming != false) {
				this.drawTank(dtk.get(i).getX(), dtk.get(i).getY(), g, dt.fangxiang, 1);
				// 绘制敌方子弹
				for (int j = 0; j < dt.dzd.size(); j++) {
					Zidan dtzd = dt.dzd.get(j);
					if (dtzd.shengming) {
						g.setColor(Color.white);
						g.fill3DRect(dtzd.x, dtzd.y, 3, 3, false);
					}
				}

			}

			this.repaint();
		}

		// 绘制子弹,并使之连发
		for (int i = 0; i < mt.aa.size(); i++) {
			Zidan zd = mt.aa.get(i);
			if (zd != null && zd.shengming == true) {
				g.setColor(Color.red);
				g.fill3DRect(zd.x, zd.y, 3, 3, false);

			}
			if (zd.shengming == false) {
				mt.aa.remove(zd);
			}
		}

		// g.setColor(Color.yellow);
		// g.fill3DRect(mt.getX(), mt.getY(), 5, 30, false);
		// g.fill3DRect(mt.getX() + 15, mt.getY(), 5, 30, false);
		// g.fill3DRect(mt.getX() + 5, mt.getY() + 5, 10, 20, false);
		// g.fillOval(mt.getX() + 5, mt.getY() + 10, 10, 10);
		// g.drawLine(mt.getX() + 10, mt.getY() + 15, mt.getX() + 10, mt.getY()
		// - 3);

		for (int i = 0; i < bzjh.size(); i++) {
			Baozha bz = bzjh.get(i);
			if (bz.shengcunqi > 6) {
				g.drawImage(tp1, bz.x, bz.y, 30, 30, this);
			} else if (bz.shengcunqi > 3) {
				g.drawImage(tp1, bz.x, bz.y, 30, 30, this);
			} else {
				g.drawImage(tp1, bz.x, bz.y, 30, 30, this);
			}
			bz.suqsd();
			if (bz.shengcunqi == 0) {
				bzjh.remove(bz);
			}

		}

	}

	public void jzwf() {
		for (int i = 0; i < this.dtk.size(); i++) {
			DiTank dt = dtk.get(i);
			for (int j = 0; j < dt.dzd.size(); j++) {
				Zidan zd = dt.dzd.get(j);
				if (mt.shengming) {
					this.jzdf(zd, mt);
				}
			}
		}
	}

	// public void jzdf1() {
	// for (int i = 0; i < mt.aa.size(); i++) {
	// Zidan zd = mt.aa.get(i);
	// if (zd.shengming) {
	// for (int j = 0; j < dtk.size(); j++) {
	// DiTank dt = dtk.get(j);
	// if (dt.shengming) {
	// if (this.jzdf(zd, dt)) {
	// Jilu.dtjs();
	// Jilu.sdsl();
	// }
	//
	// }
	// }
	// }
	// this.repaint();
	// }
	// }

	public boolean jzdf(Zidan zd, Tank dt) {
		boolean b2 = false;
		switch (dt.fangxiang) {
		case 0:
		case 2:
			if (zd.x > dt.x && zd.x < dt.x + 20 && zd.y > dt.y && zd.y < dt.y + 30) {
				zd.shengming = false;
				dt.shengming = false;
				b2 = true;
				Jilu.dtjs();
				Jilu.sdsl();
				Baozha bz = new Baozha(dt.x, dt.y);
				bzjh.add(bz);

			}
			break;
		case 1:
		case 3:
			if (zd.x > dt.x && zd.x < dt.x + 30 && zd.y > dt.y && zd.y < dt.y + 20) {
				zd.shengming = false;
				dt.shengming = false;
				b2 = true;
				Jilu.dtjs();
				Jilu.sdsl();
				Baozha bz = new Baozha(dt.x, dt.y);
				bzjh.add(bz);
				// b2=true;
			}

		}
		return b2;
	}

	public void drawTank(int x, int y, Graphics g, int fangxiang, int leixing) {
		switch (leixing) {
		case 0:// 我的坦克
			g.setColor(Color.yellow);
			break;

		case 1:// 敌人的坦克
			g.setColor(Color.green);
			break;
		}

		switch (fangxiang) {
		case 0:
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x + 15, y, 5, 30, false);
			g.fill3DRect(x + 5, y + 5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x + 10, y - 3);
			break;

		case 1:
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5, y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x + 15, y + 10, x - 3, y + 10);
			break;

		case 2:
			g.fill3DRect(x, y, 5, 30, false);
			g.fill3DRect(x + 15, y, 5, 30, false);
			g.fill3DRect(x + 5, y + 5, 10, 20, false);
			g.fillOval(x + 5, y + 10, 10, 10);
			g.drawLine(x + 10, y + 15, x + 10, y + 33);
			break;

		case 3:
			g.fill3DRect(x, y, 30, 5, false);
			g.fill3DRect(x, y + 15, 30, 5, false);
			g.fill3DRect(x + 5, y + 5, 20, 10, false);
			g.fillOval(x + 10, y + 5, 10, 10);
			g.drawLine(x + 15, y + 10, x + 33, y + 10);
			break;

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_W) {
			this.mt.setFangxiang(0);
			this.mt.xiangshang();
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			this.mt.setFangxiang(1);
			this.mt.xiangzuo();
		} else if (e.getKeyCode() == KeyEvent.VK_S) {
			this.mt.setFangxiang(2);
			this.mt.xiangxia();
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			this.mt.setFangxiang(3);
			this.mt.xiangyou();
		}

		// 子弹事件
		if (e.getKeyCode() == KeyEvent.VK_J) {
			if (this.mt.aa.size() < 8) {
				this.mt.fszd();
			}

		}
		this.repaint();// 重绘

	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}

			// 用两层循环，目的是让每发子弹与每个敌人坦克比较
			for (int i = 0; i < mt.aa.size(); i++) {
				Zidan zd = mt.aa.get(i);
				if (zd.shengming) {
					for (int j = 0; j < dtk.size(); j++) {
						DiTank dt = dtk.get(j);
						if (dt.shengming) {
							this.jzdf(zd, dt);
						}
					}
				}
				this.repaint();
			}

		}
	}

}
