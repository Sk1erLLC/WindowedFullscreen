package club.sk1er.mods.fullscreen;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.awt.Dimension;
import java.awt.Toolkit;

@Mod(modid = FullScreenMod.MODID, version = FullScreenMod.VERSION, acceptedMinecraftVersions = "*")
public class FullScreenMod {
    public static final String MODID = "sk1er_fullscreen";
    public static final String VERSION = "2.0";

    public static boolean apply() {
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.fullscreen = !minecraft.fullscreen;

        boolean grabbed = Mouse.isGrabbed();
        if (grabbed)
            Mouse.setGrabbed(false);
        try {
            DisplayMode displayMode = Display.getDesktopDisplayMode();
            if (minecraft.fullscreen) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
                Display.setDisplayMode(displayMode);

                Display.setLocation(0, 0);
                Display.setFullscreen(false);
                Display.setResizable(false);
            } else {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "false");
                displayMode = new DisplayMode(minecraft.tempDisplayWidth, minecraft.tempDisplayHeight);
                Display.setDisplayMode(displayMode);
                Display.setResizable(true);
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                int x = (int) ((dimension.getWidth() - Display.getWidth()) / 2);
                int y = (int) ((dimension.getHeight() - Display.getHeight()) / 2);
                Display.setLocation(x, y);
            }

            minecraft.displayWidth = displayMode.getWidth();
            minecraft.displayHeight = displayMode.getHeight();
            if (minecraft.currentScreen != null) {
                minecraft.resize(minecraft.displayWidth, minecraft.displayHeight);
            } else {
                minecraft.updateFramebufferSize();
            }
            minecraft.updateDisplay();
            Mouse.setCursorPosition((Display.getX() + Display.getWidth()) / 2, (Display.getY() + Display.getHeight()) / 2);
            if (grabbed)
                Mouse.setGrabbed(true);
            return true;
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
//        MinecraftForge.EVENT_BUS.register(this);
    }
}