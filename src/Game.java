import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID=1L;
	
	public static final int Width= 180;
	
	public static final int Height=Width/12*9;
	
	public static final String name="newAwesomeGame";
	public static final int Scale=3;

	private static final String EXIT_ON_CLOSE = null;


	private JFrame frame;
	
	public boolean nowRunning=true;
	public int tickCount=0;
	
	private BufferedImage image=new BufferedImage(Width,Height,BufferedImage.TYPE_INT_RGB);
	private int[]numPix=((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	
	
	
	public Game() {
		setMinimumSize(new Dimension(Width *Scale, Height*Scale));
		setMaximumSize(new Dimension(Width *Scale, Height*Scale));
		setPreferredSize(new Dimension(Width *Scale, Height*Scale));
		
		frame=new JFrame(name);
		
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this,BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	public synchronized void start(){
		nowRunning=true;
		new Thread(this).start();
		
	}
	public synchronized void stop(){
		nowRunning=false;
	}
	
	

	public void run() {
		long nanTime=System.nanoTime();
		double nanPerTick=1000000000D/60D;
		
		int getFrames=0;
		int getNumTicks=0;
		long lastTimer=System.currentTimeMillis();
		double delta=0;
		
		while(nowRunning){
			long curnanTime=System.nanoTime();
			delta+=(curnanTime-nanTime)/nanPerTick;
			nanTime=curnanTime;
			boolean shouldRender=false;
			
			while(delta >= 1){
				getNumTicks++;
				tick();
				delta-=1;
				shouldRender=false;
			}
			try{
				Thread.sleep(2);
				
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			
			if(shouldRender)
			getFrames++;
			render();
			
			
			if(System.currentTimeMillis()-lastTimer > 1000){
				lastTimer+=1000;
				getFrames=0;
				getNumTicks=0;
			}
			}
		}
		
	
	public void tick(){
		tickCount++;
		
		for (int i=0; i <numPix.length;i++){
			numPix[i]=i*tickCount;
			
		}
	}
	
	public void render(){
		BufferStrategy bufStrat=getBufferStrategy();
		if(bufStrat==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g= bufStrat.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(),getHeight(),null);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getWidth(), getHeight());
		g.dispose();
		bufStrat.show();
		
	}
	public static void main(String[]args){
		new Game().start();
	}

}
