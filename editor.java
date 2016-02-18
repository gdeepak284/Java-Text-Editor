import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
public class Thistest implements Serializable{
    
public static void main(String args[])
{

    Notepad test;    
    test = new Notepad("Untitled");
}

}
class Attributes implements Serializable{
    //create another in which only attributes are there
    //editing part leave to another class
    //required before today
    private ArrayList<String> lines;
    private int line_no;
    private int charac_no;
    private int point_size;
    //private int maxwidth;
    Graphics g;
    Font current_font;
    String FontName,FontStyle,FontSize;
    FontMetrics fm;
    Notepad instance;
    Attributes(Notepad window)
    {
        lines=new ArrayList<String>(1);
        instance=window;
        charac_no=0;
        line_no=0;
        //maxwidth=0;
        FontName="Dialog";
        FontStyle="Plain";
        FontSize="20";
        current_font = new Font(FontName,Font.PLAIN,20);
    }
    int getcharac_no()
    {
        return charac_no;
    }
    int getline_no()
    {
        return line_no;
    }
    void setcharac_no(int next)
    {
        charac_no=next;
    }
    void setline_no(int next)
    {
        line_no=next;
    }
    int getx()
    {
        int x=20;
        g=instance.getGraphics();
        if(lines.get(line_no)!=null)
        x+=g.getFontMetrics().stringWidth(lines.get(line_no).substring(0,charac_no));
        return x;
    }
    int getx(int l,int c)
    {
        int x=20;
        g=instance.getGraphics();
        if(lines.get(l)!=null)
        x+=g.getFontMetrics().stringWidth(lines.get(l).substring(0,c));
        return x;
        
    }
    int gety(int line)
    {
        int height,leading;
        fm=g.getFontMetrics(current_font);
        height=fm.getHeight();
        leading=fm.getLeading();
        int y,temp;
        y=50+height;
        temp=(height+leading)*line;//+40*line;
        y+=temp;
        return y;
        
    }
    int getHeight()
    {
        g=instance.getGraphics();
        g.setFont(current_font);
        fm=g.getFontMetrics(current_font);
        return fm.getHeight();
    }
    int getLeading()
    {
        g=instance.getGraphics();
        g.setFont(current_font);
        fm=g.getFontMetrics(current_font);
        return fm.getLeading();
    }
    int getDescent()
    {
        g=instance.getGraphics();
        g.setFont(current_font);
        fm=g.getFontMetrics(current_font);
        return fm.getDescent();
    }
    /*int getPSize()
     * {
     * g=instance.getGraphics();
     * g.setFont(current_font);
     * 
     * point_size=current_font.getSize()+1;
     * System.out.println("point_size after changing font"+point_size);
     * return point_size;
     * }*/
    int getWidth(int line)//looks good
    {
        int width;
        g=instance.getGraphics();
        
        try{
            width=g.getFontMetrics().stringWidth(lines.get(line));
        }
        catch(NullPointerException e)
        {
            width=0;
        }
        catch( IndexOutOfBoundsException e)
        {
            width=0;
            System.out.println("Index out of bounds");
        }
        return width;
    }
    int getLength(int line)//looks good
    {
        int length;
        boolean check;
        check=checkEmpty(line);
        if(check==true)
        {
            System.out.println("inside length=0");
            return 0;
        }
        else{
            length=lines.get(line).length();
           // System.out.println("inside length="+length);
            return length;
        }
    }
    String getString(int line)
    {
        return lines.get(line);
    }
    void BackspaceHandler(){
        char c[] = new char[1];
        c[0]=(char)999;
        
        int size=getSize();
        if(size==0)
            return;
        if(size==1&&checkEmpty(0)==true)
            return;
        if(charac_no==0&&line_no==0)
            return;
        
        int length=getLength(line_no);
        int prevLlength=0;
        
        if(size!=1&&line_no!=0)
        prevLlength=getLength(line_no-1);
        
        String temp;
        String tempprev;
        
        if(length!=0){
        temp=getString(line_no);
        if(charac_no!=length)
        {
            if(charac_no!=0)
            temp=temp.substring(0,charac_no-1)+temp.substring(charac_no);
            else if(size!=0&&size!=1) 
            {
                if(prevLlength!=0)
                    tempprev=getString(line_no-1)+temp;
                else
                    tempprev=temp;
                
                    lines.remove(line_no);
                    lines.set(line_no-1, tempprev);
                    setline_no(line_no-1);
                    setcharac_no(prevLlength);
                    for(int i=line_no;i<size;i++)
                        instance.repaint(i);
                    System.out.println("returning");
                    return;
            }
            else
                return;
        }
        else
        {
            if(length!=1)
            temp=temp.substring(0,length-1);
        
            else
            {
                
                temp=new String(c);
            }        
        }
        setcharac_no(charac_no-1);
       // System.out.println(temp);
        //System.out.println("charac_no="+getcharac_no());
        lines.set(line_no, temp);
        instance.repaint(line_no);
        //instance.repaint(line_no);
        }
        else// current length==0
        {
            lines.remove(line_no);
            setline_no(line_no-1);
            setcharac_no(prevLlength);
            for(int i=line_no;i<size;i++)
                instance.repaint(i);
            return;
        }
    }
    void InsertC(int Kcode,boolean Shift){
        
        char c[] = new char[1];
        int length,width;
        boolean check;
        String temps,tempc;
       
        check=checkEmpty(line_no);
        if(check==true)
        {
            length=0;
            width=0;
            temps=null;
        }
        else
        {
            temps=lines.get(line_no);
            length=temps.length();
            width=getWidth(line_no);
        }        
        
        if(Kcode>=KeyEvent.VK_A && Kcode <=KeyEvent.VK_Z){        
            if(Shift==false){            
                Kcode+=32;            
            } 
        }
        
        c[0]=(char)Kcode;
        tempc=new String(c);
        
        if(length!=0){
            if(charac_no!=length)
            {
                if(charac_no!=0)
                    temps=temps.substring(0,charac_no)+tempc+temps.substring(charac_no);
                else
                temps=tempc+temps; 
            }
            else
            temps+=tempc;   
        }
        else
            temps=new String(c);
        
        setcharac_no(charac_no+1);
        
        //System.out.println("line changed to "+temps);
        
        if(lines.size()==line_no)
            lines.add(temps);    
        else
            lines.set(line_no, temps);
        instance.repaint(line_no);
        //instance.repaint(line_no);
    }
    boolean checkEmpty(int line_no){
        int size=lines.size();
        //System.out.println("size="+size);
        String temps;
        if(line_no>=size)
        {
            return true;
        }
        else
        {
            temps=lines.get(line_no);
            if(temps.charAt(0)==(char)999)
            {
                return true;
            }
            else
                return false;
            
        }
    }
    void addEmptyLine(int line_no)
    {
        int size=lines.size();
        String tempc;
        char c[]=new char[1];
        c[0]=(char)999;
        tempc=new String(c);
        if(size==line_no)
        {
            lines.add(tempc);
        }
        else 
        {
            lines.add(line_no,tempc);
        }
    }
    void Enter()
    {
        String temps1,temps2;
        boolean check;
        check=checkEmpty(line_no);
        if(check==true)
        {
            addEmptyLine(line_no+1);
        }
        else
        {
            int length=getLength(line_no);
            if(charac_no==length)
            {
                addEmptyLine(line_no+1);
            }
            else
            {
                if(charac_no!=0)
                {
                    temps1=lines.get(line_no).substring(0,charac_no);
                   // System.out.println("start="+temps1);
                    temps2=lines.get(line_no).substring(charac_no);
                   // System.out.println("end="+temps2);
                    lines.set(line_no, temps1);
                    lines.add(line_no+1, temps2);
                }
                else
                {
                    addEmptyLine(line_no);
                    
                }
            }
        }
        int size=lines.size();
        setcharac_no(0);
        setline_no(line_no+1);
            for(int i=line_no-1;i<size;i++)
            {
                instance.repaint(i);
            }
        
    }
    int getSize()
    {
        return lines.size();
    }
    void setFontAtr(String fName, String fStyle, String fSize){
        int setsize=20,setstyle=Font.PLAIN;
        System.out.println("sf="+fName);
            System.out.println("sstyle="+fSize);
            System.out.println("ssize="+fStyle);
        switch(fSize)
        {
            case "12":setsize=12;
                break;
            case "14":setsize=14;
                break;
            case "16":setsize=16;
                break;
            case "18":setsize=18;
                break;
            case "20":setsize=20;
                break;
            case "22":setsize=22;
                break;
            case "24":setsize=24;
                break;
            case "26":setsize=26;
                break;
            case "36":setsize=36;
                break;
            case "48":setsize=48;
                break;
            case "72":setsize=72;
                break; 
        }
        switch(fStyle)
        {
            case "Plain":setstyle=Font.PLAIN;
                break;
            case "Bold":setstyle=Font.BOLD;
                break;
            case "Italics":setstyle=Font.ITALIC;
                break;
        }
        System.out.println("Came till here");
        FontName=fName;
        FontStyle=fStyle;
        FontSize=fSize;
        System.out.println("sf="+FontName);
        System.out.println("sstyle="+FontStyle);
        System.out.println("ssize="+FontSize);
        current_font=new Font(fName,setstyle,setsize);
        System.out.println("here??");
    }
    Font getFont()
    {
        return current_font;
    }
    String getFontName()
    {
        return FontName;
    }
    String getFontSize()
    {
        return FontSize;
    }
    String getFontStyle()
    {
        return FontStyle;
    }
    
}
class MEditing{
    
