package com.moviemasti.movies.entity;

import com.moviemasti.movies.enums.Genre;
import com.moviemasti.movies.enums.Language;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "MOVIES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;
    
    @Column(nullable = false)
    private int duration;

	@Column(nullable = false)
	private double rating;
    
    @Column(name = "release_date", nullable = false)
    private String releaseDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Language language;
    
    @Column(nullable = false, length = 10)
    private String certificate;
    
    @ElementCollection
    @CollectionTable(name = "movie_cast", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "cast_member")
    private List<String> cast;
    
    @Column(nullable = false)
    private String director;
}
