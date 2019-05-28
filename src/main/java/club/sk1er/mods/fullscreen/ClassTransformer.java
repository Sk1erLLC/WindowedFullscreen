package club.sk1er.mods.fullscreen;


import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ListIterator;


public final class ClassTransformer implements IClassTransformer {

    private static final Logger LOGGER = LogManager.getLogger("ASM");


    public ClassTransformer() {
    }


    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (bytes == null)
            return null;


        if (transformedName.equalsIgnoreCase("net.minecraft.client.Minecraft")) {
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

            for (MethodNode method : classNode.methods) {
                String methodName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, method.name, method.desc);
                if (methodName.equalsIgnoreCase("runGameLoop") || methodName.equalsIgnoreCase("func_71411_J")) {
                    ListIterator<AbstractInsnNode> iterator = method.instructions.iterator();
                    while (iterator.hasNext()) {
                        AbstractInsnNode next = iterator.next();
                        if (next instanceof MethodInsnNode) {
                            String method1 = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(classNode.name, ((MethodInsnNode) next).name, ((MethodInsnNode) next).desc);
                            if (method1.equalsIgnoreCase("toggleFullscreen") || method1.equalsIgnoreCase("func_71352_k")) {
                                iterator.remove();

                                for (int i = 0; i < 11; i++) {
                                    next = iterator.previous();
                                    iterator.remove();
                                }
                                break;
                            }
                        }
                    }
                }
            }
            // write new class bytes
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            try {
                classNode.accept(classWriter);
            } catch (Throwable e) {
                LOGGER.error("Exception when transforming {} : {}", transformedName, e.getClass().getSimpleName());
                e.printStackTrace();
            }
            return classWriter.toByteArray();
        } else return bytes;


    }
}