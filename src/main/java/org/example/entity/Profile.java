package org.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // creates the foreign key user_id in the database
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @lombok.ToString.Exclude // Infinite Recursion prevention, look it up
    private User user;

    @Column(name = "nom_capoeira", nullable = false)
    private String nomCapoeira;

    @Column(name = "photo_url")
    private String photoUrl;

    private String ville;

    private String pays;

    @Column(name = "organigramme_country")
    private String organigrammeCountry;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "instagram_url")
    private String instagramUrl;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "website_url")
    private String websiteUrl;
}