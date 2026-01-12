package com.serjn.online.mappers;

import com.serjn.online.DTOs.ClientDto;
import com.serjn.online.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDto toDto(Client client);
}
