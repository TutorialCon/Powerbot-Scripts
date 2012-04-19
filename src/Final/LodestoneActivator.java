
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.LinkedList;
import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.event.listener.PaintListener;

/**
 * 
 * @author TutorialCon
 */
@Manifest(name = "Lodestone Activator",
authors = {"Tutorial Con"},
version = 1.0D,
description = "Will activate all the Lodestones.",
website = "http://www.TutorialCon.com/",
premium = false)
public class LodestoneActivator extends ActiveScript implements Task, PaintListener {

    private boolean run = true;
    private int tick = 0;
    /*
     * Settings stuff
     */
    private int SETTING_RUN = 173;
    /*
     * Main interface stuff
     */
    private Widget INTERFACE_MAIN;
    private WidgetChild TAB_INVENTORY;
    private WidgetChild TAB_EQUIPMENT;
    private WidgetChild TAB_MAGIC;
    private WidgetChild TAB_LOGOUT;
    private WidgetChild BTN_LOGOUT;
    private WidgetChild BTN_RUN;
    private WidgetChild CLCK_RUN;
    /*
     * Magic interface stuff
     */
    private Widget INTERFACE_MAGIC;
    private WidgetChild TELE_HOME;
    private WidgetChild SORT_TELE;
    private WidgetChild FILT_CMB;
    private WidgetChild FILT_TELE;
    private WidgetChild FILT_MISC;
    private WidgetChild FILT_SKILL;
    private int REQ_ON_TEX = 1703;
    /*
     * Lodestone interface stuff
     */
    private Widget INTERFACE_LODESTONE;
    private WidgetChild LODE_DRAYNOR;
    private WidgetChild LODE_FALADOR;
    private WidgetChild LODE_PORTSARIM;
    private WidgetChild LODE_TAVERLY;
    private WidgetChild LODE_CATHERBY;
    private WidgetChild LODE_SEERS;
    private WidgetChild LODE_ARDOUGNE;
    private WidgetChild LODE_YANILLE;
    private WidgetChild LODE_ALKHARID;
    private WidgetChild LODE_VARROCK;
    private WidgetChild LODE_EDGEVILLE;
    private WidgetChild LODE_BURTHORPE;
    private WidgetChild LODE_LUMBRIDGE;
    private WidgetChild LODE_CROSS;
    private int REQ_LODE_DRAYNOR = 10110;
    private int REQ_LODE_FALADOR = 10103;
    private int REQ_LODE_PORTSARIM = 10109;
    private int REQ_LODE_TAVERLY = 10105;
    private int REQ_LODE_CATHERBY = 10107;
    private int REQ_LODE_SEERS = 10108;
    private int REQ_LODE_ARDOUGNE = 10104;
    private int REQ_LODE_YANILLE = 10111;
    private int REQ_LODE_ALKHARID = 10113;
    private int REQ_LODE_VARROCK = 10102;
    private int REQ_LODE_EDGEVILLE = 10112;
    private boolean[] b_done;
    private WidgetChild[] w_done;
    private int[] t_done;
    /*
     * Taskers
     */
    final SetupBot t_setupBot = new SetupBot();
    final CheckLodestones t_checkLodestones = new CheckLodestones();

    /**
     * 
     */
    @Override
    protected void setup() {
        final Strategy a_setupBot = new Strategy(t_setupBot, t_setupBot);
        final Strategy a_checkLodestones = new Strategy(t_checkLodestones, t_checkLodestones);
        final Strategy a_lodestoneActivator = new Strategy(new Condition() {

            public boolean validate() {
                return t_checkLodestones.done && run;
            }
        }, this);

        provide(a_setupBot);
        provide(a_checkLodestones);
        provide(a_lodestoneActivator);

        updateSettings();
    }

