import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class TetrisGame extends JFrame{

boolean stopthread=false;
int SIZE_HORIZ=13;
int SIZE_VERTI=35;
Graphics g;
boolean waitnow=false;
int[][] board=new int[SIZE_VERTI][SIZE_HORIZ];
int t_[]={1,0,1,1,1,2,2,1};
int t_90[]={0,1,1,0,1,1,2,1};
int t_180[]={0,1,1,0,1,1,1,2};
int t_270[]={0,1,1,1,1,2,2,1};
int l_[]={0,2,1,0,1,1,1,2};
int l_90[]={0,1,1,1,2,1,2,2};
int l_180[]={1,0,1,1,1,2,2,0};
int l_270[]={0,0,0,1,1,1,2,1};
int s_[]={0,0,0,1,1,0,1,1};
int z_[]={1,1,1,2,2,0,2,1};
int z_90[]={0,1,1,1,1,2,2,2};
int i_ver[]={0,1,1,1,2,1,3,1};
int i_hor[]={1,0,1,1,1,2,1,3};
String scorestr;
Random rand=new Random(System.currentTimeMillis());
Color colors[]={Color.BLUE,Color.GREEN,Color.MAGENTA,Color.RED,Color.YELLOW};
File HighScore;
Color bgcolor=Color.WHITE;
/*
Numbering for blocks:
(values of fallingBlockNumber)
0=T
1=L
2=S
3=Z
4=I
*/
int [] blockarray;
int fallingblockNum;
int fallingBlockVersion=0;
int fallingBlockRow=0;
int fallingBlockCol=0;
int startdelay=200;
int motiondelay;
int scoreInc=5;
int myscore=0;
int tversion;
boolean spawn=true;
int scorespeedctrl=0;
int timehalving=0;
String highscoreholder;
int highscore;
KeyListener kl;

public TetrisGame()
{
int i,j;
startdelay=250;
scoreInc=5;
myscore=0;
motiondelay=startdelay;
NextBlock();
for(i=0;i<35;i++)
    for(j=0;j<13;j++)
 board[i][j]=0;
setDefaultCloseOperation(EXIT_ON_CLOSE);
setSize(480,500);
setResizable(false);
setLocationRelativeTo(null);
HighScore=new File(System.getProperty("java.io.tmpdir")+File.separator+"TetrisHighScore");
try{
if(HighScore.exists())
    {
    BufferedReader br=new BufferedReader(new FileReader(HighScore));
    highscoreholder=br.readLine();
    highscore=Integer.parseInt(br.readLine());
    br.close();
    }
else
    {
    highscoreholder=null;
    highscore=0;
    }
}catch(Exception ex){}
kl=new KeyListener() {
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
    
        if(!spawn&&!waitnow)
 {
            waitnow=true;
            int key=e.getKeyCode();
     if(key==KeyEvent.VK_UP)//up
  {
  if(fallingblockNum==0||fallingblockNum==1)
      tversion=(fallingBlockVersion+1)%4;
  else if(fallingblockNum==4||fallingblockNum==3)
      tversion=(fallingBlockVersion+1)%2;

  if(fallingblockNum!=2&&isDrawable(fallingBlockRow,fallingBlockCol,tversion))
      {
      clearOldBlockVersion(g);
      fallingBlockVersion=tversion;
      blockarray=getFallingBlockArray();
      drawNewBlockVersion(g);
      }
  }
     else if(key==KeyEvent.VK_LEFT)//left
  {
  if(isDrawable(fallingBlockRow,fallingBlockCol-1,fallingBlockVersion))
      {
      clearOldBlockVersion(g);
      fallingBlockCol--;
      drawNewBlockVersion(g);
      }
  }
     else if(key==KeyEvent.VK_RIGHT)//right
  {
  if(isDrawable(fallingBlockRow,fallingBlockCol+1,fallingBlockVersion))
      {
      clearOldBlockVersion(g);
      fallingBlockCol++;
      drawNewBlockVersion(g);
      }
  }
     else if(key==KeyEvent.VK_DOWN)//down
  {
  if(isDrawable(fallingBlockRow+1,fallingBlockCol,fallingBlockVersion))
      {
      clearOldBlockVersion(g);
      fallingBlockRow++;
      drawNewBlockVersion(g);
      }
  }
            waitnow=false;
 }
    }
    @Override
    public void keyReleased(KeyEvent e) {}
};
th.start();
addKeyListener(kl);
}

