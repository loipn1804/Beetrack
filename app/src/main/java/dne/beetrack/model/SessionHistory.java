package dne.beetrack.model;

import java.util.List;

/**
 * Created by loipn on 7/16/2016.
 */
public class SessionHistory {
    private long session_id;
    private long company_id;
    private String name;
    private String description;
    private String session_date;
    private int f_completed;
    private String created_at;
    private String updated_at;
    private int total_scanned;
    private int total_assets;
    private int total_lost;
    private List<SessionUser> sessionUserList;
    private List<LostAsset> lostAssetList;

    public SessionHistory(long session_id, long company_id, String name, String description, String session_date, int f_completed, String created_at, String updated_at, int total_scanned, int total_assets, int total_lost, List<SessionUser> sessionUserList, List<LostAsset> lostAssetList) {
        this.session_id = session_id;
        this.company_id = company_id;
        this.name = name;
        this.description = description;
        this.session_date = session_date;
        this.f_completed = f_completed;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.total_scanned = total_scanned;
        this.total_assets = total_assets;
        this.total_lost = total_lost;
        this.sessionUserList = sessionUserList;
        this.lostAssetList = lostAssetList;
    }

    public long getSession_id() {
        return session_id;
    }

    public void setSession_id(long session_id) {
        this.session_id = session_id;
    }

    public long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSession_date() {
        return session_date;
    }

    public void setSession_date(String session_date) {
        this.session_date = session_date;
    }

    public int getF_completed() {
        return f_completed;
    }

    public void setF_completed(int f_completed) {
        this.f_completed = f_completed;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getTotal_scanned() {
        return total_scanned;
    }

    public void setTotal_scanned(int total_scanned) {
        this.total_scanned = total_scanned;
    }

    public int getTotal_assets() {
        return total_assets;
    }

    public void setTotal_assets(int total_assets) {
        this.total_assets = total_assets;
    }

    public int getTotal_lost() {
        return total_lost;
    }

    public void setTotal_lost(int total_lost) {
        this.total_lost = total_lost;
    }

    public List<SessionUser> getSessionUserList() {
        return sessionUserList;
    }

    public void setSessionUserList(List<SessionUser> sessionUserList) {
        this.sessionUserList = sessionUserList;
    }

    public List<LostAsset> getLostAssetList() {
        return lostAssetList;
    }

    public void setLostAssetList(List<LostAsset> lostAssetList) {
        this.lostAssetList = lostAssetList;
    }
}