    Notepad instance;
    Attributes text;
    private int Startx,Starty,Endx,Endy;
    private int Startl,Startc,Endl,Endc;
    
    MEditing(Notepad instance,Attributes text)
    {
        this.instance=instance;
        this.text=text;
    }
    void setStart(int x,int y)
    {
        Startx=x;
        Starty=y;
        convert(1);
    }
    void setEnd(int x,int  y)
    {
        Endx=x;
        Endy=y;
        convert(2);
    }
    void convert(int choice)
    {
        int x=0,y=0;
        int line,charac_no;
        int height=text.getHeight();
        int leading=text.getLeading();
        
        switch(choice){
            case 1:
                x=Startx;
                y=Starty;
                break;
            case 2:
                x=Endx;
                y=Endy;
                break;
            default:
                System.out.println("This Shouldn't print");
        }
        if(y-50-height-leading/2<0)
            line=0;
        else
        line=(y-50-height-leading/2)/(height+leading)+1;
        
        
        System.out.println("line_no right now is"+line);
        
    }
    
}






class Notepad extends JFrame implements Serializable
{
    Attributes atr;
    String Title;
    MEditing mouse;
    Notepad(String Title)
    {
        super(Title);
        this.Title=Title;
        this.setSize(500,500);
        addWindowListener(new WAdapter(this));
        addKeyListener(new KAdapter(this));
        MenuSetup();
        atr = new Attributes(this);
        mouse = new MEditing(this,atr);
        MouseHandler moh = new MouseHandler(this);
        this.addMouseListener(moh);
        this.setVisible(true);
    }
    void MenuSetup()
    {
        MenuBar bar = new MenuBar();
        setMenuBar(bar);
        MenuItem dash1,dash2,dash3,dash4;
        dash1 = new MenuItem("-");
        dash2 = new MenuItem("-");
        dash3 = new MenuItem("-");
        dash4 = new MenuItem("-");
        
        Menu File = new Menu("File");
        MenuItem New,Open,Save,Save_As,Exit;
        File.add(New = new MenuItem("New", new MenuShortcut(KeyEvent.VK_N)));
        File.add(Open = new MenuItem("Open", new MenuShortcut(KeyEvent.VK_O)));
        File.add(Save = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S)));
        File.add(Save_As = new MenuItem("Save As"));
        File.add(dash1);
        File.add(Exit = new MenuItem("Exit"));
        bar.add(File);
        
        
        Menu Edit = new Menu("Edit");
        MenuItem Undo,Cut,Copy,Paste,Delete,SelectA;
        Edit.add(Undo = new MenuItem("Undo", new MenuShortcut(KeyEvent.VK_Z)));
        Edit.add(dash2);
        Edit.add(Cut = new MenuItem("Cut", new MenuShortcut(KeyEvent.VK_X)));
        Edit.add(Copy = new MenuItem("Copy", new MenuShortcut(KeyEvent.VK_C)));
        Edit.add(Paste = new MenuItem("Paste", new MenuShortcut(KeyEvent.VK_V)));
        Edit.add(Delete = new MenuItem("Delete"));
        Edit.add(dash3);
        Edit.add(SelectA = new MenuItem("Select All", new MenuShortcut(KeyEvent.VK_A)));
        bar.add(Edit);
        
        Menu Format = new Menu("Format");
        MenuItem Font;
        Format.add(Font = new MenuItem("Font"));
        bar.add(Format);
        
        Menu Help = new Menu("Help");
        MenuItem Shortcuts,About;
        Help.add(Shortcuts = new MenuItem("Shortcuts"));
        Help.add(dash4);
        Help.add(About = new MenuItem("About"));
        bar.add(Help);
        
        MenuHandler mh = new MenuHandler(this);
        
        New.addActionListener(mh);
        Open.addActionListener(mh);
        Save.addActionListener(mh);
        Save_As.addActionListener(mh);
        Exit.addActionListener(mh);
        
        Undo.addActionListener(mh);
        Cut.addActionListener(mh);
        Copy.addActionListener(mh);
        Paste.addActionListener(mh);
        Delete.addActionListener(mh);
        SelectA.addActionListener(mh);
        
        Shortcuts.addActionListener(mh);
        About.addActionListener(mh);
        
        Font.addActionListener(mh);
    }
    @Override
    public void paint(Graphics g)
    {
        int line,size,height,descent,x,y,line_no;
        line_no=this.atr.getline_no();
        size=this.atr.getSize();
        for(line=0;line<size;line++)
        {
            height=this.atr.getHeight();
            descent=this.atr.getDescent();
            
            x=20+this.atr.getWidth(line);
            y=this.atr.gety(line);
            
            g.setFont(this.atr.getFont());
            g.setColor(Color.white);
            g.fillRect(0, y-height, 10000,this.atr.gety(this.atr.getSize()));
            
            
            if(this.atr.checkEmpty(line)==true)
            {
                if(line==line_no)
                this.updatecursor();
                continue;
            }
          
            g.setColor(Color.black);
            g.drawString(this.atr.getString(line),20 , y-descent);
            if(line==line_no)
            this.updatecursor();
        }
    }
    public void repaint(int line)
    {
        int height,descent,x,y,line_no;
        Graphics g;
        line_no=this.atr.getline_no();
       
        height=this.atr.getHeight();
        descent=this.atr.getDescent();
        
        x=20+this.atr.getWidth(line);
        y=this.atr.gety(line);
        
        
        g=this.getGraphics();
        g.setFont(this.atr.getFont());
        g.setColor(Color.white);
        g.fillRect(0, y-height, 1000,height);
        
        
        if(this.atr.checkEmpty(line)==true)
        {   if(line==line_no)
            this.updatecursor();
            return ;
        }
        
        g.setColor(Color.black);
        g.drawString(this.atr.getString(line),20 , y-descent);
        if(line==line_no)
            this.updatecursor();
    }
    //always update cursor first then draw over it
    //also remove prev pos of cursor
    void updatecursor()
    {
        String temp;
        Graphics g;
        FontMetrics fm;
        int charac_no,line_no,width,height,y,x;
        int leading;
        g=this.getGraphics();
        
        charac_no=this.atr.getcharac_no();
        line_no=this.atr.getline_no();
        height=this.atr.getHeight();
        y=this.atr.gety(line_no);
        g.setFont(this.atr.getFont());
        fm=g.getFontMetrics();
        if(this.atr.checkEmpty(line_no)==true)
        {
            x=20;
            width=fm.charWidth('a');
            System.out.println("empty line");
        }
        else{ 
            temp=this.atr.getString(line_no);
            if(charac_no!=temp.length()){
                if(charac_no!=0){
                width=fm.charWidth(temp.charAt(charac_no));
                x=20+fm.stringWidth(temp.substring(0, charac_no+1))-width;
                }
                else
                {
                    width=fm.charWidth(temp.charAt(0));
                    x=20;
                }
            }
            else{
                width=g.getFontMetrics().charWidth('a');
                x=20+g.getFontMetrics().stringWidth(temp);
            }
            
        }
        g.setXORMode(Color.red);
        g.fillRect(x,y-height, width, height);
        g.setPaintMode();
    }
}

