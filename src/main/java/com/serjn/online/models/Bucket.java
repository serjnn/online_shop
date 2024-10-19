package com.serjn.online.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bucket")
public class Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<BucketItems> bucketItems;


}
