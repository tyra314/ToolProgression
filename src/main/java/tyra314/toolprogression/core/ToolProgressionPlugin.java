package tyra314.toolprogression.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import tyra314.toolprogression.core.asm.ToolProgressionClassTransformer;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"tyra314.toolprogression"})
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class ToolProgressionPlugin implements IFMLLoadingPlugin
{
    public static boolean isObf;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{ToolProgressionClassTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {
        isObf = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
