package eu.komanda30.kupra.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="unit")
@Entity
@SequenceGenerator(
        name="unitIdSequence",
        sequenceName="unit_seq",
        allocationSize=1
)
public class Unit {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="unitIdSequence")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String abbreviation;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
