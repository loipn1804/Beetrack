package dne.beetrack.model;

/**
 * Created by loipn on 7/16/2016.
 */
public class LostAsset {
    private String name;
    private String asset_code;

    public LostAsset(String name, String asset_code) {
        this.name = name;
        this.asset_code = asset_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAsset_code() {
        return asset_code;
    }

    public void setAsset_code(String asset_code) {
        this.asset_code = asset_code;
    }
}
