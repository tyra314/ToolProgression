package tyra314.toolprogression.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.HarvestHelper;
import tyra314.toolprogression.harvest.HarvestLevel;

import javax.annotation.Nonnull;
import java.util.List;

public class WailaTooltipProvider implements IWailaDataProvider
{
    public static void register(IWailaRegistrar registrar)
    {
        if (ConfigHandler.waila_enabled)
        {
            registrar.registerTailProvider(new WailaTooltipProvider(), Block.class);
        }
    }

    @Nonnull
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public List<String> getWailaHead(ItemStack itemStack,
                                     List<String> currenttip,
                                     IWailaDataAccessor accessor,
                                     IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Nonnull
    public List<String> getWailaBody(ItemStack itemStack,
                                     List<String> currenttip,
                                     IWailaDataAccessor accessor,
                                     IWailaConfigHandler config)
    {
        return currenttip;
    }

    @Nonnull
    @Override
    public List<String> getWailaTail(ItemStack itemStack,
                                     List<String> currenttip,
                                     IWailaDataAccessor accessor,
                                     IWailaConfigHandler config)
    {

        String tool = accessor.getBlock().getHarvestTool(accessor.getBlockState());
        ItemStack active_tool = accessor.getPlayer().getHeldItemMainhand();

        HarvestHelper.Result r = HarvestHelper.canPlayerHarvestReason(accessor.getPlayer(), accessor
                .getBlockState(), accessor.getWorld(), accessor.getPosition());

        if (accessor.getBlock() != Blocks.BEDROCK && r == HarvestHelper.Result.NONE)
        {
            if (tool != null && !active_tool.getItem().getToolClasses(active_tool).contains(tool))
            {
                currenttip.add(0, String.format("Effective Tool : §4%s", tool));
            }

            if (ConfigHandler.waila_show_harvestable)
            {
                currenttip.add(0, "Harvestable : §2yes");
            }
        }
        else
        {
            if (tool != null && r == HarvestHelper.Result.TOOL_CLASS)
            {
                currenttip.add(0, String.format("Required Tool : §4%s", tool));

            }
            else if (tool != null && r == HarvestHelper.Result.LEVEL)
            {
                int required_level = HarvestHelper.getRequiredHarvestLevel(accessor.getPlayer(), accessor.getBlockState());

                String harvest_level = String.valueOf(required_level);

                if (HarvestLevel.levels.containsKey(required_level))
                {
                    harvest_level = HarvestLevel.levels.get(required_level).getFormatted();
                }

                currenttip.add(0, String.format("Harvest Level : %s", harvest_level));
            }

            currenttip.add(0, "Harvestable : §4no");
        }

        return currenttip;
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player,
                                     TileEntity te,
                                     NBTTagCompound tag,
                                     World world,
                                     BlockPos pos)
    {
        return tag;
    }
}
