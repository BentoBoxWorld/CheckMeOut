package world.bentobox.checkmeout;


import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;


/**
 *
 */
public class CheckMeOutPladdon extends Pladdon {

    private Addon addon;

    @Override
    public Addon getAddon() {
        if (addon == null) {
            addon = new CheckMeOut();
        }
        return addon;
    }
}