void NextBlock()
{
fallingblockNum=rand.nextInt(5);
if(fallingblockNum==0||fallingblockNum==1)
 fallingBlockVersion=rand.nextInt(4);
else if(fallingblockNum==4||fallingblockNum==3)
 fallingBlockVersion=rand.nextInt(2);
else
 fallingBlockVersion=0;
fallingBlockRow=0;
fallingBlockCol=5;
blockarray=getFallingBlockArray();
}

int[] getFallingBlockArray()
{
int a=fallingblockNum*10+fallingBlockVersion;
switch(a)
    {
    case 0:return (t_);
    case 1:return (t_90);
    case 2:return (t_180);
    case 3:return (t_270);
    case 10:return (l_);
    case 11:return (l_90);
    case 12:return (l_180);
    case 13:return (l_270);
    case 20:return (s_);
    case 30:return (z_);
    case 31:return (z_90);
    case 40:return (i_hor);
    case 41:return (i_ver);
    }
return (i_ver);
}

boolean isDrawable(int newrow,int newcol,int blockversion)
{
int i,tempversion;
boolean flag=true;
tempversion=fallingBlockVersion;
fallingBlockVersion=blockversion;
blockarray=getFallingBlockArray();
for(i=0;i<8;i+=2)
    {
    if(newrow+blockarray[i]>34||newrow+blockarray[i]<0)
 {
 flag=false;
 break;
 }
    if(newcol+blockarray[i+1]>12||newcol+blockarray[i+1]<0)
 {
 flag=false;
 break;
 }
    if(board[(newrow+blockarray[i])][(newcol+blockarray[i+1])]==2)
 {
 flag=false;
 break;
 }
    }
fallingBlockVersion=tempversion;
blockarray=getFallingBlockArray();
return flag;
}

void clearOldBlockVersion(Graphics g)
{
int i,r,c;
for(i=0;i<8;i+=2)
 {
 r=fallingBlockRow+blockarray[i];
 c=fallingBlockCol+blockarray[i+1];
 board[r][c]=0;
 g.setColor(bgcolor);
        g.fillRect(8+c*13,32+r*13,14,14);
 }
}

void drawNewBlockVersion(Graphics g)
{
int i,r,c;
for(i=0;i<8;i+=2)
 {
 r=fallingBlockRow+blockarray[i];
 c=fallingBlockCol+blockarray[i+1];
 board[r][c]=1;
        g.setColor(colors[fallingblockNum]);
 g.fillRect(8+c*13,32+r*13,13,13);
        g.setColor(Color.BLACK);//cyan,orange
 g.drawRect(8+c*13,32+r*13,13,13);
 }
}
boolean isGameOver(Graphics g)
{
if(isDrawable(0,5,fallingBlockVersion)==false)
    return true;
drawNewBlockVersion(g);
if(isAtBottom())
    return true;
return false;
}

boolean isAtBottom()
{
int i,max=0,ti,tj;
for(i=0;i<8;i+=2)
    if(blockarray[i]>max)
 max=blockarray[i];
if(fallingBlockRow+max>=34)
 return true;
for(i=0;i<8;i+=2)
    {
    ti=blockarray[i]+fallingBlockRow;
    tj=blockarray[i+1]+fallingBlockCol;
    if(board[ti+1][tj]==2)
       return true;
    }
return false;
}

void showScore(Graphics g)
{
int left,top;
left=getWidth()-100;
top=getHeight()/2;
g.setColor(bgcolor);
g.fillRect(left,top,80,70);
g.setColor(Color.RED);
g.setFont(new Font("Arial",Font.BOLD,14));
g.drawString("Score: "+Integer.toString(myscore),left,top+20);
}