    /**
     * 
     */
    public void updateSettings() {
        INTERFACE_MAIN = Widgets.get(548);
        TAB_INVENTORY = Widgets.get(548, 93);
        TAB_EQUIPMENT = Widgets.get(548, 94);
        TAB_MAGIC = Widgets.get(548, 96);
        TAB_LOGOUT = Widgets.get(548, 150);
        BTN_LOGOUT = Widgets.get(182, 13);
        BTN_RUN = Widgets.get(750, 6);
        CLCK_RUN = Widgets.get(750, 2);

        INTERFACE_MAGIC = Widgets.get(192);
        TELE_HOME = Widgets.get(192, 24);
        SORT_TELE = Widgets.get(192, 17);
        FILT_CMB = Widgets.get(192, 7);
        FILT_TELE = Widgets.get(192, 9);
        FILT_MISC = Widgets.get(192, 11);
        FILT_SKILL = Widgets.get(192, 13);

        INTERFACE_LODESTONE = Widgets.get(1092);
        LODE_LUMBRIDGE = Widgets.get(1092, 47);
        LODE_DRAYNOR = Widgets.get(1092, 44);
        LODE_FALADOR = Widgets.get(1092, 46);
        LODE_PORTSARIM = Widgets.get(1092, 48);
        LODE_TAVERLY = Widgets.get(1092, 50);
        LODE_CATHERBY = Widgets.get(1092, 43);
        LODE_SEERS = Widgets.get(1092, 49);
        LODE_ARDOUGNE = Widgets.get(1092, 41);
        LODE_YANILLE = Widgets.get(1092, 52);
        LODE_ALKHARID = Widgets.get(1092, 40);
        LODE_VARROCK = Widgets.get(1092, 51);
        LODE_EDGEVILLE = Widgets.get(1092, 45);
        LODE_BURTHORPE = Widgets.get(1092, 42);
        LODE_CROSS = Widgets.get(1092, 59);

        b_done = new boolean[]{
            false, false, false, false, false,
            false, false, false, false, false,
            false
        };
        w_done = new WidgetChild[]{
            LODE_DRAYNOR, LODE_FALADOR, LODE_PORTSARIM,
            LODE_TAVERLY, LODE_CATHERBY, LODE_SEERS,
            LODE_ARDOUGNE, LODE_YANILLE, LODE_ALKHARID,
            LODE_VARROCK, LODE_EDGEVILLE
        };
        t_done = new int[]{
            REQ_LODE_DRAYNOR, REQ_LODE_FALADOR, REQ_LODE_PORTSARIM,
            REQ_LODE_TAVERLY, REQ_LODE_CATHERBY, REQ_LODE_SEERS,
            REQ_LODE_ARDOUGNE, REQ_LODE_YANILLE, REQ_LODE_ALKHARID,
            REQ_LODE_VARROCK, REQ_LODE_EDGEVILLE
        };
    }
    private boolean firstTick = true;
    private boolean walking = false;
    private boolean traversing = false;
    private Tile[] curPath = null;
    private int curProgress = 0;
    private WidgetChild targetLodestone = null;
    private boolean resting = false;

    public void run() {
        if (firstTick) {
            firstTick = false;
            log.info("[LodestoneActivator] Main script started.");
        }
        if (!walking) {
            WidgetChild[] wcs = getNextLodestoneF2P();
            if (wcs == null) {
                log.info("[LodestoneActivator] All lodestones activated!");
                exit();
                log.info("[LodestoneActivator] Logged out!");
                log.info("[LodestoneActivator] Main script stopped.");
                stop();
            }
            curPath = getWalkPath(wcs[1]);

            Tile playerLocation = Players.getLocal().getLocation();
            if (!(playerLocation.getX() <= curPath[curProgress].getX() + 1 && playerLocation.getX() >= curPath[curProgress].getX() - 1
                    && playerLocation.getY() <= curPath[curProgress].getY() + 1 && playerLocation.getY() >= curPath[curProgress].getY() - 1)) {
                homePort(wcs[0]);
            }
            targetLodestone = wcs[1];
            walking = true;
        } else {
            if (!resting) {
                if (Integer.parseInt(BTN_RUN.getText()) > 50 && Settings.get(SETTING_RUN) != 1) {
                    CLCK_RUN.interact("Turn run mode on");
                } else if (Integer.parseInt(BTN_RUN.getText()) <= 10
                        && !Players.getLocal().isInCombat()) {
                    CLCK_RUN.interact("Rest");
                    resting = true;
                }
                if (!traversing) {
                    if (curProgress == curPath.length) {
                        SceneObject o = SceneEntities.getNearest(new Filter<SceneObject>() {

                            public boolean accept(final SceneObject o) {
                                return o != null && o.getId() == getLodestoneID(targetLodestone);
                            }
                        });
                        o.interact("Activate");
                        Time.sleep(Random.nextGaussian(400, 750, 1));
                        while (Players.getLocal().getAnimation() != -1) {
                            Time.sleep(Random.nextGaussian(25, 75, 1));
                        }
                        deepLodestoneUpdate();
                        curProgress = 0;
                        walking = false;
                    } else {
                        walkTileMM(curPath[curProgress], 1);
                        traversing = true;
                    }
                }
                Tile playerLocation = Players.getLocal().getLocation();
                if (playerLocation.getX() <= curPath[curProgress].getX() + 3 && playerLocation.getX() >= curPath[curProgress].getX() - 3
                        && playerLocation.getY() <= curPath[curProgress].getY() + 3 && playerLocation.getY() >= curPath[curProgress].getY() - 3) {
                    traversing = false;
                    curProgress++;
                }
                if (!Players.getLocal().isMoving()) {
                    walkTileMM(curPath[curProgress], 1);
                }
            } else if (Integer.parseInt(BTN_RUN.getText()) >= 100 || Players.getLocal().isInCombat()) {
                resting = false;
            }
        }
        tick++;
        Time.sleep(50);
    }

