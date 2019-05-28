package club.sk1er.mods.fullsreen;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.Dimension;
import java.awt.Toolkit;

@Mod(modid = FulllScreenMod.MODID, version = FulllScreenMod.VERSION, acceptedMinecraftVersions = "*")
public class FulllScreenMod {
    public static final String MODID = "sk1er_fullscreen";
    public static final String VERSION = "1.0";
    boolean lastFullscreen = false;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;
        boolean fullScreenNow = Minecraft.getMinecraft().isFullScreen();
        if (lastFullscreen != fullScreenNow) {
            fix(fullScreenNow);
            lastFullscreen = fullScreenNow;
        }]
    }


    public void fix(boolean fullscreen) {
        try {
            if (fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                Display.setLocation(0, 0);
                Display.setFullscreen(false);
                Display.setResizable(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                Display.setDisplayMode(new DisplayMode(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight));
                Display.setResizable(true);
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - Display.getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - Display.getHeight()) / 2);
                Display.setLocation(x, y);
            }
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}