//Handles all Window events of parent window
class WAdapter extends WindowAdapter implements Serializable{
   Notepad sample;
   public WAdapter(Notepad sample){
       this.sample=sample;
   }
   @Override
   public void windowClosing(WindowEvent we){
       //later only close the window which is open not other windows
       sample.setVisible(false);
       sample.dispose();
   }
}

//Handles all Keyboard events of parent window
class KAdapter extends KeyAdapter implements Serializable{
    Notepad test;
    boolean Shift,Ctrl,Alt,CapsLock;
    KAdapter(Notepad test){
        this.test=test;
        Shift=false;
        Ctrl=false;
        Alt=false;
        CapsLock=false;
    }
    
    @Override
    public void keyPressed(KeyEvent ke){
        int Kcode=ke.getKeyCode();
        int line_no=test.atr.getline_no();
        int charac_no=test.atr.getcharac_no();
        int length=test.atr.getLength(line_no);
        int size=test.atr.getSize();
        
        if(Kcode==KeyEvent.VK_SHIFT){
            Shift=true;
        }
        else if(Kcode==KeyEvent.VK_CONTROL){
            Ctrl=true;
        }
        else if(Kcode==KeyEvent.VK_ALT){
            Alt=true;
        }
        else if(Kcode==KeyEvent.VK_BACK_SPACE)
        {
            test.atr.BackspaceHandler();
        }
        else if(Kcode==KeyEvent.VK_ENTER)
        {
            test.atr.Enter();
        }
        else if(Kcode==KeyEvent.VK_UP)
        {
            if(line_no!=0)
            {
                test.atr.setline_no(line_no-1);
                length=test.atr.getLength(line_no-1);
                
                if(charac_no>=length)
                    test.atr.setcharac_no(test.atr.getLength(line_no-1));
                
                test.repaint(line_no);
                test.repaint(line_no-1);
            }
        }
        else if(Kcode==KeyEvent.VK_DOWN)
        {
            
            if(line_no!=size-1&&size!=0)
            {
                test.atr.setline_no(line_no+1);
                length=test.atr.getLength(line_no+1);
                
                if(charac_no>=length)
                    test.atr.setcharac_no(test.atr.getLength(line_no+1));
                
                test.repaint(line_no);
                test.repaint(line_no+1);
            }
            else
                System.out.println("Reached limit");
        }
        else if(Kcode==KeyEvent.VK_LEFT){
            if(charac_no!=0)
            {
                test.atr.setcharac_no(charac_no-1);
                test.repaint(line_no);
            }
            else if(line_no!=0)
            {
                test.atr.setline_no(line_no-1);
                test.atr.setcharac_no(test.atr.getLength(line_no-1));
                test.repaint(line_no);
                test.repaint(line_no-1);
            }
            else    
                System.out.println("Start of text");
        }
        else if(Kcode==KeyEvent.VK_RIGHT){
            if(charac_no!=length){
                test.atr.setcharac_no(charac_no+1);
                test.repaint(line_no);
            }
            else if(line_no!=size-1&&size!=0)
            {
                test.atr.setline_no(line_no+1);
                test.atr.setcharac_no(0);
                test.repaint(line_no);
                test.repaint(line_no+1);
            }
            else
                System.out.println("End of Text");
        }
        else if(Ctrl==false&&Alt==false){
            test.atr.InsertC(Kcode,Shift);
        }
        
               
    }
    @Override
    public void keyReleased(KeyEvent ke){
        int Kcode=ke.getKeyCode();
        if(Kcode==KeyEvent.VK_SHIFT)
        {
            Shift=false;
        }
        else if(Kcode==KeyEvent.VK_CONTROL)
        {
            Ctrl=false;
        }
        else if(Kcode==KeyEvent.VK_ALT)
        {
            Alt=false;
        }

    }
}

