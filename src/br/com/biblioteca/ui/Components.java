package br.com.biblioteca.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;

public class Components {

    //RoundedPanel 
    public static class RoundedPanel extends JPanel {
        private final int   radius;
        private final Color borderColor;
        public RoundedPanel(int radius)                   { this(radius, null); }
        public RoundedPanel(int radius, Color borderColor) {
            this.radius = radius; this.borderColor = borderColor; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),radius,radius));
            if (borderColor != null) {
                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-1f,getHeight()-1f,radius,radius));
            }
            g2.dispose(); super.paintComponent(g);
        }
    }

    //StyledButton 
    public static class StyledButton extends JButton {
        private final Color bg;
        private final int   radius;
        public StyledButton(String text, Color bg, Color fg) { this(text,bg,fg,8); }
        public StyledButton(String text, Color bg, Color fg, int radius) {
            super(text); this.bg=bg; this.radius=radius;
            setFont(Theme.FONT_BTN); setForeground(fg);
            setFocusPainted(false); setBorderPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(6,14,6,14));
        }
        public void setSmall() { setFont(Theme.plain(11)); setBorder(new EmptyBorder(3,9,3,9)); }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getMousePosition()!=null ? bg.darker() : bg);
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),radius,radius));
            g2.dispose(); super.paintComponent(g);
        }
    }

    // Badge
    public static class Badge extends JLabel {
        public Badge(String text, Color bg, Color fg) {
            super(text, CENTER); setFont(Theme.bold(11)); setForeground(fg);
            setBackground(bg); setOpaque(false); setBorder(new EmptyBorder(2,8,2,8));
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),6,6));
            g2.dispose(); super.paintComponent(g);
        }
    }
    public static Badge availBadge(String t) { return new Badge(t,Theme.BADGE_AVAIL_BG,Theme.BADGE_AVAIL_FG); }
    public static Badge loanBadge (String t) { return new Badge(t,Theme.BADGE_LOAN_BG, Theme.BADGE_LOAN_FG);  }
    public static Badge overBadge (String t) { return new Badge(t,Theme.BADGE_OVER_BG, Theme.BADGE_OVER_FG);  }

    //BookCover 
    public static class BookCover extends JPanel {
        private final Color color; private final String label; private final int w,h;
        public BookCover(Color color, String title, int w, int h) {
            this.color=color; this.w=w; this.h=h;
            String[] words=title.split(" "); StringBuilder sb=new StringBuilder();
            for(String wd:words){ if(sb.length()+wd.length()>14) break; if(sb.length()>0) sb.append(" "); sb.append(wd); }
            this.label=sb.toString();
            setPreferredSize(new Dimension(w,h)); setMinimumSize(new Dimension(w,h));
            setMaximumSize(new Dimension(w,h)); setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.fill(new RoundRectangle2D.Float(0,0,w,h,5,5));
            g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI",Font.BOLD,8));
            FontMetrics fm=g2.getFontMetrics();
            String[] words=label.split(" "); int lineH=fm.getHeight();
            int totalH=words.length*lineH; int y=(h-totalH)/2+fm.getAscent();
            for(String wd:words){ g2.drawString(wd,(w-fm.stringWidth(wd))/2,y); y+=lineH; }
            g2.dispose();
        }
    }

    //Avatar 
    public static class Avatar extends JPanel {
        private final String letter; private final Color color; private final int size;
        public Avatar(String name, Color color, int size) {
            this.letter=name.isEmpty()?"?":String.valueOf(name.charAt(0)).toUpperCase();
            this.color=color; this.size=size;
            setPreferredSize(new Dimension(size,size)); setMinimumSize(new Dimension(size,size));
            setMaximumSize(new Dimension(size,size)); setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            Graphics2D g2=(Graphics2D)g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); g2.fillOval(0,0,size,size);
            g2.setColor(Color.WHITE); g2.setFont(Theme.bold(size/3));
            FontMetrics fm=g2.getFontMetrics();
            g2.drawString(letter,(size-fm.stringWidth(letter))/2,(size+fm.getAscent()-fm.getDescent())/2);
            g2.dispose();
        }
    }

    //StatCard 
    public static RoundedPanel statCard(String num, String label,
            Color bgColor, Color iconBg, Color numColor, Color lblColor) {
        RoundedPanel p = new RoundedPanel(12);
        p.setBackground(bgColor);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(14,14,14,14));

        JPanel ico = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBg);
                g2.fill(new RoundRectangle2D.Float(0,0,30,30,7,7));
                g2.dispose();
            }
        };
        ico.setOpaque(false); ico.setPreferredSize(new Dimension(30,30));
        ico.setMaximumSize(new Dimension(30,30));
        // FIX: use Component.LEFT_ALIGNMENT (float constant) correctly
        ico.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(ico); p.add(Box.createVerticalStrut(6));

        JLabel numLbl = new JLabel(num);
        numLbl.setFont(Theme.FONT_STAT); numLbl.setForeground(numColor);
        numLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(numLbl);

        JLabel lblLbl = new JLabel("<html>" + label + "</html>");
        lblLbl.setFont(Theme.FONT_SMALL); lblLbl.setForeground(lblColor);
        lblLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblLbl);
        return p;
    }

    //Card do container 
    public static RoundedPanel card() {
        RoundedPanel p = new RoundedPanel(12, Theme.CARD_BORDER);
        p.setBackground(Theme.CARD_BG); p.setLayout(new BorderLayout()); return p;
    }

    public static JPanel cardHeader(String title) { return cardHeader(title, null); }
    public static JPanel cardHeader(String title, JComponent action) {
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false);
        p.setBorder(new CompoundBorder(
            new MatteBorder(0,0,1,0,Theme.CARD_BORDER), new EmptyBorder(11,14,11,14)));
        JLabel lbl = new JLabel(title); lbl.setFont(Theme.FONT_CARD_T);
        lbl.setForeground(Theme.TEXT_PRIMARY); p.add(lbl, BorderLayout.WEST);
        if (action != null) p.add(action, BorderLayout.EAST);
        return p;
    }

    public static StyledButton qaBtn(String text, Color bg, Color fg) {
        StyledButton b = new StyledButton(text,bg,fg);
        b.setFont(Theme.bold(12));
        b.setPreferredSize(new Dimension(Integer.MAX_VALUE,44));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(0,12,0,12));
        return b;
    }

    public static JLabel sectionLabel(String text) {
    JLabel l = new JLabel(text.toUpperCase());
    l.setFont(new Font("SansSerif", Font.BOLD, 12));
    l.setForeground(new Color(120, 120, 120));
    return l;
    }

    public static JLabel formLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(Theme.plain(11));
        l.setForeground(Theme.TEXT_SECONDARY); return l;
    }

    public static JTextField styledField(String placeholder) {
        JTextField f = new JTextField(); f.setFont(Theme.FONT_BODY);
        f.setBorder(new CompoundBorder(new LineBorder(Theme.CARD_BORDER,1,true), new EmptyBorder(6,10,6,10)));
        f.setBackground(Color.WHITE); return f;
    }

    public static JPasswordField styledPassword() {
        JPasswordField f = new JPasswordField(); f.setFont(Theme.FONT_BODY);
        f.setBorder(new CompoundBorder(new LineBorder(Theme.CARD_BORDER,1,true), new EmptyBorder(6,10,6,10)));
        f.setBackground(Color.WHITE); return f;
    }

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items); c.setFont(Theme.FONT_BODY);
        c.setBackground(Color.WHITE); return c;
    }

    public static JPanel progressBar(int pct) {
        Color fill = pct>80 ? Theme.ICON_RED : pct>50 ? Theme.ICON_AMBER : Theme.ICON_GREEN;
        JPanel wrapper = new JPanel(new BorderLayout(6,0)); wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(10,8,10,8));
        JPanel track = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=(Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.CONTENT_BG); g2.fillRoundRect(0,0,getWidth(),getHeight(),4,4);
                g2.setColor(fill); g2.fillRoundRect(0,0,(int)(getWidth()*pct/100.0),getHeight(),4,4);
                g2.dispose();
            }
        };
        track.setOpaque(false); track.setPreferredSize(new Dimension(0,8));
        JLabel lbl=new JLabel(pct+"%"); lbl.setFont(Theme.plain(11));
        lbl.setForeground(Theme.TEXT_SECONDARY); lbl.setPreferredSize(new Dimension(36,14));
        wrapper.add(track,BorderLayout.CENTER); wrapper.add(lbl,BorderLayout.EAST); return wrapper;
    }

    private static final Color ICON_RED   = new Color(226, 75,  74);
    private static final Color ICON_AMBER = new Color(239, 159, 39);
    private static final Color ICON_GREEN = new Color(99,  153, 34);

    public static JSeparator sep() {
        JSeparator s=new JSeparator(); s.setForeground(Theme.CARD_BORDER);
        s.setBackground(Theme.CARD_BORDER); s.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); return s;
    }

    public static JTable buildTable(javax.swing.table.TableModel model) {
        JTable t = new JTable(model) {
            @Override public Component prepareRenderer(javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c=super.prepareRenderer(r,row,col);
                if(!isRowSelected(row)) c.setBackground(row%2==0?Color.WHITE:Theme.ROW_ALT);
                else c.setBackground(Theme.ROW_HOVER);
                return c;
            }
        };
        t.setFont(Theme.FONT_BODY); t.setRowHeight(36); t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0,0));
        t.getTableHeader().setFont(Theme.bold(11));
        t.getTableHeader().setBackground(Theme.TABLE_HEADER);
        t.getTableHeader().setForeground(Theme.TEXT_SECONDARY);
        t.getTableHeader().setBorder(new MatteBorder(0,0,1,0,Theme.CARD_BORDER));
        t.getTableHeader().setReorderingAllowed(false);
        t.setSelectionBackground(Theme.ROW_HOVER);
        t.setSelectionForeground(Theme.TEXT_PRIMARY);
        t.setBorder(null);
        javax.swing.table.DefaultTableCellRenderer left=new javax.swing.table.DefaultTableCellRenderer();
        left.setHorizontalAlignment(SwingConstants.LEFT);
        left.setBorder(new EmptyBorder(0,12,0,8));
        for(int i=0;i<t.getColumnCount();i++) t.getColumnModel().getColumn(i).setCellRenderer(left);
        return t;
    }
}
