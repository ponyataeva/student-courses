package data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.math.BigInteger;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Use for collect all user's info.
 * There is a total value calculate as courses count.
 * The courses set as reference to temp table with course_id - user_id relationship.
 */
@Entity
public class UserInfo {

    private BigInteger userId;
    private User user;
    private int total = 0;
    private Set<Course> courses;

    @Id
    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    @ManyToMany
    @JoinTable(name = "user_course"
            , joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"))
    public Set<Course> getCourses() {
        return courses;
    }

    @OneToOne
    @MapsId
    @JsonIgnore public User getUser() {
        return user;
    }

    @Transient
    public int getTotal() {
        return total;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setCourses(Set<Course> courses) {
        if (courses != null) {
            total = courses.size();
        }
        this.courses = courses;
    }

    @PostLoad
    private void onLoad() {
        int result = 0;
        if (!isEmpty(getCourses())) {
            result = getCourses().size();
        }
        this.total = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserInfo userInfo = (UserInfo) o;

        return userId.equals(userInfo.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + userId +
                ", courses=" + courses +
                '}';
    }
}
