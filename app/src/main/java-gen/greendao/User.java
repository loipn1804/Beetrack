package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table USER.
 */
public class User {

    private Long account_id;
    private Long company_id;
    private Long department_id;
    private String name;
    private String email;
    private String username;
    private String phone;
    private String created_at;
    private String updated_at;

    public User() {
    }

    public User(Long account_id) {
        this.account_id = account_id;
    }

    public User(Long account_id, Long company_id, Long department_id, String name, String email, String username, String phone, String created_at, String updated_at) {
        this.account_id = account_id;
        this.company_id = company_id;
        this.department_id = department_id;
        this.name = name;
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }

    public Long getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(Long department_id) {
        this.department_id = department_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

}
