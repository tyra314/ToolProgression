package tyra314.toolprogression.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.client.renderer.IModelRegister;

public class BaseItem extends Item implements IModelRegister
{
    @SuppressWarnings("WeakerAccess")
    public BaseItem(String name)
    {
        setRegistryName(name);
        setUnlocalizedName(ToolProgressionMod.MODID + "." + name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModel()
    {
        //noinspection ConstantConditions
        ModelLoader.setCustomModelResourceLocation(this,
                0,
                new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
