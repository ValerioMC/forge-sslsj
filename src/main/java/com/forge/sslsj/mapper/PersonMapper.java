package com.forge.sslsj.mapper;


import com.forge.sslsj.model.Person;
import com.forge.sslsj.payload.PersonRequest;
import com.forge.sslsj.payload.PersonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PersonMapper {
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    PersonResponse personToPersonResponse(Person person);

    Person personRequestToPerson(PersonRequest personRequest);
}