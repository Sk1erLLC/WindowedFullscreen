package club.sk1er.mods.fullscreen.forge;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class MinecraftTransformer implements Transformer {
    @Override
    public String[] getClassName() {
        return new String[]{"net.minecraft.client.Minecraft"};
    }

    @Override
    public void transform(ClassNode classNode, String name) {
        for (MethodNode method : classNode.methods) {
            String methodName = mapMethodName(classNode, method);
            if (methodName.equals("toggleFullscreen") || methodName.equals("func_71352_k")) {
                InsnList insnList = new InsnList();
                LabelNode labelNode = new LabelNode();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "club/sk1er/mods/fullscreen/FullScreenMod", "apply", "()Z", false));
                insnList.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
                insnList.add(new InsnNode(Opcodes.RETURN));
                insnList.add(labelNode);
                method.instructions.insertBefore(method.instructions.getFirst(), insnList);
            }
        }
    }
}
