package tyra314.toolprogression.plugin;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyra314.toolprogression.harvest.HarvestHelper;
import tyra314.toolprogression.harvest.HarvestLevel;

import javax.annotation.Nonnull;
import java.util.List;

class WailaTooltipProvider implements IWailaDataProvider
{
    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Nonnull
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {

        String tool = accessor.getBlock().getHarvestTool(accessor.getBlockState());
        ItemStack active_tool = accessor.getPlayer().getHeldItemMainhand();

        int level = accessor.getBlock().getHarvestLevel(accessor.getBlockState());

        if (level > -1)
        {
            String harvest_level;

            if (HarvestLevel.levels.containsKey(level))
            {
                harvest_level = HarvestLevel.levels.get(level).getFormatted();
            } else
            {
                harvest_level = String.valueOf(level);
            }

            currenttip.add(0, String.format("Harvest Level : %s", harvest_level));
        }

        if (tool != null)
        {
            String format = "§4";

            if (!active_tool.isEmpty() && active_tool.getItem().getToolClasses(active_tool).contains(tool))
            {
                format = "§2";
            }

            currenttip.add(0, String.format("Effective Tool : %s%s", format, tool));
        }


        if (HarvestHelper.canItemHarvestBlock(accessor.getPlayer(), accessor.getBlockState(), accessor.getWorld(), accessor.getPosition()))
        {
            currenttip.add(0, "Harvestable : §2yes");
        } else
        {
            currenttip.add(0, "Harvestable : §4no");
        }


        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
    {
        return tag;
    }
}
