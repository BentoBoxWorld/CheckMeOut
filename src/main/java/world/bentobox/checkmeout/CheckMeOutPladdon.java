package world.bentobox.checkmeout;


import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.Pladdon;


/**
 *
 */
public class CheckMeOutPladdon extends Pladdon {

    @Override
    public Addon getAddon() {
        return new CheckMeOut();
    }
}
