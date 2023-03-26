package world.bentobox.checkmeout;

import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;


/**
 *
 * @author BONNe
 */
@Plugin(name="CheckMeOut", version="1.0")
@ApiVersion(ApiVersion.Target.v1_16)
public class CheckMeOutPladdon extends Pladdon {

    @Override
    public Addon getAddon() {
        return new CheckMeOut();
    }
}
