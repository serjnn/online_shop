package com.serjn.online.models;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "bucket")
public class Bucket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "bucket", cascade = CascadeType.ALL,  orphanRemoval = true)
    // orphanRemoval = true удаляет объекты в таблице BucketItems при очищении списка List<BucketItems>
    private List<BucketItems> bucketItems;


}
