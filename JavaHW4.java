import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

class Hw4Button extends JButton {		//메인 패널 내부에 들어갈 버튼 
    int buttonCode;			//버튼에 들어갈 코드
    String otherNum = new String();		//버튼위에 표시될 문자
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (buttonCode < 10) otherNum = Integer.toString(buttonCode);
        else if (buttonCode == 10) otherNum = "C";
        else if (buttonCode == 11) otherNum = "+";
        else if (buttonCode == 12) otherNum = "-";
        else if (buttonCode == 13) otherNum = "=";
        else if(buttonCode == 14) otherNum = "+/-";
        else if(buttonCode == 15) otherNum = "00";
        
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g;

        if (getModel().isArmed()) { g2.setColor(getBackground().brighter()); }
        else if (getModel().isRollover()) { g2.setColor(getBackground().darker()); }
        else { g2.setColor(getBackground()); }

        g2.fillRect(0, 0, w, h);
        Font fp=new Font("Comic Sans MS", 1, (int)(getSize().width/(float)4));
        FontMetrics fontMetrics = getFontMetrics(fp);
        g2.setFont(fp);
        Rectangle stringBounds = fontMetrics.getStringBounds(otherNum, g2).getBounds();


        int textX = (w - stringBounds.width) / 2;
        int textY = (h - stringBounds.height) / 2 + fontMetrics.getAscent();
        g2.setColor(Color.decode("#E2E1E0"));			//폰트 색
        g2.drawOval(w/6,h/6,w*10/15,h*10/15);
        g2.setStroke(new BasicStroke(2));
        if(otherNum.equals("C"))g2.setColor(Color.decode("#ff533d"));	
        g2.drawString(otherNum, textX, textY);
    }
}

class Hw4Screen extends  JPanel{		//결과값 패널
    String Ans=new String();	//결과값 폰트
    Hw4Panel getNum;		//데이터 가져올 패널
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2=(Graphics2D)g;
        Dimension var1=getSize();
        GradientPaint inScreen = new GradientPaint(0, 0, Color.decode("#323232"), var1.width/2, var1.height, Color.decode("#707070"));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(inScreen);
        g2.fillRoundRect(0,0,var1.width,var1.height,10,10);
        Font ScreenFont = new Font("Comic Sans MS",1,(int)(getNum.ButtonH*(float)0.6));
        FontMetrics tmp=getFontMetrics(ScreenFont);
        int WIDTH=(int)(var1.width-tmp.stringWidth(Ans)-var1.width/20);
        int HEIGHT=var1.height/2+tmp.getAscent()/2;
        g2.setFont(ScreenFont);
        if(getNum.operator==1 && getNum.CheckNumberPad == 0)
            g2.setColor(Color.decode("#00FADC"));
        else if(getNum.operator==-1 && getNum.CheckNumberPad==1)
            g2.setColor(Color.decode("#ff533d"));
        else if(getNum.operator == 1)
        	g2.setColor(Color.decode("#FFFFFF"));
        g2.drawString(Ans,WIDTH,HEIGHT);
        repaint();
    }
}

