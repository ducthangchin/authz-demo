package com.ducthangchin.user.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VDTUser {
    @Id
    @SequenceGenerator(
            name="user_id_sequence",
            sequenceName = "user_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Long id;
    private String fullName;

    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<VDTRole> roles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private VDTUser manager;

    @OneToMany(mappedBy = "manager")
    private Collection<VDTUser> subordinates = new ArrayList<>();
}
