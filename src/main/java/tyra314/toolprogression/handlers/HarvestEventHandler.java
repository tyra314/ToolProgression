package tyra314.toolprogression.handlers;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tyra314.toolprogression.compat.gamestages.GSEventHandler;
import tyra314.toolprogression.compat.gamestages.GSHelper;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.HarvestHelper;

public class HarvestEventHandler
{
    /*
     * This is copied from vanilla / forge
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
                f += (float)(i * i + 1);
            }
        }

        if (player.isPotionActive(MobEffects.HASTE))
        {
            f *= 1.0F + (float)(player.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
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

        if (player.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(player))
        {
            f /= 5.0F;
        }

        if (!player.onGround)
        {
            f /= 5.0F;
        }

        return f;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if(!HarvestHelper.canPlayerHarvestBlock(event.getEntityPlayer(),
                event.getState()))
        {
            event.setCanceled(true);
        }
        else
        {
            IBlockState state = event.getState();
            if (GSHelper.isLoaded())
            {
                state = GSEventHandler.getStagedBlockState(event.getEntityPlayer(), state);
            }

            event.setCanceled(false);
            float f = getBreakSpeedDefault(event.getEntityPlayer(), state);
            event.setNewSpeed(Math.max(f, 4.0F));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBreakEvent(BlockEvent.BreakEvent event)
    {
        if (HarvestHelper.canPlayerHarvestBlock(event.getPlayer(), event.getState()) &&
            !event.getState().getBlock()
                    .canHarvestBlock(event.getWorld(), event.getPos(), event.getPlayer()))
        {
            IBlockState state = event.getState();
            if (GSHelper.isLoaded())
            {
                state = GSEventHandler.getStagedBlockState(event.getPlayer(), state);
            }

            int fortune = 0;

            ItemStack tool = event.getPlayer().getHeldItemMainhand();

            if (!tool.isEmpty())
            {
                fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
            }

            state.getBlock().dropBlockAsItem(event.getWorld(), event.getPos(), state, fortune);

            event.setExpToDrop(state.getBlock()
                    .getExpDrop(state, event.getWorld(), event.getPos(), fortune));

            if (ConfigHandler.dupe_fix_hacky_macky)
            {
                // This is probably the worst idea I ever had.
                // Come to my issue tracker and yell at me.
                event.setCanceled(true);
                event.getWorld().setBlockToAir(event.getPos());
            }
        }
    }
}
