package tyra314.toolprogression.core.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.LogManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import tyra314.toolprogression.core.AsmHooks;
import tyra314.toolprogression.core.ToolProgressionPlugin;

import java.util.Arrays;

import static org.objectweb.asm.Opcodes.*;

public class ToolProgressionClassTransformer implements IClassTransformer
{
    private static final String[] classesToTransform = {
            "net.minecraftforge.common.ForgeHooks"
    };

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        int index = Arrays.asList(classesToTransform).indexOf(transformedName);

        return index != -1 ? transform(index, basicClass) : basicClass;
    }

    private byte[] transform(int index, byte[] basicClass)

    {
        LogManager.getLogger("toolprogression_core")
                .info("Transforming: " + classesToTransform[index]);

        try
        {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(basicClass);
            reader.accept(node, 0);

            switch (index)
            {
                case 0:
                    if (!transformForgeHooks(node))
                    {
                        LogManager.getLogger("toolprogression_core").error(
                                "Something went wrong while applying the ToolProgression ASM. " +
                                "You'll better not continue. Sorry. Wanna yell at me? Look here: " +
                                "https://github.com/tyra314/ToolProgression/issues");
                    }
                    break;
            }

            ClassWriter writer =
                    new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return basicClass;
    }

    private boolean transformForgeHooks(ClassNode forgeHooksClass)
    {
        final String CAN_HARVEST = "canHarvestBlock";
        final String CAN_HARVEST_DESC = ToolProgressionPlugin.isObf ? "(Laow;Laed;Lamy;Let;)Z" :
                "(Lnet/minecraft/block/Block;" +
                "Lnet/minecraft/entity/player/EntityPlayer;" +
                "Lnet/minecraft/world/IBlockAccess;" +
                "Lnet/minecraft/util/math/BlockPos;)Z";

        for (MethodNode method : forgeHooksClass.methods)
        {
            if (method.name.equals(CAN_HARVEST) && method.desc.equals(CAN_HARVEST_DESC))
            {
                AbstractInsnNode targetNode = null;

                for (AbstractInsnNode instruction : method.instructions.toArray())
                {
                    if (instruction.getOpcode() == ALOAD &&
                        ((VarInsnNode) instruction).var == 2 &&
                        instruction.getPrevious() instanceof LineNumberNode)
                    {
                        targetNode = instruction;
                        break;
                    }
                }


                if (targetNode != null)
                {
                    // We want to return the result of the call to:
                    // tyra314.toolprogression.core.AsmHooks.canHarvestBlock
                    InsnList toInsert = new InsnList();

                    // first, put arguments on the stack: player, world, pos
                    toInsert.add(new VarInsnNode(ALOAD, 1));
                    toInsert.add(new VarInsnNode(ALOAD, 2));
                    toInsert.add(new VarInsnNode(ALOAD, 3));

                    // call the function
                    toInsert.add(new MethodInsnNode(INVOKESTATIC,
                            Type.getInternalName(AsmHooks.class),
                            "canHarvestBlock",
                            ToolProgressionPlugin.isObf ? "(Laed;Lamy;Let;)Z" :
                                    "(Lnet/minecraft/entity/player/EntityPlayer;" +
                                    "Lnet/minecraft/world/IBlockAccess;" +
                                    "Lnet/minecraft/util/math/BlockPos;)Z",
                            false));

                    // return whatever we got
                    toInsert.add(new InsnNode(IRETURN));

                    // write that shit into the method
                    method.instructions.insertBefore(targetNode, toInsert);

                    return true;
                }
            }
        }

        return false;
    }
}