//Handles all mouse events of parent window
class MouseHandler implements MouseListener, MouseMotionListener{
    Notepad instance;
    int x,y;
    MouseHandler(Notepad parent)
    {
        instance=parent;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
       //not required
        x=e.getX();
        y=e.getY();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        //starts from here
        instance.mouse.setStart(x, y);
       
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        instance.mouse.setEnd(x, y);
        //ends here
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        x=e.getX();
        y=e.getY();    
        //not required
    }

    @Override
    public void mouseExited(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        //not required
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        instance.mouse.setEnd(x, y);
        //required ends here temporarily
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x=e.getX();
        y=e.getY();
        //not required really
    }
    
    
} 

//Handles all menu events of parent window
class MenuHandler implements ActionListener, Serializable{
    Notepad sample;
    MenuHandler(Notepad sample){
        this.sample=sample;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String arg = e.getActionCommand();
        switch (arg) {
            case "New":
                System.out.println("New");
                Notepad another = new Notepad("Untitled");
                break;
            case "Open":
                System.out.println("Open");
                break;
            case "Save":
                System.out.println("Save");
                //save();
                break;
            case "Save As":
                System.out.println("Save As");
                break;
            case "Exit":
                System.out.println("Exit");
                sample.setVisible(false);
                sample.dispose();
                break;
            case "Undo":
                System.out.println("Undo");
                break;
            case "Cut":
                System.out.println("Cut");
                break;
            case "Copy":
                System.out.println("Copy");
                break;
            case "Paste":
                System.out.println("Paste");
                break;
            case "Delete":
                System.out.println("Delete");
                break;
            case "Select All":
                System.out.println("Select All");
                break;
            case "Shortcuts":
                System.out.println("Shortcuts");
                break;
            case "About":
                System.out.println("About");
                break;
            case "Font":
                FontDialog font_format=new FontDialog(sample,"Font Formatting");
                System.out.println("Font");
                sample.repaint();
                break;
            default:
                System.out.println("something");
        }
    }
    void save()
    {
        FileOutputStream savefile;
        BufferedOutputStream buffer;
        ObjectOutputStream output = null;
        
        try{
            savefile= new FileOutputStream(sample.Title+".jtext");
            buffer = new BufferedOutputStream(savefile);
            output = new ObjectOutputStream(buffer);
            output.writeObject(sample.atr);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println("File Error Encountered");
        }
    }
}

