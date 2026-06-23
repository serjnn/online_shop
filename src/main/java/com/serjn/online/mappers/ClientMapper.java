package com.serjn.online.mappers;

import com.serjn.online.model.dto.ClientDto;
import com.serjn.online.model.entities.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientDto toDto(Client client);
}
