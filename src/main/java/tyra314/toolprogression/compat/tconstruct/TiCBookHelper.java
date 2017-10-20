package tyra314.toolprogression.compat.tconstruct;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.library.book.TinkerBook;
import tyra314.toolprogression.ToolProgressionMod;

public class TiCBookHelper
{
    @SideOnly(Side.CLIENT)
    public static void client_init()
    {
        TinkerBook.INSTANCE.addRepository(new FileRepository(new ResourceLocation(ToolProgressionMod.MODID,
                "book").toString()));
    }
}
