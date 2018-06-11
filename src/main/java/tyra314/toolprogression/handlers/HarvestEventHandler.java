package tyra314.toolprogression.handlers;

import buildcraft.api.tools.IToolWrench;
import cofh.api.item.IToolHammer;
import mcjty.lib.api.smartwrench.SmartWrench;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tyra314.toolprogression.compat.gamestages.GSEventHandler;
import tyra314.toolprogression.compat.gamestages.GSHelper;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockOverwrite;
import tyra314.toolprogression.harvest.HarvestHelper;

import java.util.List;

public class HarvestEventHandler
{
    /*
     * This is copied from vanilla / forge but without calling ForgeEvent
     */
    private float getBreakSpeedDefault(EntityPlayer player, IBlockState state)
    {
        float f = player.inventory.getDestroySpeed(state);

        if (f > 1.0F)
        {
            int i = EnchantmentHelper.getEfficiencyModifier(player);
            ItemStack itemstack = player.getHeldItemMainhand();

            if (i > 0 && !itemstack.isEmpty())
            {
                f += (float) (i * i + 1);
            }
        }

        if (player.isPotionActive(MobEffects.HASTE))
        {
            f *= 1.0F +
                 (float) (player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }

        if (player.isPotionActive(MobEffects.MINING_FATIGUE))
        {
            float f1;

            switch (player.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier())
            {
                case 0:
                    f1 = 0.3F;
                    break;
                case 1:
                    f1 = 0.09F;
                    break;
                case 2:
                    f1 = 0.0027F;
                    break;
                case 3:
                default:
                    f1 = 8.1E-4F;
            }

            f *= f1;
        }

        if (player.isInsideOfMaterial(Material.WATER) &&
            !EnchantmentHelper.getAquaAffinityModifier(player))
        {
            f /= 5.0F;
        }

        if (!player.onGround)
        {
            f /= 5.0F;
        }

        return f;
    }

    private boolean canHarvestBlockDefault(IBlockState state, EntityPlayer player)
    {
        if (state.getMaterial().isToolNotRequired())
        {
            return true;
        }

        ItemStack stack = player.getHeldItemMainhand();
        String tool = state.getBlock().getHarvestTool(state);
        if (stack.isEmpty() || tool == null)
        {
            return player.canHarvestBlock(state);
        }

        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
        if (toolLevel < 0)
        {
            return player.canHarvestBlock(state);
        }

        return toolLevel >= state.getBlock().getHarvestLevel(state);
    }

    private void dropBlockDefault(IBlockState state, World world, BlockPos pos, int fortune)
    {
        NonNullList<ItemStack> drops = NonNullList.create();

        state.getBlock().getDrops(drops, world, pos, state, fortune);

        // do not drop items while restoring blockstates, prevents item dupe
        if (!world.isRemote && !world.restoringBlockSnapshots)
        {
            for (ItemStack drop : drops)
            {
                state.getBlock().spawnAsEntity(world, pos, drop);
            }
        }
    }

    /*
     * Okay, this is not the best solution, BUT we have to run after the handler from OreStages, but
     * before everyone else. (Actually, we have to run after every other mod trying to change
     * harvestability for some reason.)
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        // First, get the blockstate, we are actually trying to break
        IBlockState state = event.getState();
        if (GSHelper.isLoaded())
        {
            state = GSEventHandler.getStagedBlockState(event.getEntityPlayer(), state);
        }

        // Then, get the break speed we would have, if we would break the actual blockstate
        // we have to use this value, as OreStages may already have changed the speed
        float f = getBreakSpeedDefault(event.getEntityPlayer(), state);

        // We now normalize the breakspeed to the one, if the could harvest the block.
        // Now it get's ugly.
        // In ForgeHooks::blockStrenght(), from which this event will be invoked indirectly,
        // there is an if-statement, which distinguishes whether or not, the player can break
        // the original block. Depending on this, it will slow down break speed to 30 percent.
        // If we overrule the harvestability, we have to counter this penalty.
        if (!canHarvestBlockDefault(event.getState(), event.getEntityPlayer()))
        {
            f *= 100F / 30F;
        }

        if (!HarvestHelper.canPlayerHarvestBlock(event.getEntityPlayer(),
                event.getState()))
        {
            // If we decide that the block is not harvestable ...

            BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(state);
            if (ConfigHandler.all_blocks_destroyable ||
                (overwrite != null && overwrite.destroyable))
            {
                // ...and the block is destroyable, then we apply the break speed penalty once again.
                f *= 30F / 100F;

            }
            else
            {
                // ...or else we just cancel the event and thus prevent the block from being broken
                event.setCanceled(true);
            }
        }

        event.setNewSpeed(f);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    void onHarvestCheck(PlayerEvent.HarvestCheck event)
    {
        // actually I'd say, this event is meaningless, however I fix FakePlayers with that :C

        if (event.getTargetBlock() == null || event.getEntityPlayer() == null)
        {
            return;
        }

        event.setCanHarvest(HarvestHelper.canPlayerHarvestBlock(event.getEntityPlayer(),
                event.getTargetBlock()));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onBreakEvent(BlockEvent.BreakEvent event)
    {
        Block block = event.getState().getBlock();

        if (HarvestHelper.canPlayerHarvestBlock(event.getPlayer(), event.getState()) &&
            !block.canHarvestBlock(event.getWorld(), event.getPos(), event.getPlayer()))
        {
            IBlockState state = event.getState();
            if (GSHelper.isLoaded())
            {
                state = GSEventHandler.getStagedBlockState(event.getPlayer(), state);
                block = state.getBlock();
            }

            int fortune = 0;

            ItemStack tool = event.getPlayer().getHeldItemMainhand();

            if (!tool.isEmpty())
            {
                fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
            }

            if (!event.getPlayer().isCreative())
            {
                dropBlockDefault(state, event.getWorld(), event.getPos(), fortune);
            }

            event.setExpToDrop(block.getExpDrop(state, event.getWorld(), event.getPos(), fortune));


            // This is technically still a bad hack, but orestages run on lowest priority. We are
            // only on low priority, so we should be running before orestages.
            if (ConfigHandler.dupe_fix_hacky_macky || GSHelper.isLoaded())
            {
                // This is probably the worst idea I ever had.
                // Come to my issue tracker and yell at me.

                if (block instanceof IShearable)
                {
                    if (((IShearable) block).isShearable(
                            event.getPlayer().getActiveItemStack(),
                            event.getWorld(),
                            event.getPos()))
                    {
                        List<ItemStack> drops = ((IShearable) block).onSheared(
                                event.getPlayer().getActiveItemStack(),
                                event.getWorld(),
                                event.getPos(),
                                fortune);

                        for (ItemStack drop : drops)
                        {
                            block.spawnAsEntity(event.getWorld(), event.getPos(), drop);
                        }
                    }
                }

                block.onBlockHarvested(event.getWorld(), event.getPos(), state, event.getPlayer());

                event.setCanceled(true);
                event.getWorld().setBlockToAir(event.getPos());

                event.getPlayer().addStat(StatList.getBlockStats(event.getState().getBlock()));
            }
        }
    }

    private boolean isWrench(ItemStack stack)
    {
        Item item = stack.getItem();

        if (item instanceof IToolWrench)
        {
            return true;
        }

        if (item instanceof IToolHammer)
        {
            return true;
        }

        if (item instanceof SmartWrench)
        {
            return true;
        }

        // It seems like I don't need to check for IC2 wrenches. God bless.

        return false;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onDropEvent(BlockEvent.HarvestDropsEvent event)
    {
        // Okay, I use this event to accomplish one specific behavior. I want to be able to let
        // players break a block, but it shall not drop anything. Therefore, I neither can't
        // cancel the BreakSpeed nor the BlockBreak events. However, in this event, I can't know
        // which and how the block was broken. So I must use some guessing here.


        // if there is no harvester, I can't decide anything. So skip it.
        // TODO: What about harvesters other than a player?
        if (event.getHarvester() == null)
        {
            return;
        }

        if (!HarvestHelper.canPlayerHarvestBlock(event.getHarvester(), event.getState()))
        {
            if (isWrench(event.getHarvester().getHeldItemMainhand()))
            {
                // if the harvester has a wrench in his hand, I assume that this wrench was used
                // and therefore it can circumvent my checks.

                // Fortunately, a wrench isn't a good pickaxe, so it should be fine harvesting stuff

                return;
            }

            // This disables the drops of harvested blocks, which aren't harvestable, but destroyable

            // Well... I can't cancel the event, so lets hope, just setting it to zero will take
            // take care of that. Expect incoming bugs because of shitz and gigglez. FeelsBadMan
            event.setDropChance(0);
        }
    }
}