    /**
     * 
     */
    public void exit() {
        if (Tabs.getCurrent() != Tabs.LOGOUT) {
            TAB_LOGOUT.click(true);
            Time.sleep(Random.nextGaussian(50, 90, 1));
            exit();
        } else {
            BTN_LOGOUT.click(true);
        }
    }

    /**
     * 
     * @return
     */
    public WidgetChild[] getNextLodestoneF2P() {
        if (!isActivated(LODE_DRAYNOR)) {
            return new WidgetChild[]{LODE_LUMBRIDGE, LODE_DRAYNOR};
        } else if (!isActivated(LODE_PORTSARIM)) {
            return new WidgetChild[]{LODE_DRAYNOR, LODE_PORTSARIM};
        } else if (!isActivated(LODE_FALADOR)) {
            return new WidgetChild[]{LODE_PORTSARIM, LODE_FALADOR};
        } else if (!isActivated(LODE_EDGEVILLE)) {
            return new WidgetChild[]{LODE_FALADOR, LODE_EDGEVILLE};
        } else if (!isActivated(LODE_VARROCK)) {
            return new WidgetChild[]{LODE_EDGEVILLE, LODE_VARROCK};
        } else if (!isActivated(LODE_ALKHARID)) {
            return new WidgetChild[]{LODE_VARROCK, LODE_ALKHARID};
        } else {
            return null;
        }
    }

    /**
     * 
     * @param wc
     * @return
     */
    public int getLodestoneID(WidgetChild wc) {
        if (wc == LODE_DRAYNOR) {
            return 69833;
        } else if (wc == LODE_PORTSARIM) {
            return 69837;
        } else if (wc == LODE_FALADOR) {
            return 69835;
        } else if (wc == LODE_EDGEVILLE) {
            return 69834;
        } else if (wc == LODE_VARROCK) {
            return 69840;
        } else {
            return 69829;
        }
    }

