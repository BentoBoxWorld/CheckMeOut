package world.bentobox.checkmeout.objects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.annotations.Expose;

import world.bentobox.bentobox.database.objects.DataObject;

public class SubmissionData implements DataObject {

    @Expose
    private String uniqueId = "submissions";
    @Expose
    private Map<World, Map<UUID, Location>> submissions = new HashMap<>();

    @Override
    public String getUniqueId() {
        return uniqueId;
    }

    @Override
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }


    /**
     * @return the submissions
     */
    public Map<World, Map<UUID, Location>> getSubmissions() {
        return submissions;
    }

    /**
     * @param submissions the submissions to set
     */
    public void setSubmissions(Map<World, Map<UUID, Location>> submissions) {
        this.submissions = submissions;
    }

}
