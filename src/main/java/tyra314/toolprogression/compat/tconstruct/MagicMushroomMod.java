package tyra314.toolprogression.compat.tconstruct;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierAspect;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;
import tyra314.toolprogression.item.ModItems;

public class MagicMushroomMod extends ToolModifier
{
    public static Modifier magic_mushroom;

    public static void init()
    {
        magic_mushroom = new MagicMushroomMod();
        magic_mushroom.addItem(ModItems.magic_mushroom);
    }

    public MagicMushroomMod()
    {
        super("magicmushroom", 0x55ACEE);

        addAspects(new ModifierAspect.SingleAspect(this),
                new ModifierAspect.DataAspect(this),
                ModifierAspect.freeModifier);
    }

    @Override
    public void applyEffect(NBTTagCompound rootCompound, NBTTagCompound modifierTag)
    {
        ToolNBT toolData = TagUtil.getToolStats(rootCompound);
        toolData.harvestLevel += 1;

        TagUtil.setToolTag(rootCompound, toolData.get());
    }
}
