package tyra314.toolprogression.compat.waila;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tyra314.toolprogression.api.OverwrittenContent;
import tyra314.toolprogression.compat.gamestages.GSEventHandler;
import tyra314.toolprogression.compat.gamestages.GSHelper;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.HarvestHelper;
import tyra314.toolprogression.harvest.HarvestLevelName;

import javax.annotation.Nonnull;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
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
        IBlockState state = accessor.getBlockState();

        if (GSHelper.isLoaded())
        {
            state = GSEventHandler.getStagedBlockState(accessor.getPlayer(), state);
        }

        String required_toolclass = state.getBlock().getHarvestTool(state);
        ItemStack active_tool = accessor.getPlayer().getHeldItemMainhand();

        HarvestHelper.Result r =
                HarvestHelper.canPlayerHarvestReason(accessor.getPlayer(), state);

        if (accessor.getBlock() != Blocks.BEDROCK && r == HarvestHelper.Result.NONE)
        {
            if (required_toolclass != null &&
                !active_tool.getItem().getToolClasses(active_tool).contains(required_toolclass) &&
                !required_toolclass.equals("null"))
            {
                currenttip.add(0, String.format("Effective Tool : §4%s", required_toolclass));
            }

            if (ConfigHandler.waila_show_harvestable)
            {
                currenttip.add(0, "Harvestable : §2yes");
            }
        }
        else
        {
            if (required_toolclass != null && r == HarvestHelper.Result.TOOL_CLASS)
            {
                currenttip.add(0, String.format("Required Tool : §4%s", required_toolclass));

            }
            else if (required_toolclass != null && r == HarvestHelper.Result.LEVEL)
            {
                int required_level = state.getBlock().getHarvestLevel(state);

                String harvest_level = String.valueOf(required_level);

                if (OverwrittenContent.mining_level.containsKey(required_level))
                {
                    harvest_level = OverwrittenContent.mining_level.get(required_level).getFormatted();
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