    /**
     * 
     * @param wc
     * @return
     */
    public Tile[] getWalkPath(WidgetChild wc) {
        if (wc == LODE_DRAYNOR) {
            return new Tile[]{
                        new Tile(3233, 3221, 0), new Tile(3225, 3236, 0), new Tile(3219, 3245, 0),
                        new Tile(3212, 3246, 0), new Tile(3196, 3248, 0),
                        new Tile(3183, 3251, 0), new Tile(3177, 3258, 0),
                        new Tile(3166, 3263, 0), new Tile(3154, 3260, 0), new Tile(3149, 3264, 0),
                        new Tile(3135, 3272, 0), new Tile(3134, 3289, 0),
                        new Tile(3118, 3295, 0), new Tile(3105, 3298, 0)
                    };
        } else if (wc == LODE_PORTSARIM) {
            return new Tile[]{
                        new Tile(3105, 3298, 0), new Tile(3090, 3289, 0),
                        new Tile(3076, 3280, 0), new Tile(3063, 3269, 0),
                        new Tile(3058, 3256, 0), new Tile(3045, 3246, 0),
                        new Tile(3031, 3236, 0), new Tile(3028, 3221, 0),
                        new Tile(3012, 3216, 0)
                    };
        } else if (wc == LODE_FALADOR) {
            return new Tile[]{
                        new Tile(3012, 3216, 0), new Tile(3008, 3232, 0),
                        new Tile(3007, 3246, 0), new Tile(3008, 3259, 0),
                        new Tile(3008, 3274, 0), new Tile(3005, 3285, 0),
                        new Tile(3004, 3296, 0), new Tile(3006, 3312, 0),
                        new Tile(3005, 3328, 0), new Tile(3006, 3344, 0),
                        new Tile(3001, 3357, 0), new Tile(2993, 3368, 0),
                        new Tile(2981, 3376, 0), new Tile(2966, 3381, 0),
                        new Tile(2966, 3397, 0), new Tile(2967, 3403, 0)
                    };
        } else if (wc == LODE_EDGEVILLE) {
            return new Tile[]{
                        new Tile(2967, 3403, 0), new Tile(2978, 3415, 0),
                        new Tile(2988, 3427, 0), new Tile(3004, 3432, 0),
                        new Tile(3020, 3435, 0), new Tile(3032, 3446, 0),
                        new Tile(3038, 3460, 0), new Tile(3051, 3470, 0),
                        new Tile(3063, 3482, 0), new Tile(3064, 3496, 0),
                        new Tile(3067, 3505, 0)
                    };
        } else if (wc == LODE_VARROCK) {
            return new Tile[]{
                        new Tile(3067, 3505, 0), new Tile(3082, 3504, 0),
                        new Tile(3087, 3489, 0), new Tile(3080, 3474, 0),
                        new Tile(3088, 3461, 0), new Tile(3091, 3446, 0),
                        new Tile(3094, 3430, 0), new Tile(3106, 3421, 0),
                        new Tile(3121, 3416, 0), new Tile(3133, 3408, 0),
                        new Tile(3145, 3400, 0), new Tile(3158, 3395, 0),
                        new Tile(3173, 3391, 0), new Tile(3182, 3380, 0),
                        new Tile(3198, 3380, 0), new Tile(3208, 3376, 0),
                        new Tile(3214, 3376, 0)
                    };
        } else if (wc == LODE_ALKHARID) {
            return new Tile[]{
                        new Tile(3214, 3376, 0), new Tile(3220, 3361, 0),
                        new Tile(3225, 3346, 0), new Tile(3239, 3337, 0),
                        new Tile(3255, 3334, 0), new Tile(3270, 3331, 0),
                        new Tile(3284, 3325, 0), new Tile(3284, 3310, 0),
                        new Tile(3284, 3297, 0), new Tile(3285, 3282, 0),
                        new Tile(3286, 3267, 0), new Tile(3287, 3252, 0),
                        new Tile(3289, 3237, 0), new Tile(3292, 3221, 0),
                        new Tile(3298, 3207, 0), new Tile(3295, 3192, 0),
                        new Tile(3297, 3184, 0)
                    };
        } else {
            return null;
        }
    }

    /**
     * 
     * @param tile
     * @param rnd
     * @return
     */
    public boolean walkTileMM(Tile tile, int rnd) {
        double angle = angleTo(tile) - Camera.getAngleTo(0);
        double distance = distanceTo(tile);
        angle = angle * Math.PI / 180;
        int x = 627, y = 85;
        int dx = (int) (4 * distance * Math.cos(angle)) + 4 * Random.nextGaussian(0, rnd, 1);
        int dy = (int) (4 * distance * Math.sin(angle)) + 4 * Random.nextGaussian(0, rnd, 1);
        return Mouse.click(x + dx, y - dy, true);
    }

    /**
     * 
     * @param tile
     * @return
     */
    public int distanceTo(Tile tile) {
        return (int) Calculations.distance(Players.getLocal().getLocation(), tile);
    }

    /**
     * 
     * @param tile
     * @return
     */
    public int angleTo(Tile tile) {
        double ydif = tile.getY() - Players.getLocal().getLocation().getY();
        double xdif = tile.getX() - Players.getLocal().getLocation().getX();
        return (int) (Math.atan2(ydif, xdif) * 180 / Math.PI);
    }

    /**
     * 
     * @param wc
     * @return
     */
    public boolean isActivated(WidgetChild wc) {
        for (int i = 0; i < w_done.length; i++) {
            if (w_done[i] == wc) {
                return b_done[i];
            }
        }
        return false;
    }