//Handling Font dialog box... two classes
class FontDialog extends Dialog  implements Serializable{
    Notepad instance;
    private String[] names = new String[11];
    //private GraphicsEnvironment ge;
    private JList fn,fsize,fstyle;
    private DefaultListModel fnArray,fsizeArray,fstyleArray;
    private JButton ok = new JButton("Ok");
    private JButton cancel = new JButton("Cancel");
    private JLabel fontl = new JLabel("Font");
    private JLabel fstylel=new JLabel("Font Style");
    private JLabel fsizel = new JLabel("Font Size");
    private FDialogHandler fdh;
    String sf=null,sstyle=null,ssize=null;
    private ListSelectionHandler lsd;

    FontDialog(Notepad parent,String Title){
        super(parent,"Font Formatting",true);
        instance=parent;
        setfontsatr();
        
        setLayout(new FlowLayout());
        setSize(500,500);
        
        add(fontl);
        add(fstylel);
        add(fsizel);
        
        add(fn);
        add(fsize);
        add(fstyle);
        
        add(ok);
        add(cancel);
        
       
          fn.setSelectedIndex(fnArray.indexOf(instance.atr.getFontName()));
          fsize.setSelectedIndex(fsizeArray.indexOf(instance.atr.getFontSize()));
          fstyle.setSelectedIndex(fstyleArray.indexOf(instance.atr.getFontStyle()));
         
        /* fn.setSelectedIndex(0);
         * fsize.setSelectedIndex(0);
         * fstyle.setSelectedIndex(0);*/
        
        lsd=new ListSelectionHandler();
        fn.addListSelectionListener(lsd);
        fsize.addListSelectionListener(lsd);
        fstyle.addListSelectionListener(lsd);
        
        fdh=new FDialogHandler();
        ok.addActionListener(fdh);
        cancel.addActionListener(fdh);
        
        addWindowListener(new FWAdapter());
        this.setVisible(true);
    }
    void setfontsatr()
    {
        /*ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        names=ge.getAvailableFontFamilyNames();*/
        names[0]="Agency FB";
        names[1]="Arial";
        names[2]="Arial Black";
        names[3]="Arial Narrow";
        names[4]="Arial Rounded MT Bold";
        names[5]="Calibri";
        names[6]="Courier New";
        names[7]="Dialog";
        names[8]="Monospaced";
        names[9]="SansSerif";
        names[10]="Times New Roman";
        
        fnArray=new DefaultListModel();
        for(int i=0;i<11;i++)
            fnArray.addElement(names[i]);
        
        fn = new JList(fnArray);
        
        fsizeArray= new DefaultListModel();
        fsizeArray.addElement("12");
        fsizeArray.addElement("14");
        fsizeArray.addElement("16");
        fsizeArray.addElement("18");
        fsizeArray.addElement("20");
        fsizeArray.addElement("22");
        fsizeArray.addElement("24");
        fsizeArray.addElement("26");
        fsizeArray.addElement("36");
        fsizeArray.addElement("48");
        fsizeArray.addElement("72");
        
        fsize = new JList(fsizeArray);
        
        fstyleArray=new DefaultListModel();
        fstyleArray.addElement("Plain");
        fstyleArray.addElement("Bold");
        fstyleArray.addElement("Italics");
        
        fstyle= new JList(fstyleArray);
    }
    
class FDialogHandler implements ActionListener, Serializable{
    String select;

    @Override
    public void actionPerformed(ActionEvent e) {
       
       String command = e.getActionCommand();
        switch (command) {
            case "Ok":
                instance.atr.setFontAtr(sf,sstyle,ssize);
                 System.out.println("sf="+sf);
            System.out.println("sstyle="+sstyle);
            System.out.println("ssize="+ssize);
                setVisible(false);
                dispose();
                break;
            case "Cancel":
                setVisible(false);
                dispose();
                break;
        }
    }
}
class ListSelectionHandler implements ListSelectionListener, Serializable{

        @Override
        public void valueChanged(ListSelectionEvent e) {
            System.out.println("value changed");
            sf=(String)fn.getSelectedValue();
            sstyle=(String)fstyle.getSelectedValue();
            ssize=(String)fsize.getSelectedValue();
            System.out.println("sf="+sf);
            System.out.println("sstyle="+sstyle);
            System.out.println("ssize="+ssize);
            
        }
    
}
class FWAdapter extends WindowAdapter implements Serializable{
   @Override
   public void windowClosing(WindowEvent we){
       setVisible(false);
       dispose();
   }
}
}
