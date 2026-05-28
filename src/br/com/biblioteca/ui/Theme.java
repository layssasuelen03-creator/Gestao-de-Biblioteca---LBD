package br.com.biblioteca.ui;
import java.awt.*;

public class Theme {
    //Sidebar
    public static final Color SIDEBAR_BG          = new Color(26, 32, 53);
    public static final Color SIDEBAR_ACTIVE_BG   = new Color(61, 90, 254, 55);
    public static final Color SIDEBAR_BORDER      = new Color(255, 255, 255, 20);
    public static final Color SIDEBAR_TEXT        = new Color(255, 255, 255, 153);
    public static final Color SIDEBAR_TEXT_ACTIVE = Color.WHITE;
    public static final Color SIDEBAR_SECTION_LBL = new Color(255, 255, 255, 76);

    //Topbar 
    public static final Color TOPBAR_BG   = new Color(26, 32, 53);
    public static final Color TOPBAR_TEXT = Color.WHITE;
    public static final Color TOPBAR_SUB  = new Color(255, 255, 255, 165);

    public static final Color ACCENT      = new Color(61, 90, 254);
    public static final Color ACCENT_DARK = new Color(12, 68, 124);

    public static final Color CONTENT_BG   = new Color(244, 246, 250);
    public static final Color CARD_BG      = Color.WHITE;
    public static final Color CARD_BORDER  = new Color(220, 225, 235);
    public static final Color TABLE_HEADER = new Color(248, 249, 251);
    public static final Color ROW_HOVER    = new Color(237, 243, 255);
    public static final Color ROW_ALT      = new Color(250, 251, 253);

    public static final Color STAT_AMBER_BG = new Color(250, 238, 218);
    public static final Color STAT_GREEN_BG = new Color(234, 243, 222);
    public static final Color STAT_BLUE_BG  = new Color(230, 241, 251);
    public static final Color STAT_TEAL_BG  = new Color(225, 245, 238);
    public static final Color STAT_PURP_BG  = new Color(238, 237, 254);
    public static final Color STAT_RED_BG   = new Color(252, 235, 235);

    public static final Color ICON_AMBER = new Color(239, 159, 39);
    public static final Color ICON_GREEN = new Color(99,  153, 34);
    public static final Color ICON_RED = new Color(226, 75, 74);
    public static final Color ICON_BLUE  = new Color(24,  95,  165);
    public static final Color ICON_TEAL  = new Color(29,  158, 117);

    public static final Color NUM_AMBER = new Color(99,  56,  6);
    public static final Color NUM_GREEN = new Color(39,  80,  10);
    public static final Color NUM_BLUE  = new Color(12,  68,  124);
    public static final Color NUM_TEAL  = new Color(8,   80,  65);

    public static final Color BTN_PRIMARY = new Color(61,  90,  254);
    public static final Color BTN_SUCCESS = new Color(99,  153, 34);
    public static final Color BTN_WARNING = new Color(239, 159, 39);
    public static final Color BTN_DANGER  = new Color(226, 75,  74);
    public static final Color BTN_INFO    = new Color(55,  138, 221);
    public static final Color BTN_WHITE   = Color.WHITE;

    public static final Color BADGE_AVAIL_BG = new Color(234, 243, 222);
    public static final Color BADGE_AVAIL_FG = new Color(39,  80,  10);
    public static final Color BADGE_LOAN_BG  = new Color(250, 238, 218);
    public static final Color BADGE_LOAN_FG  = new Color(99,  56,  6);
    public static final Color BADGE_OVER_BG  = new Color(252, 235, 235);
    public static final Color BADGE_OVER_FG  = new Color(121, 31,  31);

    public static final Color TEXT_PRIMARY   = new Color(28,  35,  58);
    public static final Color TEXT_SECONDARY = new Color(100, 110, 140);
    public static final Color TEXT_MUTED     = new Color(160, 168, 185);

    public static final Color[] COVERS = {
        new Color(239, 159, 39),  new Color(24,  95,  165),
        new Color(226, 75,  74),  new Color(99,  153, 34),
        new Color(127, 119, 221), new Color(29,  158, 117),
        new Color(212, 83,  126), new Color(55,  138, 221),
    };

    public static Font bold(int size)  { return new Font("Segoe UI", Font.BOLD,  size); }
    public static Font plain(int size) { return new Font("Segoe UI", Font.PLAIN, size); }

    public static final Font FONT_LOGO   = bold(16);
    public static final Font FONT_NAV    = plain(13);
    public static final Font FONT_BODY   = plain(13);
    public static final Font FONT_SMALL  = plain(11);
    public static final Font FONT_LABEL  = plain(10);
    public static final Font FONT_CARD_T = bold(13);
    public static final Font FONT_STAT   = bold(26);
    public static final Font FONT_BTN    = bold(12);
}
