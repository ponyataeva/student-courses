package data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

/**
 * Contain info about existing courses at the system.
 */
@Entity
public class Course implements Serializable {

    private BigInteger id;
    private String name;
    private Grade grade;
    private Set<UserInfo> userData;

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Enumerated(EnumType.STRING)
    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_course"
            , joinColumns = @JoinColumn(name = "course_id")
            , inverseJoinColumns = @JoinColumn(name = "user_id"))
    public Set<UserInfo> getUserData() {
        return userData;
    }

    public void setUserData(Set<UserInfo> userData) {
        this.userData = userData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (!id.equals(course.id)) return false;
        return name != null ? name.equals(course.name) : course.name == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
