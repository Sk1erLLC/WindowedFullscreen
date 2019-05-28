package club.sk1er.mods.fullscreen;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = FulllScreenMod.MODID, version = FulllScreenMod.VERSION, acceptedMinecraftVersions = "*")
public class FulllScreenMod {
    public static final String MODID = "sk1er_fullscreen";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Listener target = new Listener();
        MinecraftForge.EVENT_BUS.register(target);
        FMLCommonHandler.instance().bus().register(target);
    }
}