    /**
     * 
     * @param wc
     */
    public void homePort(WidgetChild wc) {
        // Switch to the correct tab
        if (Tabs.getCurrent() != Tabs.MAGIC) {
            TAB_MAGIC.interact("Magic Spellbook");
        }
        // Just incase, for some reason, the previous method failed
        if (Tabs.getCurrent() == Tabs.MAGIC) {
            if (!TELE_HOME.isOnScreen()) {
                t_setupBot.done = false;
                homePort(wc);
            } else {
                if (!LODE_FALADOR.isVisible()) {
                    TELE_HOME.interact("Cast");
                    Time.sleep(Random.nextGaussian(200, 300, 1));
                    homePort(wc);
                } else {
                    wc.click(true);
                    Time.sleep(Random.nextGaussian(16000, 16750, 1));
                }
            }
        }
    }

    /**
     * 
     * @return
     */
    public int getLodestonesLeft() {
        int count = 0;
        for (Boolean b : b_done) {
            if (!b) {
                count++;
            }
        }
        return count;
    }

    /**
     * 
     */
    public void deepLodestoneUpdate() {
        // Switch to the correct tab
        if (Tabs.getCurrent() != Tabs.MAGIC) {
            TAB_MAGIC.interact("Magic Spellbook");
        }
        // Just incase, for some reason, the previous method failed
        if (Tabs.getCurrent() == Tabs.MAGIC) {
            if (!TELE_HOME.isOnScreen()) {
                t_setupBot.done = false;
                deepLodestoneUpdate();
            } else {
                if (!LODE_FALADOR.isVisible()) {
                    TELE_HOME.interact("Cast");
                    Time.sleep(Random.nextGaussian(200, 300, 1));
                    deepLodestoneUpdate();
                } else {
                    updateLodestones();
                    LODE_CROSS.click(true);
                }
            }
        }
    }

    /**
     * 
     */
    public void updateLodestones() {
        for (int i = 0; i < b_done.length; i++) {
            if (w_done[i].getTextureId() == t_done[i]) {
                b_done[i] = true;
            }
        }
    }
    private byte dragLength = 0;
    private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
    private final LinkedList<Click> clicks = new LinkedList<Click>();
    private static long start_time = 0;
    private final double HALF_PI = Math.PI * 0.5,
            THREE_HALF_PI = Math.PI * 1.5;

    private int toColor(double d) {
        return Math.min(255, Math.max(0, (int) d));
    }

    private class Click {

        private long clickTime, finishTime;
        private double radians;
        private int x, y;

        public Click(int x, int y, long clickTime) {
            this.clickTime = clickTime;
            finishTime = clickTime + 5000;
            radians = (clickTime - start_time) / (2000 / (2 * Math.PI))
                    % Math.PI;
            this.x = x;
            this.y = y;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }

        public void drawTo(Graphics2D g) {
            int alpha = (int) ((finishTime - System.currentTimeMillis()) / 5000.0 * 255);
            if (alpha < 0) {
                return;
            }
            g.setColor(new Color(0, 0, 0, alpha));
            g.drawLine((int) (x + 5 * Math.cos(radians)), (int) (y + 5 * Math.sin(radians)),
                    (int) (x + 5 * Math.cos(radians + Math.PI)),
                    (int) (y + 5 * Math.sin(radians + Math.PI)));
            g.drawLine((int) (x + 5 * Math.cos(radians + HALF_PI)),
                    (int) (y + 5 * Math.sin(radians + HALF_PI)),
                    (int) (x + 5 * Math.cos(radians + THREE_HALF_PI)),
                    (int) (y + 5 * Math.sin(radians + THREE_HALF_PI)));
        }

        public boolean equals(long time) {
            return clickTime == time;
        }
    }

    private class MousePathPoint extends Point {

        private static final long serialVersionUID = 1L;
        private long finishTime;
        private double lastingTime;

        public MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }

