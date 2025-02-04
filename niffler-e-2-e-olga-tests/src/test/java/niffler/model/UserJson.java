package niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class UserJson {
    @JsonProperty("id")
    private UUID id;
    @JsonProperty("username")
    private String userName;
    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("surname")
    private String surname;
    @JsonProperty("currency")
    private CurrencyValues currency;
    @JsonProperty("photo")
    private byte[] photo;
    @JsonProperty("password")
    private String password;

    private transient List<CategoryJson> categories = new ArrayList<>();
    private transient List<SpendJson> spends = new ArrayList<>();

    public UserJson() {
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CurrencyValues getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyValues currency) {
        this.currency = currency;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CategoryJson> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryJson> categories) {
        this.categories = categories;
    }

    public void addCategory(CategoryJson category) {
        categories.add(category);
    }

    public List<SpendJson> getSpends() {
        return spends;
    }

    public void setSpends(List<SpendJson> spends) {
        this.spends = spends;
    }

    public void addSpend(SpendJson spend) {
        spends.add(spend);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserJson userJson = (UserJson) o;
        return Objects.equals(id, userJson.id) && Objects.equals(userName, userJson.userName) && Objects.equals(firstname, userJson.firstname) && Objects.equals(surname, userJson.surname) && currency == userJson.currency && Objects.equals(photo, userJson.photo) && Objects.equals(password, userJson.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, firstname, surname, currency, photo, password);
    }
}