class Hw4Panel extends JPanel implements ActionListener{		//메인 패널
    private int PrintNum=0;
    private int AnswerNum=0;
    private boolean isAnswer=true;
    int operator=1;
    int PanelW, PanelH;
    int CalculateWidth, CalculateHeight; 
    int ButtonW, ButtonH;
    int CheckNumberPad;
    private Hw4Screen ans=new Hw4Screen();
    private Hw4Button[] NumberButton= new Hw4Button[16];
    Hw4Panel() {
        int cnt=0;
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setLayout(null);
        for (int y=0;y<4;y++){
            for(int x=0;x<4;x++){
                NumberButton[cnt]=new Hw4Button();
                NumberButton[cnt].addActionListener(this);
                int buttonNum= 7 - 3*y + x;
                if(y<3 && x<3)
                    NumberButton[cnt].buttonCode=buttonNum;
                else if(y==0) 
                	NumberButton[cnt].buttonCode= 10;
                else if(y==1)
                    NumberButton[cnt].buttonCode=11;
                else if(y==2)
                    NumberButton[cnt].buttonCode=12;

                else if(y==3){
                    if(x==0)
                        NumberButton[cnt].buttonCode=14;
                    if(x == 1)
                    	NumberButton[cnt].buttonCode = 0;
                    if(x == 2)
                    	NumberButton[cnt].buttonCode = 15;
                    if(x==3)
                        NumberButton[cnt].buttonCode=13;

                }
                NumberButton[cnt].setBackground(Color.decode("#3E3B36"));
                if(NumberButton[cnt].buttonCode == 13)
                	NumberButton[cnt].setBackground(Color.decode("#4CC2FF"));
                else if(NumberButton[cnt].buttonCode >= 10 &&NumberButton[cnt].buttonCode <= 13) {
                	NumberButton[cnt].setBackground(Color.decode("#323232"));
                }
                add(NumberButton[cnt]);
                cnt++;
            }
        }
        ans.Ans="0";
        ans.getNum=this;		//해당 결과 값을 스크린으로 준다.
        add(ans);			//결과값을 패널을 만들어줌
    }
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Hw4ReSize();
        Graphics2D g2=(Graphics2D)g;
        ans.setBounds(PanelW-CalculateWidth/2+CalculateWidth/8,PanelH-3*ButtonH+ButtonH/4,CalculateWidth*6/8,ButtonH*3/2);
        if(isAnswer){
            ans.Ans=Integer.toString(AnswerNum);
        }
        else{
            ans.Ans=Integer.toString(PrintNum);
        }
        int i=0;
        for(int x=0;x<4;x++){
            for(int y=0;y<4;y++){
                NumberButton[i].setBounds(PanelW-CalculateWidth/2+ButtonW*y+CalculateWidth/300,PanelH-ButtonH+ButtonH*x+CalculateWidth/300,ButtonW-CalculateWidth/150,ButtonH-CalculateWidth/150);
                NumberButton[i].repaint();
                i++;
            }
        }
        g2.setColor(Color.decode("#202020"));
        g2.fillRect(PanelW-CalculateWidth/2,PanelH-3*ButtonH,CalculateWidth ,ButtonH*6);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3)); 
        g2.drawRoundRect(PanelW-CalculateWidth/2,PanelH-3*ButtonH,CalculateWidth,ButtonH*6,15,15);	//겉 프레임
        repaint();
    }
    private void Hw4ReSize(){
        PanelW=getWidth()/2;
        PanelH=getHeight()/2;
        CalculateHeight=getHeight()/2;
        CalculateWidth=getWidth()/2;
        if(CalculateHeight>CalculateWidth) {
            CalculateHeight = CalculateWidth * 3;
            CalculateWidth *= 2;
        }
        else {
            CalculateWidth = CalculateHeight * 2;
            CalculateHeight*=3;
        }
        ButtonW=CalculateWidth/4;
        ButtonH=CalculateHeight/9;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        int buttonNum=((Hw4Button) e.getSource()).buttonCode;
        if(e.getSource() instanceof Hw4Button){
                if(buttonNum<10){
                    PrintNum*=10;
                    PrintNum+=buttonNum;
                    isAnswer=false;
                    CheckNumberPad=1;
                }
                else if(buttonNum==10){
                    PrintNum=0;
                    AnswerNum=0;
                    isAnswer=true;
                    operator=1;
                    CheckNumberPad=0;
                }
                else if(buttonNum==11){
                    AnswerNum+=PrintNum*operator;
                    PrintNum=0;
                    operator=1;
                    isAnswer=true;
                    CheckNumberPad=0;
                }
                else if(buttonNum==12){
                    AnswerNum+=PrintNum*operator;
                    PrintNum=0;
                    operator=-1;
                    isAnswer=true;
                    CheckNumberPad=0;
                }
                else if(buttonNum==13){
                    AnswerNum+=PrintNum*operator;
                    PrintNum=0;
                    operator=1;
                    isAnswer=true;
                    CheckNumberPad=0;
                }
                else if(buttonNum==14) {
                	PrintNum *= -1;
                	isAnswer = false;
                	CheckNumberPad = 1;
                }
                else if(buttonNum== 15) {
                	PrintNum *= 100;
                	isAnswer = false;
                	CheckNumberPad = 1;
                }
        }
    }
}
public class JavaHW4 extends JFrame {
    JavaHW4(){
        setSize(600,600);
        setTitle("계산기");
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        add(new Hw4Panel());
        setVisible(true);
    }
    public static void main(String[] args){
        new JavaHW4();
    }
}