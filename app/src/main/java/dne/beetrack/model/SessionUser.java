package dne.beetrack.model;

/**
 * Created by loipn on 7/16/2016.
 */
public class SessionUser {
    private long account_id;
    private long company_id;
    private long department_id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private int f_completed;

    public SessionUser(long account_id, long company_id, long department_id, String name, String username, String email, String phone, int f_completed) {
        this.account_id = account_id;
        this.company_id = company_id;
        this.department_id = department_id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.f_completed = f_completed;
    }

    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    public long getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(long department_id) {
        this.department_id = department_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getF_completed() {
        return f_completed;
    }

    public void setF_completed(int f_completed) {
        this.f_completed = f_completed;
    }
}