        public Color getColor() {
            return new Color(0, 0, 0, toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime)));
        }
    }

    /**
     * 
     * @param gra
     */
    public void onRepaint(Graphics gra) {
        Graphics2D g = (Graphics2D) gra;
        Point clientCursor = Mouse.getLocation();
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        while (!clicks.isEmpty() && clicks.peek().isUp()) {
            clicks.remove();
        }
        long clickTime = Mouse.getPressTime();
        Point lastClickPos = Mouse.getLocation();
        if (clicks.isEmpty() || !clicks.getLast().equals(clickTime)) {
            clicks.add(new Click(lastClickPos.x, lastClickPos.y, clickTime));
        }
        for (Click c : clicks) {
            c.drawTo(g);
        }
        g.setColor(Color.BLUE);
        double radians = (System.currentTimeMillis() - start_time)
                / (2000 / (2 * Math.PI)) % Math.PI;
        g.drawLine((int) Math.round(clientCursor.x + 7 * Math.cos(radians)),
                (int) Math.round(clientCursor.y + 7 * Math.sin(radians)),
                (int) Math.round(clientCursor.x + 7
                * Math.cos(radians + Math.PI)), (int) Math.round(clientCursor.y + 7
                * Math.sin(radians + Math.PI)));
        g.drawLine((int) Math.round(clientCursor.x + 7
                * Math.cos(radians + HALF_PI)), (int) Math.round(clientCursor.y
                + 7 * Math.sin(radians + HALF_PI)),
                (int) Math.round(clientCursor.x + 7
                * Math.cos(radians + THREE_HALF_PI)), (int) Math.round(clientCursor.y + 7
                * Math.sin(radians + THREE_HALF_PI)));
        g.setColor(Color.BLACK);
        g.drawOval(clientCursor.x - 8, clientCursor.y - 8, 16, 16);
        g.setColor(Color.BLACK);
        g.fillOval(clientCursor.x - 4, clientCursor.y - 4, 8, 8);
        g.setColor(Color.lightGray);
        g.setColor(new Color(0, 0, 0, 50));
        g.fillOval(clientCursor.x - 8, clientCursor.y - 8, 16, 16);
    }

    private class SetupBot extends Strategy implements Task {

        private int clicked = 0;
        private int loop = 0;
        public boolean done = false;

        @Override
        public boolean validate() {
            return !done;
        }

        public void run() {
            if (loop == 0) {
                log.info("[SetupBot] Setup started.");
            }
            // Switch to the correct tab
            if (Tabs.getCurrent() != Tabs.MAGIC) {
                TAB_MAGIC.interact("Magic Spellbook");
            }
            // Just incase, for some reason, the previous method failed
            if (Tabs.getCurrent() == Tabs.MAGIC) {
                if (SORT_TELE.getTextureId() != REQ_ON_TEX
                        && clicked < 1) {
                    SORT_TELE.interact("Sort");
                    clicked = 1;
                } else if (FILT_CMB.getTextureId() == REQ_ON_TEX
                        && clicked < 2) {
                    FILT_CMB.interact("Filter");
                    clicked = 2;
                } else if (FILT_TELE.getTextureId() != REQ_ON_TEX
                        && clicked < 3) {
                    FILT_TELE.interact("Filter");
                    clicked = 3;
                } else if (FILT_MISC.getTextureId() == REQ_ON_TEX
                        && clicked < 4) {
                    FILT_MISC.interact("Filter");
                    clicked = 4;
                } else if (FILT_SKILL.getTextureId() == REQ_ON_TEX) {
                    FILT_SKILL.interact("Filter");
                    clicked = 5;
                } else if (clicked < 6) {
                    if (FILT_SKILL.getTextureId() == REQ_ON_TEX) {
                        // Just to allow for a bit of lag :)
                        TAB_MAGIC.interact("Magic Spellbook");
                        clicked = 0;
                    } else {
                        clicked = 6;
                    }
                } else {
                    log.info("[SetupBot] Setup finished.");
                    updateSettings();
                    done = true;
                }
                Time.sleep(Random.nextGaussian(400, 450, 1));
            }
            loop++;
        }
    }

    private class CheckLodestones extends Strategy implements Task {

        private int loop = 0;
        public boolean done = false;

        @Override
        public boolean validate() {
            return !done && t_setupBot.done;
        }

        public void run() {
            if (loop == 0) {
                log.info("[CheckLodestones] Checking lodestones.");
            }
            // Switch to the correct tab
            if (Tabs.getCurrent() != Tabs.MAGIC) {
                TAB_MAGIC.interact("Magic Spellbook");
            }
            // Just incase, for some reason, the previous method failed
            if (Tabs.getCurrent() == Tabs.MAGIC) {
                if (!TELE_HOME.isOnScreen()) {
                    t_setupBot.done = false;
                } else {
                    if (!LODE_FALADOR.isVisible()) {
                        TELE_HOME.interact("Cast");
                        Time.sleep(Random.nextGaussian(200, 300, 1));
                    } else {
                        updateLodestones();
                        log.info("[CheckLodestones] Checked lodestones.");
                        done = true;
                    }
                }
            }
            Time.sleep(Random.nextGaussian(200, 300, 1));
            loop++;
        }
    }
}
