package com.serjn.online.sevices;


import com.serjn.online.models.Bucket;
import com.serjn.online.repositories.BucketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BucketService {

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setBucketRepository(BucketRepository bucketRepository) {
        this.bucketRepository = bucketRepository;
    }

    private ClientService clientService;

    private BucketRepository bucketRepository;





    public void save(Bucket bucket) {
        bucketRepository.save(bucket);
    }
}
