package tyra314.toolprogression.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("WeakerAccess")
public class MagicMushroom extends BaseItem
{

    public MagicMushroom(String name)
    {
        super(name);

        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.BREWING);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }
}
