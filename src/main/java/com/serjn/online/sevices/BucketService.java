package com.serjn.online.sevices;


import com.serjn.online.models.Bucket;
import com.serjn.online.models.Client;
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

    public Bucket findBucketByClientId(Long clientId) {

        return bucketRepository.findBucketByClientId(clientId)
                .orElseGet(() -> createBucket(clientId));

    }

    private Bucket createBucket(Long clientId) {
        Client client = clientService.finById(clientId);
        Bucket bucket = new Bucket();
        bucket.setClient(client);
        client.setBucket(bucket);
        clientService.save(client);
        return bucketRepository.save(bucket);
    }

    public void save(Bucket bucket) {
        bucketRepository.save(bucket);
    }
}