void CollapseFilledRow(Graphics g)
{
int i,j,k,sum,copyskipover=0,r;
for(i=34;i>=0;)
    {
    sum=0;//full flag
    for(j=0;j<13;j++)
 sum+=board[i][j];
    if(sum==2*13)//row full
 {
 myscore+=scoreInc;
 copyskipover++;
 }
    if(sum==0)
 break;
    i--;
    if(copyskipover>0)
 {
 for(j=0;j<13;j++)
     {
     r=i+copyskipover;
     board[r][j]=board[i][j];
     if(board[i][j]==0)
  {
                g.setColor(bgcolor);
                g.fillRect(8+j*13,32+r*13,14,14);
  }
     else
  {
                g.setColor(Color.GREEN);
                g.fillRect(8+j*13,32+r*13,13,13);
                g.setColor(Color.BLACK);
                g.drawRect(8+j*13,32+r*13,13,13);
  }
     }
 }
    }
for(k=0;k<copyskipover;k++)
    {
    r=i+k;
    for(j=0;j<13;j++)
 {
 board[r][j]=0;
        g.setColor(bgcolor);
 g.fillRect(8+j*13,32+r*13,14,14);
 }
    }
showScore(g);
}


public static void main(String[] args)
{
TetrisGame tt=new TetrisGame();
tt.setVisible(true);
}

void GameOver(Graphics g)
{
stopthread=true;
g.setColor(Color.RED);
g.setFont(new Font("Arial",Font.BOLD,28));
String str="Game Over.";
g.drawString(str,getWidth()/2-10, getHeight()/2);
if(highscore>0)
    str="Highscore : "+highscoreholder+" - "+Integer.toString(highscore);
g.setFont(new Font("Arial",Font.BOLD,16));
g.drawString(str,getWidth()/2-30, getHeight()/2+80);
if(myscore>highscore)
    {
        highscoreholder=JOptionPane.showInputDialog("New high score. Enter your name:");
        highscore=myscore;
        try{
        if(!HighScore.exists())
            HighScore.createNewFile();
        BufferedWriter bw=new BufferedWriter(new FileWriter(HighScore));
        bw.write(highscoreholder);
        bw.newLine();
        bw.write(Integer.toString(highscore));
        bw.close();
        }catch(Exception ee){}
    }
}


Thread th=new Thread()
{
int i;
public void run()

{
try {
    while(!isShowing())
        Thread.sleep(1000);
    } catch (InterruptedException ex) {}
setOpacity(1f);
setBackground(bgcolor);
getContentPane().setBackground(bgcolor);
g=getGraphics();
g.setColor(Color.RED);
g.drawRect(6,30,13*13+6,35*13+6);
showScore(g);
while(!stopthread)
{
g.setColor(Color.RED);
g.drawRect(6,30,13*13+6,35*13+6);
while (waitnow)
    {
    try {
        Thread.sleep(20);
    } catch (InterruptedException ex) {}
    }
waitnow=true;
if(isAtBottom()&&!spawn)
 {
 for(i=0;i<8;i+=2)
     {
     board[fallingBlockRow+blockarray[i]][fallingBlockCol+blockarray[i+1]]=2;
     }
 spawn=true;
 CollapseFilledRow(g);
 }
    if(spawn)
 {
 NextBlock();
 blockarray=getFallingBlockArray();
 spawn=false;
 if(isGameOver(g))
     {
     GameOver(g);
     return;
     }
 }
    else
 {
 timehalving=(timehalving+1)%3;
 if(timehalving==2)
  {
  clearOldBlockVersion(g);
  fallingBlockRow++;
  drawNewBlockVersion(g);
  }
 }
    scorespeedctrl=(scorespeedctrl+1)%140;
    if(scorespeedctrl==0&&motiondelay>0)
 {
 motiondelay-=12;
 scoreInc+=2;
        if(motiondelay<0)
            motiondelay=0;
 }
    waitnow=false;
    try {
        Thread.sleep(motiondelay);
            } catch (InterruptedException ex) {}
}
}
};
}