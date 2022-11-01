import Colorizers.Colorize;
import Colorizers.Orange;
import Colorizers.P1;
import Fractels.Mandelbrot;
import Fractels._ComplexNumber;
import Fractels._Fractle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Page extends  JFrame{
    final int MENU_SIZE = 200;

    int preseason = 200;
    int CROP_LEVEL = 1;

    static class CanvasPanel extends JPanel{
        BufferedImage img;
        Graphics2D g2D;
        public CanvasPanel(int width, int height)
        {
            super();
            super.setSize(width,height);
        }

        public void setImg(BufferedImage _img){img = _img;}
        public void paint(Graphics g, int crop, int render_width, int render_height)
        {
            g2D = (Graphics2D) g;
            if(crop == 1)
                g2D.drawImage(img,0,0,this);
            else
                g2D.drawImage(img,0,0,render_width,render_height,this);
        }
        public int drawLines(int[] x, int[] y, FractleBuilder Builder)
        {
            g2D.setColor(Color.ORANGE);
            int rangeOver = -1;
            for(int i = 1; i < y.length; i++)
            {
                g2D.drawLine(x[i - 1],y[i - 1],x[i],y[i]);
                if(rangeOver == -1 && Math.abs(Builder.getPointX(x[i])) > 2)
                    rangeOver = i - 1;
            }
            return rangeOver;
        }
    }

    final CanvasPanel canvas;
    final sideMenu menu;
    final Panel juliaDot;

    final int SCREEN_WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    final int SCREEN_HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() ;

    BufferedImage IMG;
    FractleBuilder Builder;
    public Page() throws IOException, InterruptedException {
        super();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ;
        IMG = new BufferedImage((SCREEN_WIDTH - MENU_SIZE),SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB);
        Builder = new FractleBuilder((SCREEN_WIDTH - MENU_SIZE),SCREEN_HEIGHT,IMG);
        Builder.colorize = new Orange();
        Builder.changeFractle(new Mandelbrot(preseason));
        Builder.LoadAll();

        canvas = new CanvasPanel(SCREEN_WIDTH - MENU_SIZE,SCREEN_HEIGHT);
        add(canvas);
        canvas.setImg(IMG);
        paint();

        Panel menu_b = new Panel();
        menu_b.setSize(MENU_SIZE,SCREEN_HEIGHT);
        menu_b.setLocation(SCREEN_WIDTH - MENU_SIZE,0);
        add(menu_b);
        menu = new sideMenu(menu_b);

        addMouseListener(mouse_click);
        addMouseMotionListener(mouse_move);
        addMouseWheelListener(wheel_move);

        canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("L"),"press_l");
        canvas.getActionMap().put("press_l",clickL);
        canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"),"press_r");
        canvas.getActionMap().put("press_r",clickR);
        canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("J"),"press_j");
        canvas.getActionMap().put("press_j",clickJ);

        juliaDot = new Panel();
        juliaDot.setSize(20,20);
        juliaDot.setBackground(Color.YELLOW);
        juliaDot.addMouseMotionListener(julia_dot_move);
        add(juliaDot);
    }

    //menu
    class sideMenu
    {
        final Panel Menu;
        final Label[] propLabels;
        final List setsChooser;
        final List colorChooser;
        final TextField depthLabel;
        final Label cropProps;
        final Label SLLimit;
        final TextField editableLabel;
        final Button editableUpdate;
        //final Checkbox[] checkboxes;


        private sideMenu(Panel menu)
        {
            Menu = menu;
            menu.setFocusable(false);

            propLabels = new Label[5];
            final int PROP_LABEL_HEIGHT = 30;
            final Panel propPanel = new Panel();
            propPanel.setLocation(5,SCREEN_HEIGHT - (propLabels.length + 1) * PROP_LABEL_HEIGHT);
            propPanel.setSize(MENU_SIZE - 10,propLabels.length * PROP_LABEL_HEIGHT + PROP_LABEL_HEIGHT / 2);
            propPanel.setBackground(Color.darkGray);
            propPanel.setVisible(true);
            menu.add(propPanel);
            for(int i = 0; i < 5; i++)
            {
                propLabels[i] = new Label();
                propLabels[i].setLocation(0,PROP_LABEL_HEIGHT / 4 + (i * PROP_LABEL_HEIGHT));
                propLabels[i].setSize(MENU_SIZE,PROP_LABEL_HEIGHT);
                propLabels[i].setFont(new Font("arial",Font.BOLD,20));
                propLabels[i].setForeground(Color.WHITE);
                propLabels[i].setVisible(true);

                propPanel.add(propLabels[i]);
            }
            propLabels[0].setText("set: " + Builder.fractle.getName());
            propLabels[1].setText("color: " + Builder.colorize.getName());


            _Fractle[] fractles = _Fractle.getFractals(preseason);
            String[] names = new String[fractles.length];
            for(int i =0; i < fractles.length; i++)
                names[i] = fractles[i].getName();
            setsChooser = createList(names,10,setChooseEvent);

            Colorize[] colors = Colorize.getMethods();
            String[] names2 = new String[colors.length];
            for(int i =0 ; i < colors.length; i++)
                names2[i] = colors[i].getName();
            colorChooser = createList(names2,120,colorChooseEvent);
            colorChooser.select(3);

            Button clearButton = new Button();
            clearButton.setSize(MENU_SIZE - 20,50);
            clearButton.setBackground(Color.WHITE);
            clearButton.setFont(new Font("arial",Font.BOLD,30));
            clearButton.setLocation(10,230);
            clearButton.setLabel("reset");
            clearButton.addActionListener(clearClickEvent);
            clearButton.setVisible(true);
            menu.add(clearButton);


            int VSS = 290;
            addLine(VSS);
            VSS += 25;

            addLabel(VSS,"rendering quality");
            VSS += 35;

            final int updateButtonSize = 50;
            depthLabel = new TextField();
            depthLabel.setSize(MENU_SIZE - 20 - updateButtonSize,30);
            depthLabel.setLocation(10,VSS);
            depthLabel.setFont(new Font("arial",Font.BOLD,12));
            depthLabel.setText("" + preseason);
            depthLabel.setVisible(true);
            menu.add(depthLabel);

            Button depthUpdate = new Button();
            depthUpdate.setSize(updateButtonSize,30);
            depthUpdate.setLocation(MENU_SIZE - 10 - updateButtonSize,VSS);
            depthUpdate.setBackground(Color.WHITE);
            depthUpdate.addActionListener(depthLabelUpdate);
            depthUpdate.setVisible(true);
            menu.add(depthUpdate);
            VSS += 60;

            final int SIDE_BUTTONS_SIZE = 30;
            final int SIDE_BUTTONS_MARGIN = 5;

            addLine(VSS);
            VSS += 25;

            addLabel(VSS,"screen resolution");
            VSS += 35;

            createSideButtons(VSS,SIDE_BUTTONS_SIZE,SIDE_BUTTONS_MARGIN, cropChangeEvent);

            cropProps = new Label();
            cropProps.setLocation(SIDE_BUTTONS_MARGIN + SIDE_BUTTONS_SIZE + 10,VSS);
            cropProps.setSize(MENU_SIZE - SIDE_BUTTONS_SIZE * 2 - 10,SIDE_BUTTONS_SIZE);
            cropProps.setFont(new Font("arial",Font.BOLD,30));
            cropProps.setText("best");
            cropProps.setVisible(true);
            menu.add(cropProps);
            VSS += 60;

            addLine(VSS);
            VSS += 25;

            addLabel(VSS,"compression level");
            VSS += 35;

            createSideButtons(VSS,SIDE_BUTTONS_SIZE,SIDE_BUTTONS_MARGIN,smartLineSLLChange);

            SLLimit = new Label();
            SLLimit.setLocation(SIDE_BUTTONS_MARGIN + SIDE_BUTTONS_SIZE + 10,VSS);
            SLLimit.setSize(MENU_SIZE - SIDE_BUTTONS_SIZE * 2 - 10,SIDE_BUTTONS_SIZE);
            SLLimit.setFont(new Font("arial",Font.BOLD,30));
            SLLimit.setText("2");
            SLLimit.setVisible(true);
            menu.add(SLLimit);
            VSS += 60;

            editableLabel = new TextField();
            editableLabel.setSize(MENU_SIZE - 20 - updateButtonSize,30);
            editableLabel.setLocation(10,VSS);
            editableLabel.setFont(new Font("arial",Font.BOLD,12));
            editableLabel.setText("");
            editableLabel.setVisible(false);
            menu.add(editableLabel);

            editableUpdate = new Button();
            editableUpdate.setSize(updateButtonSize,30);
            editableUpdate.setLocation(MENU_SIZE - 10 - updateButtonSize,VSS);
            editableUpdate.setBackground(Color.WHITE);
            editableUpdate.addActionListener(editableClickEvent);
            editableUpdate.setVisible(false);
            menu.add(editableUpdate);
        }
        private List createList(String[] names, int startY, ActionListener action)
        {
            List list = new List(names.length);
            list.setSize(195,100);
            list.setLocation(0,0);
            list.setVisible(true);
            for(String str:names)
                list.add(str);
            list.select(0);
            list.addActionListener(action);
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setSize(MENU_SIZE - 10,100);
            scrollPane.setLocation(5,startY);
            scrollPane.add(list);
            Menu.add(scrollPane);
            return list;
        }
        private void createSideButtons(int VSS, int size, int start_x, ActionListener action)
        {
            Button cropUp = new Button();
            Button cropDown = new Button();
            cropUp.setSize(size,size);
            cropDown.setSize(size,size);
            cropUp.setLocation(MENU_SIZE - cropUp.getWidth() - 5,VSS);
            cropDown.setLocation(start_x,VSS);
            cropUp.setVisible(true);
            cropDown.setVisible(true);
            cropUp.setLabel("->");
            cropDown.setLabel("<-");
            cropUp.addActionListener(action);
            cropDown.addActionListener(action);
            Menu.add(cropUp);
            Menu.add(cropDown);
        }

        private void addLine(int VSS)
        {
            Panel viewSettingLine = new Panel();
            viewSettingLine.setBackground(Color.BLACK);
            viewSettingLine.setLocation(0,VSS);
            viewSettingLine.setSize(MENU_SIZE,5);
            viewSettingLine.setVisible(true);
            Menu.add(viewSettingLine);
        }
        private void addLabel(int VSS, String text)
        {
            Label viewSettingTitle = new Label();
            viewSettingTitle.setText(text);
            viewSettingTitle.setFont(new Font("arial",Font.BOLD,22));
            viewSettingTitle.setLocation(5,VSS);
            viewSettingTitle.setSize(MENU_SIZE,30);
            viewSettingTitle.setVisible(true);
            Menu.add(viewSettingTitle);
        }

        private void updateMouesLocation(int x, int y)
        {
            final String point_x = Data.fixSize(Data.round(Builder.getPointX(x / CROP_LEVEL),5),7);
            final String point_y = Data.fixSize(Data.round(Builder.getPointY(y / CROP_LEVEL),5),7);
            if(point_y.charAt(0) == '-')
                propLabels[2].setText(point_x + " - " + point_y.substring(1) + "i");
            else
                propLabels[2].setText(point_x + " + " + point_y + "i");

        }
    }



    private void paint()
    {
        canvas.paint(getGraphics(),CROP_LEVEL,SCREEN_WIDTH - MENU_SIZE,SCREEN_HEIGHT);
    }

    private int drawLines()
    {
        int[][] dots;
        if(CROP_LEVEL == 1)
            dots = Builder.getLinesFromPoint(MOUSE_X,MOUSE_Y);
        else
        {
            dots = Builder.getLinesFromPoint(MOUSE_X / CROP_LEVEL,MOUSE_Y / CROP_LEVEL);
            for(int i = 0; i < dots[0].length; i++)
            {
                dots[0][i] *= CROP_LEVEL;
                dots[1][i] *= CROP_LEVEL;
            }
        }
        return canvas.drawLines(dots[0],dots[1],Builder);
    }

    private void updateJuiliaDot(int x, int y)
    {
        Builder.fractle.setStart(new _ComplexNumber(Builder.getPointX(x),Builder.getPointY(y)));
        Builder.LoadAll();
        paint();
    }
    private void updateJuiliaDotLocation()
    {
        final int size = juliaDot.getWidth();
        final int x = Builder.getPixelX(Builder.fractle.getStart().getRealValue()) * CROP_LEVEL;
        final int y = Builder.getPixelY(Builder.fractle.getStart().getImageryVale()) * CROP_LEVEL;
        juliaDot.setLocation(x - size / 2,y - size / 2);
    }
    //mouse moves
    int MOUSE_X = 0;
    int MOUSE_Y = 0;
    boolean MOUSE_DOWN = false;

    MouseListener mouse_click = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            MOUSE_X = e.getX();
            MOUSE_Y = e.getY();
            MOUSE_DOWN = true;
            if(e.getX() >= juliaDot.getX() && e.getX() <= juliaDot.getX() + juliaDot.getWidth() && e.getY() >= juliaDot.getY() && e.getY() <= juliaDot.getY() + juliaDot.getHeight())
                System.out.println("click julia");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            MOUSE_DOWN = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
            MOUSE_DOWN = false;
        }
    };
    MouseMotionListener mouse_move = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
            final int x = e.getX();
            final int y = e.getY();
            if(Builder.fractle.isJULIA()) updateJuiliaDotLocation();
            try
            {
                if(CROP_LEVEL == 1) Builder.Move(x - MOUSE_X , y - MOUSE_Y);
                else Builder.Move((x - MOUSE_X) / CROP_LEVEL, (y - MOUSE_Y) / CROP_LEVEL);
            }
            catch (Exception a){}
            paint();

            MOUSE_X = x;
            MOUSE_Y = y;
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            MOUSE_X = e.getX();
            MOUSE_Y = e.getY();
            menu.updateMouesLocation(MOUSE_X,MOUSE_Y);
            if(DRAW_LINES)
            {
                paint();
                int linesLength = drawLines();
                menu.propLabels[4].setText((linesLength == -1?"FULL":""+ linesLength));
            }
        }
    };
    MouseWheelListener wheel_move = new MouseWheelListener() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if(CROP_LEVEL == 1) Builder.Zoom(e.getWheelRotation() == -1,e.getX(),e.getY());
            else Builder.Zoom(e.getWheelRotation() == -1,e.getX() / CROP_LEVEL,e.getY() / CROP_LEVEL);
            paint();
            if(Builder.fractle.isJULIA())  updateJuiliaDotLocation();
        }
    };

    int JULIA_MOUSE_X = 0;
    int JULIA_MOUSE_Y = 0;
    MouseMotionListener julia_dot_move = new MouseMotionListener() {
        @Override
        public void mouseDragged(MouseEvent e) {
            final int x = e.getX() + juliaDot.getX();
            final int y = e.getY() + juliaDot.getY();
            final Point mouse_point = MouseInfo.getPointerInfo().getLocation();
            final int mouse_x = (int)mouse_point.getX();
            final int mouse_y = (int)mouse_point.getY();
            if(mouse_x != JULIA_MOUSE_X || mouse_y != JULIA_MOUSE_Y)
            {
                juliaDot.setLocation(mouse_x - juliaDot.getWidth() / 2,mouse_y - juliaDot.getWidth() / 2);
                updateJuiliaDot(x / CROP_LEVEL,y / CROP_LEVEL);
            }
            menu.updateMouesLocation(mouse_x,mouse_y);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    };

    //keyboard events
    boolean DRAW_LINES = false;

    Action clickL = new AbstractAction() {
         public void actionPerformed(ActionEvent e) {
            DRAW_LINES = !DRAW_LINES;
            if(DRAW_LINES)
                drawLines();
            else{paint(); menu.propLabels[4].setText("");}
        }};
    Action clickR = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final long startTime = System.currentTimeMillis();
            Builder.LoadAll();
            paint();
            System.out.println("reset took " + (System.currentTimeMillis() - startTime));
        }
    };
    Action clickJ = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(Builder.fractle.isJULIA())
                Builder.fractle.cancelJulia();
            else
            {
                updateJuiliaDot(MOUSE_X / CROP_LEVEL,MOUSE_Y / CROP_LEVEL);
                updateJuiliaDotLocation();
            }
            juliaDot.setVisible(Builder.fractle.isJULIA());
            Builder.LoadAll();
            paint();
        }
    };

    ActionListener clearClickEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Builder.resetView();
            Builder.LoadAll();
            paint();
            updateJuiliaDotLocation();
            if(!Builder.fractle.isJULIA())
                juliaDot.setVisible(false);
        }
    };
    ActionListener depthLabelUpdate = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final String input = menu.depthLabel.getText();
            preseason = Data.StringToInt(input,false);
            Builder.changePreseason(preseason);
            paint();
        }
    };

    ActionListener setChooseEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Builder.changeFractle(_Fractle.getFractals(preseason)[menu.setsChooser.getSelectedIndex()]);
            paint();
            menu.propLabels[0].setText("set: " + Builder.fractle.getName());
            if(Builder.fractle.isJULIA())
            {
                _ComplexNumber fractelStart = Builder.fractle.getStart();
                juliaDot.setLocation(Builder.getPixelX(fractelStart.getRealValue()),Builder.getPixelY(fractelStart.getImageryVale()));
                juliaDot.setVisible(true);
            }
            else
                juliaDot.setVisible(false);
            menu.editableLabel.setVisible(Builder.fractle.isEDITABLE());
            menu.editableUpdate.setVisible(Builder.fractle.isEDITABLE());

        }
    };
    ActionListener colorChooseEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Builder.changeColorize(Colorize.getMethods()[menu.colorChooser.getSelectedIndex()]);
            paint();
            menu.propLabels[1].setText("color: " + Builder.colorize.getName());
        }
    };
    ActionListener cropChangeEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(((Button)e.getSource()).getX() == 5) {
                CROP_LEVEL *= 2;
                if(CROP_LEVEL == 16) CROP_LEVEL = 1;
            }
            else
            {
                CROP_LEVEL /= 2;
                if(CROP_LEVEL == 0) CROP_LEVEL = 8;
            }
            IMG = new BufferedImage((SCREEN_WIDTH - MENU_SIZE) / CROP_LEVEL,SCREEN_HEIGHT / CROP_LEVEL,BufferedImage.TYPE_INT_RGB);
            canvas.img = IMG;
            Builder = new FractleBuilder((SCREEN_WIDTH - MENU_SIZE) / CROP_LEVEL,SCREEN_HEIGHT / CROP_LEVEL,IMG,Builder.WIDTH,Builder.HEIGHT,Builder.START_X,Builder.START_Y,Builder.fractle,Builder.colorize,Builder.SMART_LINE_LIMIT);
            Builder.LoadAll();
            paint();
            String text = switch (CROP_LEVEL) {
                        case 1 -> "best";
                        case 2 -> "fine";
                        case 8 -> "testing";
                        default -> "worst";
                    };
            menu.cropProps.setText(text);
        }
    };
    ActionListener smartLineSLLChange = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int limit = Builder.SMART_LINE_LIMIT;
            final boolean down = ((Button)e.getSource()).getX() == 5;
            if(down) {
                if(limit != 1)
                    limit--;
            }
            else
            {
                if(limit != 12)
                    limit++;
            }
            if(limit == 1)
                menu.SLLimit.setText("lossless");
            else
                menu.SLLimit.setText(""+ limit);
            Builder.changeSmartLineLimit(limit);
            Builder.LoadAll();
            paint();
        }
    };
    ActionListener editableClickEvent = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final String input = menu.editableLabel.getText();
            final double val = Data.StringToDouble(input,true);
            Builder.fractle.changeVar(val);
            Builder.LoadAll();
            paint();
        }
    };
